package fr.amvproject.group3.mjj.analyser.symbols;

import java.util.List;

import fr.amvproject.group3.mjj.analyser.nodes.TypeNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.IdentNode;

public class MethodSymbol extends Symbols {
  private final List<TypeNode> paramsType;

  public MethodSymbol(IdentNode ident, TypeNode returnType, List<TypeNode> paramsType) {
    super(ident, returnType);
    this.paramsType = paramsType;
  }

  public List<TypeNode> getParamsType() {
    return paramsType;
  }

  @Override
  public String print() {
    StringBuilder sb = new StringBuilder();
    String usageLines = String.join(";", getLinesOfUsage().stream().map(String::valueOf).toList());

    sb.append(String.format("%s,%s,%s,%s,%s,%d,%s%n",
        getIdentNode().getName(),
        getTypeNode().getName(),
        "meth",
        "<?>",
        getParams(this),
        getIdentNode().getLine(),
        usageLines));

    return sb.toString();
  }

  private final StringBuilder getParams(MethodSymbol sym) {
    StringBuilder params = new StringBuilder();
    if (!sym.getParamsType().isEmpty()) {
      for (TypeNode type : sym.getParamsType()) {
        params.append(type.getName()).append(";");
      }
      if (!params.isEmpty()) params.setLength(params.length() - 1); // remove trailing semicolon
    } else {
      params.append("None");
    }
    return params;
  }
}
