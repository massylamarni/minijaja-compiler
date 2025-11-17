package fr.amvproject.group3.mjj.ast.nodes.finals;

import fr.amvproject.group3.mjj.ast.AstNode;
import fr.amvproject.group3.mjj.ast.AstVisitor;
import fr.amvproject.group3.mjj.ast.nodes.other.TypeNode;

public class ParamNode extends AstNode {
    private final TypeNode typeNode;
    private final IdentNode identNode;

    public ParamNode(TypeNode typeNode, IdentNode identNode) {
        this.typeNode = typeNode;
        this.identNode = identNode;
    }

    public TypeNode getType() {
        return typeNode;
    }

    public IdentNode getIdent() {
        return identNode;
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof ParamNode paramNode) {
            return this.typeNode.sameAs(paramNode.typeNode) && this.identNode.sameAs(paramNode.identNode);
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitParam(this);
    }
}
