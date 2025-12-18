package fr.amvproject.group3.mjj;

import java.util.ArrayDeque;
import java.util.Deque;

import fr.amvproject.group3.mjj.ast.nodes.abstracts.InstrsNode;
import fr.amvproject.group3.mjj.ast.nodes.abstracts.ParamsNode;
import fr.amvproject.group3.mjj.ast.nodes.abstracts.VarsNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.IdentNode;

public class ScopeManager {
  private Deque<Stack> stackOfStacks = new ArrayDeque<>();
  private Deque<Stack> stackOfExitedStacks = new ArrayDeque<>();
  private Stack stackRef;
  private int scopeDepth = 0;

  public Stack getStackRef() {
    return stackRef;
  }

  public int getScopeDepth() {
    return scopeDepth;
  }

  public ScopeManager(Stack stack) {
    this.stackRef = stack;
    stackOfStacks.push(stack);
  }

  public Stack newScope() {
    Stack newStack = new Stack();
    stackOfStacks.push(newStack);
    stackRef = newStack;
    scopeDepth++;
    return stackRef;
  }

  public Stack backToScope() {
    stackOfStacks.push(stackOfExitedStacks.pop());
    stackRef = stackOfStacks.peek();
    scopeDepth++;
    return stackRef;
  }

  public Stack exitScope() {
    stackOfExitedStacks.push(stackOfStacks.pop());
    stackRef = stackOfStacks.peek();
    scopeDepth--;
    return stackRef;
  }

  public Object val(IdentNode identNode) {
    for (Stack stack : stackOfStacks) {
      Object value = stack.val(identNode);
      if (value != null) return value;
    }
    return null;
  }

  public Object valT(IdentNode identNode, int index) {
    for (Stack stack : stackOfStacks) {
      Object value = stack.valT(identNode, index);
      if (value != null) return value;
    }
    return null;
  }

  public Object longT(IdentNode identNode) {
    for (Stack stack : stackOfStacks) {
      Object value = stack.longT(identNode);
      if (value != null) return value;
    }
    return null;
  }

  public ParamsNode parametre(IdentNode identNode, int depth) {
    for (Stack stack : stackOfStacks) {
      ParamsNode value = stack.parametre(identNode, depth);
      if (value != null) return value;
    }
    return null;
  }

  public VarsNode declaration(IdentNode identNode, int depth) {
    for (Stack stack : stackOfStacks) {
      VarsNode value = stack.declaration(identNode, depth);
      if (value != null) return value;
    }
    return null;
  }

  public InstrsNode corps(IdentNode identNode, int depth) {
    for (Stack stack : stackOfStacks) {
      InstrsNode value = stack.corps(identNode, depth);
      if (value != null) return value;
    }
    return null;
  }

  public void affecterVal(IdentNode identNode, Object value) {
    for (Stack stack : stackOfStacks) {
      if (stack.val(identNode) != null) {
        stack.affecterVal(identNode, value);
      }
    }
  }

  public void affecterValT(IdentNode identNode, int index, Object value) {
    for (Stack stack : stackOfStacks) {
      if (stack.val(identNode) != null) {
        stack.affecterValT(identNode, index, value);
      }
    }
  }  

  public void affecterValClassQuad(Object value) {
    stackOfStacks.peekLast().affecterVal(getClassQuad().getIdent(), value);
  }

  public Object valClassQuad() {
    return getClassQuad().getValue();
  }

  public Quad getClassQuad() {
    return stackOfStacks.peekLast().getClassQuad();
  }

  public void reset() {
    scopeDepth = 0;
    stackRef = stackOfStacks.peekLast();
    while (stackOfStacks.size() > 1) stackOfStacks.pop();
  }
}
