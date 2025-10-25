package fr.amvproject.group3.ast.nodes.abstracts;

import java.util.List;
import java.util.ArrayList;

import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.intermediate.InstrNode;

public class InstrsNode extends AstNode {
    private final List<InstrNode> list = new ArrayList<>();
    
    public void add(InstrNode i) {
        list.add(i);
    }

    public List<InstrNode> getList() {
        return list;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitInstrs(this);
    }
}