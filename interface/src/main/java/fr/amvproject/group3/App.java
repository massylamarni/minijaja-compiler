package fr.amvproject.group3;

import fr.amvproject.group3.jjc.ast.AstNode;
import fr.amvproject.group3.jjc.ast.PrintVisitor;
import fr.amvproject.group3.jjc.utils.AstOperations;
import fr.amvproject.group3.utils.StringSaver;

public class App {
  public static void main(String[] args) throws Exception {

    /* JajaCode */

    AstNode jjcAst = AstOperations.getAstFromFile("jajacode-core/src/test/resources/td.jjc").ast;

    // Print AST to file
    PrintVisitor jjcPrintVisitor = new PrintVisitor();
    String jjcPrintedProgram = jjcAst.accept(jjcPrintVisitor);
    StringSaver.save(jjcPrintedProgram, "printedProgram.jjc", "main");

    /* MiniJaja */
    fr.amvproject.group3.mjj.ast.AstNode mjjAst = fr.amvproject.group3.mjj.utils.AstOperations
        .getAstFromFile("minijaja-core/src/test/resources/fact.mjj").ast;

    // Print AST to file
    fr.amvproject.group3.mjj.ast.PrintVisitor mjjPrinter = new fr.amvproject.group3.mjj.ast.PrintVisitor();
    String mjjPrintedProgram = mjjAst.accept(mjjPrinter);
    StringSaver.save(mjjPrintedProgram, "fact.mjj", "main");
  }
}
