package fr.amvproject.group3.mjj;

import fr.amvproject.group3.exceptions.SemanticException;
import fr.amvproject.group3.exceptions.collectors.RuntimeErrorsCollector;
import fr.amvproject.group3.exceptions.collectors.SemanticErrorsCollector;
import fr.amvproject.group3.exceptions.listeners.LexicalErrorsListener;
import fr.amvproject.group3.exceptions.listeners.SyntaxicErrorsListener;
import fr.amvproject.group3.mjj.utils.AstOperations.AstWrapper;
import fr.amvproject.group3.mjj.analyser.SemanticVisitor;
import fr.amvproject.group3.mjj.exceptions.BreakPointHit;
import fr.amvproject.group3.mjj.exceptions.PausePointHit;
import fr.amvproject.group3.mjj.utils.AstOperations;

public class Interpreter {
  private AstWrapper astWrapper;
  private InterpreterVisitor interpreterVisitor = new InterpreterVisitor();
  private SemanticVisitor semanticVisitor = new SemanticVisitor();
  private Stack stack = interpreterVisitor.getStack();
  private ExecutionContext ctx = interpreterVisitor.getCtx();
  private RuntimeErrorsCollector runtimeErrorsCollector = new RuntimeErrorsCollector();
  private SemanticErrorsCollector semanticErrorsCollector = new SemanticErrorsCollector();

  public Stack getStack() {
    return stack;
  }

  public Interpreter(String mjjProgram) {
    astWrapper = AstOperations.getAstFromString(mjjProgram);
  }

  public LexicalErrorsListener getLexicalErrorsListener() {
    return astWrapper.lexicalErrorsListener;
  }

  public SyntaxicErrorsListener getSyntaxicErrorsListener() {
    return astWrapper.syntaxicErrorsListener;
  }

  public SemanticErrorsCollector getSemanticErrorsCollector() {
    return semanticErrorsCollector;
  }

  public RuntimeErrorsCollector getRuntimeErrorsCollector() {
    return runtimeErrorsCollector;
  }

  private void runSemanticAnalysis() {
    try {
      astWrapper.ast.accept(semanticVisitor);
    } catch (SemanticException e) {
      semanticErrorsCollector.add(e.getMessage(), e.getLine(), e.getColumn());
    }
  }

  public void restart() {
    ctx.softReset();
    runtimeErrorsCollector = new RuntimeErrorsCollector(); // TODO .reset()
    semanticErrorsCollector = new SemanticErrorsCollector();
  }

  public void run() {
    runSemanticAnalysis();
    try {
      ctx.disableDebug();
      astWrapper.ast.accept(interpreterVisitor);
    } catch (fr.amvproject.group3.exceptions.RuntimeException e) {
      runtimeErrorsCollector.add(e.getMessage(), e.getLine(), e.getColumn());
    }
  }

  public void runWithDebug() {
    runSemanticAnalysis();
    try {
      ctx.enableDebug();
      astWrapper.ast.accept(interpreterVisitor);
    } catch (BreakPointHit e) {
      System.out.println(e);
    } catch (fr.amvproject.group3.exceptions.RuntimeException e) {
      runtimeErrorsCollector.add(e.getMessage(), e.getLine(), e.getColumn());
    }
  }

  public void resume() {
    ctx.setIsPaused(false);
    ctx.ignoreNextBreakpoint();
    runWithDebug();
  }

  public void stepForward() {
    try {
      ctx.ignoreNextBreakpoint();
      astWrapper.ast.accept(interpreterVisitor);
    } catch (PausePointHit e) {
      System.out.println(e);
    } catch (fr.amvproject.group3.exceptions.RuntimeException e) {
      System.out.println(e);
    }
  }

  public void stepBackward() {
    try {
      ctx.decBreakPointHitLine();
      astWrapper.ast.accept(interpreterVisitor);
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
}
