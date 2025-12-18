package fr.amvproject.group3.model.wrappers;

import java.util.List;
import java.util.Set;

import fr.amvproject.group3.exceptions.BreakPointHit;
import fr.amvproject.group3.exceptions.PausePointHit;
import fr.amvproject.group3.mjj.Interpreter;
import fr.amvproject.group3.settings.DefaultPrograms;
import fr.amvproject.group3.utils.EditorUtils;

public class InterpreterWrapper {

  private ViewsWrapper viewsWrapper;
  private Interpreter mjjInterpreter = new Interpreter(DefaultPrograms.mjjProgram);
  private fr.amvproject.group3.jjc.Interpreter jjcInterpreter = new fr.amvproject.group3.jjc.Interpreter(DefaultPrograms.jjcProgram);
  private int mjjBreakpointHitLine = -1;
  private int jjcBreakpointHitLine = -1;
  private int mjjPreviousBreakpointHitLine = mjjBreakpointHitLine;
  private int jjcPreviousBreakpointHitLine = jjcBreakpointHitLine;
  private boolean isMjj = true;

  public InterpreterWrapper(ViewsWrapper viewsWrapper) {
    this.viewsWrapper = viewsWrapper;
  }

  public void setIsMjj(boolean isMjj) {
    this.isMjj = isMjj;
  }

  public List<Integer> getExploredLines() {
    if (isMjj) {
      return mjjInterpreter.getSemanticVisitor().getExploredLines();
    } else {
      return jjcInterpreter.getSemanticVisitor().getExploredLines();
    }
  }

  public void runSemanticAnalysis() {
    if (isMjj) {
      mjjInterpreter.runSemanticAnalysis();
    } else {
      jjcInterpreter.runSemanticAnalysis();
    }
  }

  public void restart() {
    if (isMjj) mjjInterpreter.reset();
    else jjcInterpreter.reset();
    EditorUtils.resetLineHighlighting(viewsWrapper.getEditorView());
    runWithDebug();
  }

  public void run() {
    if (isMjj) mjjInterpreter.run();
    else jjcInterpreter.run();
    if (!checkForExceptions()) onEof();
  }

  public void runWithDebug() {
    viewsWrapper.getToggleStates().showDebugToolBar();
    if (isMjj) mjjInterpreter.runWithDebug();
    else jjcInterpreter.runWithDebug();
    if (!checkForExceptions()) onEof();
  }

  public void resume() {
    if (isMjj) mjjInterpreter.resume();
    else jjcInterpreter.resume();
    if (!checkForExceptions()) onEof();
  }

  public void stepForward() {
    if (isMjj) mjjInterpreter.stepForward();
    else jjcInterpreter.stepForward();
    if (!checkForExceptions()) onEof();
  }

  public void stepBackward() {
    if (isMjj) mjjInterpreter.stepBackward();
    else jjcInterpreter.stepBackward();
    if (!checkForExceptions()) onEof();
  }

  public Set<Integer> getBreakpoints() {
    return isMjj ? mjjInterpreter.getBreakpoints() : jjcInterpreter.getBreakpoints();
  }

  public void addBreakPoint(int line) {
    if (isMjj) mjjInterpreter.addBreakPoint(line);
    else jjcInterpreter.addBreakPoint(line);
  }

  public void removeBreakpoint(int line) {
    if (isMjj) mjjInterpreter.removeBreakpoint(line);
    else jjcInterpreter.removeBreakpoint(line);
  }

  public void updateProgram(String program) {
    if (isMjj) {
      mjjInterpreter.setProgram(program);
    } else {
      jjcInterpreter.setProgram(program);
    }
  }

  public void setBreakPointHit(int line) {
    viewsWrapper.getToggleStates().openLeftSideBar();
    if (isMjj) {
      mjjPreviousBreakpointHitLine = mjjBreakpointHitLine;
      mjjBreakpointHitLine = line;
      updateMemoryViews();
      EditorUtils.highlightLine(this, viewsWrapper.getEditorView());
    } else {
      jjcPreviousBreakpointHitLine = jjcBreakpointHitLine;
      jjcBreakpointHitLine = line;
      updateMemoryViews();
      EditorUtils.highlightLine(this, viewsWrapper.getEditorView());
    }
  }

  public int getBreakpointHitLine() {
    return isMjj ? mjjBreakpointHitLine : jjcBreakpointHitLine;
  }

  public int getPreviousBreakpointHitLine() {
    return isMjj ? mjjPreviousBreakpointHitLine : jjcPreviousBreakpointHitLine;
  }

  public Object getInterpreter() {
    return isMjj ? mjjInterpreter : jjcInterpreter;
  }

  public void updateMemoryViews() {
    if (isMjj) {
      viewsWrapper.getStackView().buildMjjStack(mjjInterpreter.getStack());
      viewsWrapper.getHeapView().buildHeap(mjjInterpreter.getStack().getHeap());
    } else {
      viewsWrapper.getStackView().buildJjcStack(jjcInterpreter.getStack());
      viewsWrapper.getHeapView().buildHeap(jjcInterpreter.getStack().getHeap());
    }
  }

  public void removeAllBreakpoints() {
    if (isMjj) {
      mjjInterpreter.getBreakpoints().clear();
    } else {
      jjcInterpreter.getBreakpoints().clear();
    }
  }

  private boolean printErrors() {
    boolean hasErrors = false;
    if (isMjj) {
      if (mjjInterpreter.getLexicalErrorsListener().hasErrors()) {
        viewsWrapper.writeInTerminal(mjjInterpreter.getLexicalErrorsListener().toMessage(), false, true);
        hasErrors = true;
      }
      if (mjjInterpreter.getSyntaxicErrorsListener().hasErrors()) {
        viewsWrapper.writeInTerminal(mjjInterpreter.getSyntaxicErrorsListener().toMessage(), false, true);
        hasErrors = true;
      }
      if (mjjInterpreter.getRuntimeErrorsCollector().hasErrors()) {
        viewsWrapper.writeInTerminal(mjjInterpreter.getRuntimeErrorsCollector().toMessage(), false, true);
        hasErrors = true;
      }
      if (mjjInterpreter.getSemanticErrorsCollector().hasErrors()) {
        viewsWrapper.writeInTerminal(mjjInterpreter.getSemanticErrorsCollector().toMessage(), false, true);
        hasErrors = true;
      }
    } else {
      if (jjcInterpreter.getLexicalErrorsListener().hasErrors()) {
        viewsWrapper.writeInTerminal(jjcInterpreter.getLexicalErrorsListener().toMessage(), false, true);
        hasErrors = true;
      }
      if (jjcInterpreter.getSyntaxicErrorsListener().hasErrors()) {
        viewsWrapper.writeInTerminal(jjcInterpreter.getSyntaxicErrorsListener().toMessage(), false, true);
        hasErrors = true;
      }
      if (jjcInterpreter.getRuntimeErrorsCollector().hasErrors()) {
        hasErrors = true;
        viewsWrapper.writeInTerminal(jjcInterpreter.getRuntimeErrorsCollector().toMessage(), false, true);
      }
      if (jjcInterpreter.getSemanticErrorsCollector().hasErrors()) {
        viewsWrapper.writeInTerminal(jjcInterpreter.getSemanticErrorsCollector().toMessage(), false, true);
        hasErrors = true;
      }
    }
    return hasErrors;
  }

  private void onEof() {
    if (isMjj) {
      if (mjjInterpreter.getOutputBuffer().size() != 0) {
        for (String output : mjjInterpreter.getOutputBuffer()) {
          boolean isLn = output.endsWith("\n");
          if (isLn && output.length() >= 1) output = output.substring(0, output.length() - 1);
          viewsWrapper.writeInTerminal(output, false, isLn);
        }
      }
    } else {
      if (jjcInterpreter.getOutputBuffer().size() != 0) {
        for (String output : jjcInterpreter.getOutputBuffer()) {
          boolean isLn = output.endsWith("\n");
          if (isLn && output.length() >= 1) output = output.substring(0, output.length() - 1);
          viewsWrapper.writeInTerminal(output, false, isLn);
        }
      }
    }
    viewsWrapper.setProgramSuccess("\nProgram executed successfully !");
  }

  private boolean checkForExceptions() {
    boolean hasException = false;
    if (printErrors()) {
      viewsWrapper.setProgramFailed("\nProgram terminated with errors !");
      return true;
    }
    if (isMjj) {
      BreakPointHit breakPointHit = mjjInterpreter.getBreakPointHit();
      PausePointHit pausePointHit = mjjInterpreter.getPausePointHit();
      if (breakPointHit != null) {
        setBreakPointHit(breakPointHit.getLine());
        hasException = true;
      } else if (pausePointHit != null) {
        setBreakPointHit(pausePointHit.getLine());
        hasException = true;
      }
    } else {
      BreakPointHit breakPointHit = jjcInterpreter.getBreakPointHit();
      PausePointHit pausePointHit = jjcInterpreter.getPausePointHit();
      if (breakPointHit != null) {
        setBreakPointHit(breakPointHit.getLine());
        hasException = true;
      } else if (pausePointHit != null) {
        setBreakPointHit(pausePointHit.getLine());
        hasException = true;
      }
    }
    return hasException;
  }

  public void reset() {
    mjjBreakpointHitLine = -1;
    jjcBreakpointHitLine = -1;
    mjjPreviousBreakpointHitLine = mjjBreakpointHitLine;
    jjcPreviousBreakpointHitLine = jjcBreakpointHitLine;
    mjjInterpreter.reset();
    jjcInterpreter.reset();
  }
}
