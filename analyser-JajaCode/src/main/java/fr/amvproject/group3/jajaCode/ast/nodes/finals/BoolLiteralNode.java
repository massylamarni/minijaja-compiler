package fr.amvproject.group3.jajaCode.ast.nodes.finals;

import fr.amvproject.group3.jajaCode.ast.AstNode;
import fr.amvproject.group3.jajaCode.ast.AstVisitor;
import fr.amvproject.group3.jajaCode.ast.nodes.intermediate.ValueNode;

public class BoolLiteralNode extends ValueNode {
    private final boolean value;

    public BoolLiteralNode(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof BoolLiteralNode boolLiteralNode) {
            return this.value == boolLiteralNode.value;
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitBoolLiteral(this);
    }
}
