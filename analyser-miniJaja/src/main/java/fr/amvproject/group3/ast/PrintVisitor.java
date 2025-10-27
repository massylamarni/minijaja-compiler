package fr.amvproject.group3.ast;

import java.util.List;

import fr.amvproject.group3.ast.nodes.abstracts.*;
import fr.amvproject.group3.ast.nodes.decls.*;
import fr.amvproject.group3.ast.nodes.exprs.*;
import fr.amvproject.group3.ast.nodes.finals.*;
import fr.amvproject.group3.ast.nodes.instrs.*;
import fr.amvproject.group3.ast.nodes.intermediate.*;
import fr.amvproject.group3.ast.nodes.other.*;
import fr.amvproject.group3.ast.nodes.program.*;
import fr.amvproject.group3.utils.IndentManager;

public class PrintVisitor implements AstVisitor<String> {

  private boolean programDebug = false;

  IndentManager indentManager = new IndentManager();

  private String joinChildren(List<? extends AstNode> nodes) {
    StringBuilder sb = new StringBuilder();
    indentManager.increaseIndent();
    for (AstNode node : nodes) {
      if (node != null) {
        sb.append(node.accept(this)).append(indentManager.getIndentLevel() != 0 ? ";\n" : "");
      }
    }
    indentManager.decreaseIndent();
    return sb.toString();
  }

  private String joinChildrenLinear(List<? extends AstNode> nodes) {
    StringBuilder sb = new StringBuilder();
    indentManager.increaseIndent();
    for (int i = 0; i < nodes.size(); i++) {
      if (nodes.get(i) != null) {
        sb.append(nodes.get(i).accept(this));
        if (i + 1 != nodes.size())
          sb.append(", ");
      }
    }
    indentManager.decreaseIndent();
    return sb.toString();
  }

  private static final String BLOCK_START = ") {\n";

  // --------- Nodes ---------
  @Override
  public String visitProgram(ProgramNode node) {
    return joinChildren(node.getClasses());
  }

  @Override
  public String visitClass(ClassNode node) {
    StringBuilder sb = new StringBuilder(indentManager.indent());
    sb.append("class ").append(node.getIdent().accept(this)).append(" {\n");
    if (node.getDecls() != null)
      sb.append(indentManager.indent()).append(node.getDecls().accept(this));
    if (node.getMain() != null)
      sb.append(indentManager.indent()).append(node.getMain().accept(this));
    sb.append(indentManager.indent()).append("}\n");
    return sb.toString();
  }

  @Override
  public String visitDecls(DeclsNode node) {
    if (node.getDecls().isEmpty() && programDebug)
      return indentManager.indent() + "/* DECLS empty */\n";
    return indentManager.indent() + joinChildren(node.getDecls());
  }

  @Override
  public String visitDecl(DeclNode node) {
    return indentManager.indent() + node.getIdent().accept(this);
  }

  @Override
  public String visitVars(VarsNode node) {
    return joinChildren(node.getList());
  }

  @Override
  public String visitVarDecl(VarDeclNode node) {
    return indentManager.indent() + node.getIdent().accept(this);
  }

  @Override
  public String visitVar(VarNode node) {
    StringBuilder sb = new StringBuilder(indentManager.indent());
    sb.append(node.getType().accept(this)).append(" ").append(node.getIdent().accept(this));
    if (node.getInitExp() != null)
      sb.append(" = ").append(node.getInitExp().accept(this));
    return sb.toString();
  }

  @Override
  public String visitCst(CstNode node) {
    StringBuilder sb = new StringBuilder(indentManager.indent());
    sb.append("final ").append(node.getType().accept(this)).append(" ").append(node.getIdent().accept(this));
    if (node.getInitExp() != null)
      sb.append(" = ").append(node.getInitExp().accept(this));
    return sb.toString();
  }

  @Override
  public String visitTable(TableNode node) {
    StringBuilder sb = new StringBuilder(indentManager.indent());
    sb.append(node.getType().accept(this)).append(" ").append(node.getIdent().accept(this));
    if (node.getSizeExp() != null)
      sb.append("[").append(node.getSizeExp().accept(this)).append("]");
    return sb.toString();
  }

  @Override
  public String visitMethod(MethodNode node) {
    StringBuilder sb = new StringBuilder(indentManager.indent());
    sb.append(node.getReturnType().accept(this)).append(" ").append(node.getIdent().accept(this)).append("(");
    sb.append(joinChildrenLinear(node.getParams().getList())).append(BLOCK_START);
    if (node.getVars() != null)
      sb.append(node.getVars().accept(this));
    if (node.getInstrs() != null)
      sb.append(node.getInstrs().accept(this));
    sb.append(indentManager.indent()).append("}");
    return sb.toString();
  }

  @Override
  public String visitMain(MainNode node) {
    indentManager.increaseIndent();
    StringBuilder sb = new StringBuilder(indentManager.indent()).append("main {\n");
    if (node.getVars() != null)
      sb.append(node.getVars().accept(this));
    if (node.getInstrs() != null)
      sb.append(node.getInstrs().accept(this));
    sb.append(indentManager.indent()).append("}\n");
    indentManager.decreaseIndent();
    return sb.toString();
  }

  @Override
  public String visitParams(ParamsNode node) {
    return "(" + joinChildren(node.getList()) + ")";
  }

  @Override
  public String visitParam(ParamNode node) {
    return node.getType().accept(this) + " " + node.getIdent().accept(this);
  }

  @Override
  public String visitInstrs(InstrsNode node) {
    if (node.getList().isEmpty() && programDebug)
      return indentManager.indent() + "/* INSTRS empty */\n";
    return joinChildren(node.getList());
  }

  @Override
  public String visitListExp(ListExpNode node) {
    if (node.getList() == null || node.getList().isEmpty())
      return indentManager.indent() + "/* LISTEXP empty */";
    return joinChildren(node.getList());
  }

  @Override
  public String visitBinaryExp(BinaryExpNode node) {
    StringBuilder sb = new StringBuilder();
    sb.append("(").append(node.getLeftExp().accept(this));
    sb.append(" ").append(node.getOp().accept(this)).append(" ");
    sb.append(node.getRightExp().accept(this)).append(")");
    return sb.toString();
  }

  @Override
  public String visitUnaryExp(UnaryExpNode node) {
    StringBuilder sb = new StringBuilder();
    sb.append("(").append(node.getOp().accept(this)).append(" ");
    sb.append(node.getExp().accept(this)).append(")");
    return sb.toString();
  }

  @Override
  public String visitAssign(AssignNode node) {
    StringBuilder sb = new StringBuilder(indentManager.indent());
    sb.append(node.getLeftValue().accept(this));
    sb.append(" = ");
    sb.append(node.getExp().accept(this));
    return sb.toString();
  }

  @Override
  public String visitAddAssign(AddAssignNode node) {
    StringBuilder sb = new StringBuilder(indentManager.indent());
    sb.append(node.getLeftValueNode().accept(this));
    sb.append(" += ");
    sb.append(node.getExp().accept(this));
    return sb.toString();
  }

  public String visitInc(IncNode node) {
    return indentManager.indent() + node.getIdent().accept(this) + "++";
  }

  @Override
  public String visitLength(LengthNode node) {
    return indentManager.indent() + "Longueur(" + node.getIdent().accept(this) + ")";
  }

  @Override
  public String visitIf(IfNode node) {
    StringBuilder sb = new StringBuilder(indentManager.indent());
    sb.append("if (").append(node.getCondExp().accept(this)).append(BLOCK_START);
    sb.append(node.getThenInstrs().accept(this));
    if (node.getElseInstrs() != null)
      sb.append(indentManager.indent()).append("} else {\n").append(node.getElseInstrs().accept(this));
    sb.append(indentManager.indent()).append("}");
    return sb.toString();
  }

  @Override
  public String visitWhile(WhileNode node) {
    StringBuilder sb = new StringBuilder(indentManager.indent());
    sb.append("while (").append(node.getCondExp().accept(this)).append(BLOCK_START);
    sb.append(node.getInstrs().accept(this)).append(indentManager.indent()).append("}");
    return sb.toString();
  }

  @Override
  public String visitWrite(WriteNode node) {
    return indentManager.indent() + (node.isLn() ? "writeLn" : "write") + "(" + node.getExp().accept(this) + ")";
  }

  @Override
  public String visitReturn(ReturnNode node) {
    return indentManager.indent() + "retour " + node.getExp().accept(this);
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
  public String visitArrayAccess(ArrayAccessNode node) {
    return node.getIdent().accept(this) + "[" + node.getIndexExp().accept(this) + "]";
  }

  @Override
  public String visitCallExp(CallExpNode node) {
    StringBuilder sb = new StringBuilder();
    sb.append(node.getIdent().accept(this)).append("(");
    sb.append(joinChildrenLinear(node.getListExp().getList()));
    sb.append(")");
    return sb.toString();
  }

  @Override
  public String visitCallInstr(CallInstrNode node) {
    StringBuilder sb = new StringBuilder(indentManager.indent());
    sb.append(node.getIdent().accept(this)).append("(");
    sb.append(joinChildrenLinear(node.getListExp().getList()));
    sb.append(")");
    return sb.toString();
  }

  @Override
  public String visitType(TypeNode node) {
    return node.getType().getName();
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
