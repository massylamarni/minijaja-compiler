package fr.amvproject.group3.mjj.analyser;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import fr.amvproject.group3.mjj.ast.nodes.abstracts.ParamsNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.ArrayAccessNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.ParamNode;
import fr.amvproject.group3.mjj.ast.nodes.other.ObjNode;
import fr.amvproject.group3.mjj.ast.nodes.other.TypeMethNode;
import fr.amvproject.group3.mjj.exceptions.SemanticException;
import fr.amvproject.group3.utils.StringSaver;

public class SymbolTable {
  public static class Symbol {
    private final IdentNode ident;
    private final TypeMethNode type;
    private final ObjNode obj;
    private final Integer dimension; // Table size can be defined by an expression TODO
    private final ParamsNode entetes;
    private final Object value;
    private List<Integer> linesOfUsage;
    private List<Object> values;

    public Symbol(IdentNode ident, TypeMethNode type, ObjNode obj, Integer dimension, ParamsNode entetes, Object value) {
      this.ident = ident;
      this.type = type;
      this.obj = obj;
      this.dimension = dimension;
      this.entetes = entetes;
      this.value = value;
      this.linesOfUsage = new ArrayList<>();
      this.values = new ArrayList<>();
      linesOfUsage.add(ident.getLine());
      values.add(value);
    }

    public IdentNode getIdent() {
      return ident;
    }

    public TypeMethNode getType() {
      return type;
    }

    public ObjNode getObj() {
      return obj;
    }

    public int getDimension() {
      return dimension;
    }

    public ParamsNode getEntetes() {
      return entetes;
    }

    public Object getValue() {
      return value;
    }

    public List<Integer> getLinesOfUsage() {
      return linesOfUsage;
    }

    public List<Object> getValues() {
      return values;
    }

    public void addLineOfUsage(int lineOfUsage) {
      linesOfUsage.add(lineOfUsage);
    }
  }

  /* Utility */

  public TypeMethNode getTypeFromValue(Object value) {
     
    return null;
  }

  /* Symbols */

  private final Map<IdentNode, Symbol> symbols = new LinkedHashMap<>();

  public Symbol lookup(IdentNode identNode) {
    return symbols.get(identNode);
  }

  public Symbol lookup(ArrayAccessNode arrayAccessNode) {
    return symbols.get(arrayAccessNode.getIdent()); // TODO complete this
  }

  public boolean addSymbol(IdentNode ident, TypeMethNode type, ObjNode obj, ParamsNode entetes, Integer dimension, Object value) throws SemanticException {
    if (symbols.containsKey(ident)) {
      throw new SemanticException("Symbol " + ident.getName() + " already declared at line " + ident.getLine());
    }
    TypeMethNode expType = getTypeFromValue(value);
    if (!type.equals(expType)) {
      throw new SemanticException("Value of Type " + expType + " not compatible with declaration of type " + type + " at line " + ident.getLine());
    }
    symbols.put(ident, new Symbol(ident, type, obj, dimension, entetes, value));
    return true;
  }

  public boolean recordUsage(IdentNode ident) throws SemanticException {
    Symbol sym = symbols.get(ident);

    if (sym == null) {
      throw new SemanticException("Symbol " + ident.getName() + " not declared but used at line " + ident.getLine());
    }

    TypeMethNode expType = getTypeFromValue(sym.getValue());
    if (!sym.getType().equals(expType)) {
      throw new SemanticException("Value of Type " + expType + " not compatible with declaration of type " + sym.getType() + " at line " + ident.getLine());
    }

    sym.addLineOfUsage(ident.getLine());

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

      int dim = sym.getDimension();

      // Format parameters as "type name;type name;..."
      StringBuilder params = getParams(sym);

      // Format usage lines as "line1;line2;line3"
      String usageLines = sym.getLinesOfUsage().isEmpty() ? "None"
          : String.join(";", sym.getLinesOfUsage().stream().map(String::valueOf).toList());

      // Append CSV row
      sb.append(String.format("%s,%s,%d,%s,%d,%s%n",
          name,
          type,
          dim,
          params,
          sym.getIdent().getLine(),
          usageLines));
    }

    StringSaver.save(sb.toString(), "symbolTable.csv", "main");
  }
}
