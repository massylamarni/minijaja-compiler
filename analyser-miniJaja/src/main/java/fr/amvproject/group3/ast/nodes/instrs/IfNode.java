package fr.amvproject.group3.ast.nodes.instrs;

import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.abstracts.InstrsNode;
import fr.amvproject.group3.ast.nodes.intermediate.ExpNode;
import fr.amvproject.group3.ast.nodes.intermediate.InstrNode;

public class IfNode extends InstrNode {
    private final ExpNode condExp;
    private final InstrsNode thenInstrs;
    private final InstrsNode elseInstrs;

    public IfNode(ExpNode condExp, InstrsNode thenInstrs, InstrsNode elseInstrs) {
        this.condExp = condExp;
        this.thenInstrs = thenInstrs;
        this.elseInstrs = elseInstrs;
    }

    public ExpNode getCondExp() {
        return condExp;
    }

    public InstrsNode getThenInstrs() {
        return thenInstrs;
    }

    public InstrsNode getElseInstrs() {
        return elseInstrs;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitIf(this);
    }
}