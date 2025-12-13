package fr.amvproject.group3.model.wrappers;

import fr.amvproject.group3.settings.DefaultPrograms;
import fr.amvproject.group3.Compiler;
import fr.amvproject.group3.exceptions.SemanticException;


public class CompilerWrapper {
  private ViewsWrapper viewsWrapper;
  private Compiler compiler = new Compiler(DefaultPrograms.mjjProgram);

  public CompilerWrapper(ViewsWrapper viewsWrapper) {
    this.viewsWrapper = viewsWrapper;
  }

  public String getCompiledProgram() {
    try {
      compiler.compile();
      viewsWrapper.setProgramSuccess("Program compiled successfully !");
      return compiler.getCompiledProgram();
    } catch (SemanticException e) {
      throw e;
    }
  }

  public void  updateProgram(String program) {
    compiler.setProgram(program);
  }

  public void reset() {
    updateProgram(DefaultPrograms.newMjjProgram);
  }
}
