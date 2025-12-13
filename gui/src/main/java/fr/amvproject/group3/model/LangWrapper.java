package fr.amvproject.group3.model;

import java.util.Set;

import fr.amvproject.group3.Compiler;
import fr.amvproject.group3.exceptions.BreakPointHit;
import fr.amvproject.group3.exceptions.PausePointHit;
import fr.amvproject.group3.mjj.Interpreter;
import fr.amvproject.group3.ui.memory.HeapView;
import fr.amvproject.group3.ui.memory.StackView;
import fr.amvproject.group3.ui.terminal.nodes.TerminalBodyView;
import fr.amvproject.group3.ui.workbench.nodes.EditorView;
import fr.amvproject.group3.utils.EditorUtils;

public class LangWrapper {
  private String mjjProgram = """
        /********************************************/
        /* Programme MiniJaja                       */
        /* Validation de toutes les instructions	*/
        /* sans tableau                             */
        /********************************************/

        class C {
          int nt[3] ;
          int i = 0 ;

          main {
            while (3 > i) {
              nt[i] = i * 10 ;
              i++ ;
            };
            write(nt[2]);
          }
        }
      """;
  private String jjcProgram = """
        1 init
        2 push(0)
        3 new(x,entier,var,0)
        4 push(7)
        5 new(f,entier,meth,0)
        6 goto(11)
        7 new(p,entier,var,1)
        8 load(p) // val. résultat en sommet de pile
        9 swap // adr retour ↔ val. résultat
        10 return
        11 push(3)
        12 store(x)
        13 load(x)
        14 invoke(f)
        15 swap // retrait du paramètre
        16 pop
        17 inc(x)
        18 push(0) //valeur pour les méthodes void
        19 swap // retrait de f
        20 pop
        21 swap // retrait de x
        22 pop
        23 pop // retrait 0 du main
        24 jcstop
      """;
  private Compiler compiler = new Compiler(mjjProgram);
  private Interpreter mjjInterpreter = new Interpreter(mjjProgram);
  private fr.amvproject.group3.jjc.Interpreter jjcInterpreter = new fr.amvproject.group3.jjc.Interpreter(jjcProgram);
  private int mjjBreakpointHitLine = -1;
  private int jjcBreakpointHitLine = -1;
  private int mjjPreviousBreakpointHitLine = mjjBreakpointHitLine;
  private int jjcPreviousBreakpointHitLine = jjcBreakpointHitLine;
  private EditorView mjjEditorView = null;
  private EditorView jjcEditorView = null;
  private StackView stackView = null;
  private HeapView heapView = null;
  private TerminalBodyView terminalBodyView = null;
  public boolean isMjj = true;
  public ToggleStates toggleStates = new ToggleStates();

  public String getProgram() {
    return isMjj ? mjjProgram : jjcProgram;
  }

  public EditorView getEditorView() {
    return isMjj ? mjjEditorView : jjcEditorView;
  }

  public StackView getStackView() {
    return stackView;
  }

  public HeapView getHeapView() {
    return heapView;
  }

  public TerminalBodyView getTerminalBodyView() {
    return terminalBodyView;
  }

  public void setEditorView(EditorView editorView) {
    if (isMjj) {
      mjjEditorView = editorView;
    } else {
      jjcEditorView = editorView;
    }
  }

  public void setStackView(StackView stackView) {
    this.stackView = stackView;
  }

  public void setHeapView(HeapView heapView) {
    this.heapView = heapView;
  }

  public void setTerminalBodyView(TerminalBodyView terminalBodyView) {
    this.terminalBodyView = terminalBodyView;
  }

  public void updateProgram(String program) {
    if (isMjj) {
      mjjInterpreter.setProgram(program);
      compiler.setProgram(program);
    } else {
      jjcInterpreter.setProgram(program);
    }
  }

  public void setBreakPointHit(int line) {
    if (isMjj) {
      mjjPreviousBreakpointHitLine = mjjBreakpointHitLine;
      mjjBreakpointHitLine = line;
      getStackView().buildMjjStack(mjjInterpreter.getStack());
      getHeapView().buildHeap(mjjInterpreter.getStack().getHeap());
      getTerminalBodyView().addOutput("Breakpoint hit at line " + line);
      mjjInterpreter.getStack().reset();
      mjjInterpreter.getStack().getHeap().reset();
    } else {
      jjcPreviousBreakpointHitLine = jjcBreakpointHitLine;
      jjcBreakpointHitLine = line;
      getStackView().buildJjcStack(jjcInterpreter.getStack());
      getHeapView().buildHeap(jjcInterpreter.getStack().getHeap());
      jjcInterpreter.getStack().reset();
      jjcInterpreter.getStack().getHeap().reset();
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

  public String getCompiledProgram() {
    compiler.compile();
    return compiler.getCompiledProgram();
  }

  public void restart() {
    try {
      if (isMjj) mjjInterpreter.restart();
      else jjcInterpreter.restart();
      EditorUtils.resetLineHighlighting(getEditorView());
      runWithDebug();
    } catch (BreakPointHit bph) {
      setBreakPointHit(bph.getLine());
      EditorUtils.highlightLine(this, getEditorView());
    } catch (PausePointHit pph) {
      setBreakPointHit(pph.getLine());
      EditorUtils.highlightLine(this, getEditorView());
    }
  }

  public void run() {
    try {
      if (isMjj) mjjInterpreter.run();
      else jjcInterpreter.run();
    } catch (BreakPointHit bph) {
      setBreakPointHit(bph.getLine());
      EditorUtils.highlightLine(this, getEditorView());
    } catch (PausePointHit pph) {
      setBreakPointHit(pph.getLine());
      EditorUtils.highlightLine(this, getEditorView());
    }
  }

  public void runWithDebug() {
    try {
      if (isMjj) mjjInterpreter.runWithDebug();
      else jjcInterpreter.runWithDebug();
    } catch (BreakPointHit bph) {
      setBreakPointHit(bph.getLine());
      EditorUtils.highlightLine(this, getEditorView());
    } catch (PausePointHit pph) {
      setBreakPointHit(pph.getLine());
      EditorUtils.highlightLine(this, getEditorView());
    }
  }

  public void resume() {
    try {
      if (isMjj) mjjInterpreter.resume();
      else jjcInterpreter.resume();
    } catch (BreakPointHit bph) {
      setBreakPointHit(bph.getLine());
      EditorUtils.highlightLine(this, getEditorView());
    } catch (PausePointHit pph) {
      setBreakPointHit(pph.getLine());
      EditorUtils.highlightLine(this, getEditorView());
    }
  }

  public void stepForward() {
    try {
      if (isMjj) mjjInterpreter.stepForward();
      else jjcInterpreter.stepForward();
    } catch (BreakPointHit bph) {
      setBreakPointHit(bph.getLine());
      EditorUtils.highlightLine(this, getEditorView());
    } catch (PausePointHit pph) {
      setBreakPointHit(pph.getLine());
      EditorUtils.highlightLine(this, getEditorView());
    }
  }

  public void stepBackward() {
    try {
      if (isMjj) mjjInterpreter.stepBackward();
      else jjcInterpreter.stepBackward();
    } catch (BreakPointHit bph) {
      setBreakPointHit(bph.getLine());
      EditorUtils.highlightLine(this, getEditorView());
    } catch (PausePointHit pph) {
      setBreakPointHit(pph.getLine());
      EditorUtils.highlightLine(this, getEditorView());
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
}
