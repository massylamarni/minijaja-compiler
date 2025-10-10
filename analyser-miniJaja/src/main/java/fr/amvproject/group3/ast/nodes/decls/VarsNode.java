package fr.amvproject.group3.ast.nodes.decls;

import java.util.List;
import java.util.ArrayList;
import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.program.DeclsNode;

public class VarsNode extends DeclsNode {
    private final List<VarNode> list = new ArrayList<>();
    
    public void add(VarNode i) {
        list.add(i);
    }

    public List<VarNode> getList() {
        return list;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitVars(this);
    }
}