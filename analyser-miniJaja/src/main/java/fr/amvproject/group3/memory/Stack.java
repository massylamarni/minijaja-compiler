package fr.amvproject.group3.memory;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

import fr.amvproject.group3.ast.nodes.abstracts.ParamsNode;
import fr.amvproject.group3.ast.nodes.abstracts.InstrsNode;
import fr.amvproject.group3.ast.nodes.abstracts.VarsNode;
import fr.amvproject.group3.ast.nodes.decls.VarNode;
import fr.amvproject.group3.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.ast.nodes.finals.ParamNode;
import fr.amvproject.group3.ast.nodes.instrs.ReturnNode;
import fr.amvproject.group3.ast.nodes.intermediate.InstrNode;
import fr.amvproject.group3.ast.nodes.other.TypeNode;

public class Stack {
  static class Quad {
    IdentNode ident;
    Object value;
    String obj; // var, cst, vcst, tab, meth, frame
    TypeNode type; // int, boolean, void.

    // For methods
    List<Quad> params;
    List<Quad> vars;
    InstrsNode instrs;

    Quad(IdentNode ident, Object value, String obj, TypeNode type) {
      this.ident = ident;
      this.value = value;
      this.obj = obj;
      this.type = type;
    }

    Quad(IdentNode ident, Object value, String obj, TypeNode type, List<Quad> params, List<Quad> vars,
        InstrsNode instrs) {
      this.ident = ident;
      this.value = value;
      this.obj = obj;
      this.type = type;
      this.params = params;
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

  public void declVar(IdentNode ident, Object value, TypeNode type, List<Quad> m) {
    for (Quad quad : m) stack.push(quad);
    stack.push(new Quad(ident, value, "var", type));
  }

  public void declCst(IdentNode ident, Object value, TypeNode type, List<Quad> m) {
    String obj = value == null ? "vcst" : "cst"; // Vcst = can be initialized later but not changed after
    for (Quad quad : m) stack.push(quad);
    stack.push(new Quad(ident, value, obj, type));
  }

  public void declTab(IdentNode ident, int size, TypeNode type, List<Quad> m) {
    int ref = heap.allocate(size);
    for (Quad quad : m) stack.push(quad);
    stack.push(new Quad(ident, ref, "tab", type));
  }

  public void declMeth(IdentNode ident, ParamsNode params, TypeNode returnType, List<Quad> m) {
    for (Quad quad : m) stack.push(quad);
    stack.push(new Quad(ident, params, "meth", returnType));
  }

  // --- Access / Assignment ---

  // Retunrs the value associated with the identifier
  public Object val(IdentNode ident) {
    for (Quad q : stack)
      if (q.ident.equals(ident))
        return q.value;
    return null;
  }

  // Assigns a new value to the identifier
  public void affecterVal(IdentNode ident, Object value) {
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
  public void affecterValT(IdentNode ident, int index, Object value) {
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
  public void retirerDecl(IdentNode ident) {
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
  public Object callMethod(IdentNode Ident, List<Object> argValues) {
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
    stack.push(new Quad(new IdentNode("_METHOD_FRAME_", -1), null, "frame", method.type));

    // Initialize params
    if (method.params != null) {
      if (argValues.size() != method.params.size())
        throw new RuntimeException("Parameter count mismatch for method " + Ident.getName());
      for (int i = 0; i < method.params.size(); i++) {
        Quad p = method.params.get(i);
        declVar(p.ident, argValues.get(i), p.type, null);
      }
    }

    // Initialize local variables
    if (method.vars != null) {
      for (Quad v : method.vars)
        declVar(v.ident, v.value, v.type, null);
    }

    // Execute instrs
    Object returnExp = null;
    if (method.instrs != null) {
      for (InstrNode instr : method.instrs.getList()) {
        if (instr instanceof ReturnNode) {
          returnExp = ((ReturnNode) instr).getExp();
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

  // --- Utils ---
  public void printStack() {
    System.out.println("===== STACK DUMP =====");
    if (stack.isEmpty()) {
      System.out.println("[Empty Stack]");
      return;
    }

    int i = stack.size();
    for (Quad q : stack) {
      System.out.println("-- Entry #" + (i--) + " --");
      System.out.println("  Ident : " + (q.ident != null ? q.ident.getName() : "null"));
      System.out.println("  Obj   : " + q.obj);
      System.out.println("  Type  : " + (q.type != null ? q.type.getType() : "null"));

      if (q.obj.equals("tab")) {
        /*
        Object arr = (Object) heap.get((int) q.value);
        System.out.print("  Value : [");
          System.out.print(arr);
        System.out.println("]"); */
      } else if (q.obj.equals("meth")) {
        System.out.println("  Method parameters:");
        if (q.params != null) {
          for (Quad e : q.params) {
            System.out.println("    - " + e.ident.getName() + " : " + e.type.getType());
          }
        }
        System.out.println("  Local vars:");
        if (q.vars != null) {
          for (Quad v : q.vars) {
            System.out.println("    - " + v.ident.getName() + " : " + v.type.getType());
          }
        }
        System.out.println("  Instructions count: " + 
          (q.instrs != null ? q.instrs.getList().size() : 0));
      } else if (q.obj.equals("frame")) {
        System.out.println("  <Method Frame Boundary>");
      } else {
        System.out.println("  Value : " + q.value);
      }
      System.out.println();
    }
    System.out.println("======================");
  }

}
