package fr.amvproject.group3;

import java.util.HashSet;
import java.util.Set;

import fr.amvproject.group3.mjj.Stack;
import fr.amvproject.group3.mjj.ast.AstNode;

public class ExecutionContext {
    private AstNode currentNode;
    private AstNode previousNode;
    private Stack stack;
    private boolean isPaused = false;
    private Set<Integer> breakpoints = new HashSet<>();
    private int currentInstrNum = 0;
    private int currentLineNum = 0;

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

    public int getCurrentInstrNum() {
      return currentInstrNum;
    }

    public int getCurrentLineNum() {
      return currentLineNum;
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

    public boolean hasBreakpoint() {
        return breakpoints.contains(currentLineNum);
    }

    public void incInstrNum() {
        currentInstrNum++;
    }

    public void decInstrNum() {
        currentInstrNum--;
    }

    public void incLineNum() {
        currentLineNum++;
    }

    public void decLineNum() {
        currentLineNum--;
    }

    /* Advanced */

    public void visitNode(AstNode node) {
        setPreviousNode(currentNode);
        currentNode = node;
        incInstrNum();
        if (hasBreakpoint()) setIsPaused(true);
    }

    public void back() {
        currentNode = previousNode;
        currentInstrNum--;
        stack.goBackward();
    }

}
