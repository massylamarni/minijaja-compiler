package fr.amvproject.group3.mjj.ast.nodes.finals;

import org.antlr.v4.runtime.Token;

import fr.amvproject.group3.mjj.ast.AstNode;
import fr.amvproject.group3.mjj.ast.AstVisitor;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.ExpNode;

public class IntLiteralNode extends ExpNode {
    private final Token intToken;

    public IntLiteralNode(Token intToken) {
        super(intToken.getLine(), intToken.getCharPositionInLine());
        this.intToken = intToken;
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
