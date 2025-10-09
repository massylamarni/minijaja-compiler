package fr.amvproject.group3.ast.nodes.exprs;

public class StringLiteralNode extends ExpNode {
    public final String valeur;

    public StringLiteralNode(String valeur) {
        // Enlever les guillemets si présents
        this.valeur = valeur.replaceAll("^\"|\"$", "");
    }

    @Override
    public String toString() {
        return "\"" + valeur + "\"";
    }
}