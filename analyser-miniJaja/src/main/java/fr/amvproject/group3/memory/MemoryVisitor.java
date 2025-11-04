package fr.amvproject.group3.memory;

import fr.amvproject.group3.miniJaja.ast.AstNode;
import fr.amvproject.group3.miniJaja.ast.AstVisitor;
import fr.amvproject.group3.miniJaja.ast.nodes.abstracts.*;
import fr.amvproject.group3.miniJaja.ast.nodes.decls.*;
import fr.amvproject.group3.miniJaja.ast.nodes.exprs.BinaryExpNode;
import fr.amvproject.group3.miniJaja.ast.nodes.exprs.CallExpNode;
import fr.amvproject.group3.miniJaja.ast.nodes.exprs.LengthNode;
import fr.amvproject.group3.miniJaja.ast.nodes.exprs.UnaryExpNode;
import fr.amvproject.group3.miniJaja.ast.nodes.finals.*;
import fr.amvproject.group3.miniJaja.ast.nodes.instrs.*;
import fr.amvproject.group3.miniJaja.ast.nodes.intermediate.DeclNode;
import fr.amvproject.group3.miniJaja.ast.nodes.intermediate.ExpNode;
import fr.amvproject.group3.miniJaja.ast.nodes.intermediate.InstrNode;
import fr.amvproject.group3.miniJaja.ast.nodes.intermediate.VarDeclNode;
import fr.amvproject.group3.miniJaja.ast.nodes.other.BinaryOpNode;
import fr.amvproject.group3.miniJaja.ast.nodes.other.TypeMethNode;
import fr.amvproject.group3.miniJaja.ast.nodes.other.TypeNode;
import fr.amvproject.group3.miniJaja.ast.nodes.other.UnaryOpNode;
import fr.amvproject.group3.miniJaja.ast.nodes.program.ClassNode;
import fr.amvproject.group3.miniJaja.ast.nodes.program.ProgramNode;

public class MemoryVisitor implements AstVisitor<AstNode> {
  SymbolTable symbolTable = new SymbolTable();

  public SymbolTable getSymbolTable () {
    return symbolTable;
  }

  @Override
  public AstNode visitProgram(ProgramNode node) {
    for (ClassNode classeNode : node.getClasses()) {
      classeNode.accept(this);
    }
    return null;
  }

  @Override
  public AstNode visitClass(ClassNode node) {
    node.getDecls().accept(this);
    node.getMain().accept(this);
    return null;
  }

  @Override
  public AstNode visitDecls(DeclsNode node) {
    for (DeclNode declNode : node.getDecls()) {
      declNode.accept(this);
    }
    return null;
  }

  @Override
  public AstNode visitDecl(DeclNode node) {
    if (node instanceof VarDeclNode varDeclNode) {
      varDeclNode.accept(this);
    } else {
      ((MethodNode) node).accept(this);
    }
    return null;
  }

  @Override
  public AstNode visitVars(VarsNode node) {
    for (VarDeclNode varDeclNode : node.getList()) {
      varDeclNode.accept(this);
    }
    return null;
  }

  @Override
  public AstNode visitVarDecl(VarDeclNode node) {
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
  public AstNode visitVar(VarNode node) {
    symbolTable.addSymbol(node.getIdent(), node.getType(), null, null, node.getIdent().getLine());
    return null;
  }

  @Override
  public AstNode visitCst(CstNode node) {
    symbolTable.addSymbol(node.getIdent(), new TypeMethNode(node.getType().getName()), null, null, node.getIdent().getLine());
    return null;
  }

  @Override
  public AstNode visitTable(TableNode node) {
    symbolTable.addSymbol(node.getIdent(), node.getType(), null, node.getSizeExp().evaluate(), node.getIdent().getLine());
    return null;
  }

  @Override
  public AstNode visitMethod(MethodNode node) {
    symbolTable.addSymbol(node.getIdent(), node.getReturnType(), node.getParams(), null, node.getIdent().getLine());
    return null;
  }

  @Override
  public AstNode visitMain(MainNode node) {
    node.getVars().accept(this);
    node.getInstrs().accept(this);
    return null;
  }

  @Override
  public AstNode visitParams(ParamsNode node) {
    for (ParamNode paramNode : node.getList()) {
      paramNode.accept(this);
    }
    return null;
  }

  @Override
  public AstNode visitParam(ParamNode node) {
    node.accept(this);
    return null;
  }

  @Override
  public AstNode visitInstrs(InstrsNode node) {
    for (InstrNode instrNode : node.getList()) {
      instrNode.accept(this);
    }
    return null;
  }

  @Override
  public AstNode visitListExp(ListExpNode node) {
    for (ExpNode expNode : node.getList()) {
      expNode.accept(this);
    }
    return null;
  }

  @Override
  public AstNode visitBinaryExp(BinaryExpNode node) {
    node.getLeftExp().accept(this);
    node.getRightExp().accept(this);
    return null;
  }

  @Override
  public AstNode visitUnaryExp(UnaryExpNode node) {
    node.getExp().accept(this);
    return null;
  }

  @Override
  public AstNode visitAssign(AssignNode node) {
    node.getLeftValue().accept(this);
    node.getExp().accept(this);
    return null;
  }

  @Override
  public AstNode visitAddAssign(AddAssignNode node) {
    node.getLeftValueNode().accept(this);
    node.getExp().accept(this);
    return null;
  }

  public AstNode visitInc(IncNode node) {
    node.getIdent().accept(this);
    return null;
  }

  @Override
  public AstNode visitLength(LengthNode node) {
    node.getIdent().accept(this);
    return null;
  }

  @Override
  public AstNode visitIf(IfNode node) {
    node.getCondExp().accept(this);
    if (node.getThenInstrs() != null) node.getThenInstrs().accept(this);
    if (node.getElseInstrs() != null) node.getElseInstrs().accept(this);
    return null;
  }

  @Override
  public AstNode visitWhile(WhileNode node) {
    node.getCondExp().accept(this);
    node.getInstrs().accept(this);
    return null;
  }

  @Override
  public AstNode visitWrite(WriteNode node) {
    node.getExp().accept(this);
    return null;
  }

  @Override
  public AstNode visitReturn(ReturnNode node) {
    node.getExp().accept(this);
    return null;
  }

  @Override
  public AstNode visitBoolLiteral(BoolLiteralNode node) {
    return null;
  }

  @Override
  public AstNode visitIntLiteral(IntLiteralNode node) {
    return null;
  }

  @Override
  public AstNode visitStringLiteral(StringLiteralNode node) {
    return null;
  }

  @Override
  public AstNode visitArrayAccess(ArrayAccessNode node) {
    node.getIdent().accept(this);
    return null;
  }

  @Override
  public AstNode visitCallExp(CallExpNode node) {
    node.getIdent().accept(this);
    node.getListExp().accept(this);
    return null;
  }

  @Override
  public AstNode visitCallInstr(CallInstrNode node) {
    node.getIdent().accept(this);
    node.getListExp().accept(this);
    return null;
  }

  @Override
  public AstNode visitType(TypeNode node) {
    return null;
  }

  @Override
  public AstNode visitTypeMeth(TypeMethNode node) {
    return null;
  }

  @Override
  public AstNode visitBinaryOp(BinaryOpNode node) {
    return null;
  }

  @Override
  public AstNode visitUnaryOp(UnaryOpNode node) {
    return null;
  }

  @Override
  public AstNode visitIdent(IdentNode node) {
    symbolTable.recordUsage(node, node.getLine());
    return null;
  }
}
