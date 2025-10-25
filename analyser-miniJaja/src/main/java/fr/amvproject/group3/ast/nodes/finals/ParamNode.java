package fr.amvproject.group3.ast.nodes.finals;

import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.other.TypeNode;

public class ParamNode extends AstNode {
    private final TypeNode type;
    private final IdentNode ident;

    public ParamNode(TypeNode type, IdentNode ident) {
        this.type = type;
        this.ident = ident;
    }

    public TypeNode getType() {
        return type;
    }

    public IdentNode getIdent() {
        return ident;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitParam(this);
    }
}