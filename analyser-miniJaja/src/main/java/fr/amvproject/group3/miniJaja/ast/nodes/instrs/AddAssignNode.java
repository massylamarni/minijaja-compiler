package fr.amvproject.group3.miniJaja.ast.nodes.instrs;

import fr.amvproject.group3.miniJaja.ast.AstNode;
import fr.amvproject.group3.miniJaja.ast.AstVisitor;
import fr.amvproject.group3.miniJaja.ast.nodes.intermediate.ExpNode;
import fr.amvproject.group3.miniJaja.ast.nodes.intermediate.InstrNode;
import fr.amvproject.group3.miniJaja.ast.nodes.intermediate.LeftValueNode;

public class AddAssignNode extends InstrNode {
    private final LeftValueNode leftValueNode;
    private final ExpNode expNode;

    public AddAssignNode(LeftValueNode leftValueNode, ExpNode expNode) {
        this.leftValueNode = leftValueNode;
        this.expNode = expNode;
    }

    public LeftValueNode getLeftValueNode() {
        return leftValueNode;
    }

    public ExpNode getExp() {
        return expNode;
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof AddAssignNode addAssignNode) {
            return this.leftValueNode.sameAs(addAssignNode.leftValueNode) && this.expNode.sameAs(addAssignNode.expNode);
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitAddAssign(this);
    }
}
