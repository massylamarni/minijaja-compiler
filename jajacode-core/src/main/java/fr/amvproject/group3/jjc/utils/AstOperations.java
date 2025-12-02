package fr.amvproject.group3.jjc.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import fr.amvproject.group3.JajaCodeLexer;
import fr.amvproject.group3.JajaCodeParser;
import fr.amvproject.group3.exceptions.listeners.LexicalErrorsListener;
import fr.amvproject.group3.exceptions.listeners.SyntaxicErrorsListener;
import fr.amvproject.group3.jjc.ast.AstBuilderVisitor;
import fr.amvproject.group3.jjc.ast.AstNode;

public class AstOperations {
  private AstOperations() {}

  public static class AstWrapper {
    public final AstNode ast;
    public final LexicalErrorsListener lexical;
    public final SyntaxicErrorsListener syntaxic;

    public AstWrapper(AstNode ast, LexicalErrorsListener lexical, SyntaxicErrorsListener syntaxic) {
        this.ast = ast;
        this.lexical = lexical;
        this.syntaxic = syntaxic;
    }
  }
  
  public static AstWrapper getAstFromString(String string) {
    JajaCodeLexer lexer = new JajaCodeLexer(CharStreams.fromString(string));
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    JajaCodeParser parser = new JajaCodeParser(tokens);

    // Error handling
    LexicalErrorsListener lexicalErrorsListener = new LexicalErrorsListener();
    lexer.removeErrorListeners(); // Removes the default console error listener
    lexer.addErrorListener(lexicalErrorsListener);

    SyntaxicErrorsListener syntaxicErrorsListener = new SyntaxicErrorsListener();
    parser.removeErrorListeners();
    parser.addErrorListener(syntaxicErrorsListener);

    JajaCodeParser.ProgramContext tree = parser.program();

    if (lexicalErrorsListener.hasErrors()) {
      lexicalErrorsListener.printAll();
    }
    if (syntaxicErrorsListener.hasErrors()) {
      syntaxicErrorsListener.printAll();
    }

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
