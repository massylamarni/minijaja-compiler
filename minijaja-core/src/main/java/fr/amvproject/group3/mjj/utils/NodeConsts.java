package fr.amvproject.group3.mjj.utils;

import org.antlr.v4.runtime.Token;

import fr.amvproject.group3.mjj.ast.nodes.finals.BoolLiteralNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.IntLiteralNode;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.ExpNode;
import fr.amvproject.group3.mjj.ast.nodes.other.TypeMethNode;
import fr.amvproject.group3.mjj.ast.nodes.other.TypeNode;
import fr.amvproject.group3.mjj.utils.NodeOperations.MockToken;


public class NodeConsts {
  public static final IntLiteralNode DEFAULT_INT_NODE = new IntLiteralNode("0", 0, 0);
  public static final BoolLiteralNode DEFAULT_BOOL_NODE = new BoolLiteralNode("false", 0, 0);
  public static final ExpNode DEFAULT_VOID_NODE = null;

  public static final ExpNode getDefaultInitValue(TypeNode typeNode) {
    if (typeNode.isInt()) return DEFAULT_INT_NODE;
    return DEFAULT_BOOL_NODE;
  }

  public static final ExpNode getDefaultInitValue(TypeMethNode typeMethNode) {
    if (typeMethNode.isInt()) return DEFAULT_INT_NODE;
    if (typeMethNode.isBoolean()) return DEFAULT_BOOL_NODE;
    return DEFAULT_VOID_NODE;
  }

  public static final Token getDefaultAssignToken() {
    return new MockToken("=", 0, 0);
  }
}
