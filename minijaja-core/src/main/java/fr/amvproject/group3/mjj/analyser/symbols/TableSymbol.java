package fr.amvproject.group3.mjj.analyser.symbols;

import fr.amvproject.group3.mjj.analyser.nodes.TypeNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.ExpNode;

public class TableSymbol extends Symbols {
  private final ExpNode sizeExpNode;

  public TableSymbol(IdentNode ident, TypeNode type, ExpNode sizeExpNode) {
    super(ident, type);
    this.sizeExpNode = sizeExpNode;
  }

  public ExpNode getSize() {
    return sizeExpNode;
  }

  public boolean isDynamic() {
    return sizeExpNode != null;
  }

  @Override
  public String print() {
    StringBuilder sb = new StringBuilder();
    String usageLines = String.join(";", getLinesOfUsage().stream().map(String::valueOf).toList());

    sb.append(String.format("%s,%s,%s,%d,%s,%d,%s%n",
        getIdentNode().getName(),
        getTypeNode().getName(),
        "tab",
        sizeExpNode.toString(),
        "<?>",
        getIdentNode().getLine(),
        usageLines));

    return sb.toString();
  }
}
