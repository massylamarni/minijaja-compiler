package fr.amvproject.group3.mjj.ast.nodes.finals;

import org.antlr.v4.runtime.Token;

import fr.amvproject.group3.mjj.ast.AstNode;
import fr.amvproject.group3.mjj.ast.AstVisitor;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.ExpNode;

public class BoolLiteralNode extends ExpNode {
    private final Token boolToken;

    public BoolLiteralNode(Token boolToken) {
        super(boolToken.getLine(), boolToken.getCharPositionInLine());
        this.boolToken = boolToken;
    }

    public Token getBoolToken() {
      return boolToken;
    }

    public boolean getValue() {
        return boolToken.getText().equals("true");
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof BoolLiteralNode boolLiteralNode) {
            return this.getValue() == boolLiteralNode.getValue();
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitBoolLiteral(this);
    }
}
