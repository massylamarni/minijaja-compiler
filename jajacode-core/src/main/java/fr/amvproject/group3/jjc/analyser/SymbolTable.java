package fr.amvproject.group3.jjc.analyser;

import java.util.LinkedHashMap;
import java.util.Map;

import fr.amvproject.group3.jjc.analyser.nodes.TypeNode;
import fr.amvproject.group3.jjc.analyser.symbols.Symbols;
import fr.amvproject.group3.jjc.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.jjc.ast.nodes.intermediate.ValueNode;
import fr.amvproject.group3.jjc.ast.nodes.other.ObjNode;
import fr.amvproject.group3.utils.StringSaver;

public class SymbolTable {
  private final Map<IdentNode, Symbols> symbols = new LinkedHashMap<>();

  public Symbols lookup(IdentNode identNode) {
    return symbols.get(identNode);
  }

  public void addSymbol(IdentNode identNode, TypeNode typeNode, ObjNode objNode, ValueNode valueNode) {
    symbols.put(identNode, new Symbols(identNode, typeNode, objNode, valueNode));
  }

  public void addSymbol(IdentNode identNode, TypeNode typeNode, ValueNode valueNode) {
    symbols.put(identNode, new Symbols(identNode, typeNode, new ObjNode("tab", identNode.getLine(), identNode.getColumn()), valueNode));
  }

  public Symbols recordUsage(IdentNode ident, int scope) {
    Symbols sym = symbols.get(ident);
    sym.addLineOfUsage(ident.getLine());
    sym.addScopeOfUsage(scope);
    return sym;
  }

  public void reset() {
    symbols.clear();
  }

  /* Print */
  
  public void print() {
    StringBuilder sb = new StringBuilder();

    // CSV header
    sb.append("Name,Type,Obj,Size,Params,DeclLine,UsageLines\n");

    for (Symbols sym : symbols.values()) {
      sb.append(sym.print());
    }

    StringSaver.save(sb.toString(), "symbolTable.csv", "main");
  }
  
}
