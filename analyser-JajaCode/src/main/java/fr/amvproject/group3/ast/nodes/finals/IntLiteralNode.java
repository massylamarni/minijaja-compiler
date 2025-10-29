package fr.amvproject.group3.ast.nodes.finals;

import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.intermediate.ValueNode;

public class IntLiteralNode extends ValueNode {
    private final int value;

    public IntLiteralNode(int value) {
        this.value = value;
    }

    public int getValue() {
      return value;
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof IntLiteralNode intLiteralNode) {
            return this.value == intLiteralNode.value;
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitIntLiteral(this);
    }
}