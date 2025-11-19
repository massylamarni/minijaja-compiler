package fr.amvproject.group3.mjj.ast.nodes.other;

import org.antlr.v4.runtime.Token;

import fr.amvproject.group3.mjj.ast.AstNode;
import fr.amvproject.group3.mjj.ast.AstVisitor;

public class UnaryOpNode extends AstNode {
  public enum UnaryOpName {
    NOT("!"),
    NEG("-");

    private final String name;

    UnaryOpName(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    public static UnaryOpName fromString(String name) {
      for (UnaryOpName unaryOp : values()) {
        if (unaryOp.name.equals(name)) {
          return unaryOp;
        }
      }
      throw new IllegalArgumentException("Type non valide : " + name);
    }
  }

  private final UnaryOpName unaryOp;
  private final Token unaryOpToken;

  public UnaryOpNode(Token unaryOpToken) {
    super(unaryOpToken.getLine(), unaryOpToken.getCharPositionInLine());
    this.unaryOpToken = unaryOpToken;
    this.unaryOp = UnaryOpName.fromString(unaryOpToken.getText());
  }

  public Token getUnaryOpToken() {
    return unaryOpToken;
  }

  public UnaryOpName getOp() {
    return unaryOp;
  }

  public boolean isNot() {
    return unaryOp == UnaryOpName.NOT;
  }

  public boolean isNeg() {
    return unaryOp == UnaryOpName.NEG;
  }

  @Override
  public boolean sameAs(AstNode other) {
    if (other instanceof UnaryOpNode unaryOpNode) {
      return this.unaryOp == unaryOpNode.unaryOp;
    }
    return false;
  }

  @Override
  public <T> T accept(AstVisitor<T> visitor) {
    return visitor.visitUnaryOp(this);
  }
}
