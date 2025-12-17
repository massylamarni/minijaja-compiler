package fr.amvproject.group3.mjj.analyser.nodes;

public class SymbolIdentity {
  private final TypeNode typeNode;
  private final ObjNode objNode;

  public SymbolIdentity(TypeNode typeNode, ObjNode objNode) {
    this.typeNode = typeNode;
    this.objNode = objNode;
  }

  public TypeNode getTypeNode() {
    return typeNode;
  }

  public ObjNode getObjNode() {
    return objNode;
  }

  public boolean IsParamCompatibleWith(SymbolIdentity other) {
    if (!this.typeNode.sameAs(other.typeNode)) return false;
    if (this.objNode.isClass() || this.objNode.isArrPointer() || this.objNode.isMethPointer()) return false;
    if (!other.objNode.isVar()) return false;
    return true;
  }
  
}
