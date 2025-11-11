package fr.amvproject.group3.mjj.ast.nodes.instrs;

import fr.amvproject.group3.mjj.ast.AstNode;
import fr.amvproject.group3.mjj.ast.AstVisitor;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.InstrNode;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.LeftValueNode;

public class IncNode extends InstrNode {
    private final LeftValueNode leftValueNode;

    public IncNode(LeftValueNode leftValueNode) {
        this.leftValueNode = leftValueNode;
    }

    public LeftValueNode getLeftValueNode() {
        return leftValueNode;
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof IncNode incNode) {
            return this.leftValueNode.sameAs(incNode.leftValueNode);
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitInc(this);
    }
}
