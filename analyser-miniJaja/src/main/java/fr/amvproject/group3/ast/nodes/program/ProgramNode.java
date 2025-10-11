package fr.amvproject.group3.ast.nodes.program;

import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.AstVisitor;

import java.util.List;

public class ProgramNode extends AstNode {
    private final List<ClasseNode> classes;

    public ProgramNode(List<ClasseNode> classes) {
        this.classes = classes;
    }

    public List<ClasseNode> getClasses() {
        return classes;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitProgram(this);
    }
}
