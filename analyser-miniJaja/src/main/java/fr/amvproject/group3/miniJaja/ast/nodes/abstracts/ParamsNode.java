package fr.amvproject.group3.miniJaja.ast.nodes.abstracts;

import java.util.List;

import fr.amvproject.group3.miniJaja.ast.AstNode;
import fr.amvproject.group3.miniJaja.ast.AstVisitor;
import fr.amvproject.group3.miniJaja.ast.nodes.finals.ParamNode;

public class ParamsNode extends AstNode {
    private final List<ParamNode> list;

    public ParamsNode(List<ParamNode> list) {
        this.list = list;
    }

    public List<ParamNode> getList() {
        return list;
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof ParamsNode paramsNode && this.list.size() == paramsNode.list.size()) {
            for (int i = 0; i < this.list.size(); i++) {
                if (!this.list.get(i).sameAs(paramsNode.list.get(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitParams(this);
    }
}
