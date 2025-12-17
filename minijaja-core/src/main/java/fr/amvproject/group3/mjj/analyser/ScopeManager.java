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

  public void newScope() {
    SymbolTable newSymbolTable = new SymbolTable();
    stack.push(newSymbolTable);
    symbolTableRef = newSymbolTable;
    scopeDepth++;
  }

  public void exitScope() {
    stack.pop();
    symbolTableRef = stack.peek();
    scopeDepth--;
  }

  public Symbols lookup(IdentNode identNode) {
    for (SymbolTable symbolTable : stack) {
      Symbols sym = symbolTable.lookup(identNode);
      if (sym != null) return sym;
    }
    return null;
  }

  public void reset() {
    scopeDepth = 0;
    symbolTableRef = stack.peekLast();
    while (stack.size() > 1) stack.pop();
  }
}
