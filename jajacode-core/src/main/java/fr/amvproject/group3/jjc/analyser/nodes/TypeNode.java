package fr.amvproject.group3.jjc.analyser.nodes;

public class TypeNode {
  protected enum TypeName {
    INT("int"),
    BOOLEAN("boolean"),
    VOID("void"),
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

  public boolean isVoid() {
    return type == TypeName.VOID;
  }

  public boolean isString() {
    return type == TypeName.STRING;
  }

  public boolean sameAs(TypeNode other) {
    return this.type == other.type;
  }
}
