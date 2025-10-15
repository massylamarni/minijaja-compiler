package fr.amvproject.group3.memory;

import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.abstracts.*;
import fr.amvproject.group3.ast.nodes.decls.*;
import fr.amvproject.group3.ast.nodes.exprs.*;
import fr.amvproject.group3.ast.nodes.instrs.*;
import fr.amvproject.group3.ast.nodes.other.*;
import fr.amvproject.group3.ast.nodes.params.*;
import fr.amvproject.group3.ast.nodes.program.*;

public class MemoryVisitor implements AstVisitor<AstNode> {
  SymbolTable symbolTable = new SymbolTable();

  public SymbolTable getSymbolTable () {
    return symbolTable;
  }

  @Override
  public AstNode visitProgram(ProgramNode node) {
    for (ClasseNode classeNode : node.getClasses()) {
      classeNode.accept(this);
    }
    return null;
  }

  @Override
  public AstNode visitClasse(ClasseNode node) {
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
    if (node instanceof VarNode) {
      ((VarNode) node).accept(this);
    } else {
      ((MethodeNode) node).accept(this);
    }
    return null;
  }

  @Override
  public AstNode visitVars(VarsNode node) {
    for (VarNode varNode : node.getList()) {
      varNode.accept(this);
    }
    return null;
  }

  @Override
  public AstNode visitVar(VarNode node) {
    if (node.getVarObj() == "tab") {
      symbolTable.addSymbol(node.getIdent(), node.getType(), null, node.getSizeExp().evaluate(), node.getIdent().getLine());
    } else {
      symbolTable.addSymbol(node.getIdent(), node.getType(), null, null, node.getIdent().getLine());
    }
    return null;
  }

  @Override
  public AstNode visitMethode(MethodeNode node) {
    symbolTable.addSymbol(node.getIdent(), node.getReturnType(), node.getEntetes(), null, node.getIdent().getLine());
    return null;
  }
  
  @Override
  public AstNode visitMethMain(MethMainNode node) {
    node.getVars().accept(this);
    node.getInstrs().accept(this);
    return null;
  }

  @Override
  public AstNode visitEntetes(EntetesNode node) {
    for (EnteteNode enteteNode : node.getList()) {
      enteteNode.accept(this);
    }
    return null;
  }
  
  @Override
  public AstNode visitEntete(EnteteNode node) {
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
  public AstNode visitInstr(InstrNode node) {
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
  public AstNode visitRetour(RetourNode node) {
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
  public AstNode visitVarRef(VarRefNode node) {
    node.getIdent().accept(this);
    return null;
  }

  @Override
  public AstNode visitArrayAccess(ArrayAccessNode node) {
    node.getIdent().accept(this);
    return null;
  }

  @Override
  public AstNode visitMethodCallExp(MethodCallExpNode node) {
    node.getIdent().accept(this);
    node.getListExp().accept(this);
    return null;
  }

  @Override
  public AstNode visitMethodCallInstr(MethodCallInstrNode node) {
    node.getIdent().accept(this);
    node.getListExp().accept(this);
    return null;
  };

  @Override
  public AstNode visitType(TypeNode node) {
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
  public AstNode visitIdent(Ident node) {
    symbolTable.recordUsage(node, node.getLine());
    return null;
  }
}
