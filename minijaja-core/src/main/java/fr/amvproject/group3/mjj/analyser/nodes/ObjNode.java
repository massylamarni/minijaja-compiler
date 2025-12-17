package fr.amvproject.group3.mjj.analyser.nodes;

import fr.amvproject.group3.mjj.ast.AstNode;
import fr.amvproject.group3.mjj.ast.AstVisitor;

public class ObjNode extends AstNode {
  protected enum ObjName {
    VAR("var"),
    ARR_POINTER("arr_pointer"),
    ARR_ACCESS("arr_access"),
    CST("cst"),
    VCST("vsct"),
    CLASS("class"),
    LITERAL("literal"),
    METH("meth");

    private final String name;

    ObjName(String name) {
      this.name = name;
    }

    public static ObjName fromString(String name) {
      for (ObjName obj : values()) {
        if (obj.name.equals(name)) {
          return obj;
        }
      }
      throw new IllegalArgumentException("Obj non valide : " + name);
    }
  }

  private final ObjName obj;

  public ObjNode(String name, int line, int column) {
    super(line, column);
    this.obj = ObjName.fromString(name);
  }

  public String getName() {
    return obj.name;
  }

  public ObjName getObj() {
    return obj;
  }

  public boolean isVar() {
    return obj == ObjName.VAR;
  }

  public boolean isCst() {
    return obj == ObjName.CST;
  }

  public boolean isVcst() {
    return obj == ObjName.VCST;
  }

  public boolean isClass() {
    return obj == ObjName.CLASS;
  }

  public boolean isLiteral() {
    return obj == ObjName.LITERAL;
  }

  public boolean isArrPointer() {
    return obj == ObjName.ARR_POINTER;
  }

  public boolean isArrAccess() {
    return obj == ObjName.ARR_ACCESS;
  }

  public boolean isMeth() {
    return obj == ObjName.METH;
  }

  @Override
  public boolean sameAs(AstNode other) {
    if (other instanceof ObjNode objNode) {
      return this.obj == objNode.obj;
    }
    return false;
  }

  @Override
  public <T> T accept(AstVisitor<T> visitor) {
    return null;
  }
}
