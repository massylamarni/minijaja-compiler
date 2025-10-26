package fr.amvproject.group3.ast.nodes.abstracts;

import java.util.List;

import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.finals.ParamNode;

public class ParamsNode extends AstNode {
    private final List<ParamNode> list;
    
    public ParamsNode(List<ParamNode> list) {
        this.list = list;
    }

    public List<ParamNode> getList() {
        return list;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitParams(this);
    }
}