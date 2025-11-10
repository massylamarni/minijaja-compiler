package fr.amvproject.group3;

import org.antlr.v4.runtime.*;

import org.junit.Test;

import fr.amvproject.group3.jjc.ast.ProgramGenerator;
import fr.amvproject.group3.jjc.ast.AstNode;
import fr.amvproject.group3.jjc.ast.JajaCodeAstBuilder;
import fr.amvproject.group3.jjc.ast.PrintVisitor;
import fr.amvproject.group3.utils.StringSaver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FullAstTest {
  @Test
  public void givenExhaustiveProgram__whenBuildingAst__thenCorrectRepresentation() throws IOException {
    // Generate program
    ProgramGenerator generator = new ProgramGenerator();
    generator.generateProgram();
    generator.print("test");

    // Build AST from generated program
    Path path = Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "generatedProgram.jjc");
    CharStream input = CharStreams.fromFileName(path.toString());
    JajaCodeLexer lexer = new JajaCodeLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    JajaCodeParser parser = new JajaCodeParser(tokens);
    JajaCodeParser.ProgramContext tree = parser.program();
    JajaCodeAstBuilder builder = new JajaCodeAstBuilder();
    AstNode ast = builder.visit(tree);

    // Print AST to file
    PrintVisitor printer = new PrintVisitor();
    String printedProgram = ast.accept(printer);
    StringSaver.save(printedProgram, "printedProgram.jjc", "test");

    // Compare line counts
    long originalLines = input.toString().lines().count();
    long printedLines = printedProgram.lines().count();
    assertEquals(originalLines, printedLines);

    // Rebuild AST from printed program
    CharStream printedInput = CharStreams.fromString(printedProgram);
    JajaCodeLexer printedLexer = new JajaCodeLexer(printedInput);
    CommonTokenStream printedTokens = new CommonTokenStream(printedLexer);
    JajaCodeParser printedParser = new JajaCodeParser(printedTokens);
    JajaCodeParser.ProgramContext printedTree = printedParser.program();
    AstNode printedAst = builder.visit(printedTree);

    // Compare ASTs
    assertTrue(ast.sameAs(printedAst));
  }
}
