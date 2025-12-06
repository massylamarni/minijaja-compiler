package fr.amvproject.group3.exceptions;

public class PausePointHit extends RuntimeException {
  public PausePointHit(String nodeName, int line, int column) {
    super("Pause hit on node " + nodeName + " at line " + String.valueOf(line), line, column);
  }
}
