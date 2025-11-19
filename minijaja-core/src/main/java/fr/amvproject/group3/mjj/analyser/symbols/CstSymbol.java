package fr.amvproject.group3.mjj.analyser.symbols;

import fr.amvproject.group3.mjj.analyser.nodes.TypeNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.IdentNode;

public class CstSymbol extends Symbols {
  private boolean isVcst;

  public CstSymbol(IdentNode ident, TypeNode type, boolean isVcst) {
    super(ident, type);
    this.isVcst = isVcst;
  }

  public boolean isVcst() {
    return isVcst;
  }

  @Override
  public String print() {
    StringBuilder sb = new StringBuilder();
    String usageLines = String.join(";", getLinesOfUsage().stream().map(String::valueOf).toList());

    sb.append(String.format("%s,%s,%s,%s,%s,%d,%s%n",
        getIdentNode().getName(),
        getTypeNode().getName(),
        isVcst ? "vcst" : "cst",
        "<?>",
        "<?>",
        getIdentNode().getLine(),
        usageLines));

    return sb.toString();
  }
}
