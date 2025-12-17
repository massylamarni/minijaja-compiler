package fr.amvproject.group3.mjj;

import java.util.Iterator;
import java.util.List;

import fr.amvproject.group3.mjj.ast.nodes.abstracts.ParamsNode;
import fr.amvproject.group3.mjj.ast.nodes.abstracts.VarsNode;
import fr.amvproject.group3.mjj.ast.nodes.decls.MethodNode;
import fr.amvproject.group3.mjj.ast.nodes.abstracts.InstrsNode;
import fr.amvproject.group3.mjj.ast.nodes.abstracts.ListExpNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.ParamNode;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.ExpNode;
import fr.amvproject.group3.mjj.ast.nodes.other.TypeMethNode;
import fr.amvproject.group3.mjj.ast.nodes.other.TypeNode;
import fr.amvproject.group3.nodes.ObjNode;
import fr.amvproject.group3.nodes.PointerNode;
import fr.amvproject.group3.nodes.TypeMemNode;
import fr.amvproject.group3.utils.StringSaver;
import fr.amvproject.group3.Heap;

public class Stack extends StackManager {
  private List<Quad> queue = super.getQueue();
  private Heap heap = new Heap();

  public Heap getHeap() {
    return heap;
  }

  public void setQueue(List<Quad> queue) {
    this.queue = queue;
  }

  // --- Stack Operations ---
  public void push(Quad q) {
    queue.add(q);
  }

  public Quad pop() {
    if (queue.isEmpty())
      throw new IllegalStateException("Stack is empty");
    return queue.remove(queue.size() - 1);
  }

  public void swap() {
    int size = queue.size();
    if (size < 2)
      throw new IllegalStateException("Stack has fewer than 2 elements");

    Quad first = queue.get(size - 1);
    Quad second = queue.get(size - 2);

    queue.set(size - 1, second);
    queue.set(size - 2, first);
  }

  public Quad peek() {
    if (queue.isEmpty()) return null;
    return queue.get(queue.size() - 1);
  }

  public Quad getClassQuad() {
    if (queue.isEmpty()) return null;
    return queue.get(0);
  }

  // --- Variable / Constant / Array / Method Declarations ---

  public void declVar(IdentNode ident, Object value, TypeMethNode type) {
    this.push(new Quad(ident, value, new ObjNode("var"), type));
  }

  public void declCst(IdentNode ident, Object value, TypeNode type, boolean isVcst) {
    // Vcst = can be initialized later but not changed after
    this.push(new Quad(ident, value, new ObjNode(isVcst ? "vcst" : "cst"), type));
  }

  public void declTab(IdentNode ident, int size, TypeMethNode type) {
    PointerNode ref = heap.allocate(size, new TypeMemNode(type.getName()));
    this.push(new Quad(ident, ref, new ObjNode("tab"), type));
  }

  public void declMeth(MethodNode methodNode) {
      this.push(new Quad(methodNode.getIdent(), methodNode, new ObjNode("meth"), methodNode.getReturnType()));
  }

  // --- Access / Assignment ---

  // Replaces the Quad at stackDepth
  public void identVal(IdentNode ident, TypeMethNode type, Object newValue, int stackDepth) {
    int index = queue.size() - 1 - stackDepth;
    if (index < 0 || index >= queue.size())
      return;

    Quad replaced = new Quad(ident, newValue, new ObjNode("var"), type);

    queue.set(index, replaced);
  }

  // Remove Declaration from Stack
  public void retirerDecl(IdentNode ident) throws IndexOutOfBoundsException {
    for (int i = queue.size() - 1; i >= 0; i--) {
      Quad quad = queue.get(i);
      if (quad.getIdent().sameAs(ident)) {
        if (quad.getObj().isTab())
          heap.removeRef((PointerNode) quad.getValue(), new TypeMemNode(((TypeMethNode) quad.getType()).getName()));
        queue.remove(i);
        return;
      }
    }
  }

  // Return value of identifier
  public Object val(IdentNode ident) {
    for (int i = queue.size() - 1; i >= 0; i--) {
      Quad quad = queue.get(i);
      if (quad.getIdent().sameAs(ident))
        return quad.getValue();
    }
    return null;
  }

  // Return value of identifier (array)
  public Object valT(IdentNode ident, int index) throws IndexOutOfBoundsException {
    for (int i = queue.size() - 1; i >= 0; i--) {
      Quad quad = queue.get(i);
      if (quad.getIdent().sameAs(ident))
        return heap.get((PointerNode) quad.getValue(), index);
    }
    return null;
  }

  // Return length of array, long is reserved word
  public Integer longT(IdentNode ident) throws IndexOutOfBoundsException {
    for (int i = queue.size() - 1; i >= 0; i--) {
      Quad quad = queue.get(i);
      if (quad.getIdent().sameAs(ident))
        return (int) heap.getSize((PointerNode) quad.getValue());
    }
    return null;
  }

  // Assigns a new value to the identifier
  public void affecterVal(IdentNode ident, Object value) {
    for (int i = queue.size() - 1; i >= 0; i--) {
      Quad quad = queue.get(i);
      if (quad.getIdent().sameAs(ident)) {
        if (quad.getObj().isVcst()) {
          queue.set(i, new Quad(ident, value, new ObjNode("cst"), quad.getType()));
          return;
        } else if (quad.getObj().isTab()) {
          heap.removeRef((PointerNode) quad.getValue(), new TypeMemNode(((TypeMethNode) quad.getType()).getName())); // RetirerTas
          heap.addRef((PointerNode) value);
          queue.set(i, new Quad(ident, value, new ObjNode("tab"), quad.getType()));
          return;
        } else if (quad.getObj().isCst()) {
          return;
        }
        queue.set(i, new Quad(ident, value, quad.getObj(), quad.getType()));
        return;
      }
    }
  }

  // Assigns a new value to an array at a specific index
  public void affecterValT(IdentNode ident, int index, Object value) throws IndexOutOfBoundsException {
    for (int i = queue.size() - 1; i >= 0; i--) {
      Quad quad = queue.get(i);
      if (quad.getIdent().sameAs(ident))
        heap.set((PointerNode) quad.getValue(), index, value);
    }
  }

  // Assigns a new type to an identifier
  public void affecterType(IdentNode ident, TypeMethNode type) {
    for (int i = queue.size() - 1; i >= 0; i--) {
      Quad quad = queue.get(i);
      if (quad.getIdent().sameAs(ident)) {
        queue.set(i, new Quad(ident, quad.getValue(), quad.getObj(), type));
      }
    }
  }

  // Params declaration
  public void expParam(ListExpNode listExpNode, ParamsNode paramsNode) {
    if (paramsNode == null || listExpNode == null)
      return;
    Iterator<ExpNode> expIt = listExpNode.getList().iterator();
    Iterator<ParamNode> paramIt = paramsNode.getList().iterator();
    while (expIt.hasNext() && paramIt.hasNext()) {
      ExpNode exp = expIt.next();
      ParamNode param = paramIt.next();
      declVar(param.getIdent(), exp, new TypeMethNode(param.getType().getTypeToken()));
    }
  }

  // Return object type of identifier
  public ObjNode objet(IdentNode ident) {
    for (int i = queue.size() - 1; i >= 0; i--) {
      Quad quad = queue.get(i);
      if (quad.getIdent().sameAs(ident))
        return quad.getObj();
    }
    return null;
  }

  // Return type of identifier
  public Object sorte(IdentNode ident) {
    for (int i = queue.size() - 1; i >= 0; i--) {
      Quad quad = queue.get(i);
      if (quad.getIdent().sameAs(ident))
        return quad.getType();
    }
    return null;
  }

  // Return params of method
  public ParamsNode parametre(IdentNode ident, int depth) {
    int currentDepth = 0;
    for (int i = queue.size() - 1; i >= 0; i--) {
      Quad quad = queue.get(i);
      if (quad.getIdent().sameAs(ident) &&
          quad.getObj().isMeth() &&
          (quad.getValue() instanceof MethodNode methodNode) &&
          methodNode.getIdent().sameAs(ident) &&
          methodNode.getReturnType().sameAs((TypeMethNode) quad.getType())) {
        if (currentDepth++ == depth) return methodNode.getParams();
      }
    }
    return null;
  }

  // Return declarations of method
  public VarsNode declaration(IdentNode ident, int depth) {
    int currentDepth = 0;
    for (int i = queue.size() - 1; i >= 0; i--) {
      Quad quad = queue.get(i);
      if (quad.getIdent().sameAs(ident) &&
          quad.getObj().isMeth() &&
          (quad.getValue() instanceof MethodNode methodNode) &&
          methodNode.getIdent().sameAs(ident) &&
          methodNode.getReturnType().sameAs((TypeMethNode) quad.getType())) {
        if (currentDepth++ == depth) return methodNode.getVars();
      }
    }
    return null;
  }

  // Return instructions of method
  public InstrsNode corps(IdentNode ident, int depth) {
    int currentDepth = 0;
    for (int i = queue.size() - 1; i >= 0; i--) {
      Quad quad = queue.get(i);
      if (quad.getIdent().sameAs(ident) &&
          quad.getObj().isMeth() &&
          (quad.getValue() instanceof MethodNode methodNode) &&
          methodNode.getIdent().sameAs(ident) &&
          methodNode.getReturnType().sameAs((TypeMethNode) quad.getType())) {
        if (currentDepth++ == depth) return methodNode.getInstrs();
      }
    }
    return null;
  }

  public void reset() {
    queue.clear();
  }

  /* Print stack */

  public String print() {
    StringBuilder sb = new StringBuilder();
    sb.append("===== STACK DUMP =====\n");

    if (queue.isEmpty()) {
      sb.append("[Empty Stack]\n");
    } else {
      int i = queue.size();
      for (Quad q : queue) {
        sb.append(q.print(i--));
      }
    }

    sb.append("======================\n");
    sb.append(heap.print());
    StringSaver.save(sb.toString(), "stackDump.txt", "main");
    return sb.toString();
  }
}
