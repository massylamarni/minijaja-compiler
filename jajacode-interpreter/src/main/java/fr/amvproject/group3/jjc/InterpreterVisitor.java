package fr.amvproject.group3.jjc;

import java.util.HashMap;
import java.util.Map;

import fr.amvproject.group3.jjc.ast.AstVisitor;
import fr.amvproject.group3.jjc.ast.nodes.finals.AddressNode;
import fr.amvproject.group3.jjc.ast.nodes.finals.BoolLiteralNode;
import fr.amvproject.group3.jjc.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.jjc.ast.nodes.finals.IntLiteralNode;
import fr.amvproject.group3.jjc.ast.nodes.finals.StringLiteralNode;
import fr.amvproject.group3.jjc.ast.nodes.instrs.GotoNode;
import fr.amvproject.group3.jjc.ast.nodes.instrs.IfNode;
import fr.amvproject.group3.jjc.ast.nodes.instrs.IncNode;
import fr.amvproject.group3.jjc.ast.nodes.instrs.InvokeNode;
import fr.amvproject.group3.jjc.ast.nodes.instrs.LengthNode;
import fr.amvproject.group3.jjc.ast.nodes.instrs.LoadNode;
import fr.amvproject.group3.jjc.ast.nodes.instrs.NewArrayNode;
import fr.amvproject.group3.jjc.ast.nodes.instrs.NewNode;
import fr.amvproject.group3.jjc.ast.nodes.instrs.PushNode;
import fr.amvproject.group3.jjc.ast.nodes.instrs.StoreNode;
import fr.amvproject.group3.jjc.ast.nodes.intermediate.InstrNode;
import fr.amvproject.group3.jjc.ast.nodes.literals.InitLiteral;
import fr.amvproject.group3.jjc.ast.nodes.literals.JcstopLiteral;
import fr.amvproject.group3.jjc.ast.nodes.literals.NopLiteral;
import fr.amvproject.group3.jjc.ast.nodes.literals.PopLiteral;
import fr.amvproject.group3.jjc.ast.nodes.literals.ReturnLiteral;
import fr.amvproject.group3.jjc.ast.nodes.literals.SwapLiteral;
import fr.amvproject.group3.jjc.ast.nodes.literals.WriteLiteral;
import fr.amvproject.group3.jjc.ast.nodes.other.BinaryOpNode;
import fr.amvproject.group3.jjc.ast.nodes.other.ObjNode;
import fr.amvproject.group3.jjc.ast.nodes.other.TypeNode;
import fr.amvproject.group3.jjc.ast.nodes.other.UnaryOpNode;
import fr.amvproject.group3.jjc.ast.nodes.program.ClassNode;
import fr.amvproject.group3.jjc.ast.nodes.program.ProgramNode;

public class InterpreterVisitor implements AstVisitor<Object> {
  private static final String PUSH_FRAME = "__PUSH__";
  private static final String INVK_FRAME = "__INVK__";
  private static final String LENG_FRAME = "__LENG__";
  private static final String LOAD_FRAME = "__LOAD__";
  private static final String EXPR_FRAME = "__EXPR__";
  private static final int LOOP_LIMIT = 1000000;
  private static final int RECURSION_LIMIT = 10000;
  private int iterationCount = 0;
  private Stack stack = new Stack();
  private ExecutionContext ctx = new ExecutionContext(stack); // final
  private int currentAddress = 1;

  public Stack getStack() {
    return stack;
  }

  public ExecutionContext getCtx() {
    return ctx;
  }

  public void resetCurrentAddress() {
    currentAddress = 1;
  }

  public void decCurrentAddress() {
    currentAddress--;
  }

  @Override
  public Object visitProgram(ProgramNode node) {
    Map<Integer, InstrNode> instrsMap = new HashMap<>();
    for (ClassNode classe : node.getClasses()) {
      instrsMap.put(classe.getAddress().getValue(), classe.getInstr());
    }
    while (!(instrsMap.get(currentAddress) instanceof JcstopLiteral) && currentAddress >= 1 && currentAddress <= instrsMap.size()) {
      InstrNode instrNode = instrsMap.get(currentAddress);
      ctx.recordNode(instrNode);
      instrNode.accept(this);
    }
    return null;
  }

  @Override
  public Object visitClass(ClassNode node) {
    node.getInstr().accept(this);
    return null;
  }

  // [init] : <m,a> ⊢ init –» <[], a+1>
  @Override
  public Object visitInit(InitLiteral literal) {
    currentAddress++;
    return null;
  }

  // [jcstop] : <m,a> ⊢ jcstop –» <m, ⊥>
  @Override
  public Object visitJcstop(JcstopLiteral literal) {
    return null;
  }

  // [nop] : <m,a> ⊢ nop –» <m, a+1>
  @Override
  public Object visitNop(NopLiteral literal) {
    currentAddress++;
    return null;
  }

  // [swap] : <q1.q2.m,a> ⊢ swap –» <q2.q1.m, a+1>
  @Override
  public Object visitSwap(SwapLiteral literal) {
    stack.swap();
    currentAddress++;
    return null;
  }

  // [pop] : <<i,v,o,t>.m,a>- pop –» <m, a+1>
  @Override
  public Object visitPop(PopLiteral literal) {
    Quad quad = stack.peek();
    try {
      stack.retirerDecl(quad.getIdent());
    } catch (Exception e) {
      throw new fr.amvproject.group3.exceptions.RuntimeException("Index out of bounds", literal.getLine(), literal.getColumn());
    }
    currentAddress++;
    return null;
  }

  // [return] : <<w ,a1,cst,*>.m,a> ⊢ return –» <m, a1>
  @Override
  public Object visitReturn(ReturnLiteral literal) {
    Quad quad = stack.pop();
    currentAddress = (int) quad.getValue();
    return null;
  }

  // [write] : <<w, v, cst,*>.m,a> ⊢ write –» <Afficher(v,m),a+1>
  @Override
  public Object visitWrite(WriteLiteral literal) {
    Quad quad = stack.pop();
    if (literal.isLn()) {
      System.out.println(quad.getValue());  // TODO Use output buffer instead
    } else {
      System.out.print(quad.getValue());
    }
    currentAddress++;
    return null;
  }

  // [push]: <m,a> ⊢ push(v) –» <<w, v, cst,*>.m, a+1>
  @Override
  public Object visitPush(PushNode node) {
    stack.push(PUSH_FRAME, node.getValue().accept(this));
    currentAddress++;
    return null;
  }

  // [newV] : <m,a> ⊢ new(i,t,var,s) –» <IdentVal(i,t,m,s),a+1>
  // [newC] : <<w, v, cst,*>.m,a> ⊢ new(i,t,cst,0) –» <DeclCst(i,v,t,m),a+1>
  // [newM] : <<w, v, cst,*>.m,a> ⊢ new(i,t,meth,0) –» <DeclMeth(i, v, t, m),a+1>
  @Override
  public Object visitNew(NewNode node) {
    if (node.getObj().isVar()) {
      stack.identVal(node.getIdent(), node.getType(), (int) node.getValueNode().accept(this));
    } else if (node.getObj().isCst()) {
      Quad quad = stack.pop();
      if (quad.getObj().isVcst()) {
        stack.declCst(node.getIdent(), quad.getValue(), node.getType(), true);
      }
    } else if (node.getObj().isMeth()) {
      Quad quad = stack.pop();
      stack.declMeth(node.getIdent(), quad.getValue(), node.getType());
    }
    currentAddress++;
    return null;
  }

  // [newarray]: <<w, v, cst,*>.m,a> ⊢ newarray(i, t) –» <DeclTab(i, v, t, m),a+1>
  @Override
  public Object visitNewArray(NewArrayNode node) {
    Quad quad = stack.pop();
    stack.declTab(node.getIdent(), (int) quad.getValue(), node.getType());
    currentAddress++;
    return null;
  }

  // [invoke]: <m,a> ⊢ invoke(i) –» <<w, a+1,cst,*>.m, Val(i, m)>
  @Override
  public Object visitInvoke(InvokeNode node) {
    if (iterationCount++ > RECURSION_LIMIT) {
      throw new fr.amvproject.group3.exceptions.RuntimeException("Infinite recursion detected !", node.getLine(), node.getColumn());
    }
    stack.push(INVK_FRAME, currentAddress+1);
    currentAddress = (int) stack.val(node.getIdent());
    return null;
  }

  // [length] : <m,a> ⊢ length(i) –» <<w,long(i,m),cst,*>, a+1>
  @Override
  public Object visitLength(LengthNode node) {
    try {
      stack.push(LENG_FRAME, stack.longT(node.getIdent()));
    } catch (Exception e) {
      throw new fr.amvproject.group3.exceptions.RuntimeException("Index out of bounds", node.getLine(), node.getColumn());
    }
    currentAddress++;
    return null;
  }

  // [load]: <m,a> ⊢ load(i) –» <<w, Val(i,m), cst,*>.m, a+1>
  // [aload]: <<w,ind, cst,*>.m,a> ⊢ aload(i) –» <<w, ValT(i,ind,m), cst,*>.m, a+1>
  @Override
  public Object visitLoad(LoadNode node) {
    if (node.isA()) {
      Quad quad = stack.pop();
      try {
        stack.push(LOAD_FRAME, stack.valT(node.getIdent(), (int) quad.getValue()));
      } catch (Exception e) {
        throw new fr.amvproject.group3.exceptions.RuntimeException("Index out of bounds", node.getLine(), node.getColumn());
      }
    } else {
      stack.push(LOAD_FRAME, stack.val(node.getIdent()));
    }
    currentAddress++;
    return null;
  }

  // [store]: <<w, v, cst,*>.m,a> ⊢ store(i) –» <AffecterVal(i,v,m), a+1>
  // [astore]: <<w,v, cst,*>.<w,ind, cst,*>.m,a> ⊢ astore(i) –» <AffecterValT(i,ind,v,m), a+1>
  @Override
  public Object visitStore(StoreNode node) {
    Quad quad = stack.pop();
    if (node.isA()) {
      Quad quad2 = stack.pop();
      try {
        stack.affecterValT(node.getIdent(), (int) quad2.getValue(), quad.getValue());
      } catch (Exception e) {
        throw new fr.amvproject.group3.exceptions.RuntimeException("Index out of bounds", node.getLine(), node.getColumn());
      }
    } else {
      stack.affecterVal(node.getIdent(), quad.getValue());
    }
    currentAddress++;
    return null;
  }

  // [goto]: <m,a> ⊢ goto(a1) –» <m, a1>
  @Override
  public Object visitGoto(GotoNode node) {
    if (iterationCount++ > LOOP_LIMIT) {
      throw new fr.amvproject.group3.exceptions.RuntimeException("Infinite loop detected !", node.getLine(), node.getColumn());
    }
    currentAddress = node.getAddress().getValue();
    return null;
  }

  // [iftrue]: <<w,true, cst, *>.m,a> ⊢ if(a1) –» <m,a1>
  // [iffalse]: <<w,false, cst, *>.m,a> ⊢ if(a1) –» <m,a+1>
  @Override
  public Object visitIf(IfNode node) {
    Quad quad = stack.pop();
    if ((boolean) quad.getValue()) {
      currentAddress = node.getAddress().getValue();
      return null;
    }
    currentAddress++;
    return null;
  }

  // [inc]: <<w,v, cst,*>.m,a> ⊢ inc(i) –» <AffecterVal(i,Val(i,m)+v,m), a+1>
  // [ainc]: <<w,v, cst,*>.<w,ind, cst,*>.m,a> ⊢ ainc(i) –» <AffecterValT(i,ind,Val(i,ind,m)+v,m), a+1>
  @Override
  public Object visitInc(IncNode node) {
    Quad quad = stack.pop();
    if (node.isA()) {
      Quad quad2 = stack.pop();
      try {
        stack.affecterValT(node.getIdent(), (int) quad2.getValue(), (int) stack.valT(node.getIdent(), (int) quad2.getValue()) + (int) quad.getValue());
      } catch (Exception e) {
        throw new fr.amvproject.group3.exceptions.RuntimeException("Index out of bounds", node.getLine(), node.getColumn());
      }
    } else {
      stack.affecterVal(node.getIdent(), (int) stack.val(node.getIdent()) + (int) quad.getValue());
    }
    
    currentAddress++;
    return null;
  }

  // [op1]: <<w, v1,cst,*>.m,a> ⊢ oper1 –» <<w, oper1 v1, cst,*>.m, a+1>
  @Override
  public Object visitUnaryOp(UnaryOpNode node) {
    Quad quad = stack.pop();
    Object result = null;
    switch (node.getOp()) {
      case NOT: result = !(boolean) quad.getValue();
        break;
      case NEG: result = (int) quad.getValue() * -1;
        break;
      default: throw new IllegalArgumentException("Unknown unary operator: " + node.getOp());
    }
    stack.push(EXPR_FRAME, result);
    currentAddress++;
    return null;
  }

  // [op2]: <<w, v2,cst,*>.<w, v1,cst,*>.m,a>⊢oper2 –» <<w, v1 oper2 v2, cst,*>.m,
  // a+1>
  @Override
  public Object visitBinaryOp(BinaryOpNode node) {
    Quad quad = stack.pop();
    Quad quad2 = stack.pop();
    Object result = null;
    switch (node.getOp()) {
      case AND: result = (boolean) quad2.getValue() && (boolean) quad.getValue();
        break;
      case OR:  result = (boolean) quad2.getValue() || (boolean) quad.getValue();
        break;
      case ADD: result = (int) quad2.getValue() + (int) quad.getValue();
        break;
      case SUB: result = (int) quad2.getValue() - (int) quad.getValue();
        break;
      case MUL: result = (int) quad2.getValue() * (int) quad.getValue();
        break;
      case DIV:
        if ((int) quad.getValue() == 0) {
          throw new fr.amvproject.group3.exceptions.RuntimeException("Runtime error: division by zero", node.getLine(), node.getColumn());
        }
        result = (int) quad2.getValue() / (int) quad.getValue();
        break;
      case CMP:  result =  quad2.getValue() == quad.getValue();
        break;
      case SUP:  result = (int) quad2.getValue() > (int) quad.getValue();
        break;
      default: throw new IllegalArgumentException("Unknown binary operator: " + node.getOp());
    }
    stack.push(EXPR_FRAME, result);
    currentAddress++;
    return null;
  }

  @Override
  public Object visitIntLiteral(IntLiteralNode node) {
    return node.getValue();
  }

  @Override
  public Object visitBoolLiteral(BoolLiteralNode node) {
    return node.getValue();
  }

  @Override
  public Object visitStringLiteral(StringLiteralNode node) {
    return node.getValue();
  }

  @Override
  public Object visitIdent(IdentNode node) {
    return stack.val(node);
  }

  @Override
  public Object visitAddress(AddressNode node) {
    return node.getValue();
  }

  /* Other */

  @Override
  public Object visitObj(ObjNode node) {
    return null;
  }

  @Override
  public Object visitType(TypeNode node) {
    return null;
  }
}
