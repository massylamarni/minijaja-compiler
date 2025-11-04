package fr.amvproject.group3.memory;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import fr.amvproject.group3.miniJaja.ast.nodes.abstracts.ParamsNode;
import fr.amvproject.group3.miniJaja.ast.nodes.abstracts.VarsNode;
import fr.amvproject.group3.miniJaja.ast.nodes.decls.CstNode;
import fr.amvproject.group3.miniJaja.ast.nodes.decls.MethodNode;
import fr.amvproject.group3.miniJaja.ast.nodes.decls.TableNode;
import fr.amvproject.group3.miniJaja.ast.nodes.decls.VarNode;
import fr.amvproject.group3.miniJaja.ast.nodes.abstracts.InstrsNode;
import fr.amvproject.group3.miniJaja.ast.nodes.abstracts.ListExpNode;
import fr.amvproject.group3.miniJaja.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.miniJaja.ast.nodes.finals.ParamNode;
import fr.amvproject.group3.miniJaja.ast.nodes.intermediate.ExpNode;
import fr.amvproject.group3.miniJaja.ast.nodes.intermediate.VarDeclNode;
import fr.amvproject.group3.miniJaja.ast.nodes.other.TypeMethNode;
import fr.amvproject.group3.utils.StringSaver;

public class Stack {
  public static class Quad {
    public IdentNode ident;
    public Object value;
    public String obj; // var, cst, vcst, tab, meth, frame
    public TypeMethNode type; // int, boolean, void.

    public Quad(IdentNode ident, Object value, String obj, TypeMethNode type) {
      this.ident = ident;
      this.value = value;
      this.obj = obj;
      this.type = type;
    }
  }

  private Deque<Quad> queue = new ArrayDeque<>();
  private Heap heap = new Heap();

    // --- Stack Operations ---

  public void swap() {
    Quad first = queue.pop();
    Quad second = queue.pop();
    queue.push(first);
    queue.push(second);
  }

  public Quad pop() {
    return queue.pop();
  }

    // --- Variable / Constant / Array / Method Declarations ---

  public void declVar(IdentNode ident, Object value, TypeMethNode type) {
    queue.push(new Quad(ident, value, "var", type));
  }

  public void declCst(IdentNode ident, Object value, TypeMethNode type) {
    // Vcst = can be initialized later but not changed after
    queue.push(new Quad(ident, value, value == null ? "vcst" : "cst", type));
  }

  public void declTab(IdentNode ident, int size, TypeMethNode type) {
    int ref = heap.allocate(size, type);
    queue.push(new Quad(ident, ref, "tab", type));
  }

  public void declMeth(IdentNode ident, ParamsNode params, TypeMethNode returnType) {
    queue.push(new Quad(ident, params, "meth", returnType));
  }

    // --- Access / Assignment ---

  // Replaces the Quad at stackDepth
  public void identVal(IdentNode ident, TypeMethNode type, Quad quad, int stackDepth) {
    if (quad == null || stackDepth < 0)
      return;
    Quad q = queue.pop();
    if (stackDepth == 0) {
      queue.push(new Quad(ident, quad.value, "var", type));
    } else {
      identVal(ident, type, q, stackDepth - 1);
      queue.push(q);
    }
  }

  // Remove Declaration from Stack
  public void retirerDecl(IdentNode ident, Quad quad) {
    if (quad == null)
      return;
    Quad q = queue.pop();
    if (quad.ident.equals(ident)) {
      if (quad.obj.equals("tab"))
        heap.remove((int) quad.value, quad.type);
    } else {
      retirerDecl(ident, q);
      queue.push(q);
    }

  }

  // Return value of identifier
  public Object val(IdentNode ident, Quad quad) {
    if (quad == null)
      return null;
    // First check the quad parameter itself
    if (quad.ident.equals(ident)) {
      return quad.value;
    }
    // Then search in the queue
    if (queue.isEmpty())
      return null;
    Quad q = queue.pop();
    Object result = val(ident, q);
    queue.push(q);
    return result;
  }

  // Return value of identifier (array)
  public Object valT(IdentNode ident, int index, Quad quad) {
    if (quad == null)
      return null;
    if (queue.isEmpty())
      return null;
    Quad q = queue.pop();
    if (quad.ident.equals(ident)) {
      return heap.get((int) quad.value);
    }
    valT(ident, index, q);
    queue.push(q);
    return null;
  }

  // Return length of array, long is reserved word
  public Integer longT(IdentNode ident, Quad quad) {
    if (quad == null)
      return null;
    if (queue.isEmpty())
      return null;
    Quad q = queue.pop();
    if (quad.ident.equals(ident)) {
      return (int) heap.get((int) quad.value - 1);
    }
    longT(ident, q);
    queue.push(q);
    return null;
  }

  // Assigns a new value to the identifier
  public void affecterVal(IdentNode ident, Object value, Quad quad) {
    // First check if the quad parameter matches
    if (quad != null && quad.ident.equals(ident)) {
      // Don't modify, just return - the quad is not in the stack yet
      return;
    }

    if (queue.isEmpty())
      return;
    Quad q = queue.pop();
    if (!q.ident.equals(ident)) {
      affecterVal(ident, value, q);
      queue.push(q);
    } else if (q.obj.equals("vcst")) {
      queue.push(new Quad(ident, value, "cst", q.type));
    } else if (q.obj.equals("tab")) {
      int ref = heap.allocate((int) value, q.type); // AjouterRef(v, t)
      queue.push(new Quad(ident, ref, "tab", q.type));
      heap.remove((int) q.value, q.type); // RetirerTas
    } else if (q.obj.equals("cst")) {
      queue.push(q);
    } else {
      queue.push(new Quad(ident, value, q.obj, q.type));
    }
  }

  // Assigns a new value to an array at a specific index
  public void affecterValT(IdentNode ident, int index, Object value, Quad quad) {
    // First check if the quad parameter matches
    if (quad != null && quad.ident.equals(ident)) {
      if (quad.obj.equals("tab")) {
        heap.set((int) quad.value, index, value);
      }
      return;
    }

    if (queue.isEmpty())
      return;
    Quad q = queue.pop();
    if (!q.ident.equals(ident)) {
      affecterValT(ident, index, value, q);
      queue.push(q);
    } else {
      if (q.obj.equals("tab")) {
        heap.set((int) q.value, index, value);
      }
      queue.push(q);
    }
  }

  // Assigns a new type to an identifier
  public void affecterType(IdentNode ident, TypeMethNode type, Quad quad) {
    if (queue.isEmpty())
      return;
    Quad q = queue.pop();
    if (quad.ident.equals(ident)) {
      queue.push(new Quad(ident, quad.value, quad.obj, type));
      queue.push(q);  // Push back q
    } else {
      affecterType(ident, type, q);
      queue.push(q);  // Push back q after recursion
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
      declVar(param.getIdent(), exp, new TypeMethNode(param.getType().getName()));
    }
  }

  // Return object type of identifier
  public String objet(IdentNode ident, Quad quad) {
    if (quad == null)
      return null;
    if (quad.ident.equals(ident))
      return quad.obj;
    if (queue.isEmpty())
      return null;
    Quad q = queue.pop();
    String obj = objet(ident, q);
    queue.push(q);
    return obj;
  }

  // Return type of identifier
  public TypeMethNode sorte(IdentNode ident, Quad quad) {
    if (quad == null)
      return null;
    if (quad.ident.equals(ident))
      return quad.type;
    if (queue.isEmpty())
      return null;
    Quad q = queue.pop();
    TypeMethNode type = sorte(ident, q);
    queue.push(q);
    return type;
  }

  // Return params of method
  public ParamsNode parametre(IdentNode ident, Quad quad) {
    if (quad == null)
      return null;
    if (!quad.ident.equals(ident)) {
      if (queue.isEmpty())
        return null;
      Quad q = queue.pop();
      ParamsNode params = parametre(ident, q);
      queue.push(q);
      return params;
    } else if (quad.obj.equals("meth") && (quad.value instanceof MethodNode methodNode)
        && methodNode.getIdent().sameAs(ident) && methodNode.getReturnType().sameAs(quad.type)) {
      return methodNode.getParams();
    }
    return null;
  }

  // Return declarations of method
  public VarsNode declaration(IdentNode ident, Quad quad) {
    if (quad == null)
      return null;
    if (!quad.ident.equals(ident)) {
      if (queue.isEmpty())
        return null;
      Quad q = queue.pop();
      VarsNode vars = declaration(ident, q);
      queue.push(q);
      return vars;
    } else if (quad.obj.equals("meth") && (quad.value instanceof MethodNode methodNode)
        && methodNode.getIdent().sameAs(ident) && methodNode.getReturnType().sameAs(quad.type)) {
      return methodNode.getVars();
    }
    return null;
  }

  // Return instructions of method
  public InstrsNode corps(IdentNode ident, Quad quad) {
    if (quad == null)
      return null;
    if (!quad.ident.equals(ident)) {
      if (queue.isEmpty())
        return null;
      Quad q = queue.pop();
      InstrsNode intrs = corps(ident, q);
      queue.push(q);
      return intrs;
    } else if (quad.obj.equals("meth") && (quad.value instanceof MethodNode methodNode)
        && methodNode.getIdent().sameAs(ident) && methodNode.getReturnType().sameAs(quad.type)) {
      return methodNode.getInstrs();
    }
    return null;
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
        appendQuad(sb, q, i--);
      }
    }

    sb.append("======================\n");

    return sb.toString();
  }

  private void appendQuad(StringBuilder sb, Quad q, int index) {
    sb.append("-- Entry #").append(index).append(" --\n");
    sb.append("  Ident : ").append(q.ident != null ? q.ident.getName() : "null").append("\n");
    sb.append("  Obj   : ").append(q.obj).append("\n");
    sb.append("  Type  : ").append(q.type != null ? q.type.getType() : "null").append("\n");

    switch (q.obj) {
      case "tab" -> appendTab(sb, q);
      case "meth" -> appendMethod(sb, q);
      case "frame" -> sb.append("  <Method Frame Boundary>\n");
      default -> sb.append("  Value : ").append(q.value).append("\n");
    }
    sb.append("\n");
  }

  private void appendTab(StringBuilder sb, Quad q) {
    Object arr = heap.get((int) q.value);
    sb.append("  Value : [").append(arr).append("]\n");
  }

  private String getTypeNameHelper(VarDeclNode v) {
    if (v instanceof TableNode tableNode) return tableNode.getType().getName();
    if (v instanceof VarNode varNode) return varNode.getType().getName();
    if (v instanceof CstNode cstNode) return cstNode.getType().getName();
    return null;
  }

  private void appendMethod(StringBuilder sb, Quad q) {
    sb.append("  Method parameters:\n");
    if (q.value instanceof ParamsNode params) {
      for (ParamNode p : params.getList()) {
        sb.append("    - ").append(p.getIdent().getName())
            .append(" : ").append(p.getType().getType()).append("\n");
      }
    }

    sb.append("  Local vars:\n");
    if (q.value instanceof MethodNode methodNode) {
      VarsNode vars = methodNode.getVars();
      if (vars != null) {
        for (VarDeclNode v : vars.getList()) {
          sb.append("    - ").append(v.getIdent().getName())
              .append(" : ").append(getTypeNameHelper(v)).append("\n");
        }
      }

      sb.append("  Instructions count: ")
          .append(methodNode.getInstrs() != null ? methodNode.getInstrs().getList().size() : 0)
          .append("\n");
    }
  }

  // --- Getters for inspection ---

  public Deque<Quad> getQueue() {
    return queue;
  }

  public Heap getHeap() {
    return heap;
  }

  /* Quick Print */
  public String prettyPrint() {
    StringBuilder sb = new StringBuilder();

    sb.append("\n=========================\n");
    sb.append("      MINIJAJA STACK     \n");
    sb.append("=========================\n\n");

    if (queue.isEmpty()) {
        sb.append("[ EMPTY STACK ]\n");
        return sb.toString();
    }

    int pos = queue.size();
    for (Quad q : queue) {
        sb.append("• Entry ").append(pos--).append("\n");
        sb.append("  ├─ Identifier : ").append(q.ident != null ? q.ident.getName() : "<null>").append("\n");
        sb.append("  ├─ Kind       : ").append(q.obj).append("\n");
        sb.append("  ├─ Type       : ").append(q.type != null ? q.type.getType() : "<none>").append("\n");

        switch (q.obj) {
            case "var", "cst", "vcst" -> {
                sb.append("  └─ Value      : ").append(q.value).append("\n");
            }

            case "tab" -> {
                sb.append("  └─ Array      : ").append(formatArray(q)).append("\n");
            }

            case "meth" -> {
                sb.append("  └─ Method:\n");
                appendMethodPretty(sb, q);
            }

            case "frame" -> {
                sb.append("  └─ <FRAME-BORDER>\n");
            }

            default -> {
                sb.append("  └─ Value      : ").append(q.value).append("\n");
            }
        }

        sb.append("\n");
    }

    sb.append("=========================\n");

    return sb.toString();
}

private String formatArray(Quad q) {
    int ref = (int) q.value;
    Object arr = heap.get(ref);

    if (arr == null) return "<null>";

    // heap.get(ref) might return an Object[]
    if (arr instanceof Object[] array) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]);
            if (i < array.length - 1) sb.append(", ");
        }
        return sb.append("]").toString();
    }

    return arr.toString();
}

private void appendMethodPretty(StringBuilder sb, Quad q) {
    if (!(q.value instanceof MethodNode method)) {
        sb.append("     <Invalid method data>\n");
        return;
    }

    // Parameters
    sb.append("     Parameters:\n");
    if (method.getParams() != null) {
        for (ParamNode p : method.getParams().getList()) {
            sb.append("       - ")
              .append(p.getIdent().getName())
              .append(" : ")
              .append(p.getType().getType())
              .append("\n");
        }
    } else {
        sb.append("       <none>\n");
    }

    // Local vars
    sb.append("     Locals:\n");
    if (method.getVars() != null) {
        for (VarDeclNode v : method.getVars().getList()) {
            sb.append("       - ")
              .append(v.getIdent().getName())
              .append(" : ")
              .append(getTypeNameHelper(v))
              .append("\n");
        }
    } else {
        sb.append("       <none>\n");
    }

    // Instruction count
    int count = method.getInstrs() != null
              ? method.getInstrs().getList().size()
              : 0;

    sb.append("     Instructions: ").append(count).append("\n");
}


}
