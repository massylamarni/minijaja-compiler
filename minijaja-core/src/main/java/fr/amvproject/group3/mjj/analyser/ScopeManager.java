package fr.amvproject.group3.mjj.analyser;

import java.util.ArrayDeque;
import java.util.Deque;

import fr.amvproject.group3.mjj.analyser.symbols.Symbols;
import fr.amvproject.group3.mjj.ast.nodes.finals.IdentNode;

public class ScopeManager {
  private Deque<SymbolTable> stack = new ArrayDeque<>();
  private SymbolTable symbolTableRef;
  private int scopeDepth = 0;

  public SymbolTable getSymbolTableRef() {
    return symbolTableRef;
  }

  public int getScopeDepth() {
    return scopeDepth;
  }

  public ScopeManager(SymbolTable symbolTable) {
    this.symbolTableRef = symbolTable;
    stack.push(symbolTable);
  }

  public SymbolTable newScope() {
    SymbolTable newSymbolTable = new SymbolTable();
    stack.push(newSymbolTable);
    symbolTableRef = newSymbolTable;
    scopeDepth++;
    return symbolTableRef;
  }

  public SymbolTable exitScope() {
    stack.pop();
    symbolTableRef = stack.peek();
    scopeDepth--;
    return symbolTableRef;
  }

  public Symbols lookup(IdentNode identNode) {
    for (SymbolTable symbolTable : stack) {
      Symbols sym = symbolTable.lookup(identNode);
      if (sym != null) return sym;
    }
    return null;
  }

  public Symbols recordUsage(IdentNode identNode) {
    for (SymbolTable symbolTable : stack) {
      Symbols sym = symbolTable.recordUsage(identNode, scopeDepth);
      if (sym != null) return sym;
    }
    return null;
  }

  public void reset() {
    scopeDepth = 0;
    symbolTableRef = stack.peekLast();
    while (stack.size() > 1) stack.pop();
  }

  /* Print */
  
  public String print() {
    StringBuilder sb = new StringBuilder();

    // CSV header
    sb.append("Name,Type,Obj,Size,Params,DeclLine,UsageLines\n");

    int i = 0;
    for (SymbolTable symbolTable : stack) {
      sb.append("\n--- scope (").append(i++).append(") ---\n");
      sb.append(symbolTable.print());
    }
    sb.append("----------------------------------");

    // StringSaver.save(sb.toString(), "symbolTable.csv", "main");
    return sb.toString();
  }
}
