package fr.amvproject.group3.ast.nodes.exprs;

import fr.amvproject.group3.ast.AstVisitor;

public class StringLiteralNode extends ExpNode {
    private final String value;

    public StringLiteralNode(String value) {
        // Enlever les guillemets si présents
        this.value = value.replaceAll("^\"|\"$", "");
    }

    public String getValue() {
        return value;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitStringLiteral(this);
    }
}