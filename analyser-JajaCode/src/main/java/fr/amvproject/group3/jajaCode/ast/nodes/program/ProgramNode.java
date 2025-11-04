package fr.amvproject.group3.jajaCode.ast.nodes.program;

import fr.amvproject.group3.jajaCode.ast.AstNode;
import fr.amvproject.group3.jajaCode.ast.AstVisitor;

import java.util.List;

public class ProgramNode extends AstNode {
    private final List<ClassNode> list;

    public ProgramNode(List<ClassNode> list) {
        this.list = list;
    }

    public List<ClassNode> getClasses() {
        return list;
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof ProgramNode programNode && this.list.size() == programNode.list.size()) {
            for (int i = 0; i < this.list.size(); i++) {
                if (!this.list.get(i).sameAs(programNode.list.get(i))) return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitProgram(this);
    }
}
