package fr.amvproject.group3.ast.ast;

import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.program.*;
import fr.amvproject.group3.ast.nodes.decls.*;
import fr.amvproject.group3.ast.nodes.instrs.*;
import fr.amvproject.group3.ast.nodes.exprs.*;

import java.util.List;

public class PrintVisitor implements AstVisitor<String> {

    private int indentLevel = 0;

    private String indent() {
        return "  ".repeat(indentLevel);
    }

    private String joinChildren(List<? extends AstNode> nodes) {
        StringBuilder sb = new StringBuilder();
        indentLevel++;
        for (AstNode node : nodes) {
            if (node != null) {
                sb.append("\n").append(node.accept(this));
            }
        }
        indentLevel--;
        return sb.toString();
    }

    // -------- Program --------
    @Override
    public String visitProgram(ProgramNode node) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent()).append("Program(class ").append(node.getClassName()).append(")");
        if (!node.getDecls().isEmpty()) {
            sb.append("\n").append(indent()).append("Decls:");
            sb.append(joinChildren(node.getDecls()));
        }
        sb.append("\n").append(node.getMain().accept(this));
        return sb.toString();
    }

    @Override
    public String visitMainMethod(MethmainNode node) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent()).append("MainMethod");
        if (!node.getVars().isEmpty()) {
            sb.append("\n").append(indent()).append("Vars:");
            sb.append(joinChildren(node.getVars()));
        }
        if (!node.getInstrs().isEmpty()) {
            sb.append("\n").append(indent()).append("Instrs:");
            sb.append(joinChildren(node.getInstrs()));
        }
        return sb.toString();
    }

    // -------- Decls --------
    @Override
    public String visitArrayDecl(ArrayDeclNode node) {
        StringBuilder sb = new StringBuilder();

        return sb.toString();
    }

    @Override
    public String visitConstDecl(ConstDeclNode node) {
        return indent() + "ConstDecl(" + node.getType() + " " + node.getName() +
                " = " + node.getValue().accept(this) + ")";
    }

    @Override
    public String visitMethodDecl(MethodDeclNode node) {
        StringBuilder sb = new StringBuilder();
        
        return sb.toString();
    }

    @Override
    public String visitVarDecl(VarDeclNode node) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent()).append("VarDecl(").append(node.getType()).append(" ").append(node.getName());
        if (node.getInitExpr() != null) {
            sb.append(" = ").append(node.getInitExpr().accept(this));
        }
        sb.append(")");
        return sb.toString();
    }

    // -------- Instrs --------
    @Override
    public String visitAssign(AssignNode node) {
        return indent() + "Assign(" + node.getIdent() + " = " + node.getExpr().accept(this) + ")";
    }

    @Override
    public String visitAssignPlus(AssignPlusNode node) {
        StringBuilder sb = new StringBuilder();
        
        return sb.toString();
    }

    @Override
    public String visitCall(CallNode node) {
        StringBuilder sb = new StringBuilder();
        
        return sb.toString();
    }

    @Override
    public String visitIf(IfNode node) {
        StringBuilder sb = new StringBuilder();
        
        return sb.toString();
    }

    @Override
    public String visitIncr(IncrNode node) {
        StringBuilder sb = new StringBuilder();
        
        return sb.toString();
    }

    @Override
    public String visitReturn(ReturnNode node) {
        StringBuilder sb = new StringBuilder();
        
        return sb.toString();
    }

    @Override
    public String visitWhile(WhileNode node) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent()).append("While(").append(node.getCondition().accept(this)).append(")");
        sb.append("\n").append(indent()).append("Body:");
        sb.append(joinChildren(node.getBody()));
        return sb.toString();
    }

    @Override
    public String visitWrite(WriteNode node) {
        StringBuilder sb = new StringBuilder();
        
        return sb.toString();
    }

    // -------- Exprs --------
    @Override
    public String visitBinaryOp(BinaryOpNode node) {
        return "(" + node.getLeft().accept(this) + " " + node.getOperator() + " " + node.getRight().accept(this) + ")";
    }

    @Override
    public String visitCallExpr(CallExprNode node) {
        StringBuilder sb = new StringBuilder();
        
        return sb.toString();
    }

    @Override
    public String visitLength(LengthNode node) {
        StringBuilder sb = new StringBuilder();
        
        return sb.toString();
    }

    @Override
    public String visitLiteral(LiteralNode node) {
        return node.getValue().toString();
    }

    @Override
    public String visitUnaryOp(UnaryOpNode node) {
        StringBuilder sb = new StringBuilder();
        
        return sb.toString();
    }

    @Override
    public String visitVarRef(VarRefNode node) {
        StringBuilder sb = new StringBuilder();
        
        return sb.toString();
    }
}
