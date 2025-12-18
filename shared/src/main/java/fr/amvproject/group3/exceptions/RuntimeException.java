package fr.amvproject.group3.exceptions;

public class RuntimeException extends java.lang.RuntimeException {
  private final int line;
  private final int column;

  public RuntimeException(String message, int line, int column) {
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

  @Override
  public String toString() {
    return "[" + line + ":" + column + "] " + super.getMessage();
  }
}
