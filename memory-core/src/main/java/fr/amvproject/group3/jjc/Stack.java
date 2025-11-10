package fr.amvproject.group3.jjc;

import java.util.ArrayDeque;
import java.util.Deque;

import fr.amvproject.group3.Heap;
import fr.amvproject.group3.jjc.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.jjc.ast.nodes.other.TypeNode;
import fr.amvproject.group3.nodes.ObjNode;
import fr.amvproject.group3.nodes.TypeMemNode;
import fr.amvproject.group3.utils.StringSaver;

public class Stack {
    private Deque<Quad> queue = new ArrayDeque<>();
    private Heap heap = new Heap();

    public Heap getHeap() {
      return heap;
    }

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

    public void declVar(IdentNode ident, Object value, TypeNode type) {
        queue.push(new Quad(ident, value, new ObjNode("var"), type));
    }

    public void declCst(IdentNode ident, Object value, TypeNode type) {
        // Vcst = can be initialized later but not changed after
        queue.push(new Quad(ident, value, new ObjNode(value == null ? "vcst" : "cst"), type));
    }

    public Deque<Quad> getQueue() {
        return queue;
    }

    public void declTab(IdentNode ident, int size, TypeNode type) {
        int ref = heap.allocate(size, new TypeMemNode(type.getName()));
        queue.push(new Quad(ident, ref, new ObjNode("tab"), type));
    }

    // --- Access / Assignment ---

    // Replaces the Quad at stackDepth
    public void identVal(IdentNode ident, TypeNode type, Quad quad, int stackDepth) {
        if (quad == null || stackDepth < 0)
            return;
        Quad q = queue.pop();
        if (stackDepth == 0) {
            queue.push(new Quad(ident, quad.getValue(), new ObjNode("var"), type));
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
        if (quad.getIdent().sameAs(ident)) {
            if (quad.getObj().isTab())
                heap.remove((int) quad.getValue(), new TypeMemNode((quad.getType()).getName()));
        } else {
            retirerDecl(ident, q);
            queue.push(q);
        }

    }

    // Return value of identifier
    public Object val(IdentNode ident, Quad quad) {
        if (quad == null)
            return null;
        if (quad.getIdent().sameAs(ident)) {
            queue.push(quad);
            return quad.getValue();
        }
        Quad q = queue.pop();
        Object resultat = val(ident, q);
        queue.push(quad);
        return resultat;
    }

    // Return value of identifier (array)
    public Object valT(IdentNode ident, int index, Quad quad) {
        if (quad == null)
            return null;
        Quad q = queue.pop();
        if (quad.getIdent().sameAs(ident)) {
            return heap.get((int) quad.getValue());
        }
        Object resultat = valT(ident, index, q);
        queue.push(q);
        return resultat;
    }

    // Return length of array, long is reserved word
    public Integer longT(IdentNode ident, Quad quad) {
        if (quad == null)
            return null;
        Quad q = queue.pop();
        if (quad.getIdent().sameAs(ident)) {
            return (int) heap.get((int) quad.getValue() - 1);
        }
        int res = longT(ident, q);
        queue.push(q);
        return res;
    }

    // Assigns a new value to the identifier
    public void affecterVal(IdentNode ident, Object value, Quad quad) {
        Quad q = queue.pop();
        if (!quad.getIdent().sameAs(ident))
            affecterVal(ident, value, q);
        else if (quad.getObj().isVcst()) {
            queue.push(new Quad(ident, value, new ObjNode("cst"), quad.getType()));
            return;
        } else if (quad.getObj().isTab()) {
            int ref = heap.allocate((int) value, new TypeMemNode((quad.getType()).getName())); // AjouterRef(v, t)
            queue.push(new Quad(ident, ref, new ObjNode("tab"), quad.getType()));
            heap.remove((int) quad.getValue(), new TypeMemNode((quad.getType()).getName())); // RetirerTas
            return;
        } else if (quad.getObj().isCst()) {
            return;
        }
        queue.push(new Quad(ident, value, quad.getObj(), quad.getType()));
    }

    // Assigns a new value to an array at a specific index
    public void affecterValT(IdentNode ident, int index, Object value, Quad quad) {
        Quad q = queue.pop();
        if (!quad.getIdent().sameAs(ident))
            affecterValT(ident, index, value, q);
        else
            heap.set((int) quad.getValue(), index, value);
    }

    // Assigns a new type to an identifier
    public void affecterType(IdentNode ident, TypeNode type, Quad quad) {
        Quad q = queue.pop();
        if (quad.getIdent().sameAs(ident))
            queue.push(new Quad(ident, quad.getValue(), quad.getObj(), type));
        else {
            affecterType(ident, type, q);
        }
    }

    // Return object type of identifier
    public ObjNode objet(IdentNode ident, Quad quad) {
        if (quad == null)
            return null;
        if (quad.getIdent().sameAs(ident))
            return quad.getObj();

        Quad q = queue.pop();
        ObjNode obj = objet(ident, q);
        queue.push(q);
        return obj;
    }

    // Return type of identifier
    public TypeNode sorte(IdentNode ident, Quad quad) {
        if (quad == null)
            return null;
        if (quad.getIdent().sameAs(ident)) {
            queue.push(quad);
            return quad.getType();
        }
        Quad q = queue.pop();
        TypeNode type = sorte(ident, q);
        queue.push(quad);
        return type;
    }

    /* Print stack */

    public void print() {
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

        StringSaver.save(sb.toString(), "stackDump.txt", "main");
    }

    private void appendQuad(StringBuilder sb, Quad q, int index) {
        sb.append("-- Entry #").append(index).append(" --\n");
        sb.append("  Ident : ").append(q.getIdent().getName()).append("\n");
        sb.append("  Obj   : ").append(q.getObj().getName()).append("\n");
        sb.append("  Type  : ").append((q.getType()).getName()).append("\n");
    }
}
