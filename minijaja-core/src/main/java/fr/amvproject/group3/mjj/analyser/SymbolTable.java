package fr.amvproject.group3.mjj.analyser;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import fr.amvproject.group3.mjj.analyser.nodes.SymbolIdentity;
import fr.amvproject.group3.mjj.analyser.nodes.SymbolSignature;
import fr.amvproject.group3.mjj.analyser.nodes.TypeNode;
import fr.amvproject.group3.mjj.analyser.nodes.ObjNode;
import fr.amvproject.group3.mjj.analyser.symbols.ClassSymbol;
import fr.amvproject.group3.mjj.analyser.symbols.CstSymbol;
import fr.amvproject.group3.mjj.analyser.symbols.MethodSymbol;
import fr.amvproject.group3.mjj.analyser.symbols.Symbols;
import fr.amvproject.group3.mjj.analyser.symbols.TableSymbol;
import fr.amvproject.group3.mjj.analyser.symbols.VarSymbol;
import fr.amvproject.group3.mjj.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.mjj.utils.SemanticOperations;

public class SymbolTable {
  private final Map<SymbolSignature, Symbols> symbols = new LinkedHashMap<>();

  public Symbols lookup(SymbolSignature symbolSignature) {
    return symbols.get(symbolSignature);
  }

  public void addSymbol(IdentNode identNode) {
    symbols.put(new SymbolSignature(identNode), new ClassSymbol(identNode));
  }

  public void addSymbol(IdentNode identNode, TypeNode typeNode, ObjNode objNode) {
    if (objNode.isVar()) {
      symbols.put(new SymbolSignature(identNode), new VarSymbol(identNode, typeNode));
    } else {
      symbols.put(new SymbolSignature(identNode), new TableSymbol(identNode, typeNode));
    }
  }

  public void addSymbol(IdentNode identNode, TypeNode typeNode, boolean isVcst) {
    symbols.put(new SymbolSignature(identNode), new CstSymbol(identNode, typeNode, isVcst));
  }

  public void addSymbol(IdentNode identNode, TypeNode returnType, List<SymbolIdentity> paramsType) {
    symbols.put(new SymbolSignature(identNode, SemanticOperations.getHashFromSymIdList(paramsType)), new MethodSymbol(identNode, returnType, paramsType));
  }

  public Symbols recordUsage(IdentNode identNode, int scope) {
    Symbols sym = symbols.get(new SymbolSignature(identNode));
    if (sym == null) return null;
    sym.addLineOfUsage(identNode.getLine());
    sym.addScopeOfUsage(scope);
    return sym;
  }

  public Symbols recordUsage(SymbolSignature symS, int scope) {
    Symbols sym = symbols.get(symS);
    if (sym == null) return null;
    sym.addLineOfUsage(symS.getIdentNode().getLine());
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
