package fr.amvproject.group3.ast.nodes.finals;

import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.intermediate.ExpNode;

public class BoolLiteralNode extends ExpNode {
    private final boolean value;

    public BoolLiteralNode(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public String evaluate() {
        return value ? "true" : "false";
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