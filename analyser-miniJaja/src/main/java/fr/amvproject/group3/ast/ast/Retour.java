package fr.amvproject.group3.ast.ast;

public class Retour extends Instr {
    public final Exp exp;

    public Retour(Exp exp) {
        this.exp = exp;
    }

    @Override
    public String toString() {
        return "(RETOUR " + exp + ")";
    }
}