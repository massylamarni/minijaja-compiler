package fr.amvproject.group3.ast;

import org.antlr.v4.runtime.*;
import org.junit.Ignore;
import org.junit.Test;

import fr.amvproject.group3.mjj.ast.ProgramGenerator;
import fr.amvproject.group3.mjj.utils.AstOperations;
import fr.amvproject.group3.mjj.ast.AstNode;
import fr.amvproject.group3.mjj.ast.PrintVisitor;
import fr.amvproject.group3.utils.StringSaver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FullAstTest {
  @Ignore("After adding default values, the asts will be different")
  public void givenExhaustiveProgram__whenBuildingAst__thenCorrectRepresentation() throws IOException {
    // Generate program
    ProgramGenerator generator = new ProgramGenerator();
    generator.generateProgram();
    generator.print("test");

    // Build AST from generated program
    Path mjjProgramPath = Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "generatedProgram.mjj");
    AstNode ast = AstOperations.getAstFromFile(mjjProgramPath);

    // Print AST to file
    PrintVisitor printer = new PrintVisitor();
    String printedProgram = ast.accept(printer);
    StringSaver.save(printedProgram, "printedProgram.mjj", "test");

    // Compare line counts
    long originalLines = CharStreams.fromFileName(mjjProgramPath.toString()).toString().lines().count();
    long printedLines = printedProgram.lines().count();
    assertEquals(originalLines, printedLines);

    // Rebuild AST from printed program
    AstNode printedAst = AstOperations.getAstFromString(printedProgram);

    // Compare ASTs
    assertTrue(ast.sameAs(printedAst));
  }
}
