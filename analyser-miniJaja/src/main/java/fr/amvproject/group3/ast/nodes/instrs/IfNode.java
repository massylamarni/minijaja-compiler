package fr.amvproject.group3.ast.nodes.instrs;

import fr.amvproject.group3.ast.nodes.exprs.ExpNode;

public class IfNode extends InstrNode {
    public final ExpNode cond;
    public final InstrsNode thenInstrs;
    public final InstrsNode elseInstrs;

    public IfNode(ExpNode cond, InstrsNode thenInstrs, InstrsNode elseInstrs) {
        this.cond = cond;
        this.thenInstrs = thenInstrs;
        this.elseInstrs = elseInstrs;
    }

    @Override
    public String toString() {
        return "(SI " + cond + " " + thenInstrs + " " + elseInstrs + ")";
    }
}