package fr.amvproject.group3.ast.ast;

public class Classe implements ASTNode {
    public final Ident ident;
    public final Decls decls;
    public final MainBlock main;

    public Classe(Ident ident, Decls decls, MainBlock main) {
        this.ident = ident;
        this.decls = decls;
        this.main = main;
    }

    @Override
    public String toString() {
        return "(CLASSE " + ident + " " + decls + " " + main + ")";
    }
}