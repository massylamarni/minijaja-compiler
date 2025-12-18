package fr.amvproject.group3.exceptions;

public class BreakPointHit extends RuntimeException {  
  public BreakPointHit(String nodeName, int line, int column) {
    super("Breakpoint hit on node " + nodeName + " at line " + String.valueOf(line), line, column);
  }
}
