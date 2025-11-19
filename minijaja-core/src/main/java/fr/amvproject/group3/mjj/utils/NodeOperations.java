package fr.amvproject.group3.mjj.utils;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenSource;

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

  public static class MockToken implements Token {
    private final String text;
    private final int line;
    private final int col;

    public MockToken(String text, int line, int col) {
      this.text = text;
      this.line = line;
      this.col = col;
    }

    @Override
    public int getLine() {
      return line;
    }

    @Override
    public int getCharPositionInLine() {
      return col;
    }

    @Override
    public String getText() {
      return text;
    }

    @Override
    public int getType() {
      return 0;
    }

    @Override
    public int getChannel() {
      return 0;
    }

    @Override
    public int getTokenIndex() {
      return 0;
    }

    @Override
    public int getStartIndex() {
      return 0;
    }

    @Override
    public int getStopIndex() {
      return 0;
    }

    @Override
    public TokenSource getTokenSource() {
      return null;
    }

    @Override
    public CharStream getInputStream() {
      return null;
    }
  }

}
