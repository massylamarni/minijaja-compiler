package fr.amvproject.group3.mjj.analyser;

import java.util.ArrayList;
import java.util.List;

import fr.amvproject.group3.exceptions.SemanticException;
import fr.amvproject.group3.mjj.analyser.nodes.TypeNode;
import fr.amvproject.group3.mjj.analyser.symbols.ClassSymbol;
import fr.amvproject.group3.mjj.analyser.symbols.CstSymbol;
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
import fr.amvproject.group3.mjj.utils.NodeOperations;

public class SemanticVisitor implements AstVisitor<TypeNode> {
  SymbolTable symbolTable = new SymbolTable();
  ScopeManager scopeManager = new ScopeManager(symbolTable);
  CompileTimeVisitor compileTimeVisitor = new CompileTimeVisitor(scopeManager);

  public SymbolTable getSymbolTable() {
    return symbolTable;
  }

  /* Checks */

  // Class | Var | Cst | Tab
  private void checkIfExistsToDeclare(IdentNode identNode) {
    Symbols sym = symbolTable.lookup(identNode);
    if (sym != null) {
      throw new SemanticException("Symbol " + identNode.getName() + " already declared at line" + sym.getLine(), identNode.getLine(), identNode.getColumn());
    }
  }

  // Class | Var | Cst | Tab
  private Symbols checkIfExistsToUse(IdentNode identNode) {
    Symbols sym = scopeManager.lookup(identNode);

    if (sym == null) {
      throw new SemanticException("Symbol " + identNode.getName() + " is referenced, but never declared", identNode.getLine(), identNode.getColumn());
    }
    if (sym instanceof ClassSymbol) throw new SemanticException("Can't use a class in an expression", identNode.getLine(), identNode.getColumn());
    // if (sym instanceof MethodSymbol) throw new SemanticException("Can't use a method pointer in an expression", identNode.getLine(), identNode.getColumn());
    // if (sym instanceof TableSymbol) throw new SemanticException("Can't use a table pointer in an expression", identNode.getLine(), identNode.getColumn());

    return sym;
  }

  private void checkIfTypeCompatible(TypeNode identType, ExpNode identInitExp, IdentNode identNode) {
    TypeNode typeExpValue = identInitExp.accept(this);
    if (!identType.sameAs(typeExpValue)) {
      throw new SemanticException("Value of Type " + typeExpValue + " not compatible with declaration of type " + identType.getName(), identNode.getLine(), identNode.getColumn());
    }
  }

  private void checkIfObjCompatible(IdentNode identNode, ExpNode rightExpNode) {
    Symbols leftSym = scopeManager.lookup(identNode);
    ExpNode evaluatedRightExpNode;
    try {
      evaluatedRightExpNode = (ExpNode) rightExpNode.accept(compileTimeVisitor);

      if (leftSym instanceof ClassSymbol) throw new SemanticException("Can't affact a new value to a class !", identNode.getLine(), identNode.getColumn());
      if (leftSym instanceof CstSymbol cstSymbol && !cstSymbol.isVcst()) throw new SemanticException("Can't assign a new value to a final !", identNode.getLine(), identNode.getColumn());
      if (leftSym instanceof MethodSymbol) throw new SemanticException("Can't affact a new value to a method pointer !", identNode.getLine(), identNode.getColumn());
      if (evaluatedRightExpNode instanceof IdentNode rightIdentNode) {
        Symbols rightSym = checkIfExistsToUse(rightIdentNode);

        if (rightSym instanceof ClassSymbol) throw new SemanticException("Can't use a class in an expression", identNode.getLine(), identNode.getColumn());
        if (rightSym instanceof MethodSymbol) throw new SemanticException("Can't use a method pointer in an expression", identNode.getLine(), identNode.getColumn());
        if (leftSym instanceof TableSymbol != rightSym instanceof TableSymbol) throw new SemanticException("Table pointer can only be affected with another table pointer", identNode.getLine(), identNode.getColumn());
        if (!leftSym.getTypeNode().sameAs(rightSym.getTypeNode())) throw new SemanticException("Type mismatch", identNode.getLine(), identNode.getColumn()); // Is this used ?
      } else if (evaluatedRightExpNode instanceof ArrayAccessNode) {
        if (leftSym instanceof TableSymbol) throw new SemanticException("Table pointer can only be affected with another table pointer", identNode.getLine(), identNode.getColumn());
      }
    } catch (Exception e) {
      throw e;
    }
  }

  // Tab
  private ExpNode checkIfTabIndexIsInteger(ExpNode indexExpNode, IdentNode identNode) {
    TypeNode sizeExpType = indexExpNode.accept(this);
    if (!sizeExpType.isInt()) throw new SemanticException("Value of type " + sizeExpType.getName() + " can't be used as Array access index", identNode.getLine(), identNode.getColumn());
    ExpNode evaluatedIndexNode = null;
    try {
      evaluatedIndexNode = (ExpNode) indexExpNode.accept(compileTimeVisitor);
    } catch (SemanticException e) {
      throw e;
    }
    return evaluatedIndexNode;
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

    scopeManager.newScope();
    node.getDecls().accept(this);
    node.getMain().accept(this);
    scopeManager.exitScope();
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
  public TypeNode visitVars(VarsNode node) {
    for (VarDeclNode varDeclNode : node.getList()) {
      varDeclNode.accept(this);
    }
    return null;
  }

  @Override
  public TypeNode visitVar(VarNode node) {
    checkIfExistsToDeclare(node.getIdent());
    checkIfObjCompatible(node.getIdent(), node.getInitExp());
    checkIfTypeCompatible(new TypeNode(node.getType().getName()), node.getInitExp(), node.getIdent());

    symbolTable.addSymbol(node.getIdent(), new TypeNode(node.getType().getName()));
    return new TypeNode("void");
  }

  @Override
  public TypeNode visitCst(CstNode node) {
    checkIfExistsToDeclare(node.getIdent());
    checkIfObjCompatible(node.getIdent(), node.getInitExp());
    checkIfTypeCompatible(new TypeNode(node.getType().getName()), node.getInitExp(), node.getIdent());

    symbolTable.addSymbol(node.getIdent(), new TypeNode(node.getType().getName()), node.isVcst());
    return new TypeNode("void");
  }

  @Override
  public TypeNode visitTable(TableNode node) {
    checkIfExistsToDeclare(node.getIdent());
    ExpNode sizeExpValue = checkIfTabIndexIsInteger(node.getSizeExp(), node.getIdent());
    
    symbolTable.addSymbol(node.getIdent(), new TypeNode(node.getType().getName()), sizeExpValue);
    return new TypeNode("void");
  }

  @Override
  public TypeNode visitMethod(MethodNode node) {
    MethodSymbol sym = (MethodSymbol) symbolTable.lookup(node.getIdent());

    scopeManager.newScope();
    List<TypeNode> paramsTypeValue = new ArrayList<>();
    for (ParamNode paramNode : node.getParams().getList()) {
      paramsTypeValue.add(paramNode.accept(this));
    }

    // Check if method with same parameter type exists
    if (sym != null) {
      boolean hasSameParamsType = true;
      List<TypeNode> symParamsTypeValue = sym.getParamsType();

      for (int i = 0; i < paramsTypeValue.size(); i++) {
        if (!paramsTypeValue.get(i).sameAs(symParamsTypeValue.get(i))) {
          hasSameParamsType = false;
          break;
        }
      }

      if (paramsTypeValue.size() == symParamsTypeValue.size() && hasSameParamsType) {
        throw new SemanticException("Symbol " + node.getIdent().getName() + " already declared at line " + sym.getLine(), node.getLine(), node.getColumn());
      }
    }

    node.getParams().accept(this);
    node.getVars().accept(this);

    // Check if return value types match declared return type
    for (InstrNode instrNode : node.getInstrs().getList()) {
      if (!instrNode.accept(this).getName().equals(node.getReturnType().getName())) {
        ReturnNode returnNode = (ReturnNode) instrNode;
        throw new SemanticException("Return expression does not match the declared method type " + node.getReturnType().getName(), returnNode.getLine(), returnNode.getColumn());
      }
    }
    scopeManager.exitScope();

    symbolTable.addSymbol(node.getIdent(), new TypeNode(node.getReturnType().getName()), paramsTypeValue);
    return new TypeNode("void");
  }

  @Override
  public TypeNode visitMain(MainNode node) {
    scopeManager.newScope();
    node.getVars().accept(this);
    node.getInstrs().accept(this);
    scopeManager.exitScope();
    return new TypeNode("void");
  }

  @Override
  public TypeNode visitParams(ParamsNode node) { // Not used, implemented directly in visitMethod
    return null;
  }

  @Override
  public TypeNode visitParam(ParamNode node) {
    checkIfExistsToDeclare(node.getIdent());
    TypeNode typeNode = new TypeNode(node.getType().getName());
    symbolTable.addSymbol(node.getIdent(), typeNode);
    return typeNode;
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

    if (!leftExpTypeValue.sameAs(rightExpTypeValue)) {
      throw new SemanticException("Type mismatch", node.getLine(), node.getColumn());
    }
    if (leftExpTypeValue.isInt() && !node.getOp().takesInteger()) {
      throw new SemanticException("Incompatible operand, expecting Boolean, Integer given", node.getLine(), node.getColumn());
    }
    if (leftExpTypeValue.isBoolean() && !node.getOp().takesBoolean()) {
      throw new SemanticException("Incompatible operand, expecting Integer, Boolean given", node.getLine(), node.getColumn());
    }

    if (node.getOp().givesInteger()) return new TypeNode("int");
    return new TypeNode("boolean");
  }

  @Override
  public TypeNode visitUnaryExp(UnaryExpNode node) {
    TypeNode expTypeValue = node.getExp().accept(this);

    if (expTypeValue.isInt()&& node.getOp().isNot()) {
      throw new SemanticException("Incompatible operand, expecting Boolean, Integer given", node.getLine(), node.getColumn());
    }
    if (expTypeValue.isBoolean() && node.getOp().isNeg()) {
      throw new SemanticException("Incompatible operand, expecting Integer, Boolean given", node.getLine(), node.getColumn());
    }

    if (node.getOp().isNeg()) return new TypeNode("int");
    return new TypeNode("boolean");
  }

  @Override
  public TypeNode visitAssign(AssignNode node) {
    TypeNode leftValueNodeType = node.getLeftValueNode().accept(this);
    if (node.getLeftValueNode() instanceof IdentNode identNode) {
      checkIfObjCompatible(identNode, node.getExp());
      checkIfTypeCompatible(leftValueNodeType, node.getExp(), identNode);
    } else if (node.getLeftValueNode() instanceof ArrayAccessNode arrayAccessNode) {
      checkIfTypeCompatible(leftValueNodeType, node.getExp(), arrayAccessNode.getIdent());
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
    AssignNode assignNode = NodeOperations.incToAssign(node);
    visitAssign(assignNode);
    return new TypeNode("void");
  }

  @Override
  public TypeNode visitLength(LengthNode node) {
    node.getIdent().accept(this);
    Symbols sym = scopeManager.lookup(node.getIdent());
    if (sym instanceof ClassSymbol) {
      throw new SemanticException("Incompatible parameter for Length, expecting Array pointer, Class given", node.getLine(), node.getColumn());
    }
    if (sym instanceof MethodSymbol) {
      throw new SemanticException("Incompatible parameter for Length, expecting Array pointer, Method given", node.getLine(), node.getColumn());
    }
    if (sym instanceof CstSymbol) {
      throw new SemanticException("Incompatible parameter for Length, expecting Array pointer, Final given", node.getLine(), node.getColumn());
    }
    if (sym instanceof VarSymbol) {
      throw new SemanticException("Incompatible parameter for Length, expecting Array pointer, Variable given", node.getLine(), node.getColumn());
    }
    return new TypeNode("int");
  }

  @Override
  public TypeNode visitIf(IfNode node) {
    TypeNode expTypeValue = node.getCondExp().accept(this);
    if (!expTypeValue.isBoolean()) {
      throw new SemanticException("Incompatible expression for If, expecting Boolean, " + expTypeValue + " given", node.getLine(), node.getColumn());
    }
    if (node.getThenInstrs() != null) {
      scopeManager.newScope();
      node.getThenInstrs().accept(this);
      scopeManager.exitScope();
    }
    if (node.getElseInstrs() != null) {
      scopeManager.newScope();
      node.getElseInstrs().accept(this);
      scopeManager.exitScope();
    }
    return new TypeNode("void");
  }

  @Override
  public TypeNode visitWhile(WhileNode node) {
    TypeNode expTypeValue = node.getCondExp().accept(this);
    if (!expTypeValue.isBoolean()) {
      throw new SemanticException("Incompatible expression for While, expecting Boolean, " + expTypeValue + " given", node.getLine(), node.getColumn());
    }
    scopeManager.newScope();
    node.getInstrs().accept(this);
    scopeManager.exitScope();
    return new TypeNode("void");
  }

  @Override
  public TypeNode visitWrite(WriteNode node) {
    node.getExp().accept(this);
    if (node.getExp() instanceof IdentNode identNode) {
      Symbols sym = scopeManager.lookup(identNode);
      if (sym instanceof ClassSymbol) {
        throw new SemanticException("Incompatible parameter for Length, expecting, Variable, Final or Array access, Class given", node.getLine(), node.getColumn());
      }
      if (sym instanceof MethodSymbol) {
        throw new SemanticException("Incompatible parameter for Length, expecting, Variable, Final or Array access, Method given", node.getLine(), node.getColumn());
      }
      if (sym instanceof TableSymbol) {
        throw new SemanticException("Incompatible parameter for Length, expecting, Variable, Final or Array access, Array pointer given", node.getLine(), node.getColumn());
      }
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
    checkIfTabIndexIsInteger(node.getIndexExp(), node.getIdent());
    
    return node.getIdent().accept(this);
  }

  @Override
  public TypeNode visitCallExp(CallExpNode node) {
    MethodSymbol sym = (MethodSymbol) scopeManager.lookup(node.getIdent());

    node.getIdent().accept(this);

    List<TypeNode> listExpTypeValue = new ArrayList<>();
    for (ExpNode expNode : node.getListExp().getList()) {
      listExpTypeValue.add(expNode.accept(this));
    }

    List<TypeNode> symParamsTypeValue = sym.getParamsType();
    for (int i = 0; i < listExpTypeValue.size(); i++) {
      if (!listExpTypeValue.get(i).sameAs(symParamsTypeValue.get(i))) {
        throw new SemanticException("Method " + node.getIdent().getName() + " uses different parameter type from the declared one, at line " + sym.getLine(), node.getLine(), node.getColumn());
      }
    }
  
    return sym.getTypeNode();
  }

  @Override
  public TypeNode visitCallInstr(CallInstrNode node) {
    visitCallExp(new CallExpNode(node.getIdent(), node.getListExp()));
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
    return symbolTable.recordUsage(node, scopeManager.getScopeDepth()).getTypeNode();
  }
}
