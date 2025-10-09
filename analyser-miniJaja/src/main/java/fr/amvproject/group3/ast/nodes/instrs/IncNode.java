package fr.amvproject.group3.ast.nodes.instrs;

import fr.amvproject.group3.ast.nodes.abstracts.Ident;

public class IncNode extends InstrNode {
    public final Ident ident;
    public IncNode(Ident ident) { this.ident = ident; }

    @Override
    public String toString() {
        return "(INCREMENT " + ident + ")";
    }
}
