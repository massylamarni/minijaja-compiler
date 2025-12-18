package fr.amvproject.group3.jjc.utils;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenSource;

public class NodeOperations {
  private NodeOperations() {}

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
