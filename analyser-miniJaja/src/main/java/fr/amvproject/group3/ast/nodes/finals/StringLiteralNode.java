package fr.amvproject.group3.ast.nodes.finals;

import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.intermediate.ExpNode;

public class StringLiteralNode extends ExpNode {
    private final String value;

    public StringLiteralNode(String value) {
        // Enlever les guillemets si présents
        this.value = value.replaceAll("(^\")|(\"$)", "");
    }

    public String getValue() {
        return value;
    }

    @Override
    public String evaluate() {
        return value;
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof StringLiteralNode stringLiteralNode) {
            return this.value.equals(stringLiteralNode.value);
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitStringLiteral(this);
    }
}