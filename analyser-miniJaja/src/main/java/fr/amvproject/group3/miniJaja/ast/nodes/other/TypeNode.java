package fr.amvproject.group3.miniJaja.ast.nodes.other;

import fr.amvproject.group3.miniJaja.ast.AstNode;
import fr.amvproject.group3.miniJaja.ast.AstVisitor;

public class TypeNode extends AstNode {
  protected enum TypeName {
    INT("int"),
    BOOLEAN("boolean");

    private final String name;

    TypeName(String name) {
      this.name = name;
    }

    public static TypeName fromString(String name) {
      for (TypeName type : values()) {
        if (type.name.equals(name)) {
          return type;
        }
      }
      throw new IllegalArgumentException("Type non valide : " + name);
    }
  }

  private final TypeName type;

  public TypeNode(String name) {
    this.type = TypeName.fromString(name);
  }

  public String getName() {
    return type.name;
  }

  public TypeName getType() {
    return type;
  }

  public boolean isInt() {
    return type == TypeName.INT;
  }

  public boolean isBoolean() {
    return type == TypeName.BOOLEAN;
  }

  @Override
  public boolean sameAs(AstNode other) {
    if (other instanceof TypeNode typeNode) {
      return this.type == typeNode.type;
    }
    return false;
  }

  @Override
  public <T> T accept(AstVisitor<T> visitor) {
    return visitor.visitType(this);
  }
}
