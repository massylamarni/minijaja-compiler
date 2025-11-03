package fr.amvproject.group3;

import fr.amvproject.group3.ast.*;
import fr.amvproject.group3.ast.nodes.abstracts.*;
import fr.amvproject.group3.ast.nodes.decls.*;
import fr.amvproject.group3.ast.nodes.exprs.*;
import fr.amvproject.group3.ast.nodes.finals.*;
import fr.amvproject.group3.ast.nodes.instrs.*;
import fr.amvproject.group3.ast.nodes.intermediate.*;
import fr.amvproject.group3.ast.nodes.other.*;
import fr.amvproject.group3.ast.nodes.program.*;
import java.lang.reflect.Method;
import java.util.Collection;

public class JajaCodeCompiler implements AstVisitor<Void> {

    private final CodeEmitter emitter;
    private final LabelManager labels;
    // champ dans la classe
    private int instrCount = 0;

    // wrapper pour émettre et compter
    private void emitLine(String s) {
        emitter.emit(s);
        instrCount++;
    }

    // helper pour obtenir la position courante (avant prochain emit)
    private int currentPos() { return instrCount; }

    // champ dans JajaCodeCompiler
    private final boolean invokeConsumesArgs;

    // construceurs (surcharge)
    public JajaCodeCompiler(CodeEmitter emitter, LabelManager labels) {
        this(emitter, labels, false); // par défaut false (si ta VM dépile, passe true)
    }
    public JajaCodeCompiler(CodeEmitter emitter, LabelManager labels, boolean invokeConsumesArgs) {
        this.emitter = emitter;
        this.labels = labels;
        this.invokeConsumesArgs = invokeConsumesArgs;
    }
    private void emitCleanupArgs(int nArgs) {
        for (int i = 0; i < nArgs; i++) {
            emitLine("swap");
            emitLine("pop");
        }
    }

    // ---------------- Program / Class ----------------
    @Override
    public Void visitProgram(ProgramNode node) {
        emitLine("init");
        if (node.getClasses() != null) {
            for (ClassNode c : node.getClasses()) {
                c.accept(this);
            }
        }
        emitLine("pop");
        emitLine("jcstop");
        return null;
    }

    @Override
    public Void visitClass(ClassNode node) {

        if (node.getDecls() != null) node.getDecls().accept(this);
        if (node.getMain() != null) node.getMain().accept(this);
        emitRetraitDecls(node.getDecls());

        return null;
    }

    // ---------------- Declarations -------------------
    @Override
    public Void visitDecls(DeclsNode node) {
        if (node.getDecls() != null) {
            for (DeclNode d : node.getDecls()) {
                d.accept(this); // dynamic dispatch: VarNode -> visitVar, MethodNode -> visitMethod, ...
            }
        }
        return null;
    }
    private void emitRetraitDecls(DeclsNode decls) {
        if (decls == null || decls.getDecls() == null) return;

        // On parcourt en reverse (dernière déclaration créée retirée en premier)
        java.util.List<DeclNode> list = decls.getDecls();
        for (int i = list.size() - 1; i >= 0; i--) {
            // swap/pop comptés
            emitLine("swap");
            emitLine("pop");
        }
    }

    @Override public Void visitDecl(DeclNode node) { return null; } // generic decl (not used directly)

    // VarDeclNode (generic) - rarely used because subclasses override accept
    @Override
    public Void visitVarDecl(VarDeclNode node) {
        // default: nothing (specific subclass VarNode/TableNode/CstNode handle themselves)
        return null;
    }

    @Override
    public Void visitVar(VarNode node) {
        // if init present -> compile it (push value), else push 0
        if (node.getInitExp() != null) {
            node.getInitExp().accept(this);
        } else {
            emitLine("push (0)");
        }
        emitLine("new(" + node.getIdent().getName() + "," + node.getType().getName() + ",var,0)");
        return null;
    }

    @Override
    public Void visitCst(CstNode node) {
        if (node.getInitExp() != null) {
            node.getInitExp().accept(this);
        } else {
            emitLine("push (0)");
        }
        emitLine("new(" + node.getIdent().getName() + "," + node.getType().getName() + ",cst,0)");
        return null;
    }

    @Override
    public Void visitTable(TableNode node) {
        // compile size then new array
        if (node.getSizeExp() != null) node.getSizeExp().accept(this);
        else emitLine("push (0)");
        emitLine("newarray(" + node.getIdent().getName() + "," + node.getType().getName() +")");
        return null;
    }

    @Override
    public Void visitVars(VarsNode node) {
        if (node.getList() != null) {
            for (VarDeclNode v : node.getList()) v.accept(this);
        }
        return null;
    }

    // ---------------- Main / Method ----------------
    @Override
    public Void visitMain(MainNode node) {

        // allocate local vars
        if (node.getVars() != null) node.getVars().accept(this);
        // body
        if (node.getInstrs() != null) node.getInstrs().accept(this);
        // push default return value (main return)
        emitLine("push (0)");
        // retrait des vars locales
        emitRetraitVars(node.getVars());
        emitLine("return");
        return null;
    }

    @Override
    public Void visitMethod(MethodNode node) {
        SizeCalculator sc = new SizeCalculator();

        // tailles
        int nParams = node.getParams() != null && node.getParams().getList() != null ? node.getParams().getList().size() : 0;
        int pdvs = sc.size(node.getVars());
        int piss = sc.size(node.getInstrs());
        int nLocals = node.getVars() != null && node.getVars().getList() != null ? node.getVars().getList().size() : 0;
        int prdvs = 2 * nLocals;
        boolean hasReturn = sc.hasReturn(node);
        int extraReturn = hasReturn ? 0 : 2;

        // skip relative (instructions qui suivent le ujp)
        int skipRelative = nParams + pdvs + piss + prdvs + extraReturn;

        // position de départ avant push/new/ujp
        int base = currentPos();

        int methodTotalSize = sc.size(node); // ok comme valeur metadata

        // Emit push/new/ujp — ici nous voulons un ujp vers adresse absolue:
        emitLine("push( " + methodTotalSize +")");
        emitLine("new(" + node.getIdent().getName() + "," + node.getReturnType().getName() + ",meth,0)");
        // adresse absolue pour le corps : juste après push/new/ujp + skipRelative
        // position du prochain emit sera base+3 (après push/new/ujp)
        int bodyStart = base + 3;
        int ujpTarget = bodyStart + skipRelative; // saut vers après le corps
        emitLine("goto (" + ujpTarget +")");

        // PROLOGUE param/new ...
        int k = 1;
        if (node.getParams() != null && node.getParams().getList() != null) {
            for (ParamNode p : node.getParams().getList()) {
                emitLine("new(" + p.getIdent().getName() + "," + p.getType().getName() + ",var," + k + ")");
                k++;
            }
        }

        // locals allocation...
        if (node.getVars() != null && node.getVars().getList() != null) {
            for (VarDeclNode vd : node.getVars().getList()) {
                if (vd instanceof VarNode) {
                    VarNode var = (VarNode) vd;
                    if (var.getInitExp() != null) var.getInitExp().accept(this);
                    else emitLine("push (0)");
                    emitLine("new(" + var.getIdent().getName() + "," + var.getType().getName() + ",var,0)");
                } else if (vd instanceof CstNode) {
                    CstNode c = (CstNode) vd;
                    if (c.getInitExp() != null) c.getInitExp().accept(this);
                    else emitLine("push (0)");
                    emitLine("new(" + c.getIdent().getName() + "," + c.getType().getName() + ",cst,0)");
                } else if (vd instanceof TableNode) {
                    TableNode t = (TableNode) vd;
                    if (t.getSizeExp() != null) t.getSizeExp().accept(this);
                    else emitLine("push (0)");
                    emitLine("newarray(" + t.getIdent().getName() + "," + t.getType().getName() + ")");
                }
            }
        }

        // body
        if (node.getInstrs() != null) node.getInstrs().accept(this);

        if (!hasReturn) {
            emitLine("swap");
            emitLine("return");
        }
        return null;
    }


    // ---------------- Instructions --------------------
    @Override
    public Void visitInstrs(InstrsNode node) {
        if (node.getList() != null) {
            for (InstrNode in : node.getList()) in.accept(this);
        }
        return null;
    }

    @Override
    public Void visitAssign(AssignNode node) {
        // compile RHS
        node.getExp().accept(this);
        // left part
        LeftValueNode left = node.getLeftValue();
        if (left instanceof IdentNode) {
            emitLine("store (" + ((IdentNode) left).getName()+")");
        } else if (left instanceof ArrayAccessNode) {
            ArrayAccessNode a = (ArrayAccessNode) left;
            // we have value on stack already; push index then astore
            if (a.getIndexExp() != null) a.getIndexExp().accept(this);
            emitLine("astore (" + a.getIdent().getName()+")");
        }
        return null;
    }

    @Override
    public Void visitAddAssign(AddAssignNode node) {

        ExpNode rhs = node.getExp();
        rhs.accept(this);

        LeftValueNode lv = node.getLeftValueNode();
        if (lv == null) throw new RuntimeException("AddAssign with null left value");

        if (lv instanceof fr.amvproject.group3.ast.nodes.finals.IdentNode
                || lv.getClass().getSimpleName().equals("IdentNode")
                || lv.getClass().getSimpleName().equals("VarRefNode") // si ancien nom présent
        ) {
            // variable simple
            String name = null;
            try {
                Method m = lv.getClass().getMethod("getIdent");
                Object identObj = m.invoke(lv);
                Method getName = identObj.getClass().getMethod("getName");
                name = (String) getName.invoke(identObj);
            } catch (Exception e) {
                try {
                    Method mm = lv.getClass().getMethod("getName");
                    name = (String) mm.invoke(lv);
                } catch (Exception ex) {
                    throw new RuntimeException("Impossible d'obtenir le nom de la variable pour += (left): " + lv.getClass(), ex);
                }
            }

            // now do the arithmetic: RHS on top, load LHS, swap, add, store
            emitLine("inc (" + name +")");

        } else {
            // array access case
            try {
                Method getIdent = lv.getClass().getMethod("getIdent");
                Object identObj = getIdent.invoke(lv);
                Method getName = identObj.getClass().getMethod("getName");
                String arrName = (String) getName.invoke(identObj);

                emitLine("load (" + arrName +")");
                Method getIndex = lv.getClass().getMethod("getIndexExp");
                Object idx = getIndex.invoke(lv);
                if (idx != null && idx instanceof fr.amvproject.group3.ast.AstNode) {
                    ((fr.amvproject.group3.ast.AstNode) idx).accept((fr.amvproject.group3.ast.AstVisitor<Void>) this);
                }
                emitLine("loadref");
                emitLine("swap");
                emitLine("add");
                if (idx != null && idx instanceof fr.amvproject.group3.ast.AstNode) {
                    ((fr.amvproject.group3.ast.AstNode) idx).accept((fr.amvproject.group3.ast.AstVisitor<Void>) this);
                }
                emitLine("astore (" + arrName+")");
            } catch (NoSuchMethodException nsme) {
                throw new RuntimeException("AddAssign on array but getIdent/getIndexExp not found on " + lv.getClass(), nsme);
            } catch (Exception ex) {
                throw new RuntimeException("Error compiling AddAssign for left " + lv.getClass(), ex);
            }
        }

        return null;
    }



    @Override
    public Void visitInc(IncNode node) {
        String name = node.getIdent().getName();
        emitLine("inc " + name);
        return null;
    }

    @Override
    public Void visitCallInstr(CallInstrNode node) {
        // 1) compile args
        if (node.getListExp() != null) {
            node.getListExp().accept(this);
        }

        // 2) invoke
        emitLine("invoke (" + node.getIdent().getName()+")");

        // 3) cleanup args (if needed)
        int nArgs = 0;
        if (node.getListExp() != null && node.getListExp().getList() != null) {
            nArgs = node.getListExp().getList().size();
        }
        if (!invokeConsumesArgs && nArgs > 0) {
            emitCleanupArgs(nArgs);
        }

        // 4) retirer la valeur de retour de l'appel-instruction
        emitLine("pop");

        return null;
    }


    @Override
    public Void visitWrite(WriteNode node) {
        if (node.getExp() != null) node.getExp().accept(this);
        if (node.isLn()) emitLine("writeln");
        else emitLine("write");
        return null;
    }

    @Override
    public Void visitReturn(ReturnNode node) {
        if (node.getExp() != null) node.getExp().accept(this);
        emitLine("swap");
        emitLine("return");
        return null;
    }

    @Override
    public Void visitWhile(WhileNode node) {
        SizeCalculator sc = new SizeCalculator();
        int ne = (node.getCondExp() != null) ? sc.size(node.getCondExp()) : 0;
        int nBody = (node.getInstrs() != null) ? sc.size(node.getInstrs()) : 0;

        // position de départ du while (avant condition)
        int base = currentPos();

        // compile condition
        if (node.getCondExp() != null) node.getCondExp().accept(this);

        // adresse de sortie (après body + ujp)
        int exitAddr = base + ne + nBody + 2;

        emitLine("not ");
        emitLine("if (" + exitAddr +")");

        // body
        if (node.getInstrs() != null) node.getInstrs().accept(this);

        // ujp back to base (loop start)
        emitLine("goto (" + base +")");

        return null;
    }


    @Override
    public Void visitIf(IfNode node) {
        SizeCalculator sc = new SizeCalculator();

        // calculs de taille
        int ne    = (node.getCondExp() != null) ? sc.size(node.getCondExp()) : 0;
        int nThen = (node.getThenInstrs() != null) ? sc.size(node.getThenInstrs()) : 0;
        int nElse = (node.getElseInstrs() != null) ? sc.size(node.getElseInstrs()) : 0;

        // position de départ (avant émission de la condition)
        int base = currentPos();

        // on émet la condition (ne instructions) — utilise visit normal
        if (node.getCondExp() != null) node.getCondExp().accept(this);

        // calculer addresses absolues suivant la règle
        int elseStart = base + ne + nThen + 2;           // cible pour iffalse
        int afterElse  = elseStart + nElse;              // cible pour ujp après then

        // émettre iffalse (numérique)
        emitLine("goto (" + elseStart +")");

        // then
        if (node.getThenInstrs() != null) node.getThenInstrs().accept(this);

        // ujp vers après else
        emitLine("goto (" + afterElse + ")");

        // else (commence à elseStart)
        if (node.getElseInstrs() != null) node.getElseInstrs().accept(this);

        return null;
    }



    // ---------------- Expressions --------------------
    @Override
    public Void visitBinaryExp(BinaryExpNode node) {
        node.getLeftExp().accept(this);
        node.getRightExp().accept(this);
        String op = node.getOp().getOp().getName();
        switch (op) {
            case "+" -> emitLine("add");
            case "-" -> emitLine("sub");
            case "*" -> emitLine("mul");
            case "/" -> emitLine("div");
            case "==" -> emitLine("eq");
            case ">" -> emitLine("sup");
            case "&&" -> emitLine("et");
            case "||" -> emitLine("or");
        }
        return null;
    }

    @Override
    public Void visitUnaryExp(UnaryExpNode node) {
        node.getExp().accept(this);
        String op = node.getOp().getOp().getName();
        if ("!".equals(op)) emitLine("not");
        else if ("-".equals(op)) emitLine("neg");
        return null;
    }

    @Override
    public Void visitListExp(ListExpNode node) {
        if (node.getList() != null) {
            for (ExpNode e : node.getList()) e.accept(this);
        }
        return null;
    }


    @Override
    public Void visitCallExp(CallExpNode node) {
        // 1) compiler les arguments (empilés)
        if (node.getListExp() != null) {
            node.getListExp().accept(this);
        }

        // 2) invoke
        emitLine("invoke (" + node.getIdent().getName()+")");

        // 3) cleanup des arguments si invoke ne dépile pas (selon règle du cours)
        if (!invokeConsumesArgs) {
            int nArgs = 0;
            if (node.getListExp() != null && node.getListExp().getList() != null) {
                nArgs = node.getListExp().getList().size();
            }
            if (nArgs > 0) emitCleanupArgs(nArgs);
        }

        // On laisse la valeur de retour au sommet (appel expression)
        return null;
    }


    @Override
    public Void visitLength(LengthNode node) {
        emitLine("load (" + node.getIdent().getName()+")");
        emitLine("length");
        return null;
    }

    @Override
    public Void visitArrayAccess(ArrayAccessNode node) {
        emitLine("load (" + node.getIdent().getName()+")");
        if (node.getIndexExp() != null) node.getIndexExp().accept(this);
        emitLine("loadref");
        return null;
    }

    @Override
    public Void visitIntLiteral(IntLiteralNode node) {
        emitLine("push (" + node.getValue()+")");
        return null;
    }

    @Override
    public Void visitBoolLiteral(BoolLiteralNode node) {
        emitLine("push (" + (node.getValue() ? "true" : "false")+")") ;
        return null;
    }

    @Override
    public Void visitStringLiteral(StringLiteralNode node) {
        emitLine("push ( \"" + node.getValue() + "\""+")");
        return null;
    }

    @Override
    public Void visitIdent(IdentNode node) {
        emitLine("load (" + node.getName()+")");// For expressions the IdentNode as left-value is handled elsewhere (load/store)
        return null;
    }

    // ---------------- Misc / stubs --------------------
    @Override public Void visitBinaryOp(BinaryOpNode node) { return null; }
    @Override public Void visitUnaryOp(UnaryOpNode node) { return null; }
    @Override public Void visitParam(ParamNode node) { return null; }
    @Override public Void visitParams(ParamsNode node) { return null; }
    @Override public Void visitType(TypeNode node) { return null; }

    @Override
    public Void visitTypeMeth(TypeMethNode node) {
        return null;
    }


    // helper: retrait variables (swap ; pop) for each var
    private void emitRetraitVars(VarsNode vars) {
        if (vars == null || vars.getList() == null) return;
        for (int i = 0; i < vars.getList().size(); i++) {
            emitLine("swap");
            emitLine("pop");
        }
    }
}
