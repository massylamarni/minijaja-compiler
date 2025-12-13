package fr.amvproject.group3.model.wrappers;

import fr.amvproject.group3.settings.DefaultPrograms;

public class InteractiveWrapper {
  private boolean isMjj = true;
  private ViewsWrapper viewsWrapper = new ViewsWrapper();
  private InterpreterWrapper interpreterWrapper = new InterpreterWrapper(viewsWrapper);
  private CompilerWrapper compilerWrapper = new CompilerWrapper(viewsWrapper);

  public boolean isMjj() {
    return isMjj;
  }

  public void setIsMjj(boolean isMjj) {
    this.isMjj = isMjj;
    interpreterWrapper.setIsMjj(isMjj);
    viewsWrapper.setIsMjj(isMjj);
  }

  public String getProgram() {
    return isMjj ? DefaultPrograms.mjjProgram : DefaultPrograms.jjcProgram;
  }

  public void updateProgram(String program) {
    if (isMjj) {
      compilerWrapper.updateProgram(program);
    }
    interpreterWrapper.updateProgram(program);
  }

  public ViewsWrapper getViewsWrapper() {
    return viewsWrapper;
  }

  public CompilerWrapper getCompilerWrapper() {
    return compilerWrapper;
  }

  public InterpreterWrapper getInterpreterWrapper() {
    return interpreterWrapper;
  }

  public void reset() {
    viewsWrapper.reset();
    compilerWrapper.reset();
    interpreterWrapper.reset();
    setIsMjj(true);
  }
}