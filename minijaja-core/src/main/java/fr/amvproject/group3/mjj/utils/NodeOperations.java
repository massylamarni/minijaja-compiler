package fr.amvproject.group3.mjj.utils;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenSource;

import fr.amvproject.group3.exceptions.NodeException;
import fr.amvproject.group3.mjj.ast.nodes.exprs.BinaryExpNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.ArrayAccessNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.IntLiteralNode;
import fr.amvproject.group3.mjj.ast.nodes.instrs.AddAssignNode;
import fr.amvproject.group3.mjj.ast.nodes.instrs.AssignNode;
import fr.amvproject.group3.mjj.ast.nodes.instrs.IncNode;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.ExpNode;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.LeftValueNode;
import fr.amvproject.group3.mjj.ast.nodes.other.BinaryOpNode;

public class NodeOperations {
  private NodeOperations() {}
  
  public static AssignNode addAssignToAssign(AddAssignNode addAssignNode) {
    LeftValueNode leftValueNode = addAssignNode.getLeftValueNode();
    BinaryOpNode binaryOpNode = new BinaryOpNode("+", addAssignNode.getLine(), addAssignNode.getColumn());
    ExpNode leftExpNode = null;
    ExpNode rightExpNode = addAssignNode.getExp();

    if (leftValueNode instanceof IdentNode identNode) {
      leftExpNode = identNode;
    } else if (leftValueNode instanceof ArrayAccessNode arrayAccessNode) {
      leftExpNode = arrayAccessNode;
    }

    ExpNode newExpNode = new BinaryExpNode(binaryOpNode, leftExpNode, rightExpNode);
    return new AssignNode(leftValueNode, addAssignNode.getAddAssignToken(), newExpNode);
  }

  public static AssignNode incToAssign(IncNode incNode) throws NodeException {
    LeftValueNode leftValueNode = incNode.getLeftValueNode();
    BinaryOpNode binaryOpNode = new BinaryOpNode("+", incNode.getLine(), incNode.getColumn());
    ExpNode leftExpNode = null;
    MockToken intToken = new MockToken("1", incNode.getLine(), incNode.getColumn());
    MockToken addToken = new MockToken("+", incNode.getLine(), incNode.getColumn());
    ExpNode rightExpNode = new IntLiteralNode(intToken);

    if (leftValueNode instanceof IdentNode identNode) {
      leftExpNode = identNode;
    } else if (leftValueNode instanceof ArrayAccessNode arrayAccessNode) {
      leftExpNode = arrayAccessNode;
    }

    ExpNode newExpNode = new BinaryExpNode(binaryOpNode, leftExpNode, rightExpNode);
    return new AssignNode(leftValueNode, addToken, newExpNode);
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
