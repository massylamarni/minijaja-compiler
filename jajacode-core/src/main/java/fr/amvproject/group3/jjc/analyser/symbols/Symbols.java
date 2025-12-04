package fr.amvproject.group3.jjc.analyser.symbols;

import java.util.ArrayList;
import java.util.List;

import fr.amvproject.group3.jjc.analyser.nodes.TypeNode;
import fr.amvproject.group3.jjc.ast.nodes.finals.BoolLiteralNode;
import fr.amvproject.group3.jjc.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.jjc.ast.nodes.finals.IntLiteralNode;
import fr.amvproject.group3.jjc.ast.nodes.finals.StringLiteralNode;
import fr.amvproject.group3.jjc.ast.nodes.intermediate.ValueNode;
import fr.amvproject.group3.jjc.ast.nodes.other.ObjNode;

public class Symbols {
  private final IdentNode identNode;
  private final TypeNode typeNode;
  private final ObjNode objNode;
  private final ValueNode valueNode;
  private List<Integer> linesOfUsage = new ArrayList<>();
  private List<Integer> scopesOfUsage = new ArrayList<>();

  public Symbols(IdentNode identNode, TypeNode typeNode, ObjNode objNode, ValueNode valueNode) {
    this.identNode = identNode;
    this.typeNode = typeNode;
    this.objNode = objNode;
    this.valueNode = valueNode;
    linesOfUsage.add(identNode.getLine());
  }

  public IdentNode getIdentNode() {
    return identNode;
  }

  public TypeNode getTypeNode() {
    return typeNode;
  }

  public ObjNode getObjNode() {
    return objNode;
  }

  public ValueNode getValueNode() {
    return valueNode;
  }

  public List<Integer> getLinesOfUsage() {
    return linesOfUsage;
  }

  public void addLineOfUsage(int line) {
    linesOfUsage.add(line);
  }

  public void addScopeOfUsage(int scope) {
    scopesOfUsage.add(scope);
  }

  public int getLine() {
    return identNode.getLine();
  }

  public int getColumn() {
    return identNode.getColumn();
  }

  public List<Integer> getScopesOfUsage() {
      return scopesOfUsage;
  }

  public String print() {
    StringBuilder sb = new StringBuilder();
    String usageLines = String.join(";", getLinesOfUsage().stream().map(String::valueOf).toList());

    Object value = null;
    if (valueNode instanceof IntLiteralNode intLiteralNode) value = intLiteralNode.getValue();
    else if (valueNode instanceof BoolLiteralNode boolLiteralNode) value = boolLiteralNode.getValue();
    else if (valueNode instanceof StringLiteralNode stringLiteralNode) value = stringLiteralNode.getValue();

    sb.append(String.format("%s,%s,%s,%s,%d,%s%n",
        identNode.getName(),
        typeNode.getName(),
        objNode.getName(),
        value,
        identNode.getLine(),
        usageLines));

    return sb.toString();
  }
}
