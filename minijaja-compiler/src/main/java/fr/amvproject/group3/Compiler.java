package fr.amvproject.group3;

import fr.amvproject.group3.exceptions.SemanticException;
import fr.amvproject.group3.exceptions.collectors.SemanticErrorsCollector;
import fr.amvproject.group3.exceptions.listeners.LexicalErrorsListener;
import fr.amvproject.group3.exceptions.listeners.SyntaxicErrorsListener;
import fr.amvproject.group3.jjc.ast.PrintVisitor;
import fr.amvproject.group3.jjc.ast.nodes.program.ProgramNode;
import fr.amvproject.group3.mjj.analyser.SemanticVisitor;
import fr.amvproject.group3.mjj.utils.AstOperations;
import fr.amvproject.group3.mjj.utils.AstOperations.AstWrapper;

public class Compiler {
  private AstWrapper astWrapper;
  private SemanticVisitor semanticVisitor = new SemanticVisitor();
  private CompilerVisitor compilerVisitor = new CompilerVisitor();
  private ProgramNode compiledProgramNode = null;
  private SemanticErrorsCollector semanticErrorsCollector = new SemanticErrorsCollector();

  public Compiler(String program) {
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

  private void runSemanticAnalysis() {
    try {
      astWrapper.ast.accept(semanticVisitor);
    } catch (SemanticException e) {
      semanticErrorsCollector.add(e.getMessage(), e.getLine(), e.getColumn());
    }
  }

  public void compile() {
    // runSemanticAnalysis();
    compilerVisitor.reset();
    compiledProgramNode = new ProgramNode(astWrapper.ast.accept(compilerVisitor));
  }

  public String getCompiledProgram() {
    return compiledProgramNode.accept(new PrintVisitor());
  }
}
