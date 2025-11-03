package fr.amvproject.group3.miniJaja.ast.nodes.other;

import fr.amvproject.group3.miniJaja.ast.AstNode;
import fr.amvproject.group3.miniJaja.ast.AstVisitor;

public class BinaryOpNode extends AstNode {
  public enum BinaryOpName {
    AND("&&"),
    OR("||"),
    EQ("=="),
    GT(">"),
    ADD("+"),
    SUB("-"),
    MUL("*"),
    DIV("/");

    private final String name;

    BinaryOpName(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    public static BinaryOpName fromString(String name) {
      for (BinaryOpName binaryOp : values()) {
        if (binaryOp.name.equals(name)) {
          return binaryOp;
        }
      }
      throw new IllegalArgumentException("Type non valide : " + name);
    }
  }

  private final BinaryOpName binaryOp;

  public BinaryOpNode(String name) {
    this.binaryOp = BinaryOpName.fromString(name);
  }

  public BinaryOpName getOp() {
    return binaryOp;
  }

  public boolean isAnd() {
    return binaryOp == BinaryOpName.AND;
  }

  public boolean isOr() {
    return binaryOp == BinaryOpName.OR;
  }

  public boolean isEq() {
    return binaryOp == BinaryOpName.EQ;
  }

  public boolean isGt() {
    return binaryOp == BinaryOpName.GT;
  }

  public boolean isAdd() {
    return binaryOp == BinaryOpName.ADD;
  }

  public boolean isSub() {
    return binaryOp == BinaryOpName.SUB;
  }

  public boolean isMul() {
    return binaryOp == BinaryOpName.MUL;
  }

  public boolean isDiv() {
    return binaryOp == BinaryOpName.DIV;
  }

  @Override
  public boolean sameAs(AstNode other) {
    if (other instanceof BinaryOpNode binaryOpNode) {
      return this.binaryOp == binaryOpNode.binaryOp;
    }
    return false;
  }

  @Override
  public <T> T accept(AstVisitor<T> visitor) {
    return visitor.visitBinaryOp(this);
  }
}
