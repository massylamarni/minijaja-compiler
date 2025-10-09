package fr.amvproject.group3.ast.nodes.exprs;

import fr.amvproject.group3.ast.nodes.abstracts.Ident;

public class LengthNode extends ExpNode {
    public final Ident ident;

    public LengthNode(Ident ident) {
        this.ident = ident;
    }

    @Override
    public String toString() {
        return "(LONGUEUR " + ident + ")";
    }
}