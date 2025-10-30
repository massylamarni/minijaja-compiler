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

@SuppressWarnings("java:S6539") // Visitor pattern: large by design
public class JajaCodeCompiler implements AstVisitor<Void> {

    private final CodeEmitter emitter;
    private final LabelManager labels;

    // champ dans JajaCodeCompiler
    private final boolean invokeConsumesArgs;

    // construceurs (surcharge)
    public JajaCodeCompiler(CodeEmitter emitter, LabelManager labels) {
        this(emitter, labels, false); // par défaut true (si ta VM dépile)
    }
    public JajaCodeCompiler(CodeEmitter emitter, LabelManager labels, boolean invokeConsumesArgs) {
        this.emitter = emitter;
        this.labels = labels;
        this.invokeConsumesArgs = invokeConsumesArgs;
    }
    private void emitCleanupArgs(int nArgs) {
        for (int i = 0; i < nArgs; i++) {
            emitter.emit("swap");
            emitter.emit("pop");
        }
    }

    // ---------------- Program / Class ----------------
    @Override
    public Void visitProgram(ProgramNode node) {
        emitter.emit("init");
        if (node.getClasses() != null) {
            for (ClassNode c : node.getClasses()) {
                c.accept(this);
            }
        }
        emitter.emit("pop");
        emitter.emit("jcstop");
        return null;
    }

    @Override
    public Void visitClass(ClassNode node) {
        emitter.emit("; Début de la classe " + node.getIdent().getName());
        if (node.getDecls() != null) node.getDecls().accept(this);
        if (node.getMain() != null) node.getMain().accept(this);
        emitRetraitDecls(node.getDecls());
        emitter.emit("; Fin de la classe " + node.getIdent().getName());
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
            DeclNode d = list.get(i);
            // Chaque new(...) placé lors de l'initialisation doit être retiré par swap/pop
            // (var, table, const, method créent tous un new(...))
            // Si tu as des types particuliers à traiter différemment, adapte ici.
            emitter.emit("swap");
            emitter.emit("pop");
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
            emitter.emit("push 0");
        }
        emitter.emit("new(" + node.getIdent().getName() + "," + node.getType().getName() + ",var,0)");
        return null;
    }

    @Override
    public Void visitCst(CstNode node) {
        if (node.getInitExp() != null) {
            node.getInitExp().accept(this);
        } else {
            emitter.emit("push 0");
        }
        emitter.emit("new(" + node.getIdent().getName() + "," + node.getType().getName() + ",cst,0)");
        return null;
    }

    @Override
    public Void visitTable(TableNode node) {
        // compile size then new array
        if (node.getSizeExp() != null) node.getSizeExp().accept(this);
        else emitter.emit("push 0");
        emitter.emit("new(" + node.getIdent().getName() + "," + node.getType().getName() + ",array,0)");
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
        emitter.emit("; main:");
        // allocate local vars
        if (node.getVars() != null) node.getVars().accept(this);
        // body
        if (node.getInstrs() != null) node.getInstrs().accept(this);
        // push default return value (main return)
        emitter.emit("push 0");
        // retrait des vars locales
        emitRetraitVars(node.getVars());
        emitter.emit("return");
        return null;
    }

    @Override
    public Void visitMethod(MethodNode node) {
        // compute method size (two-pass helper)
        // au début de visitMethod
        SizeCalculator sc = new SizeCalculator();
        int sizeMethod = sc.safe(node); // nombre d'instructions "intérieures" utilisé comme valeur à push

        emitter.emit("push " + sizeMethod);
        emitter.emit("new(" + node.getIdent().getName() + "," + node.getReturnType().getName() + ",meth,0)");

        String afterLabel = labels.newLabel(node.getIdent().getName() + "_after");
        emitter.emit("goto(" + afterLabel+")");
        // method body label
        emitter.emit( ";" + node.getIdent().getName() + ":");

        // params: for each param push default then new(param,var,k)
        if (node.getParams() != null && node.getParams().getList() != null) {
            int k = 1;
            for (ParamNode p : node.getParams().getList()) {
                emitter.emit("new(" + p.getIdent().getName() + "," + p.getType().getName() + ",var," + k + ")");
                k++;
            }
        }

        // local vars
        if (node.getVars() != null) node.getVars().accept(this);

        // body
        if (node.getInstrs() != null) node.getInstrs().accept(this);

        if (!methodHasReturn(node.getInstrs())) {
            emitter.emit("swap");
            emitter.emit("return");
        }

        // after label
        emitter.emit(";" + afterLabel + ":");
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
            emitter.emit("store " + ((IdentNode) left).getName());
        } else if (left instanceof ArrayAccessNode) {
            ArrayAccessNode a = (ArrayAccessNode) left;
            // we have value on stack already; push index then astore
            a.getIndexExp().accept(this);
            emitter.emit("astore " + a.getIdent().getName());
        }
        return null;
    }

    @Override
    public Void visitAddAssign(AddAssignNode node) {
        // 1) évaluer la RHS en premier (peut être un appel de méthode)
        ExpNode rhs = node.getExp();
        rhs.accept(this);

        // 2) Si la RHS était un CallExpNode et que invoke ne dépile pas,
        //    visitCallExp() a déjà émis invoke + cleanupArgs (la valeur de retour est au sommet).
        //    Donc ici la valeur de RHS est bien au sommet et les args ont été retirés.

        // 3) charger LHS, faire swap/add/store
        LeftValueNode lv = node.getLeftValueNode();
        if (lv == null) throw new RuntimeException("AddAssign with null left value");

        if (lv instanceof fr.amvproject.group3.ast.nodes.finals.IdentNode
                || lv.getClass().getSimpleName().equals("IdentNode")
                || lv.getClass().getSimpleName().equals("VarRefNode") // si ancien nom présent
        ) {
            // variable simple
            // obtenir le nom : plusieurs versions de node peuvent exister => utilisez réflexion prudente
            String name = null;
            try {
                // si class has getIdent() returning IdentNode
                Method m = lv.getClass().getMethod("getIdent");
                Object identObj = m.invoke(lv);
                Method getName = identObj.getClass().getMethod("getName");
                name = (String) getName.invoke(identObj);
            } catch (Exception e) {
                // fallback : essayer getName() direct si ident stocke le nom
                try {
                    Method mm = lv.getClass().getMethod("getName");
                    name = (String) mm.invoke(lv);
                } catch (Exception ex) {
                    throw new RuntimeException("Impossible d'obtenir le nom de la variable pour += (left): " + lv.getClass(), ex);
                }
            }

            // now do the arithmetic: RHS on top, load LHS, swap, add, store
            emitter.emit("load " + name);
            emitter.emit("swap");
            emitter.emit("add");
            emitter.emit("store " + name);

        } else {
            // array access case: ArrayAccessNode (différentes versions existantes)
            // we assume methods getIdent() and getIndexExp() exist
            try {
                Method getIdent = lv.getClass().getMethod("getIdent");
                Object identObj = getIdent.invoke(lv);
                Method getName = identObj.getClass().getMethod("getName");
                String arrName = (String) getName.invoke(identObj);

                // push array reference and index and do loadref to get current element
                emitter.emit("load " + arrName);
                Method getIndex = lv.getClass().getMethod("getIndexExp");
                Object idx = getIndex.invoke(lv);
                if (idx != null && idx instanceof fr.amvproject.group3.ast.AstNode) {
                    ((fr.amvproject.group3.ast.AstNode) idx).accept((fr.amvproject.group3.ast.AstVisitor<Void>) this);
                } else if (idx != null) {
                    // can't compile index, but try to ignore
                }
                emitter.emit("loadref"); // now current element is on top, but RHS was computed earlier and is below
                // we need RHS (was computed earlier and is now below), so swap to have lhs/top?
                // Current stack (after loadref): ..., RHS, currentElem
                emitter.emit("swap");
                emitter.emit("add"); // sum
                // need index again for astore: recompute or re-evaluate index
                if (idx != null && idx instanceof fr.amvproject.group3.ast.AstNode) {
                    ((fr.amvproject.group3.ast.AstNode) idx).accept((fr.amvproject.group3.ast.AstVisitor<Void>) this);
                }
                emitter.emit("astore " + arrName);
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
        emitter.emit("load " + name);
        emitter.emit("push 1");
        emitter.emit("add");
        emitter.emit("store " + name);
        return null;
    }

    @Override
    public Void visitCallInstr(CallInstrNode node) {
        // 1) compile args
        if (node.getListExp() != null) {
            node.getListExp().accept(this);
        }

        // 2) invoke
        emitter.emit("invoke " + node.getIdent().getName());

        // 3) cleanup args (if needed)
        int nArgs = 0;
        if (node.getListExp() != null && node.getListExp().getList() != null) {
            nArgs = node.getListExp().getList().size();
        }
        if (!invokeConsumesArgs && nArgs > 0) {
            emitCleanupArgs(nArgs);
        }

        // 4) retirer la valeur de retour de l'appel-instruction
        emitter.emit("pop");

        return null;
    }


    @Override
    public Void visitWrite(WriteNode node) {
        if (node.getExp() != null) node.getExp().accept(this);
        if (node.isLn()) emitter.emit("writeln");
        else emitter.emit("write");
        return null;
    }

    @Override
    public Void visitReturn(ReturnNode node) {
        if (node.getExp() != null) node.getExp().accept(this);
        emitter.emit("swap");
        emitter.emit("return");
        return null;
    }

    @Override
    public Void visitWhile(WhileNode node) {
        String start = labels.newLabel("; while_start");
        String end = labels.newLabel("; while_end");
        emitter.emit(start + ":");
        node.getCondExp().accept(this);
        emitter.emit("; iffalse " + end);
        if (node.getInstrs() != null) node.getInstrs().accept(this);
        emitter.emit("goto (" + start + ")");
        emitter.emit(")" + end + ":");
        return null;
    }

    @Override
    public Void visitIf(IfNode node) {
        // two-pass: compute sizes with SizeCalculator
        SizeCalculator sc = new SizeCalculator();
        int ne = sc.safe(node.getCondExp());
        int nThen = sc.safe(node.getThenInstrs());
        int nElse = sc.safe(node.getElseInstrs());

        // then-first numeric layout:
        // emit condition
        node.getCondExp().accept(this);

        // iffalse k1 -> skip then-block + ujp (1) => k1 = nThen + 1
        int kIf = nThen + 1;
        emitter.emit("iffalse " + kIf);

        // emit then-block
        if (node.getThenInstrs() != null) node.getThenInstrs().accept(this);

        // ujp to skip else-block: kGoto = nElse
        int kGoto = nElse;
        emitter.emit("goto " + kGoto);

        // emit else-block
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
            case "+" -> emitter.emit("add");
            case "-" -> emitter.emit("sub");
            case "*" -> emitter.emit("mul");
            case "/" -> emitter.emit("div");
            case "==" -> emitter.emit("eq");
            case ">" -> emitter.emit("gt");
            case "&&" -> emitter.emit("and");
            case "||" -> emitter.emit("or");
            default -> emitter.emit("; unknown binop " + op);
        }
        return null;
    }

    @Override
    public Void visitUnaryExp(UnaryExpNode node) {
        node.getExp().accept(this);
        String op = node.getOp().getOp().getName();
        if ("!".equals(op)) emitter.emit("not");
        else if ("-".equals(op)) emitter.emit("neg");
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
        emitter.emit("invoke " + node.getIdent().getName());

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
        emitter.emit("load " + node.getIdent().getName());
        emitter.emit("length");
        return null;
    }

    @Override
    public Void visitArrayAccess(ArrayAccessNode node) {
        emitter.emit("load " + node.getIdent().getName());
        if (node.getIndexExp() != null) node.getIndexExp().accept(this);
        emitter.emit("loadref");
        return null;
    }

    @Override
    public Void visitIntLiteral(IntLiteralNode node) {
        emitter.emit("push " + node.getValue());
        return null;
    }

    @Override
    public Void visitBoolLiteral(BoolLiteralNode node) {
        emitter.emit("push " + (node.getValue() ? "true" : "false"));
        return null;
    }

    @Override
    public Void visitStringLiteral(StringLiteralNode node) {
        emitter.emit("push \"" + node.getValue() + "\"");
        return null;
    }

    @Override
    public Void visitIdent(IdentNode node) {
        emitter.emit("load " + node.getName());// For expressions the IdentNode as left-value is handled elsewhere (load/store)
        return null;
    }

    // ---------------- Misc / stubs --------------------
    @Override public Void visitBinaryOp(BinaryOpNode node) { return null; }
    @Override public Void visitUnaryOp(UnaryOpNode node) { return null; }
    @Override public Void visitParam(ParamNode node) { return null; }
    @Override public Void visitParams(ParamsNode node) { return null; }
    @Override public Void visitType(TypeNode node) { return null; }
    @Override public Void visitTypeMeth(TypeMethNode node) { return null; }


    // helper: retrait variables (swap ; pop) for each var
    private void emitRetraitVars(VarsNode vars) {
        if (vars == null || vars.getList() == null) return;
        for (int i = 0; i < vars.getList().size(); i++) {
            emitter.emit("swap");
            emitter.emit("pop");
        }
    }


    /* ... dans la classe JajaCodeCompiler ... */

    /**
     * Parcourt un subtree AST (type AstNode) et recherche récursivement
     * un nœud de type "RetourNode" ou "ReturnNode" (noms possibles selon ta version).
     * Utilise la réflexion pour être indépendant des packages / noms exacts.
     */
    private boolean methodHasReturn(fr.amvproject.group3.ast.AstNode node) {
        if (node == null) return false;

        // check by class simple name (RetourNode or ReturnNode)
        String simple = node.getClass().getSimpleName();
        if ("RetourNode".equals(simple) || "ReturnNode".equals(simple)) return true;

        try {
            // common patterns: nodes that contain lists -> getList()
            Method getList = null;
            try { getList = node.getClass().getMethod("getList"); } catch (NoSuchMethodException e) { getList = null; }
            if (getList != null) {
                Object res = getList.invoke(node);
                if (res instanceof Collection) {
                    for (Object child : (Collection<?>) res) {
                        if (child instanceof fr.amvproject.group3.ast.AstNode) {
                            if (methodHasReturn((fr.amvproject.group3.ast.AstNode) child)) return true;
                        }
                    }
                }
            }

            // instrs containers often expose getInstrs(), getThenInstrs(), getElseInstrs()
            String[] names = new String[] {"getInstrs", "getThenInstrs", "getElseInstrs", "getThen", "getElse", "getVars", "getListExp", "getList", "getDecls"};
            for (String nm : names) {
                try {
                    Method m = node.getClass().getMethod(nm);
                    Object o = m.invoke(node);
                    if (o instanceof fr.amvproject.group3.ast.AstNode) {
                        if (methodHasReturn((fr.amvproject.group3.ast.AstNode) o)) return true;
                    } else if (o instanceof Collection) {
                        for (Object child : (Collection<?>) o) {
                            if (child instanceof fr.amvproject.group3.ast.AstNode) {
                                if (methodHasReturn((fr.amvproject.group3.ast.AstNode) child)) return true;
                            }
                        }
                    }
                } catch (NoSuchMethodException ignored) {
                    // méthode non présente => on continue
                }
            }

            // parfois les nœuds d'instr contiennent directement un sous-instr via getThenInstrs()/getElseInstrs()
            // on peut tenter de trouver getLeftExp/getRightExp/getCondExp/getExp pour descendre dans les expressions
            String[] exprNames = new String[] {"getCondExp", "getExp", "getLeftExp", "getRightExp", "getIndexExp", "getInitExp", "getSizeExp"};
            for (String nm : exprNames) {
                try {
                    Method m = node.getClass().getMethod(nm);
                    Object o = m.invoke(node);
                    if (o instanceof fr.amvproject.group3.ast.AstNode) {
                        if (methodHasReturn((fr.amvproject.group3.ast.AstNode) o)) return true;
                    }
                } catch (NoSuchMethodException ignored) {
                }
            }
        } catch (Exception ex) {
            // en cas d'erreur réflexive, on échoue "silencieusement" et on retourne false.
            // (mieux que lever une exception de compilation/exécution ici)
        }
        return false;
    }

}
