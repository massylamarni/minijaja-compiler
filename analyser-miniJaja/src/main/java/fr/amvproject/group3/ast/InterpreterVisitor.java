package fr.amvproject.group3.ast;

import java.util.ArrayList;
import java.util.List;

import fr.amvproject.group3.ast.nodes.abstracts.*;
import fr.amvproject.group3.ast.nodes.decls.*;
import fr.amvproject.group3.ast.nodes.exprs.*;
import fr.amvproject.group3.ast.nodes.finals.ArrayAccessNode;
import fr.amvproject.group3.ast.nodes.finals.BoolLiteralNode;
import fr.amvproject.group3.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.ast.nodes.finals.IntLiteralNode;
import fr.amvproject.group3.ast.nodes.finals.ParamNode;
import fr.amvproject.group3.ast.nodes.finals.StringLiteralNode;
import fr.amvproject.group3.ast.nodes.instrs.*;
import fr.amvproject.group3.ast.nodes.intermediate.DeclNode;
import fr.amvproject.group3.ast.nodes.intermediate.ExpNode;
import fr.amvproject.group3.ast.nodes.intermediate.InstrNode;
import fr.amvproject.group3.ast.nodes.intermediate.VarDeclNode;
import fr.amvproject.group3.ast.nodes.other.*;
import fr.amvproject.group3.ast.nodes.program.*;
import fr.amvproject.group3.memory.Stack;

/**
 * Interpréteur respectant les règles sémantiques du cours MiniJaja
 * Structure : MEM ⊢ INSTR → MEM et MEM eval⊢ EXP ⇒ VAL
 */
@SuppressWarnings("java:S6539") // Visitor pattern: large by design
public class InterpreterVisitor implements AstVisitor<Object> {
    private final Stack stack = new Stack();

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
        stack.retirerDecl(node.getIdent(), stack.pop());

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
        for (DeclNode decl : node.getDecls()) {
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
        // Application de la règle selon le type
        if (node instanceof VarNode varNode) {
            // DeclVar(i, v, t, m)
            stack.declVar(varNode.getIdent(), evalExp(varNode.getInitExp()), varNode.getType());
        } else if (node instanceof CstNode cstNode) {
            // DeclCst(i, v, t, m)
            // Note : la distinction vcst/cst est gérée dans Stack
            stack.declCst(cstNode.getIdent(), evalExp(cstNode.getInitExp()), new TypeMethNode(cstNode.getType().getName()));
        } else if (node instanceof TableNode tableNode) {
            stack.declTab(tableNode.getIdent(), (int) evalExp(tableNode.getSizeExp()), tableNode.getType());
        }
        return null;
    }

    @Override
    public Object visitVar(VarNode node) {
        // TODO Auto-generated method stub
        return null;
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

        if (node.getLeftValue() instanceof IdentNode identNode) {
            // Règle [affectation]
            stack.affecterVal(identNode, value, stack.pop());
        } else if (node.getLeftValue() instanceof ArrayAccessNode arrayAccessNode) {
            // Règle [affectationT]
            // m eval⊢ e1 ⇒ ind
            int index = (int) evalExp(arrayAccessNode.getIndexExp());
            stack.affecterValT(arrayAccessNode.getIdent(), index, value, stack.pop());
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
        // m eval⊢ e ⇒ v
        Object addValue = evalExp(node.getExp());

        if (node.getLeftValueNode() instanceof IdentNode identNode) {
            // Règle [somme]
            int result = (int) addValue;
            stack.affecterVal(identNode, result, stack.pop());
        } else if (node.getLeftValueNode() instanceof ArrayAccessNode arrayAccessNode) {
            // Règle [sommeT]
            int index = (int) evalExp(arrayAccessNode.getIndexExp());
            stack.affecterValT(arrayAccessNode.getIdent(), index, addValue, stack.pop());
        }
        return null;
    }

    /**
     * Règle [incrément] (page 40) :
     * m ⊢ incrément(ident(i)) → AffecterVal(i, Val(i, m) + 1, m)
     */
    @Override
    public Object visitInc(IncNode node) {
        int currentValue = (int) stack.val(node.getIdent(), stack.pop());
        int newValue = currentValue + 1;
        stack.affecterVal(node.getIdent(), newValue, stack.pop());
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
        // m eval⊢ e ⇒ condition
        if ((boolean) evalExp(node.getCondExp())) {
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
     * [ident] : m eval⊢ ident(i) ⇒ Val(i,m)
     */
    @Override
    public Object visitIdent(IdentNode node) {
        return stack.val(node, stack.pop());
    }

    /**
     * [tab] : m eval⊢ e ⇒ v
     * m eval⊢ tab(ident(i), e) ⇒ ValT(i, v, m)
     */
    @Override
    public Object visitArrayAccess(ArrayAccessNode node) {
        int index = (int) evalExp(node.getIndexExp());
        return stack.valT(node.getIdent(), index, stack.pop());
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
        // Court-circuit pour AND et OR
        if (node.getOp().isAnd()) {
            if (!(boolean) evalExp(node.getLeftExp())) return false;
            return evalExp(node.getRightExp());
        } else if (node.getOp().isOr()) {
            if ((boolean) evalExp(node.getLeftExp())) return true;
            return evalExp(node.getRightExp());
        }

        // m eval⊢ e ⇒ v
        int left = (int) evalExp(node.getLeftExp());
        // m eval⊢ e1 ⇒ v1
        int right = (int) evalExp(node.getRightExp());

        // v op v1
        switch (node.getOp().getOp()) {
            case ADD: return left + right;
            case SUB: return left - right;
            case MUL: return left * right;
            case DIV:
                int divisor = right;
                if (divisor == 0) throw new RuntimeException("Division by zero");
                return left / divisor;
            case EQ: return left == right;
            case GT: return left > right;
            default: throw new RuntimeException("Unknown binary operator: " + node.getOp());
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
            case "-": return -(int) operand;                  // [moins]
            case "!": return !(boolean) operand;              // [non]
            default: throw new RuntimeException("Unknown unary operator: " + op);
        }
    }

    /**
     * Retrait des déclarations : MEM retrait⊢ DECLS → MEM (page 44)
     * [rdecls] : m retrait⊢ dss → m1, m1 retrait⊢ ds → m2
     * m retrait⊢ decls(ds, dss) → m2
     */
    private void retraitDecls(DeclsNode decls) {
        // Retrait en ordre inverse (comme dans la règle récursive)
        List<DeclNode> declList = decls.getDecls();
        for (int i = declList.size() - 1; i >= 0; i--) {
            DeclNode decl = declList.get(i);
            if (decl instanceof VarNode varNode) {
                stack.retirerDecl(varNode.getIdent(), stack.pop());
            } else if (decl instanceof MethodNode methodNode) {
                stack.retirerDecl(methodNode.getIdent(), stack.pop());
            }
        }
    }

    /**
     * Retrait des variables : MEM retrait⊢ VARS → MEM (page 44)
     * [rvars] : m retrait⊢ dvs → m1, m1 retrait⊢ dv → m2
     * m retrait⊢ vars(dv, dvs) → m2
     */
    private void retraitVars(VarsNode vars) {
        // Retrait en ordre inverse
        List<VarDeclNode> varList = vars.getList();
        for (int i = varList.size() - 1; i >= 0; i--) {
            stack.retirerDecl(varList.get(i).getIdent(), stack.pop());
        }
    }
}
