package fr.amvproject.group3.ast.ast;

public class Tableau extends Decl {
    public final Exp taille;

    public Tableau(String type, Ident ident, Exp taille) {
        super(type + "[]", ident, null);
        this.taille = taille;
    }

    @Override
    public String toString() {
        return "(TABLEAU " + type + " " + ident + " " + taille + ")";
    }
}