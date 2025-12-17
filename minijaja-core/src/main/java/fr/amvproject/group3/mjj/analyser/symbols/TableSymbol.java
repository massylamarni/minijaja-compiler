package fr.amvproject.group3.mjj.analyser.symbols;

import fr.amvproject.group3.mjj.analyser.nodes.TypeNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.IdentNode;

public class TableSymbol extends Symbols {
  public TableSymbol(IdentNode ident, TypeNode type) {
    super(ident, type);
  }

  @Override
  public String print() {
    StringBuilder sb = new StringBuilder();
    String usageLines = String.join(";", getLinesOfUsage().stream().map(String::valueOf).toList());

    sb.append(String.format("%s,%s,%s,%s,%s,%d,%s%n",
        getIdentNode().getName(),
        getTypeNode().getName(),
        "tab",
        "<?>",
        "<?>",
        getIdentNode().getLine(),
        usageLines));

    return sb.toString();
  }
}
