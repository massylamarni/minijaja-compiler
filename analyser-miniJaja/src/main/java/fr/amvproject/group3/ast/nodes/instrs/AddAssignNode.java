package fr.amvproject.group3.ast.nodes.instrs;

import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.exprs.ArrayAccessNode;
import fr.amvproject.group3.ast.nodes.exprs.BinaryExpNode;
import fr.amvproject.group3.ast.nodes.exprs.ExpNode;
import fr.amvproject.group3.ast.nodes.exprs.VarRefNode;
import fr.amvproject.group3.ast.nodes.other.BinaryOpNode;
import fr.amvproject.group3.ast.nodes.other.LeftValueNode;

public class AddAssignNode extends InstrNode {
    private final LeftValueNode leftValueNode;
    private final ExpNode exp;

    public AddAssignNode(LeftValueNode leftValueNode, ExpNode exp) {

        // Build addition expression
        ExpNode addExp;
        if (leftValueNode instanceof VarRefNode) { // Var
            addExp = new BinaryExpNode(new BinaryOpNode("+"), new VarRefNode(((VarRefNode) leftValueNode).getIdent()), exp);
        } else if (leftValueNode instanceof ArrayAccessNode) { // Array
            ArrayAccessNode arrNode = (ArrayAccessNode) leftValueNode;
            addExp = new BinaryExpNode(new BinaryOpNode("+"), new ArrayAccessNode(arrNode.getIdent(), arrNode.getIndexExp()), exp);
        } else {
            throw new RuntimeException("Unexpected left-hand side in +=: " + leftValueNode);
        }
        
        this.leftValueNode = leftValueNode;
        this.exp = addExp;
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