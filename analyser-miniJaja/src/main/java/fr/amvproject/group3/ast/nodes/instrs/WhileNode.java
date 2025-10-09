package fr.amvproject.group3.ast.nodes.instrs;

import fr.amvproject.group3.ast.nodes.exprs.ExpNode;

public class WhileNode extends InstrNode {
    public final ExpNode cond;
    public final InstrsNode body;

    public WhileNode(ExpNode cond, InstrsNode body) {
        this.cond = cond;
        this.body = body;
    }

    @Override
    public String toString() {
        return "(TANTQUE " + cond + " " + body + ")";
    }
}