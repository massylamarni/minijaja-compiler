package fr.amvproject.group3.ast.ast;

public class Longueur extends Exp {
    public final Ident ident;

    public Longueur(Ident ident) {
        this.ident = ident;
    }

    @Override
    public String toString() {
        return "(LONGUEUR " + ident + ")";
    }
}