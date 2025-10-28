package fr.amvproject.group3.ast.nodes.abstracts;

import java.util.List;

import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.intermediate.InstrNode;

public class InstrsNode extends AstNode {
    private final List<InstrNode> list;

    public InstrsNode(List<InstrNode> list) {
        this.list = list;
    }

    public List<InstrNode> getList() {
        return list;
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof InstrsNode instrsNode && this.list.size() == instrsNode.list.size()) {
            for (int i = 0; i < this.list.size(); i++) {
                if (!this.list.get(i).sameAs(instrsNode.list.get(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitInstrs(this);
    }
}