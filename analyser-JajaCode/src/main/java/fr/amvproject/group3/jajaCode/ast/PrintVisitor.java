package fr.amvproject.group3.jajaCode.ast;

import java.util.List;

import fr.amvproject.group3.jajaCode.ast.nodes.finals.*;
import fr.amvproject.group3.jajaCode.ast.nodes.instrs.*;
import fr.amvproject.group3.jajaCode.ast.nodes.finals.*;
import fr.amvproject.group3.jajaCode.ast.nodes.instrs.*;
import fr.amvproject.group3.jajaCode.ast.nodes.literals.InitLiteral;
import fr.amvproject.group3.jajaCode.ast.nodes.literals.JcstopLiteral;
import fr.amvproject.group3.jajaCode.ast.nodes.literals.NopLiteral;
import fr.amvproject.group3.jajaCode.ast.nodes.literals.PopLiteral;
import fr.amvproject.group3.jajaCode.ast.nodes.literals.ReturnLiteral;
import fr.amvproject.group3.jajaCode.ast.nodes.literals.SwapLiteral;
import fr.amvproject.group3.jajaCode.ast.nodes.literals.WriteLiteral;
import fr.amvproject.group3.jajaCode.ast.nodes.other.*;
import fr.amvproject.group3.jajaCode.ast.nodes.program.*;
import fr.amvproject.group3.jajaCode.ast.nodes.other.BinaryOpNode;
import fr.amvproject.group3.jajaCode.ast.nodes.other.ObjNode;
import fr.amvproject.group3.jajaCode.ast.nodes.other.TypeNode;
import fr.amvproject.group3.jajaCode.ast.nodes.other.UnaryOpNode;
import fr.amvproject.group3.jajaCode.ast.nodes.program.ClassNode;
import fr.amvproject.group3.jajaCode.ast.nodes.program.ProgramNode;

@SuppressWarnings("java:S6539") // Visitor pattern: large by design
public class PrintVisitor implements AstVisitor<String> {

  private boolean programDebug = false;

  private String joinChildren(List<? extends AstNode> nodes) {
    StringBuilder sb = new StringBuilder();
    for (AstNode node : nodes) {
      if (node != null) {
        sb.append(node.accept(this)).append("\n");
      }
    }
    return sb.toString();
  }

  // --------- Nodes ---------
  @Override
  public String visitProgram(ProgramNode node) {
    return joinChildren(node.getClasses());
  }

  @Override
  public String visitClass(ClassNode node) {
    StringBuilder sb = new StringBuilder();
    sb.append(node.getAddress().accept(this)).append(" ");
    if (node.getInstr() != null) sb.append(node.getInstr().accept(this));
    else if (programDebug) sb.append("/* Jcnil */");
    return sb.toString();
  }

  @Override
  public String visitAddress(AddressNode node) {
    return String.valueOf(node.getValue());
  }

  @Override
  public String visitInit(InitLiteral literal) {
    return "init";
  }

  @Override
  public String visitSwap(SwapLiteral literal) {
    return "swap";
  }

  @Override
  public String visitReturn(ReturnLiteral literal) {
    return "return";
  }

  @Override
  public String visitWrite(WriteLiteral literal) {
    return literal.isLn() ? "writeln" : "write";
  }

  @Override
  public String visitPop(PopLiteral literal) {
    return "pop";
  }

  @Override
  public String visitNop(NopLiteral literal) {
    return "nop";
  }

  @Override
  public String visitJcstop(JcstopLiteral literal) {
    return "jcstop";
  }

  @Override
  public String visitNew(NewNode node) {
    StringBuilder sb = new StringBuilder();
    sb.append("new(");
    sb.append(node.getIdent().accept(this)).append(", ");
    sb.append(node.getType().accept(this)).append(", ");
    sb.append(node.getObj().accept(this)).append(", ");
    sb.append(node.getValue().accept(this)).append(")");
    return sb.toString();
  }

  @Override
  public String visitNewArray(NewArrayNode node) {
    StringBuilder sb = new StringBuilder();
    sb.append("newarray(");
    sb.append(node.getIdent().accept(this)).append(", ");
    sb.append(node.getType().accept(this)).append(")");
    return sb.toString();
  }

  @Override
  public String visitInvoke(InvokeNode node) {
    return "invoke(" + node.getIdent().accept(this) + ")";
  }

  @Override
  public String visitLength(LengthNode node) {
    return "length(" + node.getIdent().accept(this) + ")";
  }

  @Override
  public String visitPush(PushNode node) {
    return "push(" + node.getValue().accept(this) + ")";
  }

  @Override
  public String visitLoad(LoadNode node) {
    return (node.isA() ? "aload(" : "load(") + node.getIdent().accept(this) + ")";
  }

  @Override
  public String visitStore(StoreNode node) {
    return (node.isA() ? "astore(" : "store(") + node.getIdent().accept(this) + ")";
  }

  @Override
  public String visitIf(IfNode node) {
    return "if(" + node.getAddress().accept(this) + ")";
  }

  @Override
  public String visitGoto(GotoNode node) {
    return "goto(" + node.getAddress().accept(this) + ")";
  }

  @Override
  public String visitInc(IncNode node) {
    return (node.isA() ? "ainc(" : "inc(") + node.getIdent().accept(this) + ")";
  }

  @Override
  public String visitIntLiteral(IntLiteralNode node) {
    return String.valueOf(node.getValue());
  }

  @Override
  public String visitBoolLiteral(BoolLiteralNode node) {
    return node.getValue() ? "true" : "false";
  }

  @Override
  public String visitStringLiteral(StringLiteralNode node) {
    return "\"" + node.getValue() + "\"";
  }

  @Override
  public String visitType(TypeNode node) {
    return node.getName();
  }

  @Override
  public String visitObj(ObjNode node) {
    return node.getName();
  }

  @Override
  public String visitBinaryOp(BinaryOpNode node) {
    return node.getOp().getName();
  }

  @Override
  public String visitUnaryOp(UnaryOpNode node) {
    return node.getOp().getName();
  }

  @Override
  public String visitIdent(IdentNode node) {
    return node.getName();
  }
}
