package fr.amvproject.group3.mjj.analyser;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import fr.amvproject.group3.mjj.analyser.nodes.SymbolIdentity;
import fr.amvproject.group3.mjj.analyser.nodes.TypeNode;
import fr.amvproject.group3.mjj.analyser.nodes.ObjNode;
import fr.amvproject.group3.mjj.analyser.symbols.ClassSymbol;
import fr.amvproject.group3.mjj.analyser.symbols.CstSymbol;
import fr.amvproject.group3.mjj.analyser.symbols.MethodSymbol;
import fr.amvproject.group3.mjj.analyser.symbols.Symbols;
import fr.amvproject.group3.mjj.analyser.symbols.TableSymbol;
import fr.amvproject.group3.mjj.analyser.symbols.VarSymbol;
import fr.amvproject.group3.mjj.ast.nodes.finals.IdentNode;

public class SymbolTable {
  private final Map<IdentNode, Symbols> symbols = new LinkedHashMap<>();

  public Symbols lookup(IdentNode identNode) {
    return symbols.get(identNode);
  }

  public void addSymbol(IdentNode identNode) {
    symbols.put(identNode, new ClassSymbol(identNode));
  }

  public void addSymbol(IdentNode identNode, TypeNode typeNode, ObjNode objNode) {
    if (objNode.isVar()) {
      symbols.put(identNode, new VarSymbol(identNode, typeNode));
    } else {
      symbols.put(identNode, new TableSymbol(identNode, typeNode));
    }
  }

  public void addSymbol(IdentNode identNode, TypeNode typeNode, boolean isVcst) {
    symbols.put(identNode, new CstSymbol(identNode, typeNode, isVcst));
  }

  public void addSymbol(IdentNode identNode, TypeNode returnType, List<SymbolIdentity> paramsType) {
    symbols.put(identNode, new MethodSymbol(identNode, returnType, paramsType));
  }

  public Symbols recordUsage(IdentNode identNode, int scope) {
    Symbols sym = symbols.get(identNode);
    if (sym == null) return null;
    sym.addLineOfUsage(identNode.getLine());
    sym.addScopeOfUsage(scope);
    return sym;
  }

  public void reset() {
    symbols.clear();
  }

  /* Print */
  
  public String print() {
    StringBuilder sb = new StringBuilder();

    for (Symbols sym : symbols.values()) {
      sb.append(sym.print());
    }

    return sb.toString();
  }
  
}
