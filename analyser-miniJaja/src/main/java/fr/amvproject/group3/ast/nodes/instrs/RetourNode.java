package fr.amvproject.group3.ast.nodes.instrs;

import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.exprs.ExpNode;

public class RetourNode extends InstrNode {
    private final ExpNode exp;

    public RetourNode(ExpNode exp) {
        this.exp = exp;
    }

    public ExpNode getExp() {
        return exp;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitRetour(this);
    }
}