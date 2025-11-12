package fr.amvproject.group3;

import java.awt.geom.QuadCurve2D;
import java.util.ArrayList;
import java.util.List;

import fr.amvproject.group3.mjj.Quad;
import fr.amvproject.group3.mjj.Stack;
import fr.amvproject.group3.mjj.ast.AstVisitor;
import fr.amvproject.group3.mjj.ast.nodes.abstracts.DeclsNode;
import fr.amvproject.group3.mjj.ast.nodes.abstracts.InstrsNode;
import fr.amvproject.group3.mjj.ast.nodes.abstracts.ListExpNode;
import fr.amvproject.group3.mjj.ast.nodes.abstracts.ParamsNode;
import fr.amvproject.group3.mjj.ast.nodes.abstracts.VarsNode;
import fr.amvproject.group3.mjj.ast.nodes.decls.CstNode;
import fr.amvproject.group3.mjj.ast.nodes.decls.MainNode;
import fr.amvproject.group3.mjj.ast.nodes.decls.MethodNode;
import fr.amvproject.group3.mjj.ast.nodes.decls.TableNode;
import fr.amvproject.group3.mjj.ast.nodes.decls.VarNode;
import fr.amvproject.group3.mjj.ast.nodes.exprs.BinaryExpNode;
import fr.amvproject.group3.mjj.ast.nodes.exprs.CallExpNode;
import fr.amvproject.group3.mjj.ast.nodes.exprs.LengthNode;
import fr.amvproject.group3.mjj.ast.nodes.exprs.UnaryExpNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.ArrayAccessNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.BoolLiteralNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.IntLiteralNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.ParamNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.StringLiteralNode;
import fr.amvproject.group3.mjj.ast.nodes.instrs.AddAssignNode;
import fr.amvproject.group3.mjj.ast.nodes.instrs.AssignNode;
import fr.amvproject.group3.mjj.ast.nodes.instrs.CallInstrNode;
import fr.amvproject.group3.mjj.ast.nodes.instrs.IfNode;
import fr.amvproject.group3.mjj.ast.nodes.instrs.IncNode;
import fr.amvproject.group3.mjj.ast.nodes.instrs.ReturnNode;
import fr.amvproject.group3.mjj.ast.nodes.instrs.WhileNode;
import fr.amvproject.group3.mjj.ast.nodes.instrs.WriteNode;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.DeclNode;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.ExpNode;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.InstrNode;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.VarDeclNode;
import fr.amvproject.group3.mjj.ast.nodes.other.BinaryOpNode;
import fr.amvproject.group3.mjj.ast.nodes.other.TypeMethNode;
import fr.amvproject.group3.mjj.ast.nodes.other.TypeNode;
import fr.amvproject.group3.mjj.ast.nodes.other.UnaryOpNode;
import fr.amvproject.group3.mjj.ast.nodes.program.ClassNode;
import fr.amvproject.group3.mjj.ast.nodes.program.ProgramNode;
import fr.amvproject.group3.mjj.utils.NodeOperations;
import fr.amvproject.group3.nodes.ObjNode;

/**
 * Interpréteur respectant les règles sémantiques du cours MiniJaja
 * Structure : MEM ⊢ INSTR → MEM et MEM eval⊢ EXP ⇒ VAL
 */
@SuppressWarnings("java:S6539") // Visitor pattern: large by design
public class InterpreterVisitor implements AstVisitor<Object> {
    private final Stack stack = new Stack();
    private VarDeclNode node;

    public Stack getStack() {
        return stack;
    }

    // ========== PROGRAM ==========
    @Override
    public Object visitProgram(ProgramNode node) {
        for (ClassNode classe : node.getClasses()) {
            classe.accept(this);
        }
        return null;
    }

    /**
     * Règle [classe] du cours (page 38) :
     * DeclVar(i, w, w, []) ⊢ dss → m1
     * m1 ⊢ mma → m2
     * m2 retrait⊢ dss → m3
     * [] ⊢ classe(ident(i), dss, mma) → RetirerDecl(ident(i), m3)
     */
    @Override
    public Object visitClass(ClassNode node) {
        // 1. DeclVar(i, w, w, []) - Déclarer une variable pour la classe (omega)
        stack.declVar(node.getIdent(), null, new TypeMethNode("void"));

        // 2. m1 ⊢ dss → m2 - Traiter les déclarations
        node.getDecls().accept(this);

        // 3. m2 ⊢ mma → m3 - Exécuter le main
        node.getMain().accept(this);

        // 4. m3 retrait⊢ dss → m4 - Retrait des déclarations
        retraitDecls(node.getDecls());

        // 5. RetirerDecl(ident(i), m4) - Retirer la variable de classe
        // stack.retirerDecl(node.getIdent(), TODO);

        return null;
    }

    // ========== DECLARATIONS ==========

    /**
     * Règle [decls] (page 38) :
     * m ⊢ ds → m1, m1 ⊢ dss → m2
     * m ⊢ decls(ds, dss) → m2
     */
    @Override
    public Object visitDecls(DeclsNode node) {
        for (DeclNode decl : node.getList()) {
            decl.accept(this);
        }
        return null;
    }

    @Override
    public Object visitDecl(DeclNode node) {
        // Dispatch vers le bon type de déclaration
        return null;
    }

    /**
     * Règle [vars] (page 38) :
     * m ⊢ dv → m1, m1 ⊢ dvs → m2
     * m ⊢ vars(dv, dvs) → m2
     */
    @Override
    public Object visitVars(VarsNode node) {
        for (VarDeclNode varDeclNode : node.getList()) {
            varDeclNode.accept(this);
        }
        return null;
    }

    /**
     * Règles du cours (page 38-39) :
     * [var] : m eval⊢ e ⇒ v, m ⊢ var(t, ident(i), e) → DeclVar(i, v, t, m)
     * [cst] : m eval⊢ e ⇒ v, m ⊢ cst(t, ident(i), e) → DeclCst(i, v, t, m)
     * [tableau] : m eval⊢ e ⇒ v, m ⊢ tableau(t, ident(i), e) → DeclTab(i, v, t, m)
     */
    @Override
    public Object visitVarDecl(VarDeclNode node) {
        this.node = node;
        // Application de la règle selon le type
        if (node instanceof VarNode varNode) {
            // DeclVar(i, v, t, m)
            stack.declVar(varNode.getIdent(), evalExp(varNode.getInitExp()), varNode.getType());
        } else if (node instanceof CstNode cstNode) {
            // DeclCst(i, v, t, m)
            // Note : la distinction vcst/cst est gérée dans Stack
            stack.declCst(cstNode.getIdent(), evalExp(cstNode.getInitExp()), cstNode.getType());
        } else if (node instanceof TableNode tableNode) {
            stack.declTab(tableNode.getIdent(), (int) evalExp(tableNode.getSizeExp()), tableNode.getType());
        }
        return null;
    }

    @Override
    public Object visitVar(VarNode node) {
        // TODO Auto-generated method stub
        return visitVarDecl(node);

    }

    @Override
    public Object visitTable(TableNode node) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object visitCst(CstNode node) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Règle [méthode] (page 39) :
     * m ⊢ méthode(t, ident(i), ent, dvs, iss) → DeclMeth(i, méthode(...), t, m)
     */
    @Override
    public Object visitMethod(MethodNode node) {
        stack.declMeth(node.getIdent(), node.getParams(), node.getReturnType());
        return null;
    }

    /**
     * Règle [main] (page 39) :
     * m ⊢ dvs → m1, m1 ⊢ iss → m2, m2 retrait⊢ dvs → m3
     * m ⊢ main(dvs, iss) → m3
     */
    @Override
    public Object visitMain(MainNode node) {
        // 1. m ⊢ dvs → m1 : Traiter les variables locales
        node.getVars().accept(this);

        // 2. m1 ⊢ iss → m2 : Exécuter les instructions
        node.getInstrs().accept(this);

        // 3. m2 retrait⊢ dvs → m3 : Retirer les variables locales
        retraitVars(node.getVars());

        return null;
    }

    @Override
    public Object visitParams(ParamsNode node) {
        return null;
    }

    @Override
    public Object visitParam(ParamNode node) {
        return null;
    }

    // ========== INSTRUCTIONS ==========

    /**
     * Règle [instrs] (page 40) :
     * m ⊢ is → m1, m1 ⊢ iss → m2
     * m ⊢ instrs(is, iss) → m2
     */
    @Override
    public Object visitInstrs(InstrsNode node) {
        for (InstrNode instr : node.getList()) {
            instr.accept(this);
        }
        return null;
    }

    /**
     * Règles [affectation] et [affectationT] (page 40) :
     * m eval⊢ e ⇒ v
     * m ⊢ affectation(ident(i), e) → AffecterVal(i, v, m)
     *
     * m eval⊢ e ⇒ v, m eval⊢ e1 ⇒ ind
     * m ⊢ affectation(tab(ident(i), e1), e) → AffecterValT(i, ind, v, m)
     */
    @Override
    public Object visitAssign(AssignNode node) {
        // m eval⊢ e ⇒ v
        Object value = evalExp(node.getExp());

        if (node.getLeftValueNode() instanceof IdentNode identNode) {
            // Règle [affectation]

            stack.affecterVal(identNode, value, getQuad(identNode, node.getExp(), "var"));
        } else if (node.getLeftValueNode() instanceof ArrayAccessNode arrayAccessNode) {
            // Règle [affectationT]
            // m eval⊢ e1 ⇒ ind
            int index = (int) evalExp(arrayAccessNode.getIndexExp());
            stack.affecterValT(arrayAccessNode.getIdent(), index, value, getQuad(arrayAccessNode.getIdent(), node.getExp(), "tab"));
        }
        return null;
    }

    /**
     * Règles [somme] et [sommeT] (page 41) :
     * m eval⊢ e ⇒ v
     * m ⊢ somme(ident(i), e) → AffecterVal(i, Val(i, m) + v, m)
     *
     * m eval⊢ e ⇒ v, m eval⊢ e1 ⇒ ind
     * m ⊢ somme(tab(ident(i), e1), e) → AffecterValT(i, ind, ValT(i, ind, m) + v, m)
     */
    @Override
    public Object visitAddAssign(AddAssignNode node) {
        NodeOperations no = new NodeOperations();
        return visitAssign(no.addAssignToAssign(node));
    }

    /**
     * Règle [incrément] (page 40) :
     * m ⊢ incrément(ident(i)) → AffecterVal(i, Val(i, m) + 1, m)
     */
    @Override
    public Object visitInc(IncNode node) { // TODO Handle array case
        Object currentValue = stack.val((IdentNode) node.getLeftValueNode(), stack.pop() );
        // Vérification du type
        if (!(currentValue instanceof Integer)) {
            throw new RuntimeException(
                    "Type error: increment operator requires integer variable, got " +
                            currentValue.getClass().getSimpleName()
            );
        }
        int newValue = (int) currentValue + 1;
        //if(node.)
            stack.affecterVal((IdentNode) node.getLeftValueNode(), currentValue, getQuad((IdentNode) node.getLeftValueNode(), newValue, "var" ));

        return null;
    }

    @Override
    public Object visitLength(LengthNode node) {
        // TODO: Implémenter Long(i, m) selon page 31
        return 0;
    }

    /**
     * Règles [sivrai] et [sifaux] (page 42) :
     * m eval⊢ e ⇒ true, m ⊢ iss → m1
     * m ⊢ si(e, iss, iss1) → m1
     *
     * m eval⊢ e ⇒ false, m ⊢ iss1 → m1
     * m ⊢ si(e, iss, iss1) → m1
     */
    @Override
    public Object visitIf(IfNode node) {
        Object condValue = evalExp(node.getCondExp());

        // Verification du type de la condition
        if (!(condValue instanceof Boolean)){
            throw new RuntimeException(
                    "Type error: if condition must be boolean, got " +
                            condValue.getClass().getSimpleName()
            );
        }
        // m eval⊢ e ⇒ condition
        if ((boolean) condValue) {
            // Règle [sivrai]
            node.getThenInstrs().accept(this);
        } else if (node.getElseInstrs() != null) {
            // Règle [sifaux]
            node.getElseInstrs().accept(this);
        }
        return null;
    }

    /**
     * Règles [tantquevrai] et [tantquefaux] (page 42) :
     * m eval⊢ e ⇒ true, m ⊢ iss → m1, m1 ⊢ tantque(e, iss) → m2
     * m ⊢ tantque(e, iss) → m2
     *
     * m eval⊢ e ⇒ false
     * m ⊢ tantque(e, iss) → m
     */
    @Override
    public Object visitWhile(WhileNode node) {
        // Verification du type de la condition
        if (!(evalExp(node.getCondExp()) instanceof Boolean)) {
            throw new RuntimeException(
                    "Type error: while condition must be boolean, got " +
                            evalExp(node.getCondExp()).getClass().getSimpleName()
            );
        }
        // m eval⊢ e ⇒ condition
        while ((boolean) evalExp(node.getCondExp())) {
            // Règle [tantquevrai]
            node.getInstrs().accept(this);
        }
        // Règle [tantquefaux] : retourne m inchangé
        return null;
    }

    /**
     * Règle [ecrire] (page 42) :
     * m eval⊢ e ⇒ v
     * m ⊢ ecrire(e) → Afficher(v, m)
     */
    @Override
    public Object visitWrite(WriteNode node) {
        // m eval⊢ e ⇒ v
        Object value = evalExp(node.getExp());
        // Afficher(v, m)
        if (node.isLn()) {
            System.out.println(value);
        } else {
            System.out.print(value);
        }
        return null;
    }

    /**
     * Règle [retour] (page 41) :
     * m eval⊢ e ⇒ v
     * m ⊢ retour(e) → AffecterVal(VariableClasse(m), v, m)
     */
    @Override
    public Object visitReturn(ReturnNode node) {
        // m eval⊢ e ⇒ v
        // AffecterVal(VariableClasse(m), v, m)
        // Note : Cette fonctionnalité doit être gérée par Stack.callMethod
        return evalExp(node.getExp());
    }

    /**
     * Règle [appelI] (page 41) :
     * ExpParam(lexp, Paramètre(i, m), m) ⊢ Déclaration(i, m) → m1
     * m1 ⊢ Corps(i, m) → m2
     * m2 retrait⊢ Déclaration(i, m) → m3
     * m3 retrait⊢ Paramètre(i, m) → m4
     * m ⊢ appelI(ident(i), lexp) → m4
     */
    @Override
    public Object visitCallInstr(CallInstrNode node) {
        // La logique complète est dans Stack.callMethod
        return null;
    }

    // ========== EXPRESSIONS ==========

    @Override
    public Object visitListExp(ListExpNode node) {
        List<Object> values = new ArrayList<>();
        for (ExpNode exp : node.getList()) {
            values.add(evalExp(exp));
        }
        return values;
    }

    @Override
    public Object visitBinaryExp(BinaryExpNode node) {
        return evalBinaryExp(node);
    }

    @Override
    public Object visitUnaryExp(UnaryExpNode node) {
        return evalUnaryExp(node);
    }

    /**
     * Règles MEM eval⊢ EXP ⇒ VAL (page 43)
     */
    @Override
    public Object visitIntLiteral(IntLiteralNode node) {
        // [nbre] : m eval⊢ nbre(n) ⇒ n
        return node.getValue();
    }

    @Override
    public Object visitBoolLiteral(BoolLiteralNode node) {
        // [vrai] / [faux]
        return node.getValue();
    }

    @Override
    public Object visitStringLiteral(StringLiteralNode node) {
        // [chaine] : m eval⊢ chaine(string) ⇒ string
        return node.getValue();
    }

    /**
     * [ident] : m eval⊢ ident(i  ) ⇒ Val(i,m)
     */
    @Override
    public Object visitIdent(IdentNode node) {
        try {
            return stack.val(node, stack.pop()); // TODO
        } catch (RuntimeException e) {
            throw new RuntimeException(
                    "Variable '" + node.getName() + "' not declared", e);
        }
    }

    /**
     * [tab] : m eval⊢ e ⇒ v
     * m eval⊢ tab(ident(i), e) ⇒ ValT(i, v, m)
     */

    @Override
    public Object visitArrayAccess(ArrayAccessNode node) {
        /*
        Object indexObj = evalExp(node.getIndexExp());
        if (!(indexObj instanceof Integer)) {
            throw new RuntimeException(
                    "Type error: array index must be integer, got " +
                            indexObj.getClass().getSimpleName()
            );
        }
        // Je cast apres vérification
        int index = (int) indexObj;
        if (index < 0) {
            throw new RuntimeException(
                    "Runtime error: array index cannot be negative, got " + index
            );
        }
        try {
            return stack.valT(node.getIdent(), index, TODO);
        } catch (IndexOutOfBoundsException e) {
            throw new RuntimeException(
                    "Runtime error: array index " + index +
                            " out of bounds for array '" + node.getIdent().getName() + "'",
                    e
            );
        }

         */
        return null;
    }

    /**
     * Règle [appelE] (page 43) :
     * m ⊢ appelI(ident(i), lexp) → m1
     * m1 eval⊢ VariableClasse(m1) ⇒ v
     * m eval⊢ appelE(ident(i), lexp) ⇒ v
     */
    @Override
    public Object visitCallExp(CallExpNode node) {
        return null;
    }

    // ========== OTHER NODES ==========
    @Override
    public Object visitType(TypeNode node) {
        return node;
    }

    @Override
    public Object visitTypeMeth(TypeMethNode node) {
        return node;
    }

    @Override
    public Object visitBinaryOp(BinaryOpNode node) {
        return node;
    }

    @Override
    public Object visitUnaryOp(UnaryOpNode node) {
        return node;
    }

    // ========== MÉTHODES AUXILIAIRES ==========

    /**
     * Évalue une expression selon les règles MEM eval⊢ EXP ⇒ VAL (page 43)
     */
    private Object evalExp(ExpNode exp) {
        return exp.accept(this);
    }

    /**
     * Règle [op2] (page 43) :
     * m eval⊢ e ⇒ v, m eval⊢ e1 ⇒ v1
     * m eval⊢ op2(e, e1) ⇒ v op v1
     */
    private Object evalBinaryExp(BinaryExpNode node) {
        Object left = evalExp(node.getLeftExp());
        Object right = evalExp(node.getRightExp());
        // on compare le type si cest les memes avec la methodes deja existant
        if (left.getClass() != right.getClass()) {
            throw new RuntimeException(
                    "Type mismatch: left is " + left.getClass().getSimpleName() +
                            ", right is " + right.getClass().getSimpleName()
            );
        }

        if ((node.getOp().isAnd() || node.getOp().isOr()) && !(left instanceof Boolean)) {
            throw new RuntimeException(
                    "Type error: logical operator requires boolean operands, got " +
                            left.getClass().getSimpleName() + " and " + right.getClass().getSimpleName()
            );
        }

        if (!(left instanceof Integer) && (node.getOp().isAdd() ||  node.getOp().isSub() || node.getOp().isMul() || node.getOp().isDiv() || node.getOp().isGt()) ) {
            throw new RuntimeException(
                    "Type error: arithmetic operator requires integer operands, got " +
                            left.getClass().getSimpleName() + " and " + right.getClass().getSimpleName()
            );
        }

        if (!(left instanceof Boolean) && (node.getOp().isAnd() ||  node.getOp().isOr()) ) {
            throw new RuntimeException(
                    "Type error: arithmetic operator requires integer operands, got " +
                            left.getClass().getSimpleName() + " and " + right.getClass().getSimpleName()
            );
        }

        switch (node.getOp().getOp()) {
            case AND: return (boolean) left && (boolean) right;
            case OR:  return (boolean) left || (boolean) right;
            case ADD: return (int) left + (int) right;
            case SUB: return (int) left - (int) right;
            case MUL: return (int) left * (int) right;
            case DIV:
                if ((int) right == 0) throw new RuntimeException("Runtime error: division by zero");
                return (int) left / (int) right;
            case EQ:  return  left == right;
            case GT:  return (int) left > (int) right;
            default:  throw new RuntimeException("Unknown binary operator: " + node.getOp());
        }
    }



    /**
     * Règle [op1] (page 43) :
     * m eval⊢ e ⇒ v
     * m eval⊢ op1(e) ⇒ op1 v
     */
    private Object evalUnaryExp(UnaryExpNode node) {
        // m eval⊢ e ⇒ v
        Object operand = evalExp(node.getExp());
        String op = node.getOp().getOp().getName();

        // op1 v
        switch (op) {
            case "-":
                if (!(operand instanceof Integer)) {
                    throw new RuntimeException(
                            "Type error: unary minus requires integer operand, got " +
                                    operand.getClass().getSimpleName()
                    );
                }
                return -(int) operand;
            case "!":
                if (!(operand instanceof Boolean)) {
                    throw new RuntimeException(
                            "Type error: logical NOT requires boolean operand, got " +
                                    operand.getClass().getSimpleName()
                    );
                }
                return !(boolean) operand;             // [non]
            default: throw new RuntimeException("Unknown unary operator: " + op);
        }
    }

    /**
     * Retrait des déclarations : MEM retrait⊢ DECLS → MEM (page 44)
     * [rdecls] : m retrait⊢ dss → m1, m1 retrait⊢ ds → m2
     * m retrait⊢ decls(ds, dss) → m2
     */
    private void retraitDecls(DeclsNode decls) {
        /*
        // Retrait en ordre inverse (comme dans la règle récursive)
        List<DeclNode> declList = decls.getDecls();
        for (int i = declList.size() - 1; i >= 0; i--) {
            DeclNode decl = declList.get(i);
            if (decl instanceof VarNode varNode) {
                stack.retirerDecl(varNode.getIdent(), TODO);
            } else if (decl instanceof MethodNode methodNode) {
                stack.retirerDecl(methodNode.getIdent(), TODO);
            }
        }

         */
    }

    /**
     * Retrait des variables : MEM retrait⊢ VARS → MEM (page 44)
     * [rvars] : m retrait⊢ dvs → m1, m1 retrait⊢ dv → m2
     * m retrait⊢ vars(dv, dvs) → m2
     */
    private void retraitVars(VarsNode vars) {
        // Retrait en ordre inverse
        /*
        List<VarDeclNode> varList = vars.getList();
        for (int i = varList.size() - 1; i >= 0; i--) {
            stack.retirerDecl(varList.get(i).getIdent(), TODO);
        }

         */
    }

    /*
    helper
     */
    public Quad getQuad(IdentNode identNode, ExpNode expNode, String obj) {
        return  new Quad(identNode, expNode.evaluate(), new ObjNode(obj), stack.sorte(identNode , stack.pop()));
    }

    public Quad getQuad(IdentNode identNode, int value, String obj) {
        return  new Quad(identNode, value, new ObjNode(obj), stack.sorte(identNode , stack.pop()));
    }

    public Quad getQuad(IdentNode identNode, String value, String obj) {
        return  new Quad(identNode, value, new ObjNode(obj), stack.sorte(identNode , stack.pop()));
    }




}
