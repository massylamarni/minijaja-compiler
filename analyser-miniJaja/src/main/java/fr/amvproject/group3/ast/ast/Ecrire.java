package fr.amvproject.group3.ast.ast;

public class Ecrire extends Instr {
    public final Exp contenu;
    public final boolean avecLn;

    public Ecrire(Exp contenu, boolean avecLn) {
        this.contenu = contenu;
        this.avecLn = avecLn;
    }

    @Override
    public String toString() {
        return "(" + (avecLn ? "ECRIRELN" : "ECRIRE") + " " + contenu + ")";
    }
}