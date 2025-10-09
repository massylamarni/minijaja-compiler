package fr.amvproject.group3.ast.nodes.instrs;

import fr.amvproject.group3.ast.nodes.abstracts.Ident;
import fr.amvproject.group3.ast.nodes.exprs.ExpNode;

public class AddAssignNode extends InstrNode {
    public final Ident ident;
    public final ExpNode exp;

    public AddAssignNode(Ident ident, ExpNode exp) {
        this.ident = ident;
        this.exp = exp;
    }

    @Override
    public String toString() {
        return "(SOMME " + ident + " " + exp + ")";
    }
}