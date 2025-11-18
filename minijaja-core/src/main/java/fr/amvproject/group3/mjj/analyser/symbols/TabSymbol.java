package fr.amvproject.group3.mjj.analyser.symbols;

import fr.amvproject.group3.mjj.analyser.nodes.TypeNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.IdentNode;

public class TabSymbol extends Symbols {
  private final Integer size;

  public TabSymbol(IdentNode ident, TypeNode type, Integer size) {
    super(ident, type);
    this.size = size;
  }

  public Integer getSize() {
    return size;
  }

  public boolean isDynamic() {
    return size != null;
  }

  @Override
  public String print() {
    StringBuilder sb = new StringBuilder();
    String usageLines = String.join(";", getLinesOfUsage().stream().map(String::valueOf).toList());

    sb.append(String.format("%s,%s,%s,%d,%s,%d,%s%n",
        getIdentNode().getName(),
        getTypeNode().getName(),
        "tab",
        size,
        "<?>",
        getIdentNode().getLine(),
        usageLines));

    return sb.toString();
  }
}
