package fr.amvproject.group3.jjc;

import fr.amvproject.group3.jjc.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.jjc.ast.nodes.other.TypeNode;
import fr.amvproject.group3.nodes.ObjNode;

public class Quad {
  private IdentNode ident;
  private Object value;
  private ObjNode obj; // var, cst, vcst, tab, meth, frame
  private TypeNode type; // int, boolean, void.

  public Quad(IdentNode ident, Object value, ObjNode obj, TypeNode type) {
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

  public TypeNode getType() {
    return type;
  }

  public String print(int index) {
    StringBuilder sb = new StringBuilder();
    sb.append("-- Entry #").append(index).append(" --\n");
    sb.append("  Ident : ").append(ident.getName()).append("\n");
    sb.append("  Obj   : ").append(obj.getName()).append("\n");
    sb.append("  Type  : ").append((type).getName()).append("\n");
    sb.append("  Value : ").append(value).append("\n");
    
    return sb.toString();
  }
}