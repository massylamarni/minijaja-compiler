package fr.amvproject.group3.ast.nodes.exprs;

import java.util.List;
import java.util.ArrayList;
import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.AstVisitor;

public class ListExpNode extends AstNode {
    private final List<ExpNode> list = new ArrayList<>();
    
    public void add(ExpNode i) {
        list.add(i);
    }

    public List<ExpNode> getList() {
        return list;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitListExp(this);
    }
}