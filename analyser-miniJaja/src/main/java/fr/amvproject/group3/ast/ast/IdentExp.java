package fr.amvproject.group3.ast.ast;

class IdentExp extends Exp {
    public final Ident ident;

    public IdentExp(Ident ident) {
        this.ident = ident;
    }

    @Override
    public String toString() {
        return ident.toString();
    }
}