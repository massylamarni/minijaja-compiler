package fr.amvproject.group3.jjc;

import java.util.List;
import java.util.Set;

import fr.amvproject.group3.exceptions.BreakPointHit;
import fr.amvproject.group3.exceptions.PausePointHit;
import fr.amvproject.group3.exceptions.SemanticException;
import fr.amvproject.group3.exceptions.collectors.RuntimeErrorsCollector;
import fr.amvproject.group3.exceptions.collectors.SemanticErrorsCollector;
import fr.amvproject.group3.exceptions.listeners.LexicalErrorsListener;
import fr.amvproject.group3.exceptions.listeners.SyntaxicErrorsListener;
import fr.amvproject.group3.jjc.utils.AstOperations.AstWrapper;
import fr.amvproject.group3.jjc.analyser.SemanticVisitor;
import fr.amvproject.group3.jjc.utils.AstOperations;

public class Interpreter {
  private AstWrapper astWrapper;
  private InterpreterVisitor interpreterVisitor = new InterpreterVisitor();
  private SemanticVisitor semanticVisitor = new SemanticVisitor();
  private Stack stack = interpreterVisitor.getStack();
  private ExecutionContext ctx = interpreterVisitor.getCtx();
  private List<String> outputBuffer = interpreterVisitor.getOutputBuffer();
  private RuntimeErrorsCollector runtimeErrorsCollector = new RuntimeErrorsCollector();
  private SemanticErrorsCollector semanticErrorsCollector = new SemanticErrorsCollector();

  public Stack getStack() {
    return stack;
  }

  public List<String> getOutputBuffer() {
    return outputBuffer;
  }

  public Interpreter(String program) {
    setProgram(program);
  }

  public void setProgram(String program) {
    try {
      astWrapper = AstOperations.getAstFromString(program);
    } catch (Exception e) {
      // TODO: handle exception
    }
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

  public void run() {
    runSemanticAnalysis();
    try {
      disableDebug();
      astWrapper.ast.accept(interpreterVisitor);
    } catch (fr.amvproject.group3.exceptions.RuntimeException e) {
      runtimeErrorsCollector.add(e.getMessage(), e.getLine(), e.getColumn());
    }
    onLastInstruction();
  }

  public void runWithDebug() {
    runSemanticAnalysis();
    try {
      enableDebug();
      partialReset();
      astWrapper.ast.accept(interpreterVisitor);
    } catch (BreakPointHit e) {
      throw e;
    } catch (fr.amvproject.group3.exceptions.RuntimeException e) {
      runtimeErrorsCollector.add(e.getMessage(), e.getLine(), e.getColumn());
    }
    onLastInstruction();
  }

  public void resume() {
    ctx.setIsPaused(false);
    ctx.ignoreNextBreakpoint();
    runWithDebug();
  }

  public void stepForward() {
    try {
      ctx.ignoreNextBreakpoint();
      partialReset();
      astWrapper.ast.accept(interpreterVisitor);
    } catch (PausePointHit e) {
      throw e;
    } catch (fr.amvproject.group3.exceptions.RuntimeException e) {
      runtimeErrorsCollector.add(e.getMessage(), e.getLine(), e.getColumn());
    }
    onLastInstruction();
  }

  public void stepBackward() {
    try {
      ctx.decBreakPointHitLine();
      partialReset();
      interpreterVisitor.resetCurrentAddress();
      astWrapper.ast.accept(interpreterVisitor);
    } catch (PausePointHit e) {
      throw e;
    } catch (fr.amvproject.group3.exceptions.RuntimeException e) {
      runtimeErrorsCollector.add(e.getMessage(), e.getLine(), e.getColumn());
    }
  }

  public void onLastInstruction() {
    if (ctx.isExecutionSuccessful()) reset();
  }

  public Set<Integer> getBreakpoints() {
    return ctx.getBreakpoints();
  }

  public void addBreakPoint(int line) {
    ctx.addBreakPoint(line);
  }

  public void removeBreakpoint(int line) {
    ctx.removeBreakpoint(line);
  }

  public void disableDebug() {
    ctx.disableDebug();
  }

  public void enableDebug() {
    ctx.enableDebug();
  }

  public void partialReset() {
    outputBuffer.clear();
    runtimeErrorsCollector.reset();
    semanticErrorsCollector.reset();
  }

  public void reset() {
    ctx.softReset();
    outputBuffer.clear();
    interpreterVisitor.resetCurrentAddress();
    runtimeErrorsCollector.reset();
    semanticErrorsCollector.reset();
  }
}
