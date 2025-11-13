package fr.amvproject.group3.ast;
import org.antlr.v4.runtime.*;

import org.junit.Test;

import fr.amvproject.group3.jjc.ast.AstNode;
import fr.amvproject.group3.jjc.ast.PrintVisitor;
import fr.amvproject.group3.jjc.ast.ProgramGenerator;
import fr.amvproject.group3.jjc.utils.AstOperations;
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
    Path jjcProgramPath = Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "generatedProgram.jjc");
    AstNode ast = AstOperations.getAstFromFile(jjcProgramPath);

    // Print AST to file
    PrintVisitor printVisitor = new PrintVisitor();
    String printedProgram = ast.accept(printVisitor);
    StringSaver.save(printedProgram, "printedProgram.jjc", "test");

    // Compare line counts
    long originalLines = CharStreams.fromFileName(jjcProgramPath.toString()).toString().lines().count();
    long printedLines = printedProgram.lines().count();
    assertEquals(originalLines, printedLines);

    // Rebuild AST from printed program
    AstNode printedAst = AstOperations.getAstFromString(printedProgram);

    // Compare ASTs
    assertTrue(ast.sameAs(printedAst));
  }
}
