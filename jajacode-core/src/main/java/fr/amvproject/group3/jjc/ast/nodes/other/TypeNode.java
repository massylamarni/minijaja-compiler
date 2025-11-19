package fr.amvproject.group3.jjc.ast.nodes.other;

import org.antlr.v4.runtime.Token;

import fr.amvproject.group3.jjc.ast.AstNode;
import fr.amvproject.group3.jjc.ast.AstVisitor;

public class TypeNode extends AstNode {
  protected enum TypeName {
    INT("entier"),
    BOOLEAN("boolean"),
    VOID("vide"),
    STRING("string");

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
  private final Token typeToken;

  public TypeNode(Token typeToken) {
    super(typeToken.getLine(), typeToken.getCharPositionInLine());
    this.typeToken = typeToken;
    this.type = TypeName.fromString(typeToken.getText());
  }

  // Helper
  public TypeNode(String type, int line, int column) {
    super(line, column);
    this.typeToken = null;
    this.type = TypeName.fromString(type);
  }

  public Token getTypeToken() {
    return typeToken;
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

  public boolean isVoid() {
    return type == TypeName.VOID;
  }

  public boolean isString() {
    return type == TypeName.STRING;
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
