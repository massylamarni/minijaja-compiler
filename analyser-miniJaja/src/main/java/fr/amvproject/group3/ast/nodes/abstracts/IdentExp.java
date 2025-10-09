package fr.amvproject.group3.ast.nodes.abstracts;

import fr.amvproject.group3.ast.nodes.exprs.ExpNode;

public class IdentExp extends ExpNode {
    public final Ident ident;

    public IdentExp(Ident ident) {
        this.ident = ident;
    }

    @Override
    public String toString() {
        return ident.toString();
    }
}