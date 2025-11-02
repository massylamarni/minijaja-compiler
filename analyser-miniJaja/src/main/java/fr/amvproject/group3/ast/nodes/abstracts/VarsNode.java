package fr.amvproject.group3.ast.nodes.abstracts;

import java.util.List;

import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.intermediate.VarDeclNode;

public class VarsNode extends AstNode {
    private final List<VarDeclNode> list;
    
    public VarsNode(List<VarDeclNode> list) {
        this.list = list;
    }

    public List<VarDeclNode> getList() {
        return list;
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof VarsNode varsNode && this.list.size() == varsNode.list.size()) {
            for (int i = 0; i < this.list.size(); i++) {
                if (!this.list.get(i).sameAs(varsNode.list.get(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitVars(this);
    }
}