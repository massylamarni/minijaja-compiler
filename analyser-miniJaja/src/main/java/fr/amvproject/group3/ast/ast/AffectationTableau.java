package fr.amvproject.group3.ast.ast;

public class AffectationTableau extends Instr {
    public final Ident ident;
    public final Exp index;
    public final Exp valeur;

    public AffectationTableau(Ident ident, Exp index, Exp valeur) {
        this.ident = ident;
        this.index = index;
        this.valeur = valeur;
    }

    @Override
    public String toString() {
        return "(AFFECTATION " + ident + "[" + index + "] " + valeur + ")";
    }
}