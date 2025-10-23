package fr.amvproject.group3.ast;

import java.util.List;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import fr.amvproject.group3.ast.nodes.abstracts.*;
import fr.amvproject.group3.ast.nodes.decls.*;
import fr.amvproject.group3.ast.nodes.exprs.*;
import fr.amvproject.group3.ast.nodes.instrs.*;
import fr.amvproject.group3.ast.nodes.other.*;
import fr.amvproject.group3.ast.nodes.params.*;
import fr.amvproject.group3.ast.nodes.program.*;

public class PrintVisitor implements AstVisitor<String> {
    private static final Logger LOGGER = Logger.getLogger(PrintVisitor.class.getName());
    private int indentLevel = -1;

    private String indent() {
        return "  ".repeat(indentLevel);
    }

    private String joinChildren(List<? extends AstNode> nodes) {
        StringBuilder sb = new StringBuilder();
        indentLevel++;
        for (AstNode node : nodes) {
            if (node != null) {
                sb.append(node.accept(this)).append(";\n");
            }
        }
        indentLevel--;
        return sb.toString();
    }

    private String joinChildrenLinear(List<? extends AstNode> nodes) {
        StringBuilder sb = new StringBuilder();
        indentLevel++;
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i) != null) {
                sb.append(nodes.get(i).accept(this));
                if (i+1 != nodes.size()) sb.append(", ");
            }
        }
        indentLevel--;
        return sb.toString();
    }

    // --------- Nodes ---------
    @Override
    public String visitProgram(ProgramNode node) {
        return joinChildren(node.getClasses());
    }

    @Override
    public String visitClasse(ClasseNode node) {
        StringBuilder sb = new StringBuilder(indent());
        sb.append("class ").append(node.getIdent().accept(this)).append(" {\n");
        if (node.getDecls() != null) sb.append(indent()).append(node.getDecls().accept(this));
        if (node.getMain() != null) sb.append(indent()).append(node.getMain().accept(this));
        sb.append(indent()).append("}\n");
        return sb.toString();
    }

    @Override
    public String visitDecls(DeclsNode node) {
        if (node.getDecls().isEmpty()) return indent() + "/* DECLS empty */\n";
        return indent() + joinChildren(node.getDecls());
    }

    @Override
    public String visitDecl(DeclNode node) {
        return indent() + node.getIdent().accept(this);
    }

    @Override
    public String visitVars(VarsNode node) {
        return joinChildren(node.getList());
    }

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
    public String visitMethode(MethodeNode node) {
        StringBuilder sb = new StringBuilder(indent());
        sb.append(node.getReturnType().accept(this)).append(" ").append(node.getIdent().accept(this)).append("(");
        sb.append(joinChildrenLinear(node.getEntetes().getList())).append(") {\n");
        if (node.getVars() != null) sb.append(node.getVars().accept(this));
        if (node.getInstrs() != null) sb.append(node.getInstrs().accept(this));
        sb.append(indent()).append("}");
        return sb.toString();
    }

    @Override
    public String visitMethMain(MethMainNode node) {
        indentLevel++;
        StringBuilder sb = new StringBuilder(indent()).append("main {\n");
        if (node.getVars() != null) sb.append(node.getVars().accept(this));
        if (node.getInstrs() != null) sb.append(node.getInstrs().accept(this));
        sb.append(indent()).append("}\n");
        indentLevel--;
        return sb.toString();
    }

    @Override
    public String visitEntetes(EntetesNode node) {
        return "(" + joinChildren(node.getList()) + ")";
    }

    @Override
    public String visitEntete(EnteteNode node) {
        return node.getType().accept(this) + " " + node.getIdent().accept(this);
    }
    
    @Override
    public String visitInstrs(InstrsNode node) {
        if (node.getList().isEmpty()) return indent() + "/* INSTRS empty */\n";
        return joinChildren(node.getList());
    }
    
    @Override
    public String visitInstr(InstrNode node) {
        return null;
    }

    @Override
    public String visitListExp(ListExpNode node) {
        if (node.getList() == null || node.getList().isEmpty()) return indent() + "/* LISTEXP empty */";
        return joinChildren(node.getList());
    }

    @Override
    public String visitBinaryExp(BinaryExpNode node) {
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(node.getLeftExp().accept(this));
        sb.append(" ").append(node.getOp().accept(this)).append(" ");
        sb.append(node.getRightExp().accept(this)).append(")");
        return sb.toString();
    }

    @Override
    public String visitUnaryExp(UnaryExpNode node) {
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(node.getOp().accept(this)).append(" ");
        sb.append(node.getExp().accept(this)).append(")");
        return sb.toString();
    }

    @Override
    public String visitAssign(AssignNode node) {
        StringBuilder sb = new StringBuilder(indent());
        sb.append(node.getLeftValue().accept(this));
        sb.append(" = ");
        sb.append(node.getExp().accept(this));
        return sb.toString();
    }

    @Override
    public String visitAddAssign(AddAssignNode node) {
        StringBuilder sb = new StringBuilder(indent());
        sb.append(node.getLeftValueNode().accept(this));
        sb.append(" += ");
        sb.append(node.getExp().accept(this));
        return sb.toString();
    }

    public String visitInc(IncNode node) {
        return indent() + node.getIdent().accept(this) + "++";
    }

    @Override
    public String visitLength(LengthNode node) {
        return indent() + "Longueur(" + node.getIdent().accept(this) + ")";
    }

    @Override
    public String visitIf(IfNode node) {
        StringBuilder sb = new StringBuilder(indent());
        sb.append("if (").append(node.getCondExp().accept(this)).append(") {\n");
        sb.append(node.getThenInstrs().accept(this));
        if (node.getElseInstrs() != null) sb.append(indent()).append("} else {\n").append(node.getElseInstrs().accept(this));
        sb.append(indent()).append("}");
        return sb.toString();
    }

    @Override
    public String visitWhile(WhileNode node) {
        StringBuilder sb = new StringBuilder(indent());
        sb.append("while (").append(node.getCondExp().accept(this)).append(") {\n");
        sb.append(node.getInstrs().accept(this)).append(indent()).append("}");
        return sb.toString();
    }

    @Override
    public String visitWrite(WriteNode node) {
        return indent() + (node.isLn() ? "writeLn" : "write") + "(" + node.getExp().accept(this) + ")";
    }

    @Override
    public String visitRetour(RetourNode node) {
        return indent() + "retour " + node.getExp().accept(this);
    }

    @Override
    public String visitIntLiteral(IntLiteralNode node) {
        return String.valueOf(node.getValue());
    }

    @Override
    public String visitBoolLiteral(BoolLiteralNode node) {
        return node.getValue() ? "true" : "false";
    }

    @Override
    public String visitStringLiteral(StringLiteralNode node) {
        return "\"" + node.getValue() + "\"";
    }

    @Override
    public String visitVarRef(VarRefNode node) {
        return node.getIdent().accept(this);
    }

    @Override
    public String visitArrayAccess(ArrayAccessNode node) {
        return node.getIdent().accept(this) + "[" + node.getIndexExp().accept(this) + "]";
    }

    @Override
    public String visitMethodCallExp(MethodCallExpNode node) {
        StringBuilder sb = new StringBuilder();
        sb.append(node.getIdent().accept(this)).append("(");
        sb.append(joinChildrenLinear(node.getListExp().getList()));
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String visitMethodCallInstr(MethodCallInstrNode node) {
        StringBuilder sb = new StringBuilder(indent());
        sb.append(node.getIdent().accept(this)).append("(");
        sb.append(joinChildrenLinear(node.getListExp().getList()));
        sb.append(")");
        return sb.toString();
    };

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

    @Override
    public String visitIdent(Ident node) {
        return node.getName();
    }

    public void save(String str) {
        final String fileName = "printedProgram.mj";
        Path dir = Paths.get("analyser-miniJaja", "src", "main", "java", "fr", "amvproject", "group3", "output");
        try {
            Files.createDirectories(dir);
            Path filePath = dir.resolve(fileName);
            try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
                writer.write(str);
                LOGGER.info(() -> "File saved successfully to " + filePath.toAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
