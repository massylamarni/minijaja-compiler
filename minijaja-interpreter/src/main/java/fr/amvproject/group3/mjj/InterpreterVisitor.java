package fr.amvproject.group3.mjj;

import java.util.ArrayList;
import java.util.List;

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

/**
 * Interpréteur respectant les règles sémantiques du cours MiniJaja
 * Structure : MEM ⊢ INSTR → MEM et MEM eval⊢ EXP ⇒ VAL
 */
@SuppressWarnings("java:S6539") // Visitor pattern: large by design
public class InterpreterVisitor implements AstVisitor<Object> {
    private static final int LOOP_LIMIT = 1000000;
    private static final int RECURSION_LIMIT = 100;
    private int recursionCount = 0;
    private final Stack stack = new Stack();
    private ExecutionContext ctx = new ExecutionContext(stack); // final

    public Stack getStack() {
        return stack;
    }

    public ExecutionContext getCtx() {
      return ctx;
    }

    // ========== PROGRAM ==========
    @Override
    public Object visitProgram(ProgramNode node) {
        ctx.recordNode(node);
        stack.update(false);
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
     * ---
     * [] ⊢ classe(ident(i), dss, mma) → RetirerDecl(ident(i), m3)
     */
    @Override
    public Object visitClass(ClassNode node) {
        ctx.recordNode(node);
        stack.declVar(node.getIdent(), null, new TypeMethNode("void", node.getLine(), node.getColumn()));
        
        node.getDecls().accept(this);
        node.getMain().accept(this);

        retraitDecls(node.getDecls());
        stack.retirerDecl(node.getIdent());
        
        return null;
    }

    /**
     * Règle [decls] (page 38) :
     * m ⊢ ds → m1, m1 ⊢ dss → m2
     * ---
     * m ⊢ decls(ds, dss) → m2
     */
    @Override
    public Object visitDecls(DeclsNode node) {
        for (DeclNode decl : node.getList()) {
            decl.accept(this);
        }
        return null;
    }

    /**
     * Règle [vars] (page 38) :
     * m ⊢ dv → m1, m1 ⊢ dvs → m2
     * ---
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
     * Règle [var] (page 39) :
     * m eval⊢ e ⇒ v
     * ---
     * m ⊢ var(t, ident(i), e) → DeclVar(i, v, t, m)
     */
    @Override
    public Object visitVar(VarNode node) {
        ctx.recordNode(node);
        stack.declVar(node.getIdent(), evalExp(node.getInitExp()), node.getType());
        
        return null;
    }

    /**
     * Règle [tab] (page ?) :
     * m eval⊢ e ⇒ v
     * m eval⊢ e1 ⇒ ind
     * ---
     * m ⊢ tableau(t, ident(i), e1, e) → DeclTab(i, ind, v, t, m)
     */
    @Override
    public Object visitTable(TableNode node) {
        ctx.recordNode(node);
        stack.declTab(node.getIdent(), (int) evalExp(node.getSizeExp()), node.getType());
        
        return null;
    }

    /**
     * Règle [cst] (page 39) :
     * m eval⊢ e ⇒ v
     * ---
     * m ⊢ cst(t, ident(i), e) → DeclCst(i, v, t, m)
     */
    @Override
    public Object visitCst(CstNode node) {
        ctx.recordNode(node);
        stack.declCst(node.getIdent(), evalExp(node.getInitExp()), node.getType(), node.isVcst());
        
        return null;
    }

    /**
     * Règle [main] (page 39) :
     * m ⊢ méthode(t, ident(t), ent, dvs, iss) → DeclMeth(i, méthode(t, ident(i), ent, dvs, iss), t, m)
     */
    public Object visitMethod(MethodNode node) {
        ctx.recordNode(node);
        stack.declMeth(node);
        
        return null;
    }

    /**
     * Règle [main] (page 39) :
     * m ⊢ dvs → m1, m1 ⊢ iss → m2, m2 retrait⊢ dvs → m3
     * ---
     * m ⊢ main(dvs, iss) → m3
     */
    @Override
    public Object visitMain(MainNode node) {
        ctx.recordNode(node);
        node.getVars().accept(this);
        node.getInstrs().accept(this);
        retraitVars(node.getVars());
        
        return null;
    }

    /**
     * Règle [retour] (page 41) :
     * m eval⊢ e ⇒ v
     * ---
     * m ⊢ retour(e) → AffecterVal(VariableClasse(m), v, m)
     */
    @Override
    public Object visitReturn(ReturnNode node) {
        ctx.recordNode(node);
        IdentNode classVar = stack.peekTail().getIdent();
        Object returnValue = evalExp(node.getExp());
        stack.affecterVal(classVar, returnValue); // La valeur de retour est sauvegardé dans la classe
        
        return null;
    }

    /**
     * Règle [appelE] (page 43)
     *  m ⊢ appelI(ident(i), lexp) ⇒ m1
     *  m1 eval⊢ Val(VariableClasse(m1)) ⇒ v
     *  ---
     *  m ⊢ appelE(ident(i), lexp) ⇒ v
     */
    @Override
    public Object visitCallExp(CallExpNode node) {
        if (recursionCount++ > RECURSION_LIMIT) {
            throw new fr.amvproject.group3.exceptions.RuntimeException("Infinite recursion detected !", node.getLine(), node.getColumn());
        }

        ParamsNode paramsNode = stack.parametre(node.getIdent());
        List<ParamNode> listParamNodes = paramsNode.getList();
        List<ExpNode> listExpNodes = (node.getListExp() != null) ? node.getListExp().getList() : new ArrayList<>();

        // ExpParam - declaration des paramètres
        if (!listParamNodes.isEmpty()) {
            for (int i = 0; i < listExpNodes.size(); i++) {
                ParamNode param = listParamNodes.get(i);
                ExpNode argExp = listExpNodes.get(i);
                stack.declVar(param.getIdent(), evalExp(argExp), new TypeMethNode(param.getType().getTypeToken()));
                
            }
        }

        // declaration des variables locales
        VarsNode varsNode = stack.declaration(node.getIdent());
        List<VarDeclNode> listVarDeclNodes = varsNode.getList();
        if (varsNode != null && !listVarDeclNodes.isEmpty()) {
            varsNode.accept(this);
        }

        // execute les iss
        InstrsNode instrsNode = stack.corps(node.getIdent());
        instrsNode.accept(this);

        // on recupere la valeur de retour
        IdentNode returnIdentNode = stack.peekTail().getIdent();
        Object returnValue = stack.val(returnIdentNode);

        // retrait
        if (varsNode != null && !listVarDeclNodes.isEmpty()) {
            retraitVars(varsNode);
        }
        retraitParams(paramsNode);

        return returnValue;
    }

    /**
     * Règle [appelI] (page 41)
     * ExpParam - m ⊢ ExpParam(lexp, Parametre(i,m), m) ⊢ Déclaration(i, m)  ⇒ m1
     * m1 ⊢ corps(i, m) ⇒ m2
     * m2 retrait⊢ Déclaration(i, m) → m3
     * m3 retrait⊢ Parametre(i,m) → m4
     * ---
     * m ⊢ appelI(ident(i), lexp) ⇒ m4
     */
    @Override
    public Object visitCallInstr(CallInstrNode node) {
        visitCallExp(new CallExpNode(node.getIdent(), node.getListExp()));
        return null;
    }

    /**
     * Règle [instrs] (page 40) :
     * m ⊢ is → m1, m1 ⊢ iss → m2
     * ---
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
     * ---
     * m ⊢ affectation(ident(i), e) → AffecterVal(i, v, m)
     *
     * m eval⊢ e ⇒ v, m eval⊢ e1 ⇒ ind
     * ---
     * m ⊢ affectation(tab(ident(i), e1), e) → AffecterValT(i, ind, v, m)
     */
    @Override
    public Object visitAssign(AssignNode node) {
        ctx.recordNode(node);
        Object value = evalExp(node.getExp());
        if (node.getLeftValueNode() instanceof IdentNode identNode) {
            stack.affecterVal(identNode, value);
        } else if (node.getLeftValueNode() instanceof ArrayAccessNode arrayAccessNode) {
            int index = (int) evalExp(arrayAccessNode.getIndexExp());
            try {
                stack.affecterValT(arrayAccessNode.getIdent(), index, value);
            } catch (Exception e) {
                throw new fr.amvproject.group3.exceptions.RuntimeException("Index out of bounds", node.getLine(), node.getColumn());
            }   
        }
        return null;
    }

    /**
     * Règles [somme] et [sommeT] (page 41) :
     * m eval⊢ e ⇒ v
     * ---
     * m ⊢ somme(ident(i), e) → AffecterVal(i, Val(i, m) + v, m)
     *
     * m eval⊢ e ⇒ v, m eval⊢ e1 ⇒ ind
     * ---
     * m ⊢ somme(tab(ident(i), e1), e) → AffecterValT(i, ind, ValT(i, ind, m) + v, m)
     */
    @Override
    public Object visitAddAssign(AddAssignNode node) {
        visitAssign(NodeOperations.addAssignToAssign(node));
        return null;
    }

    /**
     * Règle [incrément] et [incrémentT] (page 40) :
     * m ⊢ incrément(ident(i)) → AffecterVal(i, Val(i, m) + 1, m)
     * 
     * m ⊢ incrément(tab(ident(i), e)) → AffecterValT(i, ind, ValT(i, ind, m) + 1, m)
     */
    @Override
    public Object visitInc(IncNode node) {
        visitAssign(NodeOperations.incToAssign(node));
        return null;
    }

    /**
     * 	m ⊢eval longeur(ident(i)) ⇒ long(I,m)
     */
    @Override
    public Object visitLength(LengthNode node) {
        try {
            ctx.recordNode(node);
            return stack.longT(node.getIdent());
        } catch (Exception e) {
            throw new fr.amvproject.group3.exceptions.RuntimeException("Index out of bounds", node.getLine(), node.getColumn());
        }
    }

    /**
     * Règles [sivrai] et [sifaux] (page 42) :
     * m eval⊢ e ⇒ true, m ⊢ iss → m1
     * ---
     * m ⊢ si(e, iss, iss1) → m1
     *
     * m eval⊢ e ⇒ false, m ⊢ iss1 → m1
     * ---
     * m ⊢ si(e, iss, iss1) → m1
     */
    @Override
    public Object visitIf(IfNode node) {
        ctx.recordNode(node);
        boolean condValue = (boolean) evalExp(node.getCondExp());
        if (condValue) { // Règle [sivrai]
            node.getThenInstrs().accept(this);
        } else if (node.getElseInstrs() != null) { // Règle [sifaux]
            node.getElseInstrs().accept(this);
        }
        return null;
    }

    /**
     * Règles [tantquevrai] et [tantquefaux] (page 42) :
     * m eval⊢ e ⇒ true, m ⊢ iss → m1, m1 ⊢ tantque(e, iss) → m2
     * ---
     * m ⊢ tantque(e, iss) → m2
     *
     * m eval⊢ e ⇒ false
     * ---
     * m ⊢ tantque(e, iss) → m
     */
    @Override
    public Object visitWhile(WhileNode node) {
        ctx.recordNode(node);
        int loopCount = 0;
        boolean condValue = (boolean) evalExp(node.getCondExp());
        while (condValue) { // Règle [tantquevrai]
            if (loopCount++ > LOOP_LIMIT) {
                throw new fr.amvproject.group3.exceptions.RuntimeException("Infinite loop detected !", node.getLine(), node.getColumn());
            }
            node.getInstrs().accept(this);
            condValue = (boolean) evalExp(node.getCondExp());
        }
        return null; // Règle [tantquefaux] : retourne m inchangé
    }

    /**
     * Règle [ecrire] (page 42) :
     * m eval⊢ e ⇒ v
     * ---
     * m ⊢ ecrire(e) → Afficher(v, m)
     */
    @Override
    public Object visitWrite(WriteNode node) {
        ctx.recordNode(node);
        Object value = evalExp(node.getExp());
        if (node.isLn()) {
            System.out.println(value);  // TODO Write in an output buffer instead
        } else {
            System.out.print(value);
        }
        return null;
    }


    @Override
    public Object visitListExp(ListExpNode node) {
        List<Object> listExpValues = new ArrayList<>();
        for (ExpNode exp : node.getList()) {
            listExpValues.add(evalExp(exp));
        }
        return listExpValues;
    }

    /**
     * Règle [op2] (page 43) :
     * m eval⊢ e ⇒ v, m eval⊢ e1 ⇒ v1
     * ---
     * m eval⊢ op2(e, e1) ⇒ v op v1
     */
    @Override
    public Object visitBinaryExp(BinaryExpNode node) {
        Object left = evalExp(node.getLeftExp());
        Object right = evalExp(node.getRightExp());

        switch (node.getOp().getOp()) {
            case AND: return (boolean) left && (boolean) right;
            case OR:  return (boolean) left || (boolean) right;
            case ADD: return (int) left + (int) right;
            case SUB: return (int) left - (int) right;
            case MUL: return (int) left * (int) right;
            case DIV:
                if ((int) right == 0) throw new fr.amvproject.group3.exceptions.RuntimeException("Runtime error: division by zero", node.getLine(), node.getColumn());
                return (int) left / (int) right;
            case EQ:  return  left == right;
            case GT:  return (int) left > (int) right;
            default:  throw new IllegalArgumentException("Unknown binary operator: " + node.getOp().getOp().getName());
        }
    }

    /**
     * Règle [op1] (page ?) :
     * m eval⊢ e ⇒ v
     * ---
     * m eval⊢ op1(e) ⇒ op1 v
     */
    @Override
    public Object visitUnaryExp(UnaryExpNode node) {
        Object operand = evalExp(node.getExp());

        switch (node.getOp().getOp()) {
            case NEG:
                return (int) operand * -1;
            case NOT:
                return !(boolean) operand;
            default: throw new IllegalArgumentException("Unknown unary operator: " + node.getOp().getOp().getName());
        }
    }

    /**
     * Règle [nbre] (page 43)
     * m eval⊢ nbre(n) ⇒ n
     */
    @Override
    public Object visitIntLiteral(IntLiteralNode node) {
        ctx.recordNode(node);
        return node.getValue();
    }

    /**
     * Règles [vrai] et [faux] (page 43)
     * m eval⊢ vrai ⇒ true
     * 
     * m eval⊢ faux ⇒ false
     */
    @Override
    public Object visitBoolLiteral(BoolLiteralNode node) {
        ctx.recordNode(node);
        return node.getValue();
    }

    /**
     * Règle [chaine] (page 43)
     * m eval⊢ chaine(string) ⇒ string
     */
    @Override
    public Object visitStringLiteral(StringLiteralNode node) {
        ctx.recordNode(node);
        return node.getValue();
    }

    /**
     * Règle [ident] (page 43)
     * m eval⊢ ident(i) ⇒ Val(i,m)
     */
    @Override
    public Object visitIdent(IdentNode node) {
        ctx.recordNode(node);
        return stack.val(node);
    }

    /**
     * Règle [tab] (page 43)
     * m eval⊢ e ⇒ v
     * ---
     * m eval⊢ tab(ident(i), e) ⇒ ValT(i, v, m)
     */
    @Override
    public Object visitArrayAccess(ArrayAccessNode node) {
        ctx.recordNode(node);
        Object indexObj = evalExp(node.getIndexExp());
        int index = (int) indexObj;
        try {
            return stack.valT(node.getIdent(), index);
        } catch (Exception e) {
            throw new fr.amvproject.group3.exceptions.RuntimeException("Index out of bounds", node.getLine(), node.getColumn());
        }
    }

    @Override
    public Object visitParams(ParamsNode node) {
        return null;
    }

    @Override
    public Object visitParam(ParamNode node) {
        return null;
    }

    // ========== OTHER NODES ==========

    @Override
    public Object visitType(TypeNode node) {
        return null;
    }

    @Override
    public Object visitTypeMeth(TypeMethNode node) {
        return null;
    }

    @Override
    public Object visitBinaryOp(BinaryOpNode node) {
        return null;
    }

    @Override
    public Object visitUnaryOp(UnaryOpNode node) {
        return null;
    }

    // ========== MÉTHODES AUXILIAIRES ==========

    /**
     * Évalue une expression
     */
    private Object evalExp(ExpNode exp) {
        return exp.accept(this);
    }

    /**
     * [rentête] :	m ⊢retrait entête(t, ident(i)) → RetirerDecl(i, m)
     */
    private void retraitParams(ParamsNode params) {
        if (params == null) return;
        List<ParamNode> paramList = params.getList();
        for (int i = paramList.size() - 1; i >= 0; i--) {
            stack.retirerDecl(paramList.get(i).getIdent());
        }
    }

    /**
     * Règle [rdecls] (page 44)
     * m retrait⊢ dss → m1, m1 retrait⊢ ds → m2
     * ---
     * m retrait⊢ decls(ds, dss) → m2
     */
    private void retraitDecls(DeclsNode decls) {
        if (decls == null) return;
        List<DeclNode> declList = decls.getList();
        for (int i = declList.size() - 1; i >= 0; i--) {
            stack.retirerDecl(declList.get(i).getIdent());
        }
    }

    /**
     * Règle [rvars] (page 44)
     * m retrait⊢ dvs → m1, m1 retrait⊢ dv → m2
     * ---
     * m retrait⊢ vars(dv, dvs) → m2
     */
    private void retraitVars(VarsNode vars) {
        if (vars == null) return;
        List<VarDeclNode> varList = vars.getList();
        for (int i = varList.size() - 1; i >= 0; i--) {
            stack.retirerDecl(varList.get(i).getIdent());
        }
    }
}
