package fr.amvproject.group3.ast.ast;

public class AccesTableau extends Exp {
    public final Ident ident;
    public final Exp index;

    public AccesTableau(Ident ident, Exp index) {
        this.ident = ident;
        this.index = index;
    }

    @Override
    public String toString() {
        return ident + "[" + index + "]";
    }
}