package fr.amvproject.group3.ast.ast;

public class Affectation extends Instr {
    public final Ident ident;
    public final Exp exp;

    public Affectation(Ident ident, Exp exp) {
        this.ident = ident;
        this.exp = exp;
    }

    @Override
    public String toString() {
        return "(AFFECTATION " + ident + " " + exp + ")";
    }
}
