package fr.amvproject.group3.miniJaja.ast.nodes.exprs;

import fr.amvproject.group3.miniJaja.ast.AstNode;
import fr.amvproject.group3.miniJaja.ast.AstVisitor;
import fr.amvproject.group3.miniJaja.ast.nodes.intermediate.ExpNode;
import fr.amvproject.group3.miniJaja.ast.nodes.other.BinaryOpNode;

public class BinaryExpNode extends ExpNode {
    private final BinaryOpNode op;
    private final ExpNode leftExp;
    private final ExpNode rightExp;

    public BinaryExpNode(BinaryOpNode op, ExpNode leftExp, ExpNode rightExp) {
        this.op = op;
        this.leftExp = leftExp;
        this.rightExp = rightExp;
    }

    public BinaryOpNode getOp() {
        return op;
    }

    public ExpNode getLeftExp() {
        return leftExp;
    }

    public ExpNode getRightExp() {
        return rightExp;
    }

    @Override
    public String evaluate() {
        return leftExp.evaluate() + op.getOp().getName() + rightExp.evaluate();
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof BinaryExpNode binaryExpNode) {
            return this.leftExp.sameAs(binaryExpNode.leftExp) &&
                this.op.sameAs(binaryExpNode.op) &&
                this.rightExp.sameAs(binaryExpNode.rightExp);
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitBinaryExp(this);
    }
}
