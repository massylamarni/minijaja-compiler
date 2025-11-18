package fr.amvproject.group3.mjj.analyser;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import fr.amvproject.group3.mjj.analyser.nodes.TypeNode;
import fr.amvproject.group3.mjj.analyser.symbols.ClassSymbol;
import fr.amvproject.group3.mjj.analyser.symbols.CstSymbol;
import fr.amvproject.group3.mjj.analyser.symbols.MethodSymbol;
import fr.amvproject.group3.mjj.analyser.symbols.Symbols;
import fr.amvproject.group3.mjj.analyser.symbols.TabSymbol;
import fr.amvproject.group3.mjj.analyser.symbols.VarSymbol;
import fr.amvproject.group3.mjj.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.utils.StringSaver;

public class SymbolTable {
  private final Map<IdentNode, Symbols> symbols = new LinkedHashMap<>();

  public Symbols lookup(IdentNode identNode) {
    return symbols.get(identNode);
  }

  public void addSymbol(IdentNode identNode) {
    symbols.put(identNode, new ClassSymbol(identNode));
  }

  public void addSymbol(IdentNode identNode, TypeNode typeMethNode) {
    symbols.put(identNode, new VarSymbol(identNode, typeMethNode));
  }

  public void addSymbol(IdentNode identNode, TypeNode typeNode, boolean isVcst) {
    symbols.put(identNode, new CstSymbol(identNode, typeNode, isVcst));
  }

  public void addSymbol(IdentNode identNode, TypeNode typeMethNode, Integer size) {
    symbols.put(identNode, new TabSymbol(identNode, typeMethNode, size));
  }

  public void addSymbol(IdentNode identNode, TypeNode returnType, List<TypeNode> paramsType) {
    symbols.put(identNode, new MethodSymbol(identNode, returnType, paramsType));
  }

  public Symbols recordUsage(IdentNode ident) {
    Symbols sym = symbols.get(ident);
    sym.addLineOfUsage(ident.getLine());
    return sym;
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
