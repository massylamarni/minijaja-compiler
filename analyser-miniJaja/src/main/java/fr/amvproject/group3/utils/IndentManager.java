package fr.amvproject.group3.utils;

public class IndentManager {
    private int indentLevel = -1;

    public int getIndentLevel() {
      return indentLevel;
    }

    public void increaseIndent() {
      indentLevel++;
    }

    public void decreaseIndent() {
      indentLevel--;
    }

    public String indent() {
      return "  ".repeat(indentLevel < 0 ? 0 : indentLevel);
    }
  }