package fr.amvproject.group3.jjc.analyser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.amvproject.group3.exceptions.SemanticException;
import fr.amvproject.group3.jjc.analyser.nodes.TypeNode;
import fr.amvproject.group3.jjc.analyser.symbols.Symbols;
import fr.amvproject.group3.jjc.ast.AstVisitor;
import fr.amvproject.group3.jjc.ast.nodes.finals.*;
import fr.amvproject.group3.jjc.ast.nodes.instrs.*;
import fr.amvproject.group3.jjc.ast.nodes.intermediate.InstrNode;
import fr.amvproject.group3.jjc.ast.nodes.intermediate.ValueNode;
import fr.amvproject.group3.jjc.ast.nodes.literals.InitLiteral;
import fr.amvproject.group3.jjc.ast.nodes.literals.JcstopLiteral;
import fr.amvproject.group3.jjc.ast.nodes.literals.NopLiteral;
import fr.amvproject.group3.jjc.ast.nodes.literals.PopLiteral;
import fr.amvproject.group3.jjc.ast.nodes.literals.ReturnLiteral;
import fr.amvproject.group3.jjc.ast.nodes.literals.SwapLiteral;
import fr.amvproject.group3.jjc.ast.nodes.literals.WriteLiteral;
import fr.amvproject.group3.jjc.ast.nodes.other.BinaryOpNode;
import fr.amvproject.group3.jjc.ast.nodes.other.ObjNode;
import fr.amvproject.group3.jjc.ast.nodes.other.UnaryOpNode;
import fr.amvproject.group3.jjc.ast.nodes.program.ClassNode;
import fr.amvproject.group3.jjc.ast.nodes.program.ProgramNode;

public class SemanticVisitor implements AstVisitor<TypeNode> {
  private SymbolTable symbolTable = new SymbolTable();
  private ScopeManager scopeManager = new ScopeManager(symbolTable);
  private List<ValueNode> pushStack = new ArrayList<>();
  private int currentAddress = 1;
  private List<Integer> exploredLines = new ArrayList<>();

  public SymbolTable getSymbolTable() {
    return symbolTable;
  }

  public List<Integer> getExploredLines() {
    return exploredLines;
  }

  private TypeNode typeMapper(fr.amvproject.group3.jjc.ast.nodes.other.TypeNode node) {
    if (node.isInt()) return new TypeNode("int");
    if (node.isBoolean()) return new TypeNode("boolean");
    if (node.isString()) return new TypeNode("string");
    return new TypeNode("void");
  }

  private ValueNode peek(InstrNode instrNode) {
    if (pushStack.size() <= 0) {
      throw new SemanticException("Stack is probably empty", instrNode.getLine(), instrNode.getColumn());
    } 
    return pushStack.get(pushStack.size() - 1);
  }

  private ValueNode pop(InstrNode instrNode) {
    if (pushStack.size() <= 0) {
      throw new SemanticException("Stack is probably empty", instrNode.getLine(), instrNode.getColumn());
    } 
    return pushStack.remove(pushStack.size() - 1);
  }

  /* Checks */

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
    if (sym.getObjNode().isMeth()) {
      throw new SemanticException("Can't use a method pointer in a load", identNode.getLine(), identNode.getColumn());
    }

    return sym;
  }

  private void checkIfTypeCompatible(TypeNode identType, ValueNode valueNode, IdentNode identNode) {
    if (valueNode instanceof IntLiteralNode && !identType.isInt()) {
      throw new SemanticException("Value of Type " + "int" + " not compatible with declaration of type " + identType.getName(), identNode.getLine(), identNode.getColumn());
    } else if (valueNode instanceof BoolLiteralNode && !identType.isBoolean()) {
      throw new SemanticException("Value of Type " + "boolean" + " not compatible with declaration of type " + identType.getName(), identNode.getLine(), identNode.getColumn());
    } else if (valueNode instanceof StringLiteralNode && !identType.isString()) {
      throw new SemanticException("Value of Type " + "string" + " not compatible with declaration of type " + identType.getName(), identNode.getLine(), identNode.getColumn());
    }
  }

  /* Visitors */

  @Override
  public TypeNode visitProgram(ProgramNode node) {
    exploredLines.clear();
    Map<Integer, InstrNode> instrsMap = new HashMap<>();
    for (ClassNode classe : node.getClasses()) {
      instrsMap.put(classe.getAddress().getValue(), classe.getInstr());
    }
    while (!(instrsMap.get(currentAddress) instanceof JcstopLiteral) && currentAddress >= 1 && currentAddress <= instrsMap.size()) {
      InstrNode instrNode = instrsMap.get(currentAddress);
      exploredLines.add(instrNode.getLine());
      instrNode.accept(this);
    }
    return new TypeNode("void");
  }

  @Override
  public TypeNode visitClass(ClassNode node) {
    node.getInstr().accept(this);
    return new TypeNode("void");
  }

  @Override
  public TypeNode visitInit(InitLiteral literal) {
    currentAddress++;
    return new TypeNode("void");
  }
  
  @Override
  public TypeNode visitJcstop(JcstopLiteral literal) {
    return new TypeNode("void");
  }
  
  @Override
  public TypeNode visitNop(NopLiteral literal) {
    currentAddress++;
    return new TypeNode("void");
  }
  
  @Override
  public TypeNode visitSwap(SwapLiteral literal) {
    if (pushStack.size() <= 1) {
      throw new SemanticException("Stack should contain 2 elements to swap", literal.getLine(), literal.getColumn());
    }
    pushStack.add(pushStack.remove(pushStack.size() - 2));
    currentAddress++;
    return new TypeNode("void");
  }
  
  @Override
  public TypeNode visitPop(PopLiteral literal) {
    currentAddress++;
    return this.pop(literal).accept(this);
  }
  
  @Override
  public TypeNode visitReturn(ReturnLiteral literal) {
    ValueNode valueNode = this.pop(literal);
    if (!(valueNode instanceof IntLiteralNode)) {
      throw new SemanticException("Return expects int value", literal.getLine(), literal.getColumn());
    }
    currentAddress++;
    return new TypeNode("void");
  }
  
  @Override
  public TypeNode visitWrite(WriteLiteral literal) {
    currentAddress++;
    return new TypeNode("void");
  }
  
  @Override
  public TypeNode visitPush(PushNode node) {
    pushStack.add(node.getValue());
    currentAddress++;
    return new TypeNode("void");
  }
  
  @Override
  public TypeNode visitNew(NewNode node) {
    checkIfExistsToDeclare(node.getIdent());
    ValueNode valueNode = this.peek(node);
    checkIfTypeCompatible(typeMapper(node.getType()), valueNode, node.getIdent());
    symbolTable.addSymbol(node.getIdent(), typeMapper(node.getType()), node.getObj(), valueNode);
    currentAddress++;
    return new TypeNode("void");
  }
  
  @Override
  public TypeNode visitNewArray(NewArrayNode node) {
    checkIfExistsToDeclare(node.getIdent());
    ValueNode valueNode = this.peek(node);
    checkIfTypeCompatible(typeMapper(node.getType()), valueNode, node.getIdent());
    symbolTable.addSymbol(node.getIdent(), typeMapper(node.getType()), valueNode);
    currentAddress++;
    return new TypeNode("void");
  }
  
  @Override
  public TypeNode visitInvoke(InvokeNode node) {
    ValueNode valueNode = symbolTable.lookup(node.getIdent()).getValueNode();
    if (!(valueNode instanceof IntLiteralNode)) {
      throw new SemanticException("Return expects int value", node.getLine(), node.getColumn());
    }
    currentAddress++;
    return new TypeNode("void");
  }
  
  @Override
  public TypeNode visitLength(LengthNode node) {
    currentAddress++;
    return new TypeNode("void");
  }
  
  @Override
  public TypeNode visitLoad(LoadNode node) {
    Symbols sym;
    if (node.isA()) {
      sym = checkIfExistsToUse(node.getIdent());
      if (!sym.getObjNode().isTab()) {
        throw new SemanticException("aload not compatible with obj" + sym.getObjNode().getName(), node.getLine(), node.getColumn());
      }
    } else {
      sym = checkIfExistsToUse(node.getIdent());
      if (sym.getObjNode().isTab()) {
        throw new SemanticException("load not compatible with obj array", node.getLine(), node.getColumn());
      }
    }
    pushStack.add(sym.getValueNode());
    currentAddress++;
    return new TypeNode("void");
  }
  
  @Override
  public TypeNode visitStore(StoreNode node) {
    if (node.isA()) {

    } else {
      
    }
    currentAddress++;
    return new TypeNode("void");
  }
  
  @Override
  public TypeNode visitGoto(GotoNode node) {
    currentAddress++;
    return new TypeNode("void");
  }
  
  @Override
  public TypeNode visitIf(IfNode node) {
    currentAddress++;
    return new TypeNode("void");
  }
  
  @Override
  public TypeNode visitInc(IncNode node) {
    if (node.isA()) {
      
    } else {

    }
    
    currentAddress++;
    return new TypeNode("void");
  }
  
  @Override
  public TypeNode visitUnaryOp(UnaryOpNode node) {
    currentAddress++;
    return new TypeNode("void");
  }
  
  @Override
  public TypeNode visitBinaryOp(BinaryOpNode node) {
    currentAddress++;
    return new TypeNode("void");
  }

  @Override
  public TypeNode visitIntLiteral(IntLiteralNode node) {
    return new TypeNode("int");
  }

  @Override
  public TypeNode visitBoolLiteral(BoolLiteralNode node) {
    return new TypeNode("boolean");
  }

  @Override
  public TypeNode visitStringLiteral(StringLiteralNode node) {
    return new TypeNode("string");
  }

  @Override
  public TypeNode visitAddress(AddressNode node) {
    return new TypeNode("entier");
  }

  @Override
  public TypeNode visitIdent(IdentNode node) {
    checkIfExistsToUse(node);
    return symbolTable.recordUsage(node, scopeManager.getScopeDepth()).getTypeNode();
  }

  @Override
  public TypeNode visitObj(ObjNode node) {
    return null;
  }

  @Override
  public TypeNode visitType(fr.amvproject.group3.jjc.ast.nodes.other.TypeNode node) {
    return null;
  }
}
