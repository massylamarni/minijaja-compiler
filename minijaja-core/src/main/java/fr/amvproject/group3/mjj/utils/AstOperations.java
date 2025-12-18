package fr.amvproject.group3.mjj.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import fr.amvproject.group3.MiniJajaLexer;
import fr.amvproject.group3.MiniJajaParser;
import fr.amvproject.group3.exceptions.listeners.LexicalErrorsListener;
import fr.amvproject.group3.exceptions.listeners.SyntaxicErrorsListener;
import fr.amvproject.group3.mjj.ast.AstBuilderVisitor;
import fr.amvproject.group3.mjj.ast.AstNode;

public class AstOperations {
  private AstOperations() {}

  public static class AstWrapper {
    public final AstNode ast;
    public final LexicalErrorsListener lexicalErrorsListener;
    public final SyntaxicErrorsListener syntaxicErrorsListener;

    public AstWrapper(AstNode ast, LexicalErrorsListener lexicalErrorsListener, SyntaxicErrorsListener syntaxicErrorsListener) {
        this.ast = ast;
        this.lexicalErrorsListener = lexicalErrorsListener;
        this.syntaxicErrorsListener = syntaxicErrorsListener;
    }
  }

  public static AstWrapper getAstFromString(String string) {
    MiniJajaLexer lexer = new MiniJajaLexer(CharStreams.fromString(string));
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    MiniJajaParser parser = new MiniJajaParser(tokens);

    // Error handling
    LexicalErrorsListener lexicalErrorsListener = new LexicalErrorsListener();
    lexer.removeErrorListeners(); // Removes the default console error listener
    lexer.addErrorListener(lexicalErrorsListener);

    SyntaxicErrorsListener syntaxicErrorsListener = new SyntaxicErrorsListener();
    parser.removeErrorListeners();
    parser.addErrorListener(syntaxicErrorsListener);

    MiniJajaParser.ProgramContext tree = parser.program();

    AstBuilderVisitor astBuilderVisitor = new AstBuilderVisitor();
    return new AstWrapper(astBuilderVisitor.visit(tree), lexicalErrorsListener, syntaxicErrorsListener);
  }

  public static AstWrapper getAstFromFile(String path) throws IOException {
    try {
      return getAstFromString(Files.readString(Paths.get(path)));
    } catch (Exception e) {
      throw new IOException(e);
    }
  }

  public static AstWrapper getAstFromFile(Path path) throws IOException {
    try {
      return getAstFromString(Files.readString(path));
    } catch (Exception e) {
      throw new IOException(e);
    }
  }
}
