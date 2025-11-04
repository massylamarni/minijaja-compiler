package fr.amvproject.group3.jajaCode.ast.nodes.finals;

import fr.amvproject.group3.jajaCode.ast.AstNode;
import fr.amvproject.group3.jajaCode.ast.AstVisitor;

public class AddressNode extends AstNode {
    private final int value;

    public AddressNode(int value) {
        this.value = value;
    }

    public int getValue() {
      return value;
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof AddressNode addressNode) {
            return this.value == addressNode.value;
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitAddress(this);
    }
}
