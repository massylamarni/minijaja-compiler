package fr.amvproject.group3.jjc.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import fr.amvproject.group3.JajaCodeLexer;
import fr.amvproject.group3.JajaCodeParser;
import fr.amvproject.group3.exceptions.SyntaxException;
import fr.amvproject.group3.jjc.ast.AstBuilderVisitor;
import fr.amvproject.group3.jjc.ast.AstNode;

public class AstOperations {
  private AstOperations() {}
  public static AstNode getAstFromString(String string) {
    JajaCodeLexer lexer = new JajaCodeLexer(CharStreams.fromString(string));
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    JajaCodeParser parser = new JajaCodeParser(tokens);

    // Error handling
    JajaCodeParser.ProgramContext tree = null;
    try {
      tree = parser.program();
    } catch (Exception ex) {
      throw new SyntaxException(ex.getMessage());
    }

    if (parser.getNumberOfSyntaxErrors() > 0 || tree == null)
      throw new SyntaxException("Parsing resulted in " + parser.getNumberOfSyntaxErrors() + " errors.");

    AstBuilderVisitor astBuilderVisitor = new AstBuilderVisitor();
    return astBuilderVisitor.visit(tree);
  }

  public static AstNode getAstFromFile(String path) throws IOException {
    try {
      return getAstFromString(Files.readString(Paths.get(path)));
    } catch (Exception e) {
      throw new IOException(e);
    }
  }

  public static AstNode getAstFromFile(Path path) throws IOException {
    try {
      return getAstFromString(Files.readString(path));
    } catch (Exception e) {
      throw new IOException(e);
    }
  }
}
