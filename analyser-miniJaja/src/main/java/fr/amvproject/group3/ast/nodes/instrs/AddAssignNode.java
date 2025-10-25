package fr.amvproject.group3.ast.nodes.instrs;

import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.intermediate.ExpNode;
import fr.amvproject.group3.ast.nodes.intermediate.InstrNode;
import fr.amvproject.group3.ast.nodes.intermediate.LeftValueNode;

public class AddAssignNode extends InstrNode {
    private final LeftValueNode leftValueNode;
    private final ExpNode exp;

    public AddAssignNode(LeftValueNode leftValueNode, ExpNode exp) {        
        this.leftValueNode = leftValueNode;
        this.exp = exp;
    }

    public LeftValueNode getLeftValueNode() {
        return leftValueNode;
    }

    public ExpNode getExp() {
        return exp;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitAddAssign(this);
    }
}