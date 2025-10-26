package fr.amvproject.group3.ast.nodes.abstracts;

import java.util.List;

import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.intermediate.ExpNode;

public class ListExpNode extends AstNode {
    private final List<ExpNode> list;

    public ListExpNode(List<ExpNode> list) {
        this.list = list;
    }

    public List<ExpNode> getList() {
        return list;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitListExp(this);
    }
}