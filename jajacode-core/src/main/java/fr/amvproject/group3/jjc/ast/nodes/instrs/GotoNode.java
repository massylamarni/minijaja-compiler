package fr.amvproject.group3.jjc.ast.nodes.instrs;

import fr.amvproject.group3.jjc.ast.AstNode;
import fr.amvproject.group3.jjc.ast.AstVisitor;
import fr.amvproject.group3.jjc.ast.nodes.finals.AddressNode;
import fr.amvproject.group3.jjc.ast.nodes.intermediate.InstrNode;

public class GotoNode extends InstrNode {
    private final AddressNode addressNode;

    public GotoNode(AddressNode addressNode) {
        this.addressNode = addressNode;
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
