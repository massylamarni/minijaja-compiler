package fr.amvproject.group3.jjc.ast.nodes.finals;

import fr.amvproject.group3.jjc.ast.AstNode;
import fr.amvproject.group3.jjc.ast.AstVisitor;
import fr.amvproject.group3.jjc.ast.nodes.intermediate.ValueNode;

public class StringLiteralNode extends ValueNode {
    private final String value;

    public StringLiteralNode(String value) {
        // Enlever les guillemets si présents
        this.value = value.replaceAll("(^\")|(\"$)", "");
    }

    public String getValue() {
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
