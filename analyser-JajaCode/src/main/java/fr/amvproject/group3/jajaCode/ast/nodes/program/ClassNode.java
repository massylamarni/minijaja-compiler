package fr.amvproject.group3.jajaCode.ast.nodes.program;

import fr.amvproject.group3.jajaCode.ast.AstNode;
import fr.amvproject.group3.jajaCode.ast.AstVisitor;
import fr.amvproject.group3.jajaCode.ast.nodes.finals.AddressNode;
import fr.amvproject.group3.jajaCode.ast.nodes.intermediate.InstrNode;

public class ClassNode extends AstNode {
    private final AddressNode addressNode;
    private final InstrNode instrNode;

    public ClassNode(AddressNode addressNode, InstrNode instrNode) {
        this.addressNode = addressNode;
        this.instrNode = instrNode;
    }

    public AddressNode getAddress() {
        return addressNode;
    }

    public InstrNode getInstr() {
        return instrNode;
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof ClassNode classNode) {
            return this.addressNode.sameAs(classNode.addressNode) &&
                this.instrNode.sameAs(classNode.instrNode);
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitClass(this);
    }
}
