package fr.amvproject.group3.ast.nodes.exprs;

import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.ast.nodes.intermediate.ExpNode;

public class LengthNode extends ExpNode {
    private final IdentNode ident;

    public LengthNode(IdentNode ident) {
        this.ident = ident;
    }

    public IdentNode getIdent() {
        return ident;
    }

    @Override
    public String evaluate() {
        return ident.getName();
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitLength(this);
    }
}