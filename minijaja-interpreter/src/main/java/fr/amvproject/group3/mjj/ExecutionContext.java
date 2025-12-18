package fr.amvproject.group3.mjj;

import java.util.HashSet;
import java.util.Set;

import fr.amvproject.group3.exceptions.BreakPointHit;
import fr.amvproject.group3.exceptions.PausePointHit;
import fr.amvproject.group3.mjj.ast.AstNode;

public class ExecutionContext {
    private AstNode currentNode;
    private AstNode previousNode;
    private Stack stack;
    private boolean isPaused = false;
    private Set<Integer> breakpoints = new HashSet<>();
    private int breakPointHitLine = 0;
    private int previouslyHitBreakpointLine = -1;
    private boolean ignoreNextBreakpoint = false;
    private boolean ignoreBreakpoints = true;
    private boolean isExecutionSuccessful = false;

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

    public boolean isExecutionSuccessful() {
      return isExecutionSuccessful;
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

    public void setIsExecutionSuccessful(boolean isExecutionSuccessful) {
      this.isExecutionSuccessful = isExecutionSuccessful;
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

    public void ignoreNextBreakpoint() {
      ignoreNextBreakpoint = true;
    }

    public void decBreakPointHitLine() {
      if (previousNode != null && breakPointHitLine != 0) {
        breakPointHitLine = previousNode.getLine();
      }
    }

    public void resetMemory() {
      stack.reset();
      stack.getHeap().reset();
    }

    public void softReset() {
      currentNode = null;
      previousNode = null;
      resetMemory();
      isPaused = false;
      breakPointHitLine = 0;
      previouslyHitBreakpointLine = -1;
      isExecutionSuccessful = false;
    }

    /* Advanced */

    public void recordNode(AstNode node) {
      setPreviousNode(currentNode);
      currentNode = node;
      if (node.getLine() >= breakPointHitLine) {
        if (ignoreNextBreakpoint) {
          ignoreNextBreakpoint = false;
          return;
        }
        breakPointHitLine = 0;
        if (isPaused()) {
          breakPointHitLine = node.getLine();
          throw new PausePointHit(currentNode.getClass().getName(), currentNode.getLine(), currentNode.getColumn());
        }
        if (hasBreakpoint() && !ignoreBreakpoints && node.getLine() != previouslyHitBreakpointLine) {
          setIsPaused(true);
          breakPointHitLine = node.getLine();
          previouslyHitBreakpointLine = node.getLine();
          throw new BreakPointHit(currentNode.getClass().getName(), currentNode.getLine(), currentNode.getColumn());
        }
      }
    }

    public void back() {
        currentNode = previousNode;
        stack.goBackward();
    }

    public void enableDebug() {
      ignoreBreakpoints = false;
    }

    public void disableDebug() {
      ignoreBreakpoints = true;
    }

}
