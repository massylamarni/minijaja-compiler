package fr.amvproject.group3.ast.nodes.instrs;

import fr.amvproject.group3.ast.nodes.exprs.ExpNode;

public class RetourNode extends InstrNode {
    public final ExpNode exp;

    public RetourNode(ExpNode exp) {
        this.exp = exp;
    }

    @Override
    public String toString() {
        return "(RETOUR " + exp + ")";
    }
}