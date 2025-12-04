package fr.amvproject.group3.mjj.analyser;

import fr.amvproject.group3.exceptions.SemanticException;
import fr.amvproject.group3.mjj.analyser.nodes.TypeNode;
import fr.amvproject.group3.mjj.analyser.symbols.ClassSymbol;
import fr.amvproject.group3.mjj.analyser.symbols.MethodSymbol;
import fr.amvproject.group3.mjj.analyser.symbols.Symbols;
import fr.amvproject.group3.mjj.analyser.symbols.TableSymbol;
import fr.amvproject.group3.mjj.analyser.symbols.VarSymbol;
import fr.amvproject.group3.mjj.ast.AstVisitor;
import fr.amvproject.group3.mjj.ast.nodes.abstracts.*;
import fr.amvproject.group3.mjj.ast.nodes.decls.*;
import fr.amvproject.group3.mjj.ast.nodes.exprs.BinaryExpNode;
import fr.amvproject.group3.mjj.ast.nodes.exprs.CallExpNode;
import fr.amvproject.group3.mjj.ast.nodes.exprs.LengthNode;
import fr.amvproject.group3.mjj.ast.nodes.exprs.UnaryExpNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.*;
import fr.amvproject.group3.mjj.ast.nodes.instrs.*;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.DeclNode;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.ExpNode;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.InstrNode;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.VarDeclNode;
import fr.amvproject.group3.mjj.ast.nodes.other.BinaryOpNode;
import fr.amvproject.group3.mjj.ast.nodes.other.TypeMethNode;
import fr.amvproject.group3.mjj.ast.nodes.other.UnaryOpNode;
import fr.amvproject.group3.mjj.ast.nodes.program.ClassNode;
import fr.amvproject.group3.mjj.ast.nodes.program.ProgramNode;

public class CompileTimeVisitor implements AstVisitor<ExpNode> {
  private ScopeManager scopeManager;

  public CompileTimeVisitor(ScopeManager scopeManager) {
    this.scopeManager = scopeManager;
  }

  private ExpNode getExpNodeFromValue(Object value, ExpNode expNode) {
    ExpNode evaluatedExpNode;
    if (value instanceof Integer) evaluatedExpNode = new IntLiteralNode(String.valueOf(value), expNode.getLine(), expNode.getColumn());
    else evaluatedExpNode = new BoolLiteralNode(String.valueOf(value), expNode.getLine(), expNode.getColumn());
    return evaluatedExpNode;
  }

  private Object getValueFromExpNode(ExpNode expNode) {
    Object value = null;
    if (expNode instanceof IntLiteralNode intLiteralNode) value = intLiteralNode.getValue();
    else if (expNode instanceof BoolLiteralNode boolLiteralNode) value = boolLiteralNode.getValue();
    else throw new SemanticException("Unhandled expression format", expNode.getLine(), expNode.getColumn());
    return value;
  }

  private void checkIfSymbolIsUsableInExp(ExpNode expNode) {
    if (expNode instanceof IdentNode identNode) {
      Symbols sym = scopeManager.lookup(identNode);

      if (sym == null) {
        throw new SemanticException("Symbol " + identNode.getName() + " is referenced, but never declared", expNode.getLine(), expNode.getColumn());
      }

      if (sym instanceof ClassSymbol) throw new SemanticException("Can't use a class in an expression", expNode.getLine(), expNode.getColumn());
      if (sym instanceof MethodSymbol) throw new SemanticException("Can't use a method pointer in an expression", expNode.getLine(), expNode.getColumn());
      if (sym instanceof TableSymbol) throw new SemanticException("Can't use a table pointer in an expression", expNode.getLine(), expNode.getColumn());
    }
  }

  @Override
  public ExpNode visitProgram(ProgramNode node) {
    for (ClassNode classeNode : node.getClasses()) {
      classeNode.accept(this);
    }
    return null;
  }

  @Override
  public ExpNode visitClass(ClassNode node) {
    node.getDecls().accept(this);
    node.getMain().accept(this);
    return null;
  }

  @Override
  public ExpNode visitDecls(DeclsNode node) {
    for (DeclNode declNode : node.getList()) {
      declNode.accept(this);
    }
    return null;
  }

  @Override
  public ExpNode visitVars(VarsNode node) {
    for (VarDeclNode varDeclNode : node.getList()) {
      varDeclNode.accept(this);
    }
    return null;
  }

  @Override
  public ExpNode visitVar(VarNode node) {
    return null;
  }

  @Override
  public ExpNode visitCst(CstNode node) {
    return null;
  }

  @Override
  public ExpNode visitTable(TableNode node) {
    return null;
  }

  @Override
  public ExpNode visitMethod(MethodNode node) {
    return null;
  }

  @Override
  public ExpNode visitMain(MainNode node) {
    node.getVars().accept(this);
    node.getInstrs().accept(this);
    return null;
  }

  @Override
  public ExpNode visitParams(ParamsNode node) {
    return null;
  }

  @Override
  public ExpNode visitParam(ParamNode node) {
    return null;
  }

  @Override
  public ExpNode visitInstrs(InstrsNode node) {
    for (InstrNode instrNode : node.getList()) {
      instrNode.accept(this);
    }
    return null;
  }

  @Override
  public ExpNode visitListExp(ListExpNode node) {
    return null;
  }

  @Override
  public ExpNode visitBinaryExp(BinaryExpNode node) {
    ExpNode leftExpNode = node.getLeftExp().accept(this);
    ExpNode rightExpNode = node.getRightExp().accept(this);
    checkIfSymbolIsUsableInExp(leftExpNode);
    checkIfSymbolIsUsableInExp(rightExpNode);

    if (leftExpNode instanceof BoolLiteralNode != rightExpNode instanceof BoolLiteralNode) {
      throw new SemanticException("Type mismatch !", node.getLine(), node.getColumn());
    }
    if (leftExpNode instanceof IntLiteralNode && !node.getOp().takesInteger()) {
      throw new SemanticException("Incompatible operand, expecting Boolean, Integer given !", node.getLine(), node.getColumn());
    }
    if (leftExpNode instanceof BoolLiteralNode && !node.getOp().takesBoolean()) {
      throw new SemanticException("Incompatible operand, expecting Integer, Boolean given !", node.getLine(), node.getColumn());
    }
    Object result = null;
    Object leftExpValue = getValueFromExpNode(leftExpNode);
    Object rightExpValue = getValueFromExpNode(rightExpNode);
    if (leftExpValue == null || rightExpValue == null) return node;
    switch (node.getOp().getOp()) {
      case AND: result = (boolean) leftExpValue && (boolean) rightExpValue;
        break;
      case OR: result = (boolean) leftExpValue || (boolean) rightExpValue;
        break;
      case ADD: result = (int) leftExpValue + (int) rightExpValue;
        break;
      case SUB: result = (int) leftExpValue - (int) rightExpValue;
        break;
      case MUL: result = (int) leftExpValue * (int) rightExpValue;
        break;
      case DIV:
          if ((int) rightExpValue == 0) throw new SemanticException("Division by zero !", node.getLine(), node.getColumn());
          result = (int) leftExpValue / (int) rightExpValue;
          break;
      case EQ: result = leftExpValue == rightExpValue;
        break;
      case GT: result = (int) leftExpValue > (int) rightExpValue;
        break;
      default: throw new IllegalArgumentException("Illegal operand !");
    }
    return getExpNodeFromValue(result, node);
  }

  @Override
  public ExpNode visitUnaryExp(UnaryExpNode node) {
    ExpNode expNode = node.getExp().accept(this);
    checkIfSymbolIsUsableInExp(expNode);

    if (expNode instanceof IntLiteralNode && node.getOp().isNot()) {
      throw new SemanticException("Incompatible operand, expecting Boolean, Integer given !", node.getLine(), node.getColumn());
    }
    if (expNode instanceof BoolLiteralNode && node.getOp().isNeg()) {
      throw new SemanticException("Incompatible operand, expecting Integer, Boolean given !", node.getLine(), node.getColumn());
    }
    Object result;
    Object expValue = getValueFromExpNode(expNode);
    switch (node.getOp().getOp()) {
        case NEG: result = (int) expValue * -1;
        break;
        case NOT: result = !((boolean) expValue);
        break;
      default: throw new IllegalArgumentException("Illegal operand !");
    }
    return getExpNodeFromValue(result, node);
  }

  @Override
  public ExpNode visitLength(LengthNode node) {
    Symbols sym = scopeManager.lookup(node.getIdent());
    if (sym instanceof MethodSymbol) {
      throw new SemanticException("Incompatible parameter for Length, expecting Array, Variable or Final, Method given !", node.getLine(), node.getColumn());
    }
    if (sym instanceof TableSymbol tableSymbol) {
      return tableSymbol.getSize();
    }

    if (sym instanceof VarSymbol varSymbol) {
      TypeNode identType = varSymbol.getTypeNode();
      if (identType.isInt()) {
        throw new SemanticException("Incompatible expression for Length, expecting Array or String, Integer given !", node.getLine(), node.getColumn());
      }
      if (identType.isBoolean()) {
        throw new SemanticException("Incompatible expression for Length, expecting Array or String, Boolean given !", node.getLine(), node.getColumn());
      }
      if (identType.isString()) {
        return node.getIdent();
      }
    }
    return null;
  }

  @Override
  public ExpNode visitBoolLiteral(BoolLiteralNode node) {
    return node;
  }

  @Override
  public ExpNode visitIntLiteral(IntLiteralNode node) {
    return node;
  }

  @Override
  public ExpNode visitStringLiteral(StringLiteralNode node) {
    return node;
  }

  @Override
  public ExpNode visitArrayAccess(ArrayAccessNode node) {
    return node;
  }

  @Override
  public ExpNode visitIdent(IdentNode node) {
    return node;
  }

  /* Unused */

  @Override
  public ExpNode visitAssign(AssignNode node) {
    return null;
  }

  @Override
  public ExpNode visitAddAssign(AddAssignNode node) {
    return null;
  }

  public ExpNode visitInc(IncNode node) {
    return null;
  }

  @Override
  public ExpNode visitIf(IfNode node) {
    return null;
  }

  @Override
  public ExpNode visitWhile(WhileNode node) {
    return null;
  }

  @Override
  public ExpNode visitWrite(WriteNode node) {
    return null;
  }

  @Override
  public ExpNode visitReturn(ReturnNode node) {
    return null;
  }

  @Override
  public ExpNode visitCallExp(CallExpNode node) {
    return null;
  }

  @Override
  public ExpNode visitCallInstr(CallInstrNode node) {
    return null;
  }

  @Override
  public ExpNode visitType(fr.amvproject.group3.mjj.ast.nodes.other.TypeNode node) {
    return null;
  }

  @Override
  public ExpNode visitTypeMeth(TypeMethNode node) {
    return null;
  }

  @Override
  public ExpNode visitBinaryOp(BinaryOpNode node) {
    return null;
  }

  @Override
  public ExpNode visitUnaryOp(UnaryOpNode node) {
    return null;
  }
}
