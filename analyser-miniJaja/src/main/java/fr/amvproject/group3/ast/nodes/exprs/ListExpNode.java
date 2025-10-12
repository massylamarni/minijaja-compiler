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

    public String evaluate() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            ExpNode exp = list.get(i);
            sb.append(exp.evaluate());
            if (i < list.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitListExp(this);
    }
}