package fr.amvproject.group3.mjj.exceptions;

import fr.amvproject.group3.mjj.ast.AstNode;

public class BreakpointHit extends RuntimeException {
  public BreakpointHit(AstNode node) {
    super("Breakpoint hit on node " + node.getClass() + " at line " + node.getLine());
  }
}
