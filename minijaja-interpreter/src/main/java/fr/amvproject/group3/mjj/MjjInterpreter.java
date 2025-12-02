package fr.amvproject.group3.mjj;

import fr.amvproject.group3.mjj.Stack;
import fr.amvproject.group3.mjj.ast.AstNode;
import fr.amvproject.group3.mjj.utils.AstOperations;

public class MjjInterpreter {
  private AstNode ast;
  private InterpreterVisitor interpreterVisitor;
  private Stack stack;
  private ExecutionContext ctx;

  public Stack getStack() {
    return stack;
  }

  public MjjInterpreter(String mjjProgram) {
    ast = AstOperations.getAstFromString(mjjProgram);
    interpreterVisitor = new InterpreterVisitor();
    stack = interpreterVisitor.getStack();
    ctx = interpreterVisitor.getCtx();
  }

  public void run() {
    ast.accept(interpreterVisitor);
    stack.print();
  }

  public void runWithDebug() {
    try {
      ast.accept(interpreterVisitor);
    } catch (Exception e) {
      System.out.println(e);
    }
    stack.print();
  }

  public void resume() {
    try {
      ctx.setIsPaused(false);
      ast.accept(interpreterVisitor);
    } catch (Exception e) {
      System.out.println(e);
    }
    stack.print();
  }

  public void addBreakPoint(int line) {
    ctx.addBreakPoint(line);
  }

  public void removeBreakpoint(int line) {
    ctx.removeBreakpoint(line);
  }

  public void stepForward() {
    ctx.getCurrentNode().accept(interpreterVisitor);
  }

  public void stepBackward() {
    ctx.back();
  }

}
