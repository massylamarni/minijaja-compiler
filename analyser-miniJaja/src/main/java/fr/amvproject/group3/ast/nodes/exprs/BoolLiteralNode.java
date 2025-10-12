package fr.amvproject.group3.ast.nodes.exprs;

import fr.amvproject.group3.ast.AstVisitor;

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
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitBoolLiteral(this);
    }
}