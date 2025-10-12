package fr.amvproject.group3.ast.nodes.instrs;

import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.exprs.ExpNode;
import fr.amvproject.group3.ast.nodes.other.LeftValueNode;

public class AssignNode extends InstrNode {

    private final LeftValueNode leftValue;   // Variable ou case de tableau
    private final ExpNode exp;           // Expression assignée

    public AssignNode(LeftValueNode leftValue, ExpNode exp) {
        this.leftValue = leftValue;
        this.exp = exp;
    }

    public LeftValueNode getLeftValue() {
        return leftValue;
    }

    public ExpNode getExp() {
        return exp;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitAssign(this);
    }
}
