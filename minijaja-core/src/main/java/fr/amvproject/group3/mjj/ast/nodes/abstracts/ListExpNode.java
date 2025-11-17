package fr.amvproject.group3.mjj.ast.nodes.abstracts;

import java.util.List;

import fr.amvproject.group3.mjj.ast.AstNode;
import fr.amvproject.group3.mjj.ast.AstVisitor;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.ExpNode;

public class ListExpNode extends AstNode {
    private final List<ExpNode> list;

    public ListExpNode(List<ExpNode> list) {
        this.list = list;
    }

    public List<ExpNode> getList() {
        return list;
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof ListExpNode listExpNode && this.list.size() == listExpNode.list.size()) {
            for (int i = 0; i < this.list.size(); i++) {
                if (!this.list.get(i).sameAs(listExpNode.list.get(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitListExp(this);
    }
}
