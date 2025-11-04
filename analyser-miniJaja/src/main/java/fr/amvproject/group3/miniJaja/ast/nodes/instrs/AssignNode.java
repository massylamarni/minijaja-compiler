package fr.amvproject.group3.miniJaja.ast.nodes.instrs;

import fr.amvproject.group3.miniJaja.ast.AstNode;
import fr.amvproject.group3.miniJaja.ast.AstVisitor;
import fr.amvproject.group3.miniJaja.ast.nodes.intermediate.ExpNode;
import fr.amvproject.group3.miniJaja.ast.nodes.intermediate.InstrNode;
import fr.amvproject.group3.miniJaja.ast.nodes.intermediate.LeftValueNode;

public class AssignNode extends InstrNode {

    private final LeftValueNode leftValueNode;   // Variable ou case de tableau
    private final ExpNode expNode;              // Expression assignée

    public AssignNode(LeftValueNode leftValueNode, ExpNode expNode) {
        this.leftValueNode = leftValueNode;
        this.expNode = expNode;
    }

    public LeftValueNode getLeftValue() {
        return leftValueNode;
    }

    public ExpNode getExp() {
        return expNode;
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof AssignNode assignNode) {
            return this.leftValueNode.sameAs(assignNode.leftValueNode) && this.expNode.sameAs(assignNode.expNode);
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitAssign(this);
    }
}
