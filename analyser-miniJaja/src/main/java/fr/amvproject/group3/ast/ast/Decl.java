package fr.amvproject.group3.ast.ast;

public class Decl implements ASTNode {
    public final String type;
    public final Ident ident;
    public final Exp exp; // Peut être null si pas d'initialisation

    public Decl(String type, Ident ident, Exp exp) {
        this.type = type;
        this.ident = ident;
        this.exp = exp;
    }

    @Override
    public String toString() {
        return "(DECL " + type + " " + ident + (exp != null ? " " + exp : "") + ")";
    }
}