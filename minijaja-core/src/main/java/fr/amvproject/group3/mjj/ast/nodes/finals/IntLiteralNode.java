package fr.amvproject.group3.mjj.ast.nodes.finals;

import fr.amvproject.group3.mjj.ast.AstNode;
import fr.amvproject.group3.mjj.ast.AstVisitor;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.ExpNode;

public class IntLiteralNode extends ExpNode {
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
