package fr.amvproject.group3.jjc.ast.nodes.other;

import org.antlr.v4.runtime.Token;

import fr.amvproject.group3.jjc.ast.AstNode;
import fr.amvproject.group3.jjc.ast.AstVisitor;

public class ObjNode extends AstNode {
  protected enum ObjName {
    VAR("var"),
    TAB("tab"),
    CST("cst"),
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
  private final Token objToken;

  public ObjNode(Token objToken) {
    this.objToken = objToken;
    this.obj = ObjName.fromString(objToken.getText());
  }

  // Helper
  public ObjNode(String obj, int line, int column) {
    super(line, column);
    this.objToken = null;
    this.obj = ObjName.fromString(obj);
  }

  public Token getObjToken() {
    return objToken;
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
