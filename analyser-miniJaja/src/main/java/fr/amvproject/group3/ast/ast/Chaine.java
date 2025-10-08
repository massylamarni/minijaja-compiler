package fr.amvproject.group3.ast.ast;

public class Chaine extends Exp {
    public final String valeur;

    public Chaine(String valeur) {
        // Enlever les guillemets si présents
        this.valeur = valeur.replaceAll("^\"|\"$", "");
    }

    @Override
    public String toString() {
        return "\"" + valeur + "\"";
    }
}