package fr.amvproject.group3.mjj;

import fr.amvproject.group3.mjj.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.mjj.ast.nodes.other.TypeMethNode;
import fr.amvproject.group3.mjj.ast.nodes.other.TypeNode;
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

  public String print(int index) {
    StringBuilder sb = new StringBuilder();
    sb.append("-- Entry #").append(index).append(" --\n");
    sb.append("  Ident : ").append(ident.getName()).append("\n");
    sb.append("  Obj   : ").append(obj.getName()).append("\n");
    sb.append("  Type  : ");
    if (obj.isCst())
      sb.append(((TypeNode) type).getName()).append("\n");
    else
      sb.append(((TypeMethNode) type).getName()).append("\n");
    sb.append("  Value : ").append(value).append("\n");
    
    return sb.toString();
  }
}