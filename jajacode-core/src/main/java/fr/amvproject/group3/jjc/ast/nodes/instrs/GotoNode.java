package fr.amvproject.group3.jjc.ast.nodes.instrs;

import org.antlr.v4.runtime.Token;

import fr.amvproject.group3.jjc.ast.AstNode;
import fr.amvproject.group3.jjc.ast.AstVisitor;
import fr.amvproject.group3.jjc.ast.nodes.finals.AddressNode;
import fr.amvproject.group3.jjc.ast.nodes.intermediate.InstrNode;
import fr.amvproject.group3.jjc.utils.NodeOperations.MockToken;

public class GotoNode extends InstrNode {
    private final Token gotoToken;
    private final AddressNode addressNode;

    public GotoNode(Token gotoToken, AddressNode addressNode) {
        super(gotoToken.getLine(), gotoToken.getCharPositionInLine());
        this.gotoToken = gotoToken;
        this.addressNode = addressNode;
    }

    public GotoNode(AddressNode addressNode) {
        super(0, 0);
        this.gotoToken = new MockToken("goto", 0, 0);
        this.addressNode = addressNode;
    }

    public Token getGotoToken() {
      return gotoToken;
    }

    public AddressNode getAddress() {
        return addressNode;
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof GotoNode gotoNode) {
            return this.addressNode.sameAs(gotoNode.addressNode);
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitGoto(this);
    }
}
