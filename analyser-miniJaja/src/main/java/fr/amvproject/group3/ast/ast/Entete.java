package fr.amvproject.group3.ast.ast;

public class Entete implements ASTNode {
    public final String type;
    public final Ident ident;

    public Entete(String type, Ident ident) {
        this.type = type;
        this.ident = ident;
    }

    @Override
    public String toString() {
        return "(ENTETE " + type + " " + ident + ")";
    }
}