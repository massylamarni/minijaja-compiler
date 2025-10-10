package fr.amvproject.group3.ast.nodes.exprs;

import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.abstracts.Ident;

public class LengthNode extends ExpNode {
    private final Ident ident;

    public LengthNode(Ident ident) {
        this.ident = ident;
    }

    public Ident getIdent() {
        return ident;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitLength(this);
    }
}