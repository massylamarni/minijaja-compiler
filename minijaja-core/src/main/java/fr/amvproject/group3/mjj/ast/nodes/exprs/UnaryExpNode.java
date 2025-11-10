package fr.amvproject.group3.mjj.ast.nodes.exprs;

import fr.amvproject.group3.mjj.ast.AstNode;
import fr.amvproject.group3.mjj.ast.AstVisitor;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.ExpNode;
import fr.amvproject.group3.mjj.ast.nodes.other.UnaryOpNode;

public class UnaryExpNode extends ExpNode {
    private final UnaryOpNode op;
    private final ExpNode expNode;

    public UnaryExpNode(UnaryOpNode op, ExpNode expNode) {
        this.op = op;
        this.expNode = expNode;
    }

    public UnaryOpNode getOp() {
        return op;
    }

    public ExpNode getExp() {
        return expNode;
    }

    @Override
    public String evaluate() {
        return op.getOp().getName() + expNode.evaluate();
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof UnaryExpNode unaryExpNode) {
            return this.op.sameAs(unaryExpNode.op) && this.expNode.sameAs(unaryExpNode.expNode);
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitUnaryExp(this);
    }
}
