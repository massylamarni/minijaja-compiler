package fr.amvproject.group3.model;

import java.util.Set;

import fr.amvproject.group3.exceptions.BreakPointHit;
import fr.amvproject.group3.exceptions.PausePointHit;
import fr.amvproject.group3.mjj.Interpreter;
import fr.amvproject.group3.ui.workbench.nodes.EditorView;
import fr.amvproject.group3.utils.EditorUtils;

public class LangWrapper {
  private String mjjProgram = """
        /****************************************/
        /* Programme MiniJaja			*/
        /* Validation des appels recursifs	*/
        /****************************************/

        class factorielle{ 
            int x=7;
            
            void pause(){
          int i;};
            
            int fact(int x){
                int retour;
                if (x==0) {
                    retour=1;
                }else{
                    retour=x*fact(x-1);
                };
                return retour;
            };
            
            main {
                int res=fact(x);
                write ("x(5040) = ");
            writeln(res);
                pause(); // res = 5040
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
  private Interpreter mjjInterpreter = new Interpreter(mjjProgram);
  private fr.amvproject.group3.jjc.Interpreter jjcInterpreter = new fr.amvproject.group3.jjc.Interpreter(jjcProgram);
  private int mjjBreakpointHitLine = -1;
  private int jjcBreakpointHitLine = -1;
  private int mjjPreviousBreakpointHitLine = mjjBreakpointHitLine;
  private int jjcPreviousBreakpointHitLine = jjcBreakpointHitLine;
  public boolean isMjj = true;
  public EditorView editorView = null;

  public String getProgram() {
    return isMjj ? mjjProgram : jjcProgram;
  }

  public void updateProgram(String program) {
    if (isMjj) {
      mjjProgram = program;
    } else {
      jjcProgram = program;
    }
  }

  public void setBreakPointHit(int line) {
    if (isMjj) {
      mjjPreviousBreakpointHitLine = mjjBreakpointHitLine;
      mjjBreakpointHitLine = line;
    } else {
      jjcPreviousBreakpointHitLine = jjcBreakpointHitLine;
      jjcBreakpointHitLine = line;
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

  public void restart() {
    try {
      if (isMjj) mjjInterpreter.restart();
      else jjcInterpreter.restart();
      EditorUtils.resetLineHighlighting(editorView);
      runWithDebug();
    } catch (BreakPointHit bph) {
      setBreakPointHit(bph.getLine());
      EditorUtils.highlightLine(this, editorView);
    } catch (PausePointHit pph) {
      setBreakPointHit(pph.getLine());
      EditorUtils.highlightLine(this, editorView);
    }
  }

  public void run() {
    try {
      if (isMjj) mjjInterpreter.run();
      else jjcInterpreter.run();
    } catch (BreakPointHit bph) {
      setBreakPointHit(bph.getLine());
      EditorUtils.highlightLine(this, editorView);
    } catch (PausePointHit pph) {
      setBreakPointHit(pph.getLine());
      EditorUtils.highlightLine(this, editorView);
    }
  }

  public void runWithDebug() {
    try {
      if (isMjj) mjjInterpreter.runWithDebug();
      else jjcInterpreter.runWithDebug();
    } catch (BreakPointHit bph) {
      setBreakPointHit(bph.getLine());
      EditorUtils.highlightLine(this, editorView);
    } catch (PausePointHit pph) {
      setBreakPointHit(pph.getLine());
      EditorUtils.highlightLine(this, editorView);
    }
  }

  public void resume() {
    try {
      if (isMjj) mjjInterpreter.resume();
      else jjcInterpreter.resume();
    } catch (BreakPointHit bph) {
      setBreakPointHit(bph.getLine());
      EditorUtils.highlightLine(this, editorView);
    } catch (PausePointHit pph) {
      setBreakPointHit(pph.getLine());
      EditorUtils.highlightLine(this, editorView);
    }
  }

  public void stepForward() {
    try {
      if (isMjj) mjjInterpreter.stepForward();
      else jjcInterpreter.stepForward();
    } catch (BreakPointHit bph) {
      setBreakPointHit(bph.getLine());
      EditorUtils.highlightLine(this, editorView);
    } catch (PausePointHit pph) {
      setBreakPointHit(pph.getLine());
      EditorUtils.highlightLine(this, editorView);
    }
  }

  public void stepBackward() {
    try {
      if (isMjj) mjjInterpreter.stepBackward();
      else jjcInterpreter.stepBackward();
    } catch (BreakPointHit bph) {
      setBreakPointHit(bph.getLine());
      EditorUtils.highlightLine(this, editorView);
    } catch (PausePointHit pph) {
      setBreakPointHit(pph.getLine());
      EditorUtils.highlightLine(this, editorView);
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
