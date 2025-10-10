package fr.amvproject.group3.ast.nodes.instrs;

import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.exprs.ExpNode;

public class WhileNode extends InstrNode {
    private final ExpNode condExp;
    private final InstrsNode instrs;

    public WhileNode(ExpNode condExp, InstrsNode instrs) {
        this.condExp = condExp;
        this.instrs = instrs;
    }

    public ExpNode getCondExp() {
        return condExp;
    }

    public InstrsNode getInstrs() {
        return instrs;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitWhile(this);
    }
}