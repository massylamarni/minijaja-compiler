package fr.amvproject.group3;

import fr.amvproject.group3.ast.*;
import fr.amvproject.group3.ast.nodes.program.*;
import fr.amvproject.group3.ast.nodes.decls.*;
import fr.amvproject.group3.ast.nodes.instrs.*;
import fr.amvproject.group3.ast.nodes.exprs.*;

import java.util.HashMap;
import java.util.Map;

public class InterpreterVisitor implements AstVisitor<Void> {

    private Map<String, Integer> memory = new HashMap<>();

    public Void printMemory() {
        System.out.println(memory);
        return null;
    }

    // -------- Program --------
    @Override
    public Void visitProgram(ProgramNode node) {
        return node.getMain().accept(this);
    }

    @Override
    public Void visitMainMethod(MethmainNode node) {
        for (AstNode var : node.getVars()) {
            var.accept(this);
        }
        for (AstNode instr : node.getInstrs()) {
            instr.accept(this);
        }
        return null;
    }

    // -------- Declarations --------
    @Override
    public Void visitArrayDecl(ArrayDeclNode node) {
        
        return null;
    }

    @Override
    public Void visitConstDecl(ConstDeclNode node) {
        int value = evaluate(node.getValue());
        memory.put(node.getName(), value);
        return null;
    }

    @Override
    public Void visitMethodDecl(MethodDeclNode node) {
        
        return null;
    }

    @Override
    public Void visitVarDecl(VarDeclNode node) {
        int value = 0;
        if (node.getInitExpr() != null) {
            value = evaluate(node.getInitExpr());
        }
        memory.put(node.getName(), value);
        return null;
    }

    // -------- Instructions --------
    @Override
    public Void visitAssign(AssignNode node) {
        int value = evaluate(node.getExpr());
        memory.put(node.getIdent(), value);
        return null;
    }

    @Override
    public Void visitAssignPlus(AssignPlusNode node) {
        
        return null;
    }

    @Override
    public Void visitCall(CallNode node) {
        
        return null;
    }

    @Override
    public Void visitIf(IfNode node) {
        
        return null;
    }

    @Override
    public Void visitIncr(IncrNode node) {
        
        return null;
    }

    @Override
    public Void visitReturn(ReturnNode node) {
        
        return null;
    }

    @Override
    public Void visitWhile(WhileNode node) {
        while (evaluate(node.getCondition()) != 0) {
            for (AstNode instr : node.getBody()) {
                instr.accept(this);
            }
        }
        return null;
    }

    @Override
    public Void visitWrite(WriteNode node) {
        
        return null;
    }

    // -------- Expressions --------
    private int evaluate(AstNode expr) {
        if (expr instanceof LiteralNode) {
            return (Integer) ((LiteralNode) expr).getValue();
        } else if (expr instanceof BinaryOpNode) {
            BinaryOpNode bin = (BinaryOpNode) expr;
            int left = evaluate(bin.getLeft());
            int right = evaluate(bin.getRight());
            switch (bin.getOperator()) {
                case "+": return left + right;
                case "-": return left - right;
                case "*": return left * right;
                case "/": return left / right;
                case ">": return left > right ? 1 : 0;
                case "==": return left == right ? 1 : 0;
                case "&&": return (left != 0 && right != 0) ? 1 : 0;
                case "||": return (left != 0 || right != 0) ? 1 : 0;
                default: throw new RuntimeException("Opérateur inconnu: " + bin.getOperator());
            }
        } else if (expr instanceof VarDeclNode) {
            return memory.get(((VarDeclNode) expr).getName());
        } else if (expr instanceof AssignNode) {
            expr.accept(this);
            return memory.get(((AssignNode) expr).getIdent());
        }
        throw new RuntimeException("Expression inconnue : " + expr);
    }

    @Override
    public Void visitBinaryOp(BinaryOpNode node) { return null; }
    @Override
    public Void visitCallExpr(CallExprNode node) { return null; }
    @Override
    public Void visitLength(LengthNode node) { return null; }
    @Override
    public Void visitLiteral(LiteralNode node) { return null; }
    @Override
    public Void visitUnaryOp(UnaryOpNode node) { return null; }
    @Override
    public Void visitVarRef(VarRefNode node) { return null; }
}
