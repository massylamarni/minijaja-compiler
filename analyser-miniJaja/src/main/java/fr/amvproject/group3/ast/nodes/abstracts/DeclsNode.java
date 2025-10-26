package fr.amvproject.group3.ast.nodes.abstracts;

import java.util.List;

import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.intermediate.DeclNode;

public class DeclsNode extends AstNode {
    private final List<DeclNode> list;

    public DeclsNode(List<DeclNode> list) {
        this.list = list;
    }

    public List<DeclNode> getDecls() {
        return list;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitDecls(this);
    }
}