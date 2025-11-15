package fr.amvproject.group3.mjj.ast.nodes.intermediate;

import fr.amvproject.group3.mjj.analyser.SymbolTable;
import fr.amvproject.group3.mjj.ast.AstNode;
import fr.amvproject.group3.mjj.ast.nodes.exprs.BinaryExpNode;
import fr.amvproject.group3.mjj.ast.nodes.exprs.CallExpNode;
import fr.amvproject.group3.mjj.ast.nodes.exprs.LengthNode;
import fr.amvproject.group3.mjj.ast.nodes.exprs.UnaryExpNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.ArrayAccessNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.BoolLiteralNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.IntLiteralNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.StringLiteralNode;
import fr.amvproject.group3.mjj.exceptions.SemanticException;

public abstract class ExpNode extends AstNode {
  public Object evaluate(SymbolTable symbolTable) {
    if (this instanceof IntLiteralNode intLiteralNode) {
      return intLiteralNode.getValue();
    } else if (this instanceof StringLiteralNode stringLiteralNode) {
      return stringLiteralNode.getValue();
    } else if (this instanceof BoolLiteralNode boolLiteralNode) {
      return boolLiteralNode.getValue();
    } else if (this instanceof IdentNode identNode) {
      return symbolTable.lookup(identNode).getValue();
    } else if (this instanceof ArrayAccessNode arrayAccessNode) {
      return symbolTable.lookup(arrayAccessNode);
    } else if (this instanceof BinaryExpNode binaryExpNode) {
      Object leftExpValue = binaryExpNode.getLeftExp().evaluate(symbolTable);
      Object rightExpValue = binaryExpNode.getRightExp().evaluate(symbolTable);

      if (leftExpValue instanceof Boolean != rightExpValue instanceof Boolean) {
        throw new SemanticException("Type mismatch !");
      }
      if (leftExpValue instanceof Integer && !binaryExpNode.getOp().takesInteger()) {
        throw new SemanticException("Incompatible operand, expecting Boolean, Integer given !");
      }
      if (leftExpValue instanceof Boolean && !binaryExpNode.getOp().takesBoolean()) {
        throw new SemanticException("Incompatible operand, expecting Integer, Boolean given !");
      }
      switch (binaryExpNode.getOp().getOp()) {
        case AND: return (boolean) leftExpValue && (boolean) rightExpValue;
        case OR:  return (boolean) leftExpValue || (boolean) rightExpValue;
        case ADD: return (int) leftExpValue + (int) rightExpValue;
        case SUB: return (int) leftExpValue - (int) rightExpValue;
        case MUL: return (int) leftExpValue * (int) rightExpValue;
        case DIV:
            if ((int) rightExpValue == 0) throw new SemanticException("Division by zero !");
            return (int) leftExpValue / (int) rightExpValue;
        case EQ: return leftExpValue == rightExpValue;
        case GT: return (int) leftExpValue > (int) rightExpValue;
        default: throw new SemanticException("Unknown semantic exception !");
      }
    } else if (this instanceof UnaryExpNode unaryExpNode) {
      Object expValue = unaryExpNode.getExp().evaluate(symbolTable);

      if (expValue instanceof Integer && unaryExpNode.getOp().isNot()) {
        throw new SemanticException("Incompatible operand, expecting Boolean, Integer given !");
      }
      if (expValue instanceof Boolean && unaryExpNode.getOp().isNeg()) {
        throw new SemanticException("Incompatible operand, expecting Integer, Boolean given !");
      }
      switch (unaryExpNode.getOp().getOp()) {
        case NOT: return (int) expValue * -1;
        case NEG: return !((boolean) expValue);
        default: throw new SemanticException("Unknown semantic exception !");
      }
    } else if (this instanceof LengthNode lengthNode) {
      if (symbolTable.lookup(lengthNode.getIdent()).getObj().isMeth()) {
        throw new SemanticException("Incompatible parameter for Length, expecting Array, Variable or Final, Method given !");
      }
      if (symbolTable.lookup(lengthNode.getIdent()).getObj().isTab()) {
        return symbolTable.lookup(lengthNode.getIdent()).getDimension();
      }

      Object identValue = symbolTable.lookup(lengthNode.getIdent()).getValue();
      if (identValue instanceof Integer) {
        throw new SemanticException("Incompatible expression for Length, expecting Array or String, Integer given !");
      }
      if (identValue instanceof Boolean) {
        throw new SemanticException("Incompatible expression for Length, expecting Array or String, Boolean given !");
      }
      if (identValue instanceof String stringValue) {
        return stringValue.length();
      }

    } else if (this instanceof CallExpNode callExpNode) {
      
    }
    return null;
  }// TODO handle method calls
}
