package fr.amvproject.group3.ast;

import java.util.ArrayList;
import java.util.List;

import fr.amvproject.group3.ast.nodes.abstracts.*;
import fr.amvproject.group3.ast.nodes.decls.*;
import fr.amvproject.group3.ast.nodes.exprs.*;
import fr.amvproject.group3.ast.nodes.instrs.*;
import fr.amvproject.group3.ast.nodes.other.*;
import fr.amvproject.group3.ast.nodes.params.*;
import fr.amvproject.group3.ast.nodes.program.*;
import fr.amvproject.group3.memory.Stack;

/**
 * Interpréteur respectant les règles sémantiques du cours MiniJaja
 * Structure : MEM ⊢ INSTR → MEM et MEM eval⊢ EXP ⇒ VAL
 */
public class InterpreterVisitor implements AstVisitor<Object> {
    private final Stack stack = new Stack();

    public Stack getStack() {
        return stack;
    }

    // ========== PROGRAM ==========
    @Override
    public Object visitProgram(ProgramNode node) {
        for (ClasseNode classe : node.getClasses()) {
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
    public Object visitClasse(ClasseNode node) {
        // 1. DeclVar(i, w, w, []) - Déclarer une variable pour la classe (omega)
        stack.declVar(node.getIdent(), null, new TypeNode("void"));

        // 2. m1 ⊢ dss → m2 - Traiter les déclarations
        node.getDecls().accept(this);

        // 3. m2 ⊢ mma → m3 - Exécuter le main
        node.getMain().accept(this);

        // 4. m3 retrait⊢ dss → m4 - Retrait des déclarations
        retraitDecls(node.getDecls());

        // 5. RetirerDecl(ident(i), m4) - Retirer la variable de classe
        stack.retirerDecl(node.getIdent());

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
        for (VarNode var : node.getList()) {
            var.accept(this);
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
    public Object visitVar(VarNode node) {
        String varObj = node.getVarObj();
        Object value = null;

        // m eval⊢ e ⇒ v : Évaluer l'expression d'initialisation
        if (node.getInitExp() != null) {
            value = evalExp(node.getInitExp());
        } else if (node.getSizeExp() == null) {
            // omega (valeur par défaut)
            String type = node.getType().getType().getName();
            if ("int".equals(type)) {
                value = 0; // ou null selon votre implémentation de omega
            } else if ("boolean".equals(type)) {
                value = false;
            }
        }

        // Application de la règle selon le type
        if ("var".equals(varObj)) {
            // DeclVar(i, v, t, m)
            stack.declVar(node.getIdent(), value, node.getType());
        } else if ("cst".equals(varObj)) {
            // DeclCst(i, v, t, m)
            // Note : la distinction vcst/cst est gérée dans Stack
            boolean isVcst = (value == null); // omega → vcst
            stack.declCst(node.getIdent(), value, node.getType(), isVcst);
        } else if ("tab".equals(varObj)) {
            // DeclTab(i, v, t, m)
            Object sizeObj = evalExp(node.getSizeExp());
            int size = ((Number) sizeObj).intValue();
            Object[] arr = new Object[size];
            // Initialiser le tableau avec des valeurs par défaut
            String type = node.getType().getType().getName();
            Object defaultVal = "int".equals(type) ? 0 : false;
            for (int i = 0; i < size; i++) {
                arr[i] = defaultVal;
            }
            stack.declTab(node.getIdent(), arr, node.getType());
        }
        return null;
    }

    /**
     * Règle [méthode] (page 39) :
     * m ⊢ méthode(t, ident(i), ent, dvs, iss) → DeclMeth(i, méthode(...), t, m)
     */
    @Override
    public Object visitMethode(MethodeNode node) {
        stack.declMeth(node.getIdent(), node.getReturnType(), node.getEntetes(),
                node.getVars(), node.getInstrs());
        return null;
    }

    /**
     * Règle [main] (page 39) :
     * m ⊢ dvs → m1, m1 ⊢ iss → m2, m2 retrait⊢ dvs → m3
     * m ⊢ main(dvs, iss) → m3
     */
    @Override
    public Object visitMethMain(MethMainNode node) {
        // 1. m ⊢ dvs → m1 : Traiter les variables locales
        node.getVars().accept(this);

        // 2. m1 ⊢ iss → m2 : Exécuter les instructions
        node.getInstrs().accept(this);

        // 3. m2 retrait⊢ dvs → m3 : Retirer les variables locales
        retraitVars(node.getVars());

        return null;
    }

    @Override
    public Object visitEntetes(EntetesNode node) {
        return null;
    }

    @Override
    public Object visitEntete(EnteteNode node) {
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

    @Override
    public Object visitInstr(InstrNode node) {
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

        if (node.getLeftValue() instanceof VarRefNode) {
            // Règle [affectation]
            VarRefNode varRef = (VarRefNode) node.getLeftValue();
            stack.affecterVal(varRef.getIdent(), value);
        } else if (node.getLeftValue() instanceof ArrayAccessNode) {
            // Règle [affectationT]
            ArrayAccessNode arrayAccess = (ArrayAccessNode) node.getLeftValue();
            // m eval⊢ e1 ⇒ ind
            int index = ((Number) evalExp(arrayAccess.getIndexExp())).intValue();
            stack.affecterValT(arrayAccess.getIdent(), index, value);
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

        if (node.getLeftValueNode() instanceof VarRefNode) {
            // Règle [somme]
            VarRefNode varRef = (VarRefNode) node.getLeftValueNode();
            Object currentValue = stack.val(varRef.getIdent());
            int result = ((Number) currentValue).intValue() + ((Number) addValue).intValue();
            stack.affecterVal(varRef.getIdent(), result);
        } else if (node.getLeftValueNode() instanceof ArrayAccessNode) {
            // Règle [sommeT]
            ArrayAccessNode arrayAccess = (ArrayAccessNode) node.getLeftValueNode();
            int index = ((Number) evalExp(arrayAccess.getIndexExp())).intValue();
            Object currentValue = stack.valT(arrayAccess.getIdent(), index);
            int result = ((Number) currentValue).intValue() + ((Number) addValue).intValue();
            stack.affecterValT(arrayAccess.getIdent(), index, result);
        }
        return null;
    }

    /**
     * Règle [incrément] (page 40) :
     * m ⊢ incrément(ident(i)) → AffecterVal(i, Val(i, m) + 1, m)
     */
    @Override
    public Object visitInc(IncNode node) {
        Object currentValue = stack.val(node.getIdent());
        int newValue = ((Number) currentValue).intValue() + 1;
        stack.affecterVal(node.getIdent(), newValue);
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
        Object condValue = evalExp(node.getCondExp());
        boolean condition = (boolean) condValue;

        if (condition) {
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
    public Object visitRetour(RetourNode node) {
        // m eval⊢ e ⇒ v
        Object value = evalExp(node.getExp());
        // AffecterVal(VariableClasse(m), v, m)
        // Note : Cette fonctionnalité doit être gérée par Stack.callMethod
        return value;
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
    public Object visitMethodCallInstr(MethodCallInstrNode node) {
        List<Object> args = (List<Object>) node.getListExp().accept(this);
        // La logique complète est dans Stack.callMethod
        stack.callMethod(node.getIdent(), args);
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
    public Object visitVarRef(VarRefNode node) {
        return stack.val(node.getIdent());
    }

    /**
     * [tab] : m eval⊢ e ⇒ v
     * m eval⊢ tab(ident(i), e) ⇒ ValT(i, v, m)
     */
    @Override
    public Object visitArrayAccess(ArrayAccessNode node) {
        int index = ((Number) evalExp(node.getIndexExp())).intValue();
        return stack.valT(node.getIdent(), index);
    }

    /**
     * Règle [appelE] (page 43) :
     * m ⊢ appelI(ident(i), lexp) → m1
     * m1 eval⊢ VariableClasse(m1) ⇒ v
     * m eval⊢ appelE(ident(i), lexp) ⇒ v
     */
    @Override
    public Object visitMethodCallExp(MethodCallExpNode node) {
        List<Object> args = (List<Object>) node.getListExp().accept(this);
        return stack.callMethod(node.getIdent(), args);
    }

    // ========== OTHER NODES ==========
    @Override
    public Object visitType(TypeNode node) {
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

    @Override
    public Object visitIdent(Ident node) {
        return node.getName();
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
        String op = node.getOp().getOp().getName();

        // Court-circuit pour && et ||
        if ("&&".equals(op)) {
            Object left = evalExp(node.getLeftExp());
            if (!(Boolean) left) return false;
            return evalExp(node.getRightExp());
        } else if ("||".equals(op)) {
            Object left = evalExp(node.getLeftExp());
            if ((Boolean) left) return true;
            return evalExp(node.getRightExp());
        }

        // m eval⊢ e ⇒ v
        Object left = evalExp(node.getLeftExp());
        // m eval⊢ e1 ⇒ v1
        Object right = evalExp(node.getRightExp());

        // v op v1
        switch (op) {
            case "+": return ((Number) left).intValue() + ((Number) right).intValue();
            case "-": return ((Number) left).intValue() - ((Number) right).intValue();
            case "*": return ((Number) left).intValue() * ((Number) right).intValue();
            case "/":
                int divisor = ((Number) right).intValue();
                if (divisor == 0) throw new RuntimeException("Division by zero");
                return ((Number) left).intValue() / divisor;
            case "==": return left.equals(right);
            case ">": return ((Number) left).intValue() > ((Number) right).intValue();
            default: throw new RuntimeException("Unknown binary operator: " + op);
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
            case "-": return -((Number) operand).intValue();  // [moins]
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
            if (decl instanceof VarNode) {
                stack.retirerDecl(((VarNode) decl).getIdent());
            } else if (decl instanceof MethodeNode) {
                stack.retirerDecl(((MethodeNode) decl).getIdent());
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
        List<VarNode> varList = vars.getList();
        for (int i = varList.size() - 1; i >= 0; i--) {
            stack.retirerDecl(varList.get(i).getIdent());
        }
    }
}