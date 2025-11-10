package fr.amvproject.group3.mjj;

import fr.amvproject.group3.mjj.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.nodes.ObjNode;

public class Quad {
  private IdentNode ident;
  private Object value;
  private ObjNode obj; // var, cst, vcst, tab, meth, frame
  private Object type; // int, boolean, void.

  public Quad(IdentNode ident, Object value, ObjNode obj, Object type) {
    this.ident = ident;
    this.value = value;
    this.obj = obj;
    this.type = type;
  }

  public IdentNode getIdent() {
    return ident;
  }

  public Object getValue() {
    return value;
  }

  public ObjNode getObj() {
    return obj;
  }

  public Object getType() {
    return type;
  }
}