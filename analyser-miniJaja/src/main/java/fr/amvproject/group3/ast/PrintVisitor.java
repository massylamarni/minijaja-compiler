package fr.amvproject.group3.ast;

import java.util.List;

import fr.amvproject.group3.ast.nodes.abstracts.*;
import fr.amvproject.group3.ast.nodes.decls.*;
import fr.amvproject.group3.ast.nodes.exprs.*;
import fr.amvproject.group3.ast.nodes.instrs.*;
import fr.amvproject.group3.ast.nodes.other.*;
import fr.amvproject.group3.ast.nodes.params.*;
import fr.amvproject.group3.ast.nodes.program.*;

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

    // --------- Abstracts ---------
    @Override
    public String visitIdent(Ident node) {
        return node.getName();
    }

    @Override
    public String visitIdentExp(IdentExp node) {
        return node.getIdent().accept(this);
    }

    // --------- Decls ---------
    @Override
    public String visitVar(VarNode node) {
        StringBuilder sb = new StringBuilder(indent());
        if (node.isFinal()) sb.append("final ");
        sb.append(node.getType().accept(this)).append(" ").append(node.getIdent().accept(this));
        if (node.getSizeExp() != null) sb.append("[").append(node.getSizeExp().accept(this)).append("]");
        if (node.getInitExp() != null) sb.append(" = ").append(node.getInitExp().accept(this));
        return sb.toString();
    }

    @Override
    public String visitVars(VarsNode node) {
        return joinChildren(node.getList());
    }

    @Override
    public String visitMethMain(MethMainNode node) {
        StringBuilder sb = new StringBuilder(indent() + "(MAIN");
        if (node.getVars() != null) sb.append("\n").append(node.getVars().accept(this));
        if (node.getInstrs() != null) sb.append("\n").append(node.getInstrs().accept(this));
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String visitMethode(MethodeNode node) {
        StringBuilder sb = new StringBuilder(indent() + "(METHODE ");
        sb.append(node.getReturnType().accept(this)).append(" ").append(node.getIdent().accept(this));
        sb.append("\nENTETES").append(joinChildren(node.getEntetes().getList()));
        if (node.getVars() != null) sb.append("\n").append(node.getVars().accept(this));
        if (node.getInstrs() != null) sb.append("\n").append(node.getInstrs().accept(this));
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String visitDecl(DeclNode node) {
        return indent() + "Decl(" + node.getIdent().accept(this) + ")";
    }

    @Override
    public String visitDecls(DeclsNode node) {
        if (node.getDecls().isEmpty()) return indent() + "(DECLS empty)";
        StringBuilder sb = new StringBuilder(indent() + "(DECLS");
        sb.append(joinChildren(node.getDecls()));
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String visitProgram(ProgramNode node) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent()).append("(PROGRAM");

        sb.append(joinChildren(node.getClasses()));

        sb.append("\n").append(indent()).append(")");
        return sb.toString();
    }


    @Override
    public String visitClasse(ClasseNode node) {
        StringBuilder sb = new StringBuilder(indent() + "(CLASSE " + node.getIdent().accept(this));
        if (node.getDecls() != null) sb.append("\n").append(node.getDecls().accept(this));
        if (node.getMain() != null) sb.append("\n").append(node.getMain().accept(this));
        sb.append(")");
        return sb.toString();
    }

    // --------- Exprs ---------
    @Override
    public String visitBinaryExp(BinaryExpNode node) {
        return indent() + "(" + node.getLeftExp().accept(this) + " " + node.getOp().accept(this) + " " + node.getRightExp().accept(this) + ")";
    }

    @Override
    public String visitUnaryExp(UnaryExpNode node) {
        return indent() + "(" + node.getOp().accept(this) + " " + node.getExp().accept(this) + ")";
    }

    @Override
    public String visitBoolLiteral(BoolLiteralNode node) {
        return indent() + (node.getValue() ? "vrai" : "faux");
    }

    @Override
    public String visitIntLiteral(IntLiteralNode node) {
        return indent() + node.getValue();
    }

    @Override
    public String visitLength(LengthNode node) {
        return indent() + "(LONGUEUR " + node.getIdent().accept(this) + ")";
    }

    @Override
    public String visitListExp(ListExpNode node) {
        if (node.getList() == null || node.getList().isEmpty()) return indent() + "(LISTEXP empty)";
        StringBuilder sb = new StringBuilder(indent() + "(LISTEXP");
        sb.append(joinChildren(node.getList()));
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String visitStringLiteral(StringLiteralNode node) {
        return indent() + "\"" + node.getValue() + "\"";
    }

    @Override
    public String visitVarRef(VarRefNode node) {
        return indent() + node.getIdent().accept(this);
    }

    @Override
    public String visitArrayAccess(ArrayAccessNode node) {
        return indent() + node.getIdent().accept(this) + "[" + node.getIndexExp().accept(this) + "]";
    }

    @Override
    public String visitMethodCallExp(MethodCallExpNode node) {
        StringBuilder sb = new StringBuilder(indent() + "(CALL " + node.getIdent().accept(this));
        for (ExpNode arg : node.getListExp().getList()) {
            sb.append(" ").append(arg.accept(this));
        }
        sb.append(")");
        return sb.toString();
    }

    // --------- Instrs ---------
    @Override
    public String visitInstrs(InstrsNode node) {
        if (node.getList().isEmpty()) return indent() + "(INSTRS empty)";
        return joinChildren(node.getList());
    }

    @Override
    public String visitAssign(AssignNode node) {
        return indent() + node.getLeftValue().accept(this) + " = " + node.getExp().accept(this);
    }

    @Override
    public String visitAddAssign(AddAssignNode node) {
        return indent() + node.getLeftValueNode().accept(this) + " += " + node.getExp().accept(this);
    }

    @Override
    public String visitIf(IfNode node) {
        StringBuilder sb = new StringBuilder(indent() + "(IF " + node.getCondExp().accept(this));
        sb.append("\n").append(node.getThenInstrs().accept(this));
        if (node.getElseInstrs() != null) sb.append("\n").append(indent() + "(ELSE " + node.getElseInstrs().accept(this));
        sb.append(")");
        return sb.toString();
    }

    public String visitInc(IncNode node) {
        return indent() + "(INCREMENT " + node.getIdent().accept(this) + ")";
    }

    @Override
    public String visitMethodCallInstr(MethodCallInstrNode node) {
        StringBuilder sb = new StringBuilder();
        sb.append("(APPELI ").append(node.getIdent()).append(" (LISTEXP");
        for (ExpNode arg : node.getListExp().getList()) sb.append(" ").append(arg);
        sb.append("))");
        return sb.toString();
    };

    @Override
    public String visitWhile(WhileNode node) {
        return indent() + "(WHILE " + node.getCondExp().accept(this) + "\n" + node.getInstrs().accept(this) + ")";
    }

    @Override
    public String visitWrite(WriteNode node) {
        return indent() + "(" + (node.isLn() ? "WRITELN" : "WRITE") + " " + node.getExp().accept(this) + ")";
    }

    @Override
    public String visitRetour(RetourNode node) {
        return indent() + "(RETURN " + node.getExp().accept(this) + ")";
    }

    // --------- Other ---------
    @Override
    public String visitType(TypeNode node) {
        return node.getType().getName();
    }

    @Override
    public String visitBinaryOp(BinaryOpNode node) {
        return node.getOp().getName();
    }

    @Override
    public String visitUnaryOp(UnaryOpNode node) {
        return node.getOp().getName();
    }

    // --------- Params ---------
    @Override
    public String visitEntete(EnteteNode node) {
        return indent() + "(PARAM " + node.getIdent().accept(this) + " " + node.getIdent().accept(this) + ")";
    }

    @Override
    public String visitEntetes(EntetesNode node) {
        return joinChildren(node.getList());
    }

}
