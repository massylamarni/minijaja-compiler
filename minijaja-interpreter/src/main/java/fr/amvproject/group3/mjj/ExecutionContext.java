package fr.amvproject.group3.mjj;

import java.util.HashSet;
import java.util.Set;

import fr.amvproject.group3.mjj.Stack;
import fr.amvproject.group3.mjj.ast.AstNode;
import fr.amvproject.group3.mjj.exceptions.BreakpointHit;

public class ExecutionContext {
    private AstNode currentNode;
    private AstNode previousNode;
    private Stack stack;
    private boolean isPaused = false;
    private Set<Integer> breakpoints = new HashSet<>();
    private int breakPointHitLine = 0;

    public ExecutionContext(Stack stack) {
        this.stack = stack;
    }

    public AstNode getCurrentNode() {
      return currentNode;
    }

    public AstNode getPreviousNode() {
      return previousNode;
    }

    public Stack getStack() {
      return stack;
    }

    public Set<Integer> getBreakpoints() {
      return breakpoints;
    }

    public boolean isPaused() {
      return isPaused;
    }

    public void setCurrentNode(AstNode node) {
        currentNode = node;
    }

    public void setPreviousNode(AstNode node) {
        previousNode = node;
    }

    public void setIsPaused(boolean isPaused) {
        this.isPaused = isPaused;
    }

    public void addBreakPoint(int line) {
        breakpoints.add(line);
    }

    public void removeBreakpoint(int line) {
      breakpoints.remove(line);
    }

    public boolean hasBreakpoint() {
        return breakpoints.contains(currentNode.getLine());
    }

    public void resetPosition() {
      currentNode = null;
      previousNode = null;
    }

    /* Advanced */

    public void recordNode(AstNode node) {
      if (node.getLine() >= breakPointHitLine) {
        setPreviousNode(currentNode);
        currentNode = node;
        if (node.getLine() == breakPointHitLine) {
          breakPointHitLine = 0;
          return;
        }
        breakPointHitLine = 0;
        if (hasBreakpoint()) {
          setIsPaused(true);
          breakPointHitLine = node.getLine();
          throw new BreakpointHit(currentNode);
        }
      }
    }

    public void back() {
        currentNode = previousNode;
        stack.goBackward();
    }

}
