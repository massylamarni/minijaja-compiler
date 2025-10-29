package fr.amvproject.group3.ast.nodes.other;

import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.AstVisitor;

public class ObjNode extends AstNode {
  protected enum ObjName {
    VAR("var"),
    TAB("tab"),
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

  public ObjNode(String name) {
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

  public boolean isTab() {
    return obj == ObjName.TAB;
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
    return visitor.visitObj(this);
  }
}
