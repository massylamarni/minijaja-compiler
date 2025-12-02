package fr.amvproject.group3.jjc.exceptions;

import fr.amvproject.group3.jjc.ast.AstNode;

public class PausePointHit extends RuntimeException {
  public PausePointHit(AstNode node) {
    super("Pause point hit on node " + node.getClass() + " at line " + node.getLine());
  }
}
