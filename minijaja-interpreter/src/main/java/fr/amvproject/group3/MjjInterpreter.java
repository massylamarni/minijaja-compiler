package fr.amvproject.group3;

import fr.amvproject.group3.mjj.Stack;
import fr.amvproject.group3.mjj.ast.AstNode;
import fr.amvproject.group3.mjj.utils.AstOperations;

public class MjjInterpreter {
  private AstNode ast;
  private InterpreterVisitor interpreterVisitor;
  private Stack stack;
  private ExecutionContext ctx;

  public MjjInterpreter(String mjjProgram) {
    ast = AstOperations.getAstFromString(mjjProgram);
    interpreterVisitor = new InterpreterVisitor();
    stack = interpreterVisitor.getStack();
    ctx = interpreterVisitor.getCtx();
  }

  public void interpretAll() {
    ast.accept(interpreterVisitor);
    stack.print();
  }

  public void stepForward() {
    if (ctx.isPaused()) ctx.setIsPaused(false);
    ctx.getCurrentNode().accept(interpreterVisitor);
  }

  public void stepBackward() {
    ctx.back();
  }

}
