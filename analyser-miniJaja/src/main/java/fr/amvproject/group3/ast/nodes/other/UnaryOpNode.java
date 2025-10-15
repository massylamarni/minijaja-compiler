package fr.amvproject.group3.ast.nodes.other;

import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.AstVisitor;

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

  public UnaryOpNode(String name) {
    this.unaryOp = UnaryOpName.fromString(name);
  }

  public UnaryOpName getOp() {
    return unaryOp;
  }

  public boolean isAnd() {
    return unaryOp == UnaryOpName.NOT;
  }

  public boolean isOr() {
    return unaryOp == UnaryOpName.NEG;
  }

  @Override
  public <T> T accept(AstVisitor<T> visitor) {
    return visitor.visitUnaryOp(this);
  }
}
