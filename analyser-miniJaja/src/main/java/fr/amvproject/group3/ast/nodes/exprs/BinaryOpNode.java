package fr.amvproject.group3.ast.nodes.exprs;

import fr.amvproject.group3.ast.*;

// +, -, *, /, ==, >, &&, ||
public class BinaryOpNode extends AstNode {
    private final String operator;
    private final AstNode left;
    private final AstNode right;

    public BinaryOpNode(String operator, AstNode left, AstNode right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    public String getOperator() { return operator; }
    public AstNode getLeft() { return left; }
    public AstNode getRight() { return right; }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitBinaryOp(this);
    }
}