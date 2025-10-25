package fr.amvproject.group3.utils;

import fr.amvproject.group3.ast.nodes.exprs.BinaryExpNode;
import fr.amvproject.group3.ast.nodes.finals.ArrayAccessNode;
import fr.amvproject.group3.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.ast.nodes.instrs.AddAssignNode;
import fr.amvproject.group3.ast.nodes.instrs.AssignNode;
import fr.amvproject.group3.ast.nodes.intermediate.ExpNode;
import fr.amvproject.group3.ast.nodes.intermediate.LeftValueNode;
import fr.amvproject.group3.ast.nodes.other.BinaryOpNode;
import fr.amvproject.group3.exceptions.NodeExceptions;

public class NodeOperations {
  public AssignNode addAssignToAssign(AddAssignNode addAssignNode) {
    LeftValueNode leftValueNode = addAssignNode.getLeftValueNode();

    ExpNode addExp;
    if (leftValueNode instanceof IdentNode identNode) { // Var
      addExp = new BinaryExpNode(new BinaryOpNode("+"), identNode, addAssignNode.getExp());
    } else if (leftValueNode instanceof ArrayAccessNode arrayAccessNode) { // Array
      addExp = new BinaryExpNode(new BinaryOpNode("+"), arrayAccessNode, addAssignNode.getExp());
    } else {
      throw new NodeExceptions("Unexpected left-hand side in +=");
    }
    return new AssignNode(leftValueNode, addExp);
  }
}
