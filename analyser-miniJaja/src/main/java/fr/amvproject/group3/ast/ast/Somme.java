package fr.amvproject.group3.ast.ast;

public class Somme extends Instr {
    public final Ident ident;
    public final Exp exp;

    public Somme(Ident ident, Exp exp) {
        this.ident = ident;
        this.exp = exp;
    }

    @Override
    public String toString() {
        return "(SOMME " + ident + " " + exp + ")";
    }
}