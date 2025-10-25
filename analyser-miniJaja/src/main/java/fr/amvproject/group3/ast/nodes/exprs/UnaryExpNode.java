package fr.amvproject.group3.ast.nodes.exprs;

import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.intermediate.ExpNode;
import fr.amvproject.group3.ast.nodes.other.UnaryOpNode;

public class UnaryExpNode extends ExpNode {
    private final UnaryOpNode op;
    private final ExpNode exp;

    public UnaryExpNode(UnaryOpNode op, ExpNode exp) {
        this.op = op;
        this.exp = exp;
    }

    public UnaryOpNode getOp() {
        return op;
    }

    public ExpNode getExp() {
        return exp;
    }

    @Override
    public String evaluate() {
        return op.getOp().getName() + exp.evaluate();
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitUnaryExp(this);
    }
}