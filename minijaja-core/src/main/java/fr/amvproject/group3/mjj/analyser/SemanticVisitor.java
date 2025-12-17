package fr.amvproject.group3.mjj.analyser;

import java.util.ArrayList;
import java.util.List;

import fr.amvproject.group3.exceptions.SemanticException;
import fr.amvproject.group3.mjj.analyser.nodes.SymbolIdentity;
import fr.amvproject.group3.mjj.analyser.nodes.TypeNode;
import fr.amvproject.group3.mjj.analyser.nodes.ObjNode;
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

public class SemanticVisitor implements AstVisitor<SymbolIdentity> {
  private SymbolTable symbolTable = new SymbolTable();
  private ScopeManager scopeManager = new ScopeManager(symbolTable);
  private List<Integer> exploredLines = new ArrayList<>();

  public SymbolTable getSymbolTable() {
    return symbolTable;
  }

  public ScopeManager getScopeManager() {
    return scopeManager;
  }

  public List<Integer> getExploredLines() {
    return exploredLines;
  }

  public void reset() {
    symbolTable.reset();
    scopeManager.reset();
    exploredLines.clear();
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
    
    return sym;
  }

  private void checkIfTypeCompatibleToAssign(TypeNode leftExpType, TypeNode rightExpNode, int line, int column) {
    if (!leftExpType.sameAs(rightExpNode)) {
      throw new SemanticException("Value of Type " + rightExpNode.getName() + " not compatible with declaration of type " + leftExpType.getName(), line, column);
    }
  }

  private void checkIfTypeCompatibleInExpression(TypeNode leftExpType, TypeNode rightExpNode, int line, int column) {
    if (!leftExpType.sameAs(rightExpNode)) {
      throw new SemanticException("Value of Type " + rightExpNode.getName() + " not compatible with value of type " + leftExpType.getName(), line, column);
    }
  }

  private void checkIfObjCompatibleToAssign(ObjNode leftExpObj, ObjNode rightExpObj, int line, int column) {
    if (leftExpObj.isMethPointer()) throw new SemanticException("Can't affect a new value to a method pointer !", line, column);
    else if (leftExpObj.isCst()) throw new SemanticException("Can't assign a new value to a final !", line, column);
    else if (leftExpObj.isArrPointer()) {
      if (!rightExpObj.isArrPointer()) throw new SemanticException("Table pointer can only be affected with another table pointer", line, column);
    } else if (!(leftExpObj.isVar() || leftExpObj.isArrAccess() || leftExpObj.isMethCall() || leftExpObj.isVcst())) {
      throw new SemanticException("Can't affect a new value to a class !", line, column);
    }
    if (rightExpObj.isMethPointer()) throw new SemanticException("Can't use a method pointer in an expression", line, column);
    else if (rightExpObj.isArrPointer()) {
      if (!leftExpObj.isArrPointer()) throw new SemanticException("Table pointer can only be affected with another table pointer", line, column);
    } else if (!(rightExpObj.isCst() || rightExpObj.isVar()|| rightExpObj.isArrAccess() || rightExpObj.isMethCall() || rightExpObj.isVcst() || rightExpObj.isLiteral())) {
      throw new SemanticException("Can't affect a value to a class", line, column);
    }
  }

  private void checkIfObjCompatibleInExpression(ObjNode leftExpObj, ObjNode rightExpObj, int line, int column) {
    if (leftExpObj.isMethPointer()) throw new SemanticException("Method pointer not compatible with " + rightExpObj.getName() + " !", line, column);
    else if (leftExpObj.isArrPointer()) {
      if (!rightExpObj.isArrPointer()) throw new SemanticException("Table pointer not compatible with " + rightExpObj.getName() + " !", line, column);
    } else if (!(leftExpObj.isVar() || leftExpObj.isArrAccess() || leftExpObj.isMethCall() || leftExpObj.isVcst() || leftExpObj.isLiteral())) {
      throw new SemanticException("Class can't be used in an expression !", line, column);
    }
    if (rightExpObj.isMethPointer()) throw new SemanticException("Can't use a method pointer in an expression", line, column);
    else if (rightExpObj.isArrPointer()) {
      if (!leftExpObj.isArrPointer()) throw new SemanticException("Table pointer not compatible with " + rightExpObj.getName() + " !", line, column);
    } else if (!(rightExpObj.isCst() || rightExpObj.isVar() || rightExpObj.isArrAccess() || rightExpObj.isMethCall() || rightExpObj.isVcst() || rightExpObj.isLiteral())) {
      throw new SemanticException("Class can't be used in an expression !", line, column);
    }
  }

  // Tab
  private void checkIfTabIndexIsInteger(ExpNode indexExpNode, int line, int column) {
    SymbolIdentity symId = indexExpNode.accept(this);
    if (!symId.getTypeNode().isInt()) throw new SemanticException("Value of type " + symId.getTypeNode().getName() + " can't be used as Array access index", line, column);
  }

  /* Visitors */

  @Override
  public SymbolIdentity visitProgram(ProgramNode node) {
    reset();
    for (ClassNode classeNode : node.getClasses()) {
      classeNode.accept(this);
    }
    return new SymbolIdentity(new TypeNode("void"), new ObjNode("literal", node.getLine(), node.getColumn()));
  }

  @Override
  public SymbolIdentity visitClass(ClassNode node) {
    exploredLines.add(node.getLine());
    checkIfExistsToDeclare(node.getIdent());
    symbolTable.addSymbol(node.getIdent());

    node.getDecls().accept(this);
    node.getMain().accept(this);
    return new SymbolIdentity(new TypeNode("void"), new ObjNode("literal", node.getLine(), node.getColumn()));
  }

  @Override
  public SymbolIdentity visitDecls(DeclsNode node) {
    for (DeclNode declNode : node.getList()) {
      declNode.accept(this);
    }
    return new SymbolIdentity(new TypeNode("void"), new ObjNode("literal", node.getLine(), node.getColumn()));
  }

  @Override
  public SymbolIdentity visitVars(VarsNode node) {
    for (VarDeclNode varDeclNode : node.getList()) {
      varDeclNode.accept(this);
    }
    return new SymbolIdentity(new TypeNode("void"), new ObjNode("literal", node.getLine(), node.getColumn()));
  }

  @Override
  public SymbolIdentity visitVar(VarNode node) {
    exploredLines.add(node.getLine());
    checkIfExistsToDeclare(node.getIdent());
    ObjNode leftExpObjNode = new ObjNode("var", node.getLine(), node.getColumn());
    SymbolIdentity rightSymId = node.getInitExp().accept(this);
    checkIfObjCompatibleToAssign(leftExpObjNode, rightSymId.getObjNode(), node.getLine(), node.getColumn());
    checkIfTypeCompatibleToAssign(new TypeNode(node.getType().getName()), rightSymId.getTypeNode(), node.getLine(), node.getColumn());

    symbolTable.addSymbol(node.getIdent(), new TypeNode(node.getType().getName()), leftExpObjNode);
    return new SymbolIdentity(new TypeNode("void"), new ObjNode("literal", node.getLine(), node.getColumn()));
  }

  @Override
  public SymbolIdentity visitCst(CstNode node) {
    exploredLines.add(node.getLine());
    checkIfExistsToDeclare(node.getIdent());
    ObjNode leftExpObjNode = new ObjNode("vcst", node.getLine(), node.getColumn());
    SymbolIdentity rightSymId = node.getInitExp().accept(this);
    checkIfObjCompatibleToAssign(leftExpObjNode, rightSymId.getObjNode(), node.getLine(), node.getColumn());
    checkIfTypeCompatibleToAssign(new TypeNode(node.getType().getName()), rightSymId.getTypeNode(), node.getLine(), node.getColumn());

    symbolTable.addSymbol(node.getIdent(), new TypeNode(node.getType().getName()), node.isVcst());
    return new SymbolIdentity(new TypeNode("void"), new ObjNode("literal", node.getLine(), node.getColumn()));
  }

  @Override
  public SymbolIdentity visitTable(TableNode node) {
    exploredLines.add(node.getLine());
    checkIfExistsToDeclare(node.getIdent());
    checkIfTabIndexIsInteger(node.getSizeExp(), node.getLine(), node.getColumn());

    symbolTable.addSymbol(node.getIdent(), new TypeNode(node.getType().getName()), new ObjNode("arr_pointer", node.getLine(), node.getColumn()));
    return new SymbolIdentity(new TypeNode("void"), new ObjNode("literal", node.getLine(), node.getColumn()));
  }

  @Override
  public SymbolIdentity visitMethod(MethodNode node) {
    exploredLines.add(node.getLine());
    MethodSymbol sym = (MethodSymbol) symbolTable.lookup(node.getIdent());
    symbolTable = scopeManager.newScope();
    List<SymbolIdentity> paramsTypeValue = new ArrayList<>();
    for (ParamNode paramNode : node.getParams().getList()) {
      paramsTypeValue.add(paramNode.accept(this));
    }

    // Check if method with same parameter type exists
    if (sym != null) {
      boolean hasSameParamsType = true;
      List<SymbolIdentity> symParamsTypeValue = sym.getParamsType();

      for (int i = 0; i < paramsTypeValue.size(); i++) {
        if (!paramsTypeValue.get(i).IsParamCompatibleWith(symParamsTypeValue.get(i))) {
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
      if (instrNode instanceof ReturnNode returnNode) {
        if (!instrNode.accept(this).getTypeNode().getName().equals(node.getReturnType().getName())) {
          throw new SemanticException("Return expression does not match the declared method type " + node.getReturnType().getName(), returnNode.getLine(), returnNode.getColumn());
        }
      }
    }
    symbolTable = scopeManager.exitScope();

    symbolTable.addSymbol(node.getIdent(), new TypeNode(node.getReturnType().getName()), paramsTypeValue);
    return new SymbolIdentity(new TypeNode("void"), new ObjNode("literal", node.getLine(), node.getColumn()));
  }

  @Override
  public SymbolIdentity visitMain(MainNode node) {
    exploredLines.add(node.getLine());
    symbolTable = scopeManager.newScope();
    node.getVars().accept(this);
    node.getInstrs().accept(this);
    symbolTable = scopeManager.exitScope();
    return new SymbolIdentity(new TypeNode("void"), new ObjNode("literal", node.getLine(), node.getColumn()));
  }

  @Override
  public SymbolIdentity visitParams(ParamsNode node) { // Not used, implemented directly in visitMethod
    return new SymbolIdentity(new TypeNode("void"), new ObjNode("literal", node.getLine(), node.getColumn()));
  }

  @Override
  public SymbolIdentity visitParam(ParamNode node) {
    exploredLines.add(node.getLine());
    checkIfExistsToDeclare(node.getIdent());
    TypeNode typeNode = new TypeNode(node.getType().getName());
    ObjNode objNode = new ObjNode("var", node.getLine(), node.getColumn());
    symbolTable.addSymbol(node.getIdent(), typeNode, objNode);
    return new SymbolIdentity(typeNode, objNode);
  }

  @Override
  public SymbolIdentity visitInstrs(InstrsNode node) {
    for (InstrNode instrNode : node.getList()) {
      instrNode.accept(this);
    }
    return new SymbolIdentity(new TypeNode("void"), new ObjNode("literal", node.getLine(), node.getColumn()));
  }

  @Override
  public SymbolIdentity visitListExp(ListExpNode node) {
    return new SymbolIdentity(new TypeNode("void"), new ObjNode("literal", node.getLine(), node.getColumn()));
  }

  @Override
  public SymbolIdentity visitBinaryExp(BinaryExpNode node) {
    exploredLines.add(node.getLine());
    SymbolIdentity leftSymId = node.getLeftExp().accept(this);
    SymbolIdentity rightSymId = node.getRightExp().accept(this);

    checkIfObjCompatibleInExpression(leftSymId.getObjNode(), rightSymId.getObjNode(), node.getLine(), node.getColumn());
    checkIfTypeCompatibleInExpression(leftSymId.getTypeNode(), rightSymId.getTypeNode(), node.getLine(), node.getColumn());

    if (leftSymId.getTypeNode().isInt() && !node.getOp().takesInteger()) {
      throw new SemanticException("Incompatible operand, expecting Boolean, Integer given", node.getLine(), node.getColumn());
    }
    if (leftSymId.getTypeNode().isBoolean() && !node.getOp().takesBoolean()) {
      throw new SemanticException("Incompatible operand, expecting Integer, Boolean given", node.getLine(), node.getColumn());
    }

    if (node.getOp().givesInteger()) return new SymbolIdentity(new TypeNode("int"), new ObjNode("literal", node.getLine(), node.getColumn()));
    return new SymbolIdentity(new TypeNode("boolean"), new ObjNode("literal", node.getLine(), node.getColumn()));
  }

  @Override
  public SymbolIdentity visitUnaryExp(UnaryExpNode node) {
    exploredLines.add(node.getLine());
    SymbolIdentity symId = node.getExp().accept(this);

    if (symId.getTypeNode().isInt()&& node.getOp().isNot()) {
      throw new SemanticException("Incompatible operand, expecting Boolean, Integer given", node.getLine(), node.getColumn());
    }
    if (symId.getTypeNode().isBoolean() && node.getOp().isNeg()) {
      throw new SemanticException("Incompatible operand, expecting Integer, Boolean given", node.getLine(), node.getColumn());
    }

    if (node.getOp().isNeg()) return new SymbolIdentity(new TypeNode("int"), new ObjNode("literal", node.getLine(), node.getColumn()));
    return new SymbolIdentity(new TypeNode("boolean"), new ObjNode("literal", node.getLine(), node.getColumn()));
  }

  @Override
  public SymbolIdentity visitAssign(AssignNode node) {
    exploredLines.add(node.getLine());
    SymbolIdentity leftSymId = node.getLeftValueNode().accept(this);
    SymbolIdentity rightSymId = node.getExp().accept(this);

    checkIfObjCompatibleToAssign(leftSymId.getObjNode(), rightSymId.getObjNode(), node.getLine(), node.getColumn());
    checkIfTypeCompatibleToAssign(leftSymId.getTypeNode(), rightSymId.getTypeNode(), node.getLine(), node.getColumn());

    return new SymbolIdentity(new TypeNode("void"), new ObjNode("literal", node.getLine(), node.getColumn()));
  }

  @Override
  public SymbolIdentity visitAddAssign(AddAssignNode node) {
    AssignNode assignNode = NodeOperations.addAssignToAssign(node);
    visitAssign(assignNode);
    return new SymbolIdentity(new TypeNode("void"), new ObjNode("literal", node.getLine(), node.getColumn()));
  }

  public SymbolIdentity visitInc(IncNode node) {
    AssignNode assignNode = NodeOperations.incToAssign(node);
    visitAssign(assignNode);
    return new SymbolIdentity(new TypeNode("void"), new ObjNode("literal", node.getLine(), node.getColumn()));
  }

  @Override
  public SymbolIdentity visitLength(LengthNode node) {
    exploredLines.add(node.getLine());
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
    return new SymbolIdentity(new TypeNode("int"), new ObjNode("literal", node.getLine(), node.getColumn()));
  }

  @Override
  public SymbolIdentity visitIf(IfNode node) {
    exploredLines.add(node.getLine());
    SymbolIdentity symId = node.getCondExp().accept(this);
    if (!symId.getTypeNode().isBoolean()) {
      throw new SemanticException("Incompatible expression for If, expecting Boolean, " + symId + " given", node.getLine(), node.getColumn());
    }
    if (node.getThenInstrs() != null) {
      node.getThenInstrs().accept(this);
    }
    if (node.getElseInstrs() != null) {
      node.getElseInstrs().accept(this);
    }
    return new SymbolIdentity(new TypeNode("void"), new ObjNode("literal", node.getLine(), node.getColumn()));
  }

  @Override
  public SymbolIdentity visitWhile(WhileNode node) {
    exploredLines.add(node.getLine());
    SymbolIdentity symId = node.getCondExp().accept(this);
    if (!symId.getTypeNode().isBoolean()) {
      throw new SemanticException("Incompatible expression for While, expecting Boolean, " + symId + " given", node.getLine(), node.getColumn());
    }
    symbolTable = scopeManager.newScope();
    node.getInstrs().accept(this);
    symbolTable = scopeManager.exitScope();
    return new SymbolIdentity(new TypeNode("void"), new ObjNode("literal", node.getLine(), node.getColumn()));
  }

  @Override
  public SymbolIdentity visitWrite(WriteNode node) {
    exploredLines.add(node.getLine());
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
    return new SymbolIdentity(new TypeNode("void"), new ObjNode("literal", node.getLine(), node.getColumn()));
  }

  @Override
  public SymbolIdentity visitReturn(ReturnNode node) {
    exploredLines.add(node.getLine());
    return node.getExp().accept(this);
  }

  @Override
  public SymbolIdentity visitBoolLiteral(BoolLiteralNode node) {
    return new SymbolIdentity(new TypeNode("boolean"), new ObjNode("literal", node.getLine(), node.getColumn()));
  }

  @Override
  public SymbolIdentity visitIntLiteral(IntLiteralNode node) {
    return new SymbolIdentity(new TypeNode("int"), new ObjNode("literal", node.getLine(), node.getColumn()));
  }

  @Override
  public SymbolIdentity visitStringLiteral(StringLiteralNode node) {
    return new SymbolIdentity(new TypeNode("string"), new ObjNode("literal", node.getLine(), node.getColumn()));
  }

  @Override
  public SymbolIdentity visitArrayAccess(ArrayAccessNode node) {
    checkIfTabIndexIsInteger(node.getIndexExp(), node.getLine(), node.getColumn());

    SymbolIdentity symId = node.getIdent().accept(this);

    return new SymbolIdentity(symId.getTypeNode(), new ObjNode("arr_access", node.getLine(), node.getColumn()));
  }

  @Override
  public SymbolIdentity visitCallExp(CallExpNode node) {
    exploredLines.add(node.getLine());
    MethodSymbol sym = (MethodSymbol) scopeManager.lookup(node.getIdent());

    node.getIdent().accept(this);

    List<SymbolIdentity> listExpTypeValue = new ArrayList<>();
    for (ExpNode expNode : node.getListExp().getList()) {
      listExpTypeValue.add(expNode.accept(this));
    }

    List<SymbolIdentity> symParamsTypeValue = sym.getParamsType();
    for (int i = 0; i < listExpTypeValue.size(); i++) {
      if (!listExpTypeValue.get(i).IsParamCompatibleWith(symParamsTypeValue.get(i))) {
        throw new SemanticException("Method " + node.getIdent().getName() + " uses different parameter type from the declared one, at line " + sym.getLine(), node.getLine(), node.getColumn());
      }
    }

    if (listExpTypeValue.size() != symParamsTypeValue.size()) {
      throw new SemanticException("Method " + node.getIdent().getName() + " uses different parameter count from the declared one, at line " + sym.getLine(), node.getLine(), node.getColumn());
    }

    return new SymbolIdentity(sym.getTypeNode(), new ObjNode("meth_call", node.getLine(), node.getColumn()));
  }

  @Override
  public SymbolIdentity visitCallInstr(CallInstrNode node) {
    visitCallExp(new CallExpNode(node.getIdent(), node.getListExp()));
    return new SymbolIdentity(new TypeNode("void"), new ObjNode("literal", node.getLine(), node.getColumn()));
  }

  @Override
  public SymbolIdentity visitType(fr.amvproject.group3.mjj.ast.nodes.other.TypeNode node) {
    return null;
  }

  @Override
  public SymbolIdentity visitTypeMeth(TypeMethNode node) {
    return null;
  }

  @Override
  public SymbolIdentity visitBinaryOp(BinaryOpNode node) {
    return null;
  }

  @Override
  public SymbolIdentity visitUnaryOp(UnaryOpNode node) {
    return null;
  }

  @Override
  public SymbolIdentity visitIdent(IdentNode node) {
    checkIfExistsToUse(node);
    Symbols sym = scopeManager.recordUsage(node);
    return new SymbolIdentity(sym.getTypeNode(), sym.getObjNode());
  }
}
