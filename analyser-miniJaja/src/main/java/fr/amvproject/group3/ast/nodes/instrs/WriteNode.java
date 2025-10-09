package fr.amvproject.group3.ast.nodes.instrs;

import fr.amvproject.group3.ast.nodes.exprs.ExpNode;

public class WriteNode extends InstrNode {
    public final ExpNode contenu;
    public final boolean avecLn;

    public WriteNode(ExpNode contenu, boolean avecLn) {
        this.contenu = contenu;
        this.avecLn = avecLn;
    }

    @Override
    public String toString() {
        return "(" + (avecLn ? "ECRIRELN" : "ECRIRE") + " " + contenu + ")";
    }
}