package fr.amvproject.group3.jjc.ast.nodes.other;

import fr.amvproject.group3.jjc.ast.AstNode;
import fr.amvproject.group3.jjc.ast.AstVisitor;
import fr.amvproject.group3.jjc.ast.nodes.intermediate.OpNode;

public class UnaryOpNode extends OpNode {
  public enum UnaryOpName {
    NOT("not"),
    NEG("neg");

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
