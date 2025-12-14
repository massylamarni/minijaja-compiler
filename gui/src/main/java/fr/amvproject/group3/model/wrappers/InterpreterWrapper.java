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
    printErrors();
  }

  public void restart() {
    try {
      if (isMjj) mjjInterpreter.reset();
      else jjcInterpreter.reset();
      EditorUtils.resetLineHighlighting(viewsWrapper.getEditorView());
      runWithDebug();
    } catch (BreakPointHit bph) {
      setBreakPointHit(bph.getLine());
    } catch (PausePointHit pph) {
      setBreakPointHit(pph.getLine());
    }
  }

  public void run() {
    try {
      if (isMjj) mjjInterpreter.run();
      else jjcInterpreter.run();
      onEof();
    } catch (BreakPointHit bph) {
      setBreakPointHit(bph.getLine());
    } catch (PausePointHit pph) {
      setBreakPointHit(pph.getLine());
    }
  }

  public void runWithDebug() {
    try {
      viewsWrapper.getToggleStates().showDebugToolBar();
      if (isMjj) mjjInterpreter.runWithDebug();
      else jjcInterpreter.runWithDebug();
      onEof();
    } catch (BreakPointHit bph) {
      setBreakPointHit(bph.getLine());
    } catch (PausePointHit pph) {
      setBreakPointHit(pph.getLine());
    }
  }

  public void resume() {
    try {
      if (isMjj) mjjInterpreter.resume();
      else jjcInterpreter.resume();
      onEof();
    } catch (BreakPointHit bph) {
      setBreakPointHit(bph.getLine());
    } catch (PausePointHit pph) {
      setBreakPointHit(pph.getLine());
    }
  }

  public void stepForward() {
    try {
      if (isMjj) mjjInterpreter.stepForward();
      else jjcInterpreter.stepForward();
      onEof();
    } catch (BreakPointHit bph) {
      setBreakPointHit(bph.getLine());
    } catch (PausePointHit pph) {
      setBreakPointHit(pph.getLine());
    }
  }

  public void stepBackward() {
    try {
      if (isMjj) mjjInterpreter.stepBackward();
      else jjcInterpreter.stepBackward();
    } catch (BreakPointHit bph) {
      setBreakPointHit(bph.getLine());
    } catch (PausePointHit pph) {
      setBreakPointHit(pph.getLine());
    }
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
      viewsWrapper.writeInTerminal("Breakpoint hit at line " + line, false);
      updateMemoryViews();
      EditorUtils.highlightLine(this, viewsWrapper.getEditorView());
    } else {
      jjcPreviousBreakpointHitLine = jjcBreakpointHitLine;
      jjcBreakpointHitLine = line;
      viewsWrapper.writeInTerminal("Breakpoint hit at line " + line, false);
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
        viewsWrapper.writeInTerminal(mjjInterpreter.getLexicalErrorsListener().toMessage(), false);
        hasErrors = true;
      }
      if (mjjInterpreter.getSyntaxicErrorsListener().hasErrors()) {
        viewsWrapper.writeInTerminal(mjjInterpreter.getSyntaxicErrorsListener().toMessage(), false);
        hasErrors = true;
      }
      if (mjjInterpreter.getRuntimeErrorsCollector().hasErrors()) {
        viewsWrapper.writeInTerminal(mjjInterpreter.getRuntimeErrorsCollector().toMessage(), false);
        hasErrors = true;
      }
      if (mjjInterpreter.getSemanticErrorsCollector().hasErrors()) {
        viewsWrapper.writeInTerminal(mjjInterpreter.getSemanticErrorsCollector().toMessage(), false);
        hasErrors = true;
      }
    } else {
      if (jjcInterpreter.getLexicalErrorsListener().hasErrors()) {
        viewsWrapper.writeInTerminal(jjcInterpreter.getLexicalErrorsListener().toMessage(), false);
        hasErrors = true;
      }
      if (jjcInterpreter.getSyntaxicErrorsListener().hasErrors()) {
        viewsWrapper.writeInTerminal(jjcInterpreter.getSyntaxicErrorsListener().toMessage(), false);
        hasErrors = true;
      }
      if (jjcInterpreter.getRuntimeErrorsCollector().hasErrors()) {
        hasErrors = true;
        viewsWrapper.writeInTerminal(jjcInterpreter.getRuntimeErrorsCollector().toMessage(), false);
      }
      if (jjcInterpreter.getSemanticErrorsCollector().hasErrors()) {
        viewsWrapper.writeInTerminal(jjcInterpreter.getSemanticErrorsCollector().toMessage(), false);
        hasErrors = true;
      }
    }
    return hasErrors;
  }

  private void onEof() {
    if (isMjj) {
      if (mjjInterpreter.getOutputBuffer().size() != 0) {
        for (String output : mjjInterpreter.getOutputBuffer()) {
          viewsWrapper.writeInTerminal(output, false);
        }
      }
      if (!printErrors()) viewsWrapper.setProgramSuccess("Program executed successfully !");
      else viewsWrapper.setProgramFailed("Program terminated with errors !");
    } else {
      if (jjcInterpreter.getOutputBuffer().size() != 0) {
        for (String output : jjcInterpreter.getOutputBuffer()) {
          viewsWrapper.writeInTerminal(output, false);
        }
      }
      if (!printErrors()) viewsWrapper.setProgramSuccess("Program executed successfully !");
      else viewsWrapper.setProgramFailed("Program terminated with errors !");
    }
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
