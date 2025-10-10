package fr.amvproject.group3.ast.nodes.exprs;

import fr.amvproject.group3.ast.AstVisitor;

public class IntLiteralNode extends ExpNode {
    private final int value;

    public IntLiteralNode(int value) {
        this.value = value;
    }

    public int getValue() {
      return value;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitIntLiteral(this);
    }
}