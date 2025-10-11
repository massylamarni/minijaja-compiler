package fr.amvproject.group3.memory;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

import fr.amvproject.group3.ast.nodes.abstracts.Ident;
import fr.amvproject.group3.ast.nodes.instrs.InstrNode;
import fr.amvproject.group3.ast.nodes.instrs.InstrsNode;
import fr.amvproject.group3.ast.nodes.instrs.RetourNode;
import fr.amvproject.group3.ast.nodes.other.TypeNode;

public class Stack {
  static class Quad {
    Ident ident;
    Object value;
    String obj; // var, cst, vcst, tab, meth, frame
    TypeNode type; // int, boolean, void.

    // For methods
    List<Quad> entetes;
    List<Quad> vars;
    InstrsNode instrs;

    Quad(Ident ident, Object value, String obj, TypeNode type) {
      this.ident = ident;
      this.value = value;
      this.obj = obj;
      this.type = type;
    }

    Quad(Ident ident, Object value, String obj, TypeNode type, List<Quad> entetes, List<Quad> vars,
        InstrsNode instrs) {
      this.ident = ident;
      this.value = value;
      this.obj = obj;
      this.type = type;
      this.entetes = entetes;
      this.vars = vars;
      this.instrs = instrs;
    }
  }

  private Deque<Quad> stack = new ArrayDeque<>();
  private Heap heap = new Heap();

  // --- Stack Operations ---

  public void swap() {
    Quad first = stack.pop();
    Quad second = stack.pop();
    stack.push(first);
    stack.push(second);
  }

  // --- Variable / Constant / Array / Method Declarations ---

  public void declVar(Ident ident, Object value, TypeNode type) {
    stack.push(new Quad(ident, value, "var", type));
  }

  public void declCst(Ident ident, Object value, TypeNode type, boolean isVcst) {
    String obj = isVcst ? "vcst" : "cst"; // Vcst = variable constant, can be initialized later but not changed after
    stack.push(new Quad(ident, value, obj, type));
  }

  public void declTab(Ident ident, Object[] values, TypeNode type) {
    int ref = heap.allocate(values);
    stack.push(new Quad(ident, ref, "tab", type));
  }

  public void declMeth(Ident ident, TypeNode returnType, List<Quad> entetes, List<Quad> vars, InstrsNode instrs) {
    stack.push(new Quad(ident, null, "meth", returnType, entetes, vars, instrs));
  }

  // --- Access / Assignment ---

  // Retunrs the value associated with the identifier
  public Object val(Ident ident) {
    for (Quad q : stack)
      if (q.ident.equals(ident))
        return q.value;
    return null;
  }

  // Assigns a new value to the identifier
  public void affecterVal(Ident ident, Object value) {
    for (Quad q : stack) {
      if (q.ident.equals(ident)) {
        switch (q.obj) {
          case "vcst":
            q.obj = "cst";
            break;
          case "cst":
            return;
          case "tab":
            throw new RuntimeException("Use affecterValT for arrays");
          default:
            q.value = value;
        }
        return;
      }
    }
    throw new RuntimeException("Identifier not found: " + ident.getName());
  }

  // Assigns a new value to an array at a specific index
  public void affecterValT(Ident ident, int index, Object value) {
    for (Quad q : stack) {
      if (q.ident.equals(ident) && q.obj.equals("tab")) {
        Object[] arr = (Object[]) heap.get((int) q.value);
        if (index < 0 || index >= arr.length)
          throw new IndexOutOfBoundsException();
        arr[index] = value;
        heap.set((int) q.value, arr);
        return;
      }
    }
    throw new RuntimeException("Array not found: " + ident.getName());
  }

  // Remove Declaration from Stack
  public void retirerDecl(Ident ident) {
    Iterator<Quad> it = stack.iterator();
    while (it.hasNext()) {
      Quad q = it.next();
      if (q.ident.equals(ident)) {
        it.remove();
        break;
      }
    }
  }

  // --- Method Call Execution ---

  public Object callMethod(Ident Ident, List<Object> argValues) {
    Quad method = null;
    for (Quad q : stack) {
      if (q.ident.equals(Ident) && q.obj.equals("meth")) {
        method = q;
        break;
      }
    }
    if (method == null)
      throw new RuntimeException("Method not found: " + Ident.getName());

    // Save current stack
    stack.push(new Quad(new Ident("_METHOD_FRAME_"), null, "frame", method.type));

    // Initialize entetes
    if (method.entetes != null) {
      if (argValues.size() != method.entetes.size())
        throw new RuntimeException("Parameter count mismatch for method " + Ident.getName());
      for (int i = 0; i < method.entetes.size(); i++) {
        Quad p = method.entetes.get(i);
        declVar(p.ident, argValues.get(i), p.type);
      }
    }

    // Initialize local variables
    if (method.vars != null) {
      for (Quad v : method.vars)
        declVar(v.ident, v.value, v.type);
    }

    // Execute instrs
    Object returnExp = null;
    if (method.instrs != null) {
      for (InstrNode instr : method.instrs.getList()) {
        if (instr instanceof RetourNode) {
          returnExp = ((RetourNode) instr).getExp();
          break;
        }
        //instr.execute(this);
      }
    }

    // Remove frame and vars
    while (!stack.isEmpty()) {
      Quad top = stack.pop();
      if (top.obj.equals("frame"))
        break;
    }

    return returnExp;
  }
}
