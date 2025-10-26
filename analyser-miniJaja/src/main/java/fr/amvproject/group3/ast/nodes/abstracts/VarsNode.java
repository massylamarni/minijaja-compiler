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
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitVars(this);
    }
}