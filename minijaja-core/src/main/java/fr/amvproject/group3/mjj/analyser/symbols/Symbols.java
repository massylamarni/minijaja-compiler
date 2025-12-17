package fr.amvproject.group3.mjj.analyser.symbols;

import java.util.ArrayList;
import java.util.List;

import fr.amvproject.group3.mjj.analyser.nodes.TypeNode;
import fr.amvproject.group3.mjj.analyser.nodes.ObjNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.IdentNode;

public abstract class Symbols {
  private final IdentNode identNode;
  private final TypeNode typeNode;
  private List<Integer> linesOfUsage = new ArrayList<>();
  private List<Integer> scopesOfUsage = new ArrayList<>();

  protected Symbols(IdentNode identNode, TypeNode typeNode) {
    this.identNode = identNode;
    this.typeNode = typeNode;
    linesOfUsage.add(identNode.getLine());
  }

  public IdentNode getIdentNode() {
    return identNode;
  }

  public TypeNode getTypeNode() {
    return typeNode;
  }

  public ObjNode getObjNode() {
    if (this instanceof VarSymbol) return new ObjNode("var", getLine(), getColumn());
    if (this instanceof CstSymbol cstSymbol) return new ObjNode(cstSymbol.isVcst() ? "vcst" : "cst", getLine(), getColumn());
    if (this instanceof TableSymbol) return new ObjNode("arr_pointer", getLine(), getColumn());
    if (this instanceof MethodSymbol) return new ObjNode("meth", getLine(), getColumn());
    if (this instanceof ClassSymbol) return new ObjNode("class", getLine(), getColumn());
    return new ObjNode("literal", getLine(), getColumn()); 
  }

  public List<Integer> getLinesOfUsage() {
    return linesOfUsage;
  }

  public void addLineOfUsage(int line) {
    linesOfUsage.add(line);
  }

  public void addScopeOfUsage(int scope) {
    scopesOfUsage.add(scope);
  }

  public int getLine() {
    return identNode.getLine();
  }

  public int getColumn() {
    return identNode.getColumn();
  }

  public List<Integer> getScopesOfUsage() {
      return scopesOfUsage;
  }

    public abstract String print();
}
