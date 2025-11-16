package fr.amvproject.group3.mjj.analyser;

import fr.amvproject.group3.mjj.analyser.nodes.TypeNode;
import fr.amvproject.group3.mjj.analyser.symbols.MethodSymbol;
import fr.amvproject.group3.mjj.analyser.symbols.Symbols;
import fr.amvproject.group3.mjj.analyser.symbols.TabSymbol;
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
import fr.amvproject.group3.mjj.ast.nodes.intermediate.InstrNode;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.VarDeclNode;
import fr.amvproject.group3.mjj.ast.nodes.other.BinaryOpNode;
import fr.amvproject.group3.mjj.ast.nodes.other.TypeMethNode;
import fr.amvproject.group3.mjj.ast.nodes.other.UnaryOpNode;
import fr.amvproject.group3.mjj.ast.nodes.program.ClassNode;
import fr.amvproject.group3.mjj.ast.nodes.program.ProgramNode;
import fr.amvproject.group3.mjj.exceptions.SemanticException;

public class CompileTimeVisitor implements AstVisitor<Object> {
  private SymbolTable symbolTable;

  public CompileTimeVisitor(SymbolTable symbolTable) {
    this.symbolTable = symbolTable;
  }

  @Override
  public Object visitProgram(ProgramNode node) {
    for (ClassNode classeNode : node.getClasses()) {
      classeNode.accept(this);
    }
    return null;
  }

  @Override
  public Object visitClass(ClassNode node) {
    node.getDecls().accept(this);
    node.getMain().accept(this);
    return null;
  }

  @Override
  public Object visitDecls(DeclsNode node) {
    for (DeclNode declNode : node.getList()) {
      declNode.accept(this);
    }
    return null;
  }

  @Override
  public Object visitDecl(DeclNode node) {
    if (node instanceof VarDeclNode varDeclNode) {
      varDeclNode.accept(this);
    } else {
      ((MethodNode) node).accept(this);
    }
    return null;
  }

  @Override
  public Object visitVars(VarsNode node) {
    for (VarDeclNode varDeclNode : node.getList()) {
      varDeclNode.accept(this);
    }
    return null;
  }

  @Override
  public Object visitVarDecl(VarDeclNode node) {
    if (node instanceof CstNode cstNode) {
      cstNode.accept(this);
    } else if (node instanceof VarNode varNode) {
      varNode.accept(this);
    } else {
      ((TableNode) node).accept(this);
    }
    return null;
  }

  @Override
  public Object visitVar(VarNode node) {
    return null;
  }

  @Override
  public Object visitCst(CstNode node) {
    return null;
  }

  @Override
  public Object visitTable(TableNode node) {
    return null;
  }

  @Override
  public Object visitMethod(MethodNode node) {
    return null;
  }

  @Override
  public Object visitMain(MainNode node) {
    node.getVars().accept(this);
    node.getInstrs().accept(this);
    return null;
  }

  @Override
  public Object visitParams(ParamsNode node) {
    return null;
  }

  @Override
  public Object visitParam(ParamNode node) {
    return null;
  }

  @Override
  public Object visitInstrs(InstrsNode node) {
    for (InstrNode instrNode : node.getList()) {
      instrNode.accept(this);
    }
    return null;
  }

  @Override
  public Object visitListExp(ListExpNode node) {
    return null;
  }

  @Override
  public Object visitBinaryExp(BinaryExpNode node) {
    Object leftExpValue = node.getLeftExp().accept(this);
    Object rightExpValue = node.getRightExp().accept(this);

    if (leftExpValue instanceof Boolean != rightExpValue instanceof Boolean) {
      throw new SemanticException("Type mismatch !");
    }
    if (leftExpValue instanceof Integer && !node.getOp().takesInteger()) {
      throw new SemanticException("Incompatible operand, expecting Boolean, Integer given !");
    }
    if (leftExpValue instanceof Boolean && !node.getOp().takesBoolean()) {
      throw new SemanticException("Incompatible operand, expecting Integer, Boolean given !");
    }
    switch (node.getOp().getOp()) {
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
  }

  @Override
  public Object visitUnaryExp(UnaryExpNode node) {
    Object expValue = node.getExp().accept(this);

    if (expValue instanceof Integer && node.getOp().isNot()) {
      throw new SemanticException("Incompatible operand, expecting Boolean, Integer given !");
    }
    if (expValue instanceof Boolean && node.getOp().isNeg()) {
      throw new SemanticException("Incompatible operand, expecting Integer, Boolean given !");
    }
    switch (node.getOp().getOp()) {
      case NOT: return (int) expValue * -1;
      case NEG: return !((boolean) expValue);
      default: throw new SemanticException("Unknown semantic exception !");
    }
  }

  @Override
  public Object visitAssign(AssignNode node) {
    return null;
  }

  @Override
  public Object visitAddAssign(AddAssignNode node) {
    return null;
  }

  public Object visitInc(IncNode node) {
    return null;
  }

  @Override
  public Object visitLength(LengthNode node) {
    Symbols sym = symbolTable.lookup(node.getIdent());
    if (sym instanceof MethodSymbol) {
      throw new SemanticException("Incompatible parameter for Length, expecting Array, Variable or Final, Method given !");
    }
    if (sym instanceof TabSymbol tabSymbol) {
      return tabSymbol.getSize();
    }

    if (sym instanceof VarSymbol varSymbol) {
      TypeNode identType = varSymbol.getTypeNode();
      if (identType.isInt()) {
        throw new SemanticException("Incompatible expression for Length, expecting Array or String, Integer given !");
      }
      if (identType.isBoolean()) {
        throw new SemanticException("Incompatible expression for Length, expecting Array or String, Boolean given !");
      }
      if (identType.isString()) {
        return node.getIdent();
      }
    }
    return 0;
  }

  @Override
  public Object visitIf(IfNode node) {
    return null;
  }

  @Override
  public Object visitWhile(WhileNode node) {
    return null;
  }

  @Override
  public Object visitWrite(WriteNode node) {
    return null;
  }

  @Override
  public Object visitReturn(ReturnNode node) {
    return null;
  }

  @Override
  public Object visitBoolLiteral(BoolLiteralNode node) {
    return node.getValue();
  }

  @Override
  public Object visitIntLiteral(IntLiteralNode node) {
    return node.getValue();
  }

  @Override
  public Object visitStringLiteral(StringLiteralNode node) {
    return node.getValue();
  }

  @Override
  public Object visitArrayAccess(ArrayAccessNode node) {
    return node.getIdent().accept(this);
  }

  @Override
  public Object visitCallExp(CallExpNode node) {
    return null;
  }

  @Override
  public Object visitCallInstr(CallInstrNode node) {
    return null;
  }

  @Override
  public Object visitType(fr.amvproject.group3.mjj.ast.nodes.other.TypeNode node) {
    return null;
  }

  @Override
  public Object visitTypeMeth(TypeMethNode node) {
    return null;
  }

  @Override
  public Object visitBinaryOp(BinaryOpNode node) {
    return null;
  }

  @Override
  public Object visitUnaryOp(UnaryOpNode node) {
    return null;
  }

  @Override
  public Object visitIdent(IdentNode node) {
    return node;
  }
}
