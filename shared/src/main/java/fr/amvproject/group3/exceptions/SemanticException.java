package fr.amvproject.group3.exceptions;

public class SemanticException extends RuntimeException {
  private final int line;
  private final int column;

  public SemanticException(String message, int line, int column) {
    super(message);
    this.line = line;
    this.column = column;
  }

  public int getLine() {
    return line;
  }

  public int getColumn() {
    return column;
  }
}

