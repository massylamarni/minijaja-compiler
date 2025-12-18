package fr.amvproject.group3.mjj.ast.nodes.other;

import org.antlr.v4.runtime.Token;

import fr.amvproject.group3.mjj.ast.AstNode;
import fr.amvproject.group3.mjj.ast.AstVisitor;

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
  private final Token typeMethToken;

  public TypeMethNode(Token typeMethToken) {
    super(typeMethToken.getLine(), typeMethToken.getCharPositionInLine());
    this.typeMethToken = typeMethToken;
    this.typeMeth = TypeMethName.fromString(typeMethToken.getText());
  }

  // Helper
  public TypeMethNode(String typeMeth, int line, int column) {
    super(line, column);
    this.typeMethToken = null;
    this.typeMeth = TypeMethName.fromString(typeMeth);
  }

  public Token getTypeMethToken() {
    return typeMethToken;
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
