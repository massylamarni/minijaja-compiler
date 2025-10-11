package fr.amvproject.group3.memory;

import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.abstracts.*;
import fr.amvproject.group3.ast.nodes.decls.*;
import fr.amvproject.group3.ast.nodes.exprs.*;
import fr.amvproject.group3.ast.nodes.instrs.*;
import fr.amvproject.group3.ast.nodes.other.*;
import fr.amvproject.group3.ast.nodes.params.*;
import fr.amvproject.group3.ast.nodes.program.*;

public class MiniJajaInterpreter implements AstVisitor<Void> {
  Stack stack = new Stack();
  Heap heap = new Heap();

  // --------- Abstracts ---------
  @Override
  public Void visitIdent(Ident node) {
    return null;
  }

  @Override
  public Void visitIdentExp(IdentExp node) {
    return null;
  }

  // --------- Decls ---------
  @Override
  public Void visitVar(VarNode node) {
    return null;
  }

  @Override
  public Void visitVars(VarsNode node) {
    return null;
  }

  @Override
  public Void visitMethMain(MethMainNode node) {
    return null;
  }

  @Override
  public Void visitMethode(MethodeNode node) {
    return null;
  }

  @Override
  public Void visitDecl(DeclNode node) {
    return null;
  }

  @Override
  public Void visitDecls(DeclsNode node) {
    return null;
  }

  @Override
  public Void visitClasse(ClasseNode node) {
    return null;
  }

  @Override
  public Void visitProgram(ProgramNode node) {
    return null;
  }

  // --------- Exprs ---------
  @Override
  public Void visitBinaryExp(BinaryExpNode node) {
    return null;
  }

  @Override
  public Void visitUnaryExp(UnaryExpNode node) {
    return null;
  }

  @Override
  public Void visitBoolLiteral(BoolLiteralNode node) {
    return null;
  }

  @Override
  public Void visitIntLiteral(IntLiteralNode node) {
    return null;
  }

  @Override
  public Void visitLength(LengthNode node) {
    return null;
  }

  @Override
  public Void visitListExp(ListExpNode node) {
    return null;
  }

  @Override
  public Void visitStringLiteral(StringLiteralNode node) {
    return null;
  }

  @Override
  public Void visitVarRef(VarRefNode node) {
    return null;
  }

  @Override
  public Void visitArrayAccess(ArrayAccessNode node) {
    return null;
  }

  @Override
  public Void visitMethodCallExp(MethodCallExpNode node) {
    return null;
  }

  // --------- Instrs ---------
  @Override
  public Void visitInstrs(InstrsNode node) {
    return null;
  }

  @Override
  public Void visitAssign(AssignNode node) {
    return null;
  }

  @Override
  public Void visitAddAssign(AddAssignNode node) {
    return null;
  }

  @Override
  public Void visitIf(IfNode node) {
    return null;
  }

  public Void visitInc(IncNode node) {
    return null;
  }

  @Override
  public Void visitMethodCallInstr(MethodCallInstrNode node) {
    return null;
  };

  @Override
  public Void visitWhile(WhileNode node) {
    return null;
  }

  @Override
  public Void visitWrite(WriteNode node) {
    return null;
  }

  @Override
  public Void visitRetour(RetourNode node) {
    return null;
  }

  // --------- Other ---------
  @Override
  public Void visitType(TypeNode node) {
    return null;
  }

  @Override
  public Void visitBinaryOp(BinaryOpNode node) {
    return null;
  }

  @Override
  public Void visitUnaryOp(UnaryOpNode node) {
    return null;
  }

  // --------- Params ---------
  @Override
  public Void visitEntete(EnteteNode node) {
    return null;
  }

  @Override
  public Void visitEntetes(EntetesNode node) {
    return null;
  }

}
