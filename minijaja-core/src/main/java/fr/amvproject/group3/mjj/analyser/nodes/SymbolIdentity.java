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
    return objNode != null ? objNode : new ObjNode("literal", 0, 0);
  }

  public boolean sameAs(SymbolIdentity other) {
    return this.typeNode.sameAs(other.typeNode) && this.objNode.sameAs(other.objNode);
  }
  
}
