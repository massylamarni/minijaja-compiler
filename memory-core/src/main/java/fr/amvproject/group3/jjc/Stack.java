package fr.amvproject.group3.jjc;

import java.util.List;

import fr.amvproject.group3.jjc.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.jjc.ast.nodes.other.TypeNode;
import fr.amvproject.group3.nodes.ObjNode;
import fr.amvproject.group3.nodes.TypeMemNode;
import fr.amvproject.group3.utils.StringSaver;
import fr.amvproject.group3.Heap;

public class Stack extends StackManager {
  private List<Quad> queue = super.getQueue();
  private Heap heap = new Heap();

  public Heap getHeap() {
    return heap;
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

  // --- Variable / Constant / Array / Method Declarations ---

  public void declVar(IdentNode ident, Object value, TypeNode type) {
    this.push(new Quad(ident, value, new ObjNode("var"), type));
  }

  public void declCst(IdentNode ident, Object value, TypeNode type) {
    // Vcst = can be initialized later but not changed after
    this.push(new Quad(ident, value, new ObjNode(value == null ? "vcst" : "cst"), type));
  }

  public void declTab(IdentNode ident, int size, TypeNode type) {
    int ref = heap.allocate(size, typeMapper(type));
    this.push(new Quad(ident, ref, new ObjNode("tab"), type));
  }

  // --- Access / Assignment ---

  // Replaces the Quad at stackDepth
  public void identVal(IdentNode ident, TypeNode type, Object newValue, int stackDepth) {
    int index = queue.size() - 1 - stackDepth;
    if (index < 0 || index >= queue.size())
      return;

    Quad replaced = new Quad(ident, newValue, new ObjNode("var"), type);

    queue.set(index, replaced);
  }

  // Remove Declaration from Stack
  public void retirerDecl(IdentNode ident) {
    for (int i = queue.size() - 1; i >= 0; i--) {
      Quad quad = queue.get(i);
      if (quad.getIdent().equals(ident)) {
        if (quad.getObj().isTab())
          heap.remove((int) quad.getValue(), typeMapper(quad.getType()));
        queue.remove(i);
        return;
      }
    }
  }

  // Return value of identifier
  public Object val(IdentNode ident) {
    for (Quad quad : queue) {
      if (quad.getIdent().equals(ident))
        return quad.getValue();
    }
    return null;
  }

  // Return value of identifier (array)
  public Object valT(IdentNode ident, int index) {
    for (Quad quad : queue) {
      if (quad.getIdent().equals(ident))
        return heap.get((int) quad.getValue() + index);
    }
    return null;
  }

  // Return length of array, long is reserved word
  public Integer longT(IdentNode ident) {
    for (Quad quad : queue) {
      if (quad.getIdent().equals(ident))
        return (int) heap.get((int) quad.getValue() - 1);
    }
    return null;
  }

  // Assigns a new value to the identifier
  public void affecterVal(IdentNode ident, Object value) {
    for (int i = queue.size() - 1; i >= 0; i--) {
      Quad quad = queue.get(i);
      if (quad.getIdent().equals(ident)) {
        if (quad.getObj().isVcst()) {
          queue.set(i, new Quad(ident, value, new ObjNode("cst"), quad.getType()));
          return;
        } else if (quad.getObj().isTab()) {
          int ref = heap.allocate((int) value, typeMapper(quad.getType())); // AjouterRef(v, t)
          queue.set(i, new Quad(ident, ref, new ObjNode("tab"), quad.getType()));
          heap.remove((int) quad.getValue(), typeMapper(quad.getType())); // RetirerTas
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
  public void affecterValT(IdentNode ident, int index, Object value) {
    for (Quad quad : queue) {
      if (quad.getIdent().equals(ident))
        heap.set((int) quad.getValue() + index, value);
    }
  }

  // Assigns a new type to an identifier
  public void affecterType(IdentNode ident, TypeNode type) {
    for (int i = queue.size() - 1; i >= 0; i--) {
      Quad quad = queue.get(i);
      if (quad.getIdent().equals(ident)) {
        queue.set(i, new Quad(ident, quad.getValue(), quad.getObj(), type));
      }
    }
  }

  // Return object type of identifier
  public ObjNode objet(IdentNode ident) {
    for (Quad quad : queue) {
      if (quad.getIdent().equals(ident))
        return quad.getObj();
    }
    return null;
  }

  // Return type of identifier
  public Object sorte(IdentNode ident) {
    for (Quad quad : queue) {
      if (quad.getIdent().equals(ident))
        return quad.getType();
    }
    return null;
  }

  /* Helper */
  private TypeMemNode typeMapper(TypeNode node) {
    if (node.isInt()) return new TypeMemNode("int");
    if (node.isBoolean()) return new TypeMemNode("boolean");
    if (node.isString()) return new TypeMemNode("string");
    return new TypeMemNode("void");
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

    StringSaver.save(sb.toString(), "stackDump.txt", "main");
    return sb.toString();
  }
}
