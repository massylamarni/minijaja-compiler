package fr.amvproject.group3.ast.nodes.params;

import java.util.List;
import java.util.ArrayList;
import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.AstVisitor;

public class EntetesNode extends AstNode {
    private final List<EnteteNode> list = new ArrayList<>();
    
    public void add(EnteteNode i) {
        list.add(i);
    }

    public List<EnteteNode> getList() {
        return list;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitEntetes(this);
    }
}