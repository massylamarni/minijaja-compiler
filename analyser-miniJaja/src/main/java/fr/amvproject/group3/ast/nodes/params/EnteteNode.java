package fr.amvproject.group3.ast.nodes.params;

import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.nodes.abstracts.Ident;

public class EnteteNode implements AstNode {
    public final String type;
    public final Ident ident;

    public EnteteNode(String type, Ident ident) {
        this.type = type;
        this.ident = ident;
    }

    @Override
    public String toString() {
        return "(ENTETE " + type + " " + ident + ")";
    }
}