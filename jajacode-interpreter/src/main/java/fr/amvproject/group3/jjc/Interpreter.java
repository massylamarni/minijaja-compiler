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
  private ExecutionContext ctx = interpreterVisitor.getCtx();
  private List<String> outputBuffer = interpreterVisitor.getOutputBuffer();
  private RuntimeErrorsCollector runtimeErrorsCollector = new RuntimeErrorsCollector();
  private SemanticErrorsCollector semanticErrorsCollector = new SemanticErrorsCollector();
  private BreakPointHit breakPointHit = null;
  private PausePointHit pausePointHit = null;

  public Stack getStack() {
    return interpreterVisitor.getStack();
  }
  
  public SemanticVisitor getSemanticVisitor() {
    return semanticVisitor;
  }

  public List<String> getOutputBuffer() {
    return outputBuffer;
  }

  public BreakPointHit getBreakPointHit() {
    BreakPointHit hit = breakPointHit;
    breakPointHit = null;
    return hit;
  }

  public PausePointHit getPausePointHit() {
    PausePointHit hit = pausePointHit;
    pausePointHit = null;
    return hit;
  }

  public Interpreter(String program) {
    setProgram(program);
  }

  public void setProgram(String program) {
    AstWrapper newAstWrapper = AstOperations.getAstFromString(program);
    astWrapper = newAstWrapper.ast != null ? newAstWrapper : new AstWrapper(astWrapper.ast, newAstWrapper.lexicalErrorsListener, newAstWrapper.syntaxicErrorsListener);
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

  public boolean runSemanticAnalysis() {
    try {
      astWrapper.ast.accept(semanticVisitor);
      return true;
    } catch (SemanticException e) {
      semanticErrorsCollector.add(e.getMessage(), e.getLine(), e.getColumn());
      return false;
    }
  }

  public void run() {
    reset();
    if (!runSemanticAnalysis()) return;
    ctx.disableDebug();
    try {
      astWrapper.ast.accept(interpreterVisitor);
    } catch (fr.amvproject.group3.exceptions.RuntimeException e) {
      runtimeErrorsCollector.add(e.getMessage(), e.getLine(), e.getColumn());
    }
  }

  public void runWithDebug() {
    reset();
    if (!runSemanticAnalysis()) return;
    ctx.enableDebug();
    try {
      astWrapper.ast.accept(interpreterVisitor);
    } catch (BreakPointHit e) {
      breakPointHit = e;
    } catch (fr.amvproject.group3.exceptions.RuntimeException e) {
      runtimeErrorsCollector.add(e.getMessage(), e.getLine(), e.getColumn());
    }
  }

  public void resume() {
    ctx.setIsPaused(false);
    if (ctx.isExecutionSuccessful()) reset();
    else partialReset();
    try {
      astWrapper.ast.accept(interpreterVisitor);
    } catch (BreakPointHit e) {
      breakPointHit = e;
    } catch (fr.amvproject.group3.exceptions.RuntimeException e) {
      runtimeErrorsCollector.add(e.getMessage(), e.getLine(), e.getColumn());
    }
  }

  public void stepForward() {
    if (ctx.isExecutionSuccessful()) reset();
    else partialReset();
    try {
      astWrapper.ast.accept(interpreterVisitor);
    } catch (PausePointHit e) {
      pausePointHit = e;
    } catch (fr.amvproject.group3.exceptions.RuntimeException e) {
      runtimeErrorsCollector.add(e.getMessage(), e.getLine(), e.getColumn());
    }
  }

  public void stepBackward() {
    if (ctx.isExecutionSuccessful()) reset();
    else partialReset();
    ctx.decrInstrCountUntilPreviousInstr();
    interpreterVisitor.resetCurrentAddress();
    try {
      astWrapper.ast.accept(interpreterVisitor);
    } catch (PausePointHit e) {
      pausePointHit = e;
    } catch (fr.amvproject.group3.exceptions.RuntimeException e) {
      runtimeErrorsCollector.add(e.getMessage(), e.getLine(), e.getColumn());
    }
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
    ctx.resetMemory();
    ctx.resetInstrCounter();
    interpreterVisitor.resetCurrentAddress();
  }

  public void reset() {
    ctx.softReset();
    ctx.resetMemory();
    outputBuffer.clear();
    runtimeErrorsCollector.reset();
    semanticErrorsCollector.reset();
    interpreterVisitor.resetCurrentAddress();
    breakPointHit = null;
    pausePointHit = null;
  }
}
