package fr.amvproject.group3;

import fr.amvproject.group3.ast.*;
import fr.amvproject.group3.ast.nodes.program.*;
import fr.amvproject.group3.ast.nodes.decls.*;
import fr.amvproject.group3.ast.nodes.instrs.*;
import fr.amvproject.group3.ast.nodes.exprs.*;

import java.util.ArrayList;
import java.util.List;

public class CompilerVisitor implements AstVisitor<List<String>> {

    private int labelCounter = 0;
    private String newLabel(String prefix) {
        return prefix + "_" + (labelCounter++);
    }

    // -------- Program --------
    @Override
    public List<String> visitProgram(ProgramNode node) {
        List<String> code = new ArrayList<>();
        code.add("; Class " + node.getClassName());
        for (AstNode decl : node.getDecls()) {
            code.addAll(decl.accept(this));
        }
        code.addAll(node.getMain().accept(this));
        code.add("HALT");
        return code;
    }

    @Override
    public List<String> visitMainMethod(MethmainNode node) {
        List<String> code = new ArrayList<>();
        code.add("; Main Method");
        for (AstNode var : node.getVars()) {
            code.addAll(var.accept(this));
        }
        for (AstNode instr : node.getInstrs()) {
            code.addAll(instr.accept(this));
        }
        return code;
    }

    // -------- Decls --------
    @Override
    public List<String> visitArrayDecl(ArrayDeclNode node) {
        List<String> code = new ArrayList<>();
        
        return code;
    }

    @Override
    public List<String> visitConstDecl(ConstDeclNode node) {
        List<String> code = new ArrayList<>();
        code.add("; const " + node.getName());
        code.addAll(node.getValue().accept(this));
        code.add("STORE " + node.getName());
        return code;
    }

    @Override
    public List<String> visitVarDecl(VarDeclNode node) {
        List<String> code = new ArrayList<>();
        code.add("; var " + node.getName());
        if (node.getInitExpr() != null) {
            code.addAll(node.getInitExpr().accept(this));
            code.add("STORE " + node.getName());
        } else {
            code.add("PUSH 0"); // valeur par défaut
            code.add("STORE " + node.getName());
        }
        return code;
    }

    @Override
    public List<String> visitMethodDecl(MethodDeclNode node) {
        List<String> code = new ArrayList<>();
        
        return code;
    }

    // -------- Instrs --------
    @Override
    public List<String> visitAssign(AssignNode node) {
        List<String> code = new ArrayList<>();
        code.add("; assign " + node.getIdent());
        code.addAll(node.getExpr().accept(this));
        code.add("STORE " + node.getIdent());
        return code;
    }

    @Override
    public List<String> visitAssignPlus(AssignPlusNode node) {
        List<String> code = new ArrayList<>();
        
        return code;
    }

    @Override
    public List<String> visitCall(CallNode node) {
        List<String> code = new ArrayList<>();
        
        return code;
    }

    @Override
    public List<String> visitIf(IfNode node) {
        List<String> code = new ArrayList<>();
        
        return code;
    }

    @Override
    public List<String> visitIncr(IncrNode node) {
        List<String> code = new ArrayList<>();
        
        return code;
    }

    @Override
    public List<String> visitReturn(ReturnNode node) {
        List<String> code = new ArrayList<>();
        
        return code;
    }

    @Override
    public List<String> visitWhile(WhileNode node) {
        List<String> code = new ArrayList<>();
        String startLabel = newLabel("while_start");
        String endLabel = newLabel("while_end");

        code.add(startLabel + ":");
        code.addAll(node.getCondition().accept(this));
        code.add("JZ " + endLabel); // jump if false
        for (AstNode instr : node.getBody()) {
            code.addAll(instr.accept(this));
        }
        code.add("JUMP " + startLabel);
        code.add(endLabel + ":");
        return code;
    }

    @Override
    public List<String> visitWrite(WriteNode node) {
        List<String> code = new ArrayList<>();
        
        return code;
    }

    // -------- Exprs --------
    @Override
    public List<String> visitBinaryOp(BinaryOpNode node) {
        List<String> code = new ArrayList<>();
        code.addAll(node.getLeft().accept(this));
        code.addAll(node.getRight().accept(this));

        switch (node.getOperator()) {
            case "+": code.add("ADD"); break;
            case "-": code.add("SUB"); break;
            case "*": code.add("MUL"); break;
            case "/": code.add("DIV"); break;
            case "==": code.add("EQ"); break;
            case ">": code.add("GT"); break;
            case "&&": code.add("AND"); break;
            case "||": code.add("OR"); break;
            default: code.add("; Unknown op " + node.getOperator());
        }
        return code;
    }

    @Override
    public List<String> visitCallExpr(CallExprNode node) {
        List<String> code = new ArrayList<>();
        
        return code;
    }

    @Override
    public List<String> visitLength(LengthNode node) {
        List<String> code = new ArrayList<>();
        
        return code;
    }

    @Override
    public List<String> visitLiteral(LiteralNode node) {
        List<String> code = new ArrayList<>();
        if (node.getValue() instanceof Integer) {
            code.add("PUSH " + node.getValue());
        } else if (node.getValue() instanceof Boolean) {
            code.add("PUSH " + (((Boolean) node.getValue()) ? 1 : 0));
        } else if (node.getValue() instanceof String) {
            code.add("PUSHS \"" + node.getValue() + "\"");
        }
        return code;
    }

    @Override
    public List<String> visitUnaryOp(UnaryOpNode node) {
        List<String> code = new ArrayList<>();
        
        return code;
    }

    @Override
    public List<String> visitVarRef(VarRefNode node) {
        List<String> code = new ArrayList<>();
        
        return code;
    }
}
