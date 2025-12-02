package fr.amvproject.group3.jjc.exceptions;

import fr.amvproject.group3.jjc.ast.AstNode;

public class BreakPointHit extends RuntimeException {
  public BreakPointHit(AstNode node) {
    super("Breakpoint hit on node " + node.getClass() + " at line " + node.getLine());
  }
}
