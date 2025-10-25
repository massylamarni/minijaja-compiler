package fr.amvproject.group3.ast.nodes.finals;

import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.intermediate.ExpNode;

public class IntLiteralNode extends ExpNode {
    private final int value;

    public IntLiteralNode(int value) {
        this.value = value;
    }

    public int getValue() {
      return value;
    }

    @Override
    public String evaluate() {
        return String.valueOf(value);
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitIntLiteral(this);
    }
}