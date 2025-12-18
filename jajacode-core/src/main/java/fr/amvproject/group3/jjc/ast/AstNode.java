package fr.amvproject.group3.jjc.ast;


public abstract class AstNode {
    public abstract <T> T accept(AstVisitor<T> visitor);
    public abstract boolean sameAs(AstNode other);
    private int line;
    private int column;

    protected AstNode() {
        this.line = 0;
        this.column = 0;
    }

    protected AstNode(int line, int column) {
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
