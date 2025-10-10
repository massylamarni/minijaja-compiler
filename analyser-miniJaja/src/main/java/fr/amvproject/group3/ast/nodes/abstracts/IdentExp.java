package fr.amvproject.group3.ast.nodes.abstracts;

import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.exprs.ExpNode;

public class IdentExp extends ExpNode {
    private final Ident ident;

    public IdentExp(Ident ident) {
        this.ident = ident;
    }

    public Ident getIdent() {
        return ident;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitIdentExp(this);
    }
}