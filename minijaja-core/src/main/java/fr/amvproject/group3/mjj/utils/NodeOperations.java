package fr.amvproject.group3.mjj.utils;

import fr.amvproject.group3.exceptions.NodeException;
import fr.amvproject.group3.mjj.ast.nodes.exprs.BinaryExpNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.ArrayAccessNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.mjj.ast.nodes.instrs.AddAssignNode;
import fr.amvproject.group3.mjj.ast.nodes.instrs.AssignNode;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.ExpNode;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.LeftValueNode;
import fr.amvproject.group3.mjj.ast.nodes.other.BinaryOpNode;

public class NodeOperations {
  private NodeOperations() {}
  public static AssignNode addAssignToAssign(AddAssignNode addAssignNode) throws NodeException {
    LeftValueNode leftValueNode = addAssignNode.getLeftValueNode();

    ExpNode addExp;
    if (leftValueNode instanceof IdentNode identNode) { // Var
      addExp = new BinaryExpNode(new BinaryOpNode("+", addAssignNode.getAddAssignToken().getLine(), addAssignNode.getAddAssignToken().getCharPositionInLine()), identNode, addAssignNode.getExp());
    } else if (leftValueNode instanceof ArrayAccessNode arrayAccessNode) { // Array
      addExp = new BinaryExpNode(new BinaryOpNode("+", addAssignNode.getAddAssignToken().getLine(), addAssignNode.getAddAssignToken().getCharPositionInLine()), arrayAccessNode, addAssignNode.getExp());
    } else {
      throw new NodeException("Unexpected left-hand side in +=");
    }
    return new AssignNode(leftValueNode, addAssignNode.getAddAssignToken(), addExp);
  }
}
