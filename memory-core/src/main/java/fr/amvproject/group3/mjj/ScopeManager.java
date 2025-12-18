package fr.amvproject.group3.mjj;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

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
    System.out.println("newScope, " + "ss: " + stackOfStacks.size() + ", es: " + stackOfExitedStacks.size());
    Stack newStack = new Stack();
    stackOfStacks.push(newStack);
    stackRef = newStack;
    scopeDepth++;
    return stackRef;
  }

  public Stack backToScope() {
    System.out.println("backToScope " + "ss: " + stackOfStacks.size() + ", es: " + stackOfExitedStacks.size());
    stackOfStacks.push(stackOfExitedStacks.pop());
    stackRef = stackOfStacks.peek();
    scopeDepth++;
    return stackRef;
  }

  public Stack exitScope() {
    System.out.println("exitScope" + "ss: " + stackOfStacks.size() + ", es: " + stackOfExitedStacks.size());
    stackOfExitedStacks.push(stackOfStacks.pop());
    stackRef = stackOfStacks.peek();
    scopeDepth--;
    return stackRef;
  }

  public Stack deleteScope() {
    System.out.println("deleteScope" + "ss: " + stackOfStacks.size() + ", es: " + stackOfExitedStacks.size());
    stackOfStacks.pop();
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

  public Stack getFlatStack() {
    Stack flatStack = new Stack();
    List<Object> flatHeapQueue = new ArrayList<>();

    for (Iterator<Stack> it = stackOfStacks.descendingIterator(); it.hasNext();) {
      Stack stack = it.next();
      List<Quad> queue = stack.getQueue();
      for (int i = 0; i < queue.size(); i++) {
        flatStack.push(queue.get(i));
      }
      for (int i = 0; i < stack.getHeap().getQueue().size(); i++) {
        flatHeapQueue.add(stack.getHeap().getQueue().get(i));
      }
    }
    flatStack.getHeap().setQueue(flatHeapQueue);
    return flatStack;
  }

  public void reset() {
    scopeDepth = 0;
    stackRef = stackOfStacks.peekLast();
    while (stackOfStacks.size() > 1) stackOfStacks.pop();
  }

  public void printScopes() {
    int scopeIndex = 0;
    for (Iterator<Stack> it = stackOfStacks.descendingIterator(); it.hasNext();) {
      System.out.println("--- Current Scopes --- stack: " + scopeIndex);
      Stack stack = it.next();
      System.out.println("Scope " + scopeIndex + " (depth " + (stackOfStacks.size() - 1 - scopeIndex) + "):");
      printStack(stack);
      scopeIndex++;
    }
    if (!stackOfExitedStacks.isEmpty()) {
      int exitedIndex = 0;
      for (Stack stack : stackOfExitedStacks) {
        System.out.println("--- Exited Scopes --- stack: " + exitedIndex);
        printStack(stack);
        exitedIndex++;
      }
    }
    System.out.println("-----------------------------------------");
  }

  private void printStack(Stack stack) {
    List<Quad> queue = stack.getQueue();
    for (int i = 0; i < queue.size(); i++) {
      System.out.println(queue.get(i).print(i));
    }
  }
}
