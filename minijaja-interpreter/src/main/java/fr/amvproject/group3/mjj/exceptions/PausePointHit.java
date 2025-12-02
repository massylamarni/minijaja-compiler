package fr.amvproject.group3.mjj.exceptions;

import fr.amvproject.group3.mjj.ast.AstNode;

public class PausePointHit extends RuntimeException {
  public PausePointHit(AstNode node) {
    super("Pause point hit on node " + node.getClass() + " at line " + node.getLine());
  }
}
