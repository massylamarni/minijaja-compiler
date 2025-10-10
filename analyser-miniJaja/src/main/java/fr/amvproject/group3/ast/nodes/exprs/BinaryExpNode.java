package fr.amvproject.group3.ast.nodes.exprs;

import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.other.BinaryOpNode;

public class BinaryExpNode extends ExpNode {
    private final BinaryOpNode op;
    private final ExpNode leftExp, rightExp;

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
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitBinaryExp(this);
    }
}