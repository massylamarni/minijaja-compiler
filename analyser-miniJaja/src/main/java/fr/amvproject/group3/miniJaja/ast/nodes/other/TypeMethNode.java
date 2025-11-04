package fr.amvproject.group3.miniJaja.ast.nodes.other;

import fr.amvproject.group3.miniJaja.ast.AstNode;
import fr.amvproject.group3.miniJaja.ast.AstVisitor;

public class TypeMethNode extends AstNode {
  private enum TypeMethName {
    INT("int"),
    BOOLEAN("boolean"),
    VOID("void");

    private final String name;

    TypeMethName(String name) {
      this.name = name;
    }

    public static TypeMethName fromString(String name) {
      for (TypeMethName type : values()) {
        if (type.name.equals(name)) {
          return type;
        }
      }
      throw new IllegalArgumentException("Type non valide : " + name);
    }
  }

  private final TypeMethName typeMeth;

  public TypeMethNode(String name) {
    this.typeMeth = TypeMethName.fromString(name);
  }

  public String getName() {
    return typeMeth.name;
  }

  public TypeMethName getType() {
    return typeMeth;
  }

  public boolean isInt() {
    return typeMeth == TypeMethName.INT;
  }

  public boolean isBoolean() {
    return typeMeth == TypeMethName.BOOLEAN;
  }

  public boolean isVoid() {
    return typeMeth == TypeMethName.VOID;
  }

  @Override
  public boolean sameAs(AstNode other) {
    if (other instanceof TypeMethNode typeMethNode) {
      return this.typeMeth == typeMethNode.typeMeth;
    }
    return false;
  }

  @Override
  public <T> T accept(AstVisitor<T> visitor) {
    return visitor.visitTypeMeth(this);
  }
}
