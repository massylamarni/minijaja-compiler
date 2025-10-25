package fr.amvproject.group3.ast.nodes.abstracts;

import java.util.List;
import java.util.ArrayList;

import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.finals.ParamNode;

public class ParamsNode extends AstNode {
    private final List<ParamNode> list = new ArrayList<>();
    
    public void add(ParamNode i) {
        list.add(i);
    }

    public List<ParamNode> getList() {
        return list;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitParams(this);
    }
}