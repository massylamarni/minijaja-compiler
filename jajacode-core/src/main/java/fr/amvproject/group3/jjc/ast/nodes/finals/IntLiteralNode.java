package fr.amvproject.group3.jjc.ast.nodes.finals;

import org.antlr.v4.runtime.Token;

import fr.amvproject.group3.jjc.ast.AstNode;
import fr.amvproject.group3.jjc.ast.AstVisitor;
import fr.amvproject.group3.jjc.ast.nodes.intermediate.ValueNode;
import fr.amvproject.group3.jjc.utils.NodeOperations.MockToken;

public class IntLiteralNode extends ValueNode {
    private final Token intToken;

    public IntLiteralNode(Token intToken) {
        super(intToken.getLine(), intToken.getCharPositionInLine());
        this.intToken = intToken;
    }

    public IntLiteralNode(String literal, int line, int column) {
        super(line, column);
        intToken = new MockToken(literal, line, column);
    }

    public Token getIntToken() {
      return intToken;
    }

    public int getValue() {
      return Integer.parseInt(intToken.getText());
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof IntLiteralNode intLiteralNode) {
            return this.getValue() == intLiteralNode.getValue();
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitIntLiteral(this);
    }
}
