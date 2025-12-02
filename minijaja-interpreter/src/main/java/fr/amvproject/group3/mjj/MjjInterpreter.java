package fr.amvproject.group3.mjj;

import fr.amvproject.group3.exceptions.SemanticException;
import fr.amvproject.group3.exceptions.collectors.RuntimeErrorsCollector;
import fr.amvproject.group3.exceptions.collectors.SemanticErrorsCollector;
import fr.amvproject.group3.mjj.analyser.SemanticVisitor;
import fr.amvproject.group3.mjj.ast.AstNode;
import fr.amvproject.group3.mjj.exceptions.BreakPointHit;
import fr.amvproject.group3.mjj.exceptions.PausePointHit;
import fr.amvproject.group3.mjj.utils.AstOperations;

public class MjjInterpreter {
  private AstNode ast;
  private InterpreterVisitor interpreterVisitor = new InterpreterVisitor();
  private SemanticVisitor semanticVisitor = new SemanticVisitor();
  private Stack stack = interpreterVisitor.getStack();
  private ExecutionContext ctx = interpreterVisitor.getCtx();
  private RuntimeErrorsCollector runtimeErrorsCollector;
  private SemanticErrorsCollector semanticErrorsCollector;

  public Stack getStack() {
    return stack;
  }

  public MjjInterpreter(String mjjProgram) {
    ast = AstOperations.getAstFromString(mjjProgram);
  }

  public SemanticErrorsCollector getSemanticErrorsCollector() {
    return semanticErrorsCollector;
  }

  public RuntimeErrorsCollector getRuntimeErrorsCollector() {
    return runtimeErrorsCollector;
  }

  private void runSemanticAnalysis() {
    try {
      ast.accept(semanticVisitor);
    } catch (SemanticException e) {
      semanticErrorsCollector.add(e.getMessage(), e.getLine(), e.getColumn());
    }
  }

  public void run() {
    runSemanticAnalysis();
    try {
      ctx.disableDebug();
      ast.accept(interpreterVisitor);
    } catch (fr.amvproject.group3.exceptions.RuntimeException e) {
      runtimeErrorsCollector.add(e.getMessage(), e.getLine(), e.getColumn());
    }
  }

  public void runWithDebug() {
    runSemanticAnalysis();
    try {
      ctx.enableDebug();
      ast.accept(interpreterVisitor);
    } catch (BreakPointHit e) {
      System.out.println(e);
    } catch (fr.amvproject.group3.exceptions.RuntimeException e) {
      runtimeErrorsCollector.add(e.getMessage(), e.getLine(), e.getColumn());
    }
  }

  public void resume() {
    ctx.setIsPaused(false);
    runWithDebug();
  }

  public void stepForward() {
    try {
      ast.accept(interpreterVisitor);
    } catch (PausePointHit e) {
      System.out.println(e);
    } catch (fr.amvproject.group3.exceptions.RuntimeException e) {
      System.out.println(e);
    }
  }

  public void addBreakPoint(int line) {
    ctx.addBreakPoint(line);
  }

  public void removeBreakpoint(int line) {
    ctx.removeBreakpoint(line);
  }

  public void stepBackward() {
    ctx.back();
  }
}
