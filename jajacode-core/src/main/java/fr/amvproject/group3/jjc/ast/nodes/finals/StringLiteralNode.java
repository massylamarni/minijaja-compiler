package fr.amvproject.group3.jjc.ast.nodes.finals;

import org.antlr.v4.runtime.Token;

import fr.amvproject.group3.jjc.ast.AstNode;
import fr.amvproject.group3.jjc.ast.AstVisitor;
import fr.amvproject.group3.jjc.ast.nodes.intermediate.ValueNode;
import fr.amvproject.group3.jjc.utils.NodeOperations.MockToken;

public class StringLiteralNode extends ValueNode {
    private final Token stringToken;

    public StringLiteralNode(Token stringToken) {
        super(stringToken.getLine(), stringToken.getCharPositionInLine());
        this.stringToken = stringToken;
    }

    public StringLiteralNode(String literal, int line, int column) {
        super(line, column);
        stringToken = new MockToken(literal, line, column);
    }

    public Token getStringToken() {
      return stringToken;
    }

    public String getValue() {
        // Enlever les guillemets si présents
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
