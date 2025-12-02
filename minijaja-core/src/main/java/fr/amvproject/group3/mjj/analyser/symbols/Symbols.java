package fr.amvproject.group3.mjj.analyser.symbols;

import java.util.ArrayList;
import java.util.List;

import fr.amvproject.group3.mjj.analyser.nodes.TypeNode;
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
