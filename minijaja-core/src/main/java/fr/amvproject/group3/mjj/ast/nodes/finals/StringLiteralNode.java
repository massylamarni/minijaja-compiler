package fr.amvproject.group3.mjj.ast.nodes.finals;

import org.antlr.v4.runtime.Token;

import fr.amvproject.group3.mjj.ast.AstNode;
import fr.amvproject.group3.mjj.ast.AstVisitor;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.ExpNode;
import fr.amvproject.group3.mjj.utils.NodeOperations.MockToken;

public class StringLiteralNode extends ExpNode {
    private final Token stringToken;

    public StringLiteralNode(Token stringToken) {
        super(stringToken.getLine(), stringToken.getCharPositionInLine());
        this.stringToken = stringToken;
    }

    public StringLiteralNode(String literal, int line, int column) {
        super(line, column);
        stringToken = new MockToken(literal, line, column);
    }

    // Enlever les guillemets si présents
    public String getValue() {
        return stringToken.getText().replaceAll("(^\")|(\"$)", "");
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof StringLiteralNode stringLiteralNode) {
            return this.getValue().equals(stringLiteralNode.getValue());
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitStringLiteral(this);
    }
}
