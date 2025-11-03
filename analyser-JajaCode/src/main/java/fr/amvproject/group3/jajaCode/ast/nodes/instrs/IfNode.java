package fr.amvproject.group3.jajaCode.ast.nodes.instrs;

import fr.amvproject.group3.jajaCode.ast.AstNode;
import fr.amvproject.group3.jajaCode.ast.AstVisitor;
import fr.amvproject.group3.jajaCode.ast.nodes.finals.AddressNode;
import fr.amvproject.group3.jajaCode.ast.nodes.intermediate.InstrNode;

public class IfNode extends InstrNode {
    private final AddressNode addressNode;

    public IfNode(AddressNode addressNode) {
        this.addressNode = addressNode;
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
