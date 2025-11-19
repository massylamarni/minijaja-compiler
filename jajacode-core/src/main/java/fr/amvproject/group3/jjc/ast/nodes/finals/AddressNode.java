package fr.amvproject.group3.jjc.ast.nodes.finals;

import org.antlr.v4.runtime.Token;

import fr.amvproject.group3.jjc.ast.AstNode;
import fr.amvproject.group3.jjc.ast.AstVisitor;

public class AddressNode extends AstNode {
    private final Token intToken;

    public AddressNode(Token intToken) {
        this.intToken = intToken;
    }

    public Token getAddressToken() {
      return intToken;
    }

    public int getValue() {
      return Integer.parseInt(intToken.getText());
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof AddressNode addressNode) {
            return this.getValue() == addressNode.getValue();
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitAddress(this);
    }
}
