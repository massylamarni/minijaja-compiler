package fr.amvproject.group3.jjc.ast.nodes.instrs;

import org.antlr.v4.runtime.Token;

import fr.amvproject.group3.jjc.ast.AstNode;
import fr.amvproject.group3.jjc.ast.AstVisitor;
import fr.amvproject.group3.jjc.ast.nodes.finals.AddressNode;
import fr.amvproject.group3.jjc.ast.nodes.intermediate.InstrNode;

public class IfNode extends InstrNode {
    private final Token ifToken;
    private final AddressNode addressNode;

    public IfNode(Token ifToken, AddressNode addressNode) {
        super(ifToken.getLine(), ifToken.getCharPositionInLine());
        this.ifToken = ifToken;
        this.addressNode = addressNode;
    }

    public Token getIfToken() {
      return ifToken;
    }

    public AddressNode getAddress() {
        return addressNode;
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof IfNode ifNode) {
            return this.addressNode.sameAs(ifNode.addressNode);
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitIf(this);
    }
}
