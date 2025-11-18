package fr.amvproject.group3.mjj.analyser;

import java.util.ArrayList;
import java.util.List;

import fr.amvproject.group3.mjj.analyser.nodes.TypeNode;
import fr.amvproject.group3.mjj.analyser.symbols.MethodSymbol;
import fr.amvproject.group3.mjj.analyser.symbols.Symbols;
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
import fr.amvproject.group3.mjj.exceptions.SemanticException;
import fr.amvproject.group3.mjj.utils.NodeOperations;

public class SemanticVisitor implements AstVisitor<TypeNode> {
  SymbolTable symbolTable = new SymbolTable();
  CompileTimeVisitor compileTimeVisitor = new CompileTimeVisitor(symbolTable);

  public SymbolTable getSymbolTable() {
    return symbolTable;
  }

  /* Checks */

  // Class | Var | Cst | Tab
  private void checkIfExistsToDeclare(IdentNode identNode) {
    Symbols sym = symbolTable.lookup(identNode);

    if (sym != null) {
      throw new SemanticException("Symbol " + identNode.getName() + " already declared at line " + sym.getIdentNode().getLine());
    }
  }

  // Class | Var | Cst | Tab
  private void checkIfExistsToUse(IdentNode identNode) {
    Symbols sym = symbolTable.lookup(identNode);

    if (sym == null) {
      throw new SemanticException("Symbol " + identNode.getName() + " is referenced, but never declared, at line " + identNode.getLine());
    }
  }  

  // Var
  private void checkIfTypeCompatible(TypeNode identType, ExpNode identInitExp, int line) {
    TypeNode typeExpValue = identInitExp.accept(this);
    if (!identType.equals(typeExpValue)) {
      throw new SemanticException("Value of Type " + typeExpValue + " not compatible with declaration of type " + identType.getName() + " at line " + line);
    }
  }

  // Tab
  private Integer checkIfTabIndexIsInteger(ExpNode sizeExpNode, int line) {
    TypeNode sizeExpType = sizeExpNode.accept(this);
    if (!sizeExpType.isInt()) throw new SemanticException("Value of type " + sizeExpType.getName() + " can't be used as array index,"  + " at line " + line);
    return (Integer) sizeExpNode.accept(compileTimeVisitor);
  }

  /* Visitors */

  @Override
  public TypeNode visitProgram(ProgramNode node) {
    for (ClassNode classeNode : node.getClasses()) {
      classeNode.accept(this);
    }
    return new TypeNode("void");
  }

  @Override
  public TypeNode visitClass(ClassNode node) {
    checkIfExistsToDeclare(node.getIdent());
    symbolTable.addSymbol(node.getIdent());

    node.getDecls().accept(this);
    node.getMain().accept(this);
    return new TypeNode("void");
  }

  @Override
  public TypeNode visitDecls(DeclsNode node) {
    for (DeclNode declNode : node.getList()) {
      declNode.accept(this);
    }
    return null;
  }

  @Override
  public TypeNode visitDecl(DeclNode node) {
    if (node instanceof VarDeclNode varDeclNode) {
      varDeclNode.accept(this);
    } else {
      ((MethodNode) node).accept(this);
    }
    return null;
  }

  @Override
  public TypeNode visitVars(VarsNode node) {
    for (VarDeclNode varDeclNode : node.getList()) {
      varDeclNode.accept(this);
    }
    return null;
  }

  @Override
  public TypeNode visitVarDecl(VarDeclNode node) {
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
  public TypeNode visitVar(VarNode node) {
    checkIfExistsToDeclare(node.getIdent());
    checkIfTypeCompatible(new TypeNode(node.getType().getName()), node.getInitExp(), node.getIdent().getLine());

    symbolTable.addSymbol(node.getIdent(), new TypeNode(node.getType().getName()));
    return new TypeNode("void");
  }

  @Override
  public TypeNode visitCst(CstNode node) {
    checkIfExistsToDeclare(node.getIdent());
    ExpNode initExp = node.getInitExp();
    if (initExp != null) checkIfTypeCompatible(new TypeNode(node.getType().getName()), node.getInitExp(), node.getIdent().getLine());

    symbolTable.addSymbol(node.getIdent(), new TypeNode(node.getType().getName()), initExp == null);
    return new TypeNode("void");
  }

  @Override
  public TypeNode visitTable(TableNode node) {
    checkIfExistsToDeclare(node.getIdent());
    Integer sizeExpValue = checkIfTabIndexIsInteger(node.getSizeExp(), node.getIdent().getLine());
    
    symbolTable.addSymbol(node.getIdent(), new TypeNode(node.getType().getName()), sizeExpValue);
    return new TypeNode("void");
  }

  @Override
  public TypeNode visitMethod(MethodNode node) {
    MethodSymbol sym = (MethodSymbol) symbolTable.lookup(node.getIdent());
    List<TypeNode> paramsTypeValue = new ArrayList<>();
    for (ParamNode paramNode : node.getParams().getList()) {
      paramsTypeValue.add(paramNode.accept(this));
    }

    // Check if method with same parameter type exists
    if (sym != null) {
      boolean hasSameParamsType = true;
      List<TypeNode> symParamsTypeValue = sym.getParamsType();

      for (int i = 0; i < paramsTypeValue.size(); i++) {
        if (!paramsTypeValue.get(i).equals(symParamsTypeValue.get(i))) {
          hasSameParamsType = false;
          break;
        }
      }

      if (paramsTypeValue.size() == symParamsTypeValue.size() && hasSameParamsType) {
        throw new SemanticException("Symbol " + node.getIdent().getName() + " already declared at line " + sym.getIdentNode().getLine());
      }
    }

    node.getVars().accept(this);

    // Check if return value types match declared return type
    for (InstrNode instrNode : node.getInstrs().getList()) {
      if (!instrNode.accept(this).getName().equals(node.getReturnType().getName())) {
        throw new SemanticException("Return expression does not match the declared method type " + node.getReturnType().getName() + " !");
      }
    }

    symbolTable.addSymbol(node.getIdent(), new TypeNode(node.getReturnType().getName()), paramsTypeValue);
    return new TypeNode("void");
  }

  @Override
  public TypeNode visitMain(MainNode node) {
    node.getVars().accept(this);
    node.getInstrs().accept(this);
    return new TypeNode("void");
  }

  @Override
  public TypeNode visitParams(ParamsNode node) {
    return null;
  }

  @Override
  public TypeNode visitParam(ParamNode node) {
    return new TypeNode(node.getType().getName());
  }

  @Override
  public TypeNode visitInstrs(InstrsNode node) {
    for (InstrNode instrNode : node.getList()) {
      instrNode.accept(this);
    }
    return null;
  }

  @Override
  public TypeNode visitListExp(ListExpNode node) {
    return null;
  }

  @Override
  public TypeNode visitBinaryExp(BinaryExpNode node) {
    TypeNode leftExpTypeValue = node.getLeftExp().accept(this);
    TypeNode rightExpTypeValue = node.getRightExp().accept(this);

    if (!leftExpTypeValue.equals(rightExpTypeValue)) {
      throw new SemanticException("Type mismatch !");
    }
    if (leftExpTypeValue.isInt() && !node.getOp().takesInteger()) {
      throw new SemanticException("Incompatible operand, expecting Boolean, Integer given !");
    }
    if (leftExpTypeValue.isBoolean() && !node.getOp().takesBoolean()) {
      throw new SemanticException("Incompatible operand, expecting Integer, Boolean given !");
    }

    if (node.getOp().givesInteger()) return new TypeNode("int");
    return new TypeNode("boolean");
  }

  @Override
  public TypeNode visitUnaryExp(UnaryExpNode node) {
    TypeNode expTypeValue = node.getExp().accept(this);

    if (expTypeValue.isInt()&& node.getOp().isNot()) {
      throw new SemanticException("Incompatible operand, expecting Boolean, Integer given !");
    }
    if (expTypeValue.isBoolean() && node.getOp().isNeg()) {
      throw new SemanticException("Incompatible operand, expecting Integer, Boolean given !");
    }

    if (node.getOp().isNeg()) return new TypeNode("int");
    return new TypeNode("boolean");
  }

  @Override
  public TypeNode visitAssign(AssignNode node) {
    TypeNode leftValueNodeType = node.getLeftValueNode().accept(this);
    if (node.getLeftValueNode() instanceof IdentNode identNode) {
      checkIfTypeCompatible(leftValueNodeType, node.getExp(), identNode.getLine());
    } else if (node.getLeftValueNode() instanceof ArrayAccessNode arrayAccessNode) {
      checkIfTypeCompatible(leftValueNodeType, node.getExp(), arrayAccessNode.getIdent().getLine());
    }
    return new TypeNode("void");
  }

  @Override
  public TypeNode visitAddAssign(AddAssignNode node) {
    AssignNode assignNode = NodeOperations.addAssignToAssign(node);
    visitAssign(assignNode);
    return new TypeNode("void");
  }

  public TypeNode visitInc(IncNode node) {
    TypeNode leftValueNodeType = node.getLeftValueNode().accept(this);
    if (!leftValueNodeType.isInt()) {
      if (node.getLeftValueNode() instanceof IdentNode identNode) {
        throw new SemanticException("Value of Type " + leftValueNodeType + " can't be incremented," + " at line " + identNode.getLine());
      } else if (node.getLeftValueNode() instanceof ArrayAccessNode arrayAccessNode) {
        throw new SemanticException("Value of Type " + leftValueNodeType + " can't be incremented," + " at line " + arrayAccessNode.getIdent().getLine());
      }
    }

    return new TypeNode("void");
  }

  @Override
  public TypeNode visitLength(LengthNode node) {
    TypeNode leftValueNodeType = node.getIdent().accept(this);
    Symbols sym = symbolTable.lookup(node.getIdent());
    if (sym instanceof MethodSymbol) {
      throw new SemanticException("Incompatible parameter for Length, expecting Array, Variable or Final, Method given, at line " + sym.getIdentNode().getLine() + " !");
    }

    if (leftValueNodeType.isInt()) {
      throw new SemanticException("Incompatible expression for Length, expecting Array or String, Integer given !");
    } else if (leftValueNodeType.isBoolean()) {
      throw new SemanticException("Incompatible expression for Length, expecting Array or String, Boolean given !");
    }
    return new TypeNode("int");
  }

  @Override
  public TypeNode visitIf(IfNode node) {
    TypeNode expTypeValue = node.getCondExp().accept(this);
    if (!expTypeValue.isBoolean()) {
      throw new SemanticException("Incompatible expression for If, expecting Boolean, " + expTypeValue + " given !");
    }
    if (node.getThenInstrs() != null) node.getThenInstrs().accept(this);
    if (node.getElseInstrs() != null) node.getElseInstrs().accept(this);
    return new TypeNode("void");
  }

  @Override
  public TypeNode visitWhile(WhileNode node) {
    TypeNode expTypeValue = node.getCondExp().accept(this);
    if (!expTypeValue.isBoolean()) {
      throw new SemanticException("Incompatible expression for While, expecting Boolean, " + expTypeValue + " given !");
    }
    node.getInstrs().accept(this);
    return new TypeNode("void");
  }

  @Override
  public TypeNode visitWrite(WriteNode node) {
    TypeNode expTypeValue = node.getExp().accept(this);
    if (node.getExp() instanceof IdentNode identNode) {
      Symbols sym = symbolTable.lookup(identNode);
      if (sym instanceof MethodSymbol) throw new SemanticException("Incompatible parameter for Length, expecting Array, Variable or Final, Method given, at line " + sym.getIdentNode().getLine() + " !");
    }

    if (expTypeValue.isInt()) {
      throw new SemanticException("Incompatible expression for Length, expecting Array or String, Integer given !");
    } else if (expTypeValue.isBoolean()) {
      throw new SemanticException("Incompatible expression for Length, expecting Array or String, Boolean given !");
    }
    return new TypeNode("void");
  }

  @Override
  public TypeNode visitReturn(ReturnNode node) {
    return node.getExp().accept(this);
  }

  @Override
  public TypeNode visitBoolLiteral(BoolLiteralNode node) {
    return new TypeNode("bool");
  }

  @Override
  public TypeNode visitIntLiteral(IntLiteralNode node) {
    return new TypeNode("int");
  }

  @Override
  public TypeNode visitStringLiteral(StringLiteralNode node) {
    return new TypeNode("string");
  }

  @Override
  public TypeNode visitArrayAccess(ArrayAccessNode node) {
    checkIfTabIndexIsInteger(node.getIndexExp(), node.getIdent().getLine());
    
    return node.getIdent().accept(this);
  }

  @Override
  public TypeNode visitCallExp(CallExpNode node) {
    MethodSymbol sym = (MethodSymbol) symbolTable.lookup(node.getIdent());

    node.getIdent().accept(this);

    List<TypeNode> listExpTypeValue = new ArrayList<>();
    for (ExpNode expNode : node.getListExp().getList()) {
      listExpTypeValue.add(expNode.accept(this));
    }

    List<TypeNode> symParamsTypeValue = sym.getParamsType();
    for (int i = 0; i < listExpTypeValue.size(); i++) {
      if (!listExpTypeValue.get(i).equals(symParamsTypeValue.get(i))) {
        throw new SemanticException("Method " + node.getIdent().getName() + " uses different parameter type from the declared one, at line " + node.getIdent().getLine());
      }
    }
  
    return sym.getTypeNode();
  }

  @Override
  public TypeNode visitCallInstr(CallInstrNode node) {
    MethodSymbol sym = (MethodSymbol) symbolTable.lookup(node.getIdent());

    node.getIdent().accept(this);

    List<TypeNode> listExpTypeValue = new ArrayList<>();
    for (ExpNode expNode : node.getListExp().getList()) {
      listExpTypeValue.add(expNode.accept(this));
    }

    List<TypeNode> symParamsTypeValue = sym.getParamsType();
    for (int i = 0; i < listExpTypeValue.size(); i++) {
      if (!listExpTypeValue.get(i).equals(symParamsTypeValue.get(i))) {
        throw new SemanticException("Method " + node.getIdent().getName() + " uses different parameter type from the declared one, at line " + node.getIdent().getLine());
      }
    }
    return new TypeNode("void");
  }

  @Override
  public TypeNode visitType(fr.amvproject.group3.mjj.ast.nodes.other.TypeNode node) {
    return null;
  }

  @Override
  public TypeNode visitTypeMeth(TypeMethNode node) {
    return null;
  }

  @Override
  public TypeNode visitBinaryOp(BinaryOpNode node) {
    return null;
  }

  @Override
  public TypeNode visitUnaryOp(UnaryOpNode node) {
    return null;
  }

  @Override
  public TypeNode visitIdent(IdentNode node) {
    checkIfExistsToUse(node);
    return symbolTable.recordUsage(node).getTypeNode();
  }
}
