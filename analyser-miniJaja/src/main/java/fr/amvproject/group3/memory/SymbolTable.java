package fr.amvproject.group3.memory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import fr.amvproject.group3.ast.nodes.abstracts.ParamsNode;
import fr.amvproject.group3.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.ast.nodes.finals.ParamNode;
import fr.amvproject.group3.ast.nodes.other.TypeNode;
import fr.amvproject.group3.exceptions.SymbolTableException;
import fr.amvproject.group3.utils.StringSaver;

public class SymbolTable {
  public static class Symbol {
    private final IdentNode ident;
    private final TypeNode type;
    private final String dimension; // Table size can be defined by an expression
    private final ParamsNode entetes;
    private final int lineOfDeclaration;
    private List<Integer> linesOfUsage;

    public Symbol(IdentNode ident, TypeNode type, String dimension, ParamsNode entetes, int lineOfDeclaration) {
      this.ident = ident;
      this.type = type;
      this.dimension = dimension;
      this.entetes = entetes;
      this.lineOfDeclaration = lineOfDeclaration;
      this.linesOfUsage = new ArrayList<>();
    }

    public IdentNode getIdent() {
      return ident;
    }

    public TypeNode getType() {
      return type;
    }

    public String getDimension() {
      return dimension;
    }

    public ParamsNode getEntetes() {
      return entetes;
    }

    public int getLineOfDeclaration() {
      return lineOfDeclaration;
    }

    public List<Integer> getLinesOfUsage() {
      return linesOfUsage;
    }

    public void addLineOfUsage(int lineOfUsage) {
      linesOfUsage.add(lineOfUsage);
    }
  }

  private final Map<String, Symbol> symbols = new LinkedHashMap<>();

  public boolean addSymbol(IdentNode ident, TypeNode type, ParamsNode entetes, String dimension,
      int lineOfDeclaration) throws SymbolTableException {
    if (symbols.containsKey(ident.getName())) {
      throw new SymbolTableException("Symbol " + ident.getName() + " already declared at line " + lineOfDeclaration);
    }
    symbols.put(ident.getName(), new Symbol(ident, type, dimension, entetes, lineOfDeclaration));
    return true;
  }

  public Symbol lookup(IdentNode ident) {
    return symbols.get(ident.getName());
  }

  public boolean recordUsage(IdentNode ident, int lineOfUsage) throws SymbolTableException {
    Symbol sym = symbols.get(ident.getName());

    if (sym == null) {
      throw new SymbolTableException("Symbol " + ident.getName() + " not declared but used at line " + lineOfUsage);
    }
    sym.addLineOfUsage(lineOfUsage);
    
    return true;
  }

  /* Print */

  private final StringBuilder getParams(Symbol sym) {
    StringBuilder params = new StringBuilder();
    if (sym.getEntetes() != null && !sym.getEntetes().getList().isEmpty()) {
      for (ParamNode e : sym.getEntetes().getList()) {
        String pType = e.getType() != null ? e.getType().getName() : "<?>";
        String pName = e.getIdent() != null ? e.getIdent().getName() : "<?>";
        params.append(pType).append(" ").append(pName).append(";");
      }
      if (!params.isEmpty()) params.setLength(params.length() - 1); // remove trailing semicolon
    } else {
      params.append("None");
    }
    return params;
  }

  public void print() {
    StringBuilder sb = new StringBuilder();

    // CSV header
    sb.append("Name,Type,Dimension,Params,DeclLine,UsageLines\n");

    for (Symbol sym : symbols.values()) {
      String name = sym.getIdent() != null ? sym.getIdent().getName() : "<?>";

      String type = sym.getType() != null ? sym.getType().getName() : "<?>";

      String dim = sym.getDimension();

      // Format parameters as "type name;type name;..."
      StringBuilder params = getParams(sym);

      // Format usage lines as "line1;line2;line3"
      String usageLines = sym.getLinesOfUsage().isEmpty() ? "None"
          : String.join(";", sym.getLinesOfUsage().stream().map(String::valueOf).toList());

      // Append CSV row
      sb.append(String.format("%s,%s,%s,%s,%d,%s%n",
          name,
          type,
          dim,
          params,
          sym.getLineOfDeclaration(),
          usageLines));
    }

    StringSaver.save(sb.toString(), "symbolTable.csv");
  }
}