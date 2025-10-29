package fr.amvproject.group3;

import org.antlr.v4.runtime.*;

import org.junit.Test;

import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.MiniJajaAstBuilder;
import fr.amvproject.group3.ast.PrintVisitor;
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
    Path path = Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "generatedProgram.mjj");
    CharStream input = CharStreams.fromFileName(path.toString());
    MiniJajaLexer lexer = new MiniJajaLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    MiniJajaParser parser = new MiniJajaParser(tokens);
    MiniJajaParser.ProgramContext tree = parser.program();
    MiniJajaAstBuilder builder = new MiniJajaAstBuilder();
    AstNode ast = builder.visit(tree);

    // Print AST to file
    PrintVisitor printer = new PrintVisitor();
    String printedProgram = ast.accept(printer);
    StringSaver.save(printedProgram, "printedProgram.mjj", "test");

    // Compare line counts
    long originalLines = input.toString().lines().count();
    long printedLines = printedProgram.lines().count();
    assertEquals(originalLines, printedLines);

    // Rebuild AST from printed program
    CharStream printedInput = CharStreams.fromString(printedProgram);
    MiniJajaLexer printedLexer = new MiniJajaLexer(printedInput);
    CommonTokenStream printedTokens = new CommonTokenStream(printedLexer);
    MiniJajaParser printedParser = new MiniJajaParser(printedTokens);
    MiniJajaParser.ProgramContext printedTree = printedParser.program();
    AstNode printedAst = builder.visit(printedTree);

    // Compare ASTs
    assertTrue(ast.sameAs(printedAst));
  }
}
