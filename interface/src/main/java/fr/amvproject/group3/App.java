package fr.amvproject.group3;

import fr.amvproject.group3.jjc.ast.AstNode;
import fr.amvproject.group3.jjc.ast.PrintVisitor;
import fr.amvproject.group3.jjc.utils.AstOperations;
import fr.amvproject.group3.utils.StringSaver;
import fr.amvproject.group3.mjj.MemoryVisitor;
import fr.amvproject.group3.mjj.SymbolTable;
import fr.amvproject.group3.nodes.TypeMemNode;

public class App {
  public static void main(String[] args) throws Exception {

    /* JajaCode */

    AstNode jjcAst = AstOperations.getAstFromFile("jajacode-core/src/test/resources/td.jjc");

    // Print AST to file
    PrintVisitor jjcPrintVisitor = new PrintVisitor();
    String jjcPrintedProgram = jjcAst.accept(jjcPrintVisitor);
    StringSaver.save(jjcPrintedProgram, "printedProgram.jjc", "main");

    /* MiniJaja */
    fr.amvproject.group3.mjj.ast.AstNode mjjAst = fr.amvproject.group3.mjj.utils.AstOperations
        .getAstFromFile("minijaja-core/src/test/resources/fact.mjj");

    // Print AST to file
    fr.amvproject.group3.mjj.ast.PrintVisitor mjjPrinter = new fr.amvproject.group3.mjj.ast.PrintVisitor();
    String mjjPrintedProgram = mjjAst.accept(mjjPrinter);
    StringSaver.save(mjjPrintedProgram, "printedProgram.mjj", "main");

    /* Symbol table */

    MemoryVisitor memoryVisitor = new MemoryVisitor();
    mjjAst.accept(memoryVisitor);
    SymbolTable symbolTable = memoryVisitor.getSymbolTable();
    symbolTable.print();

    /* Interpretation */

    MjjInterpreter mjjInterpreter = new MjjInterpreter(mjjPrintedProgram);
    mjjInterpreter.interpretAll();

    /* Heap */

    Heap heap = new Heap();
    heap.allocate(5, new TypeMemNode("int"));
    heap.allocate(3, new TypeMemNode("boolean"));
    heap.allocate(2, new TypeMemNode("void"));
    heap.allocate(2, new TypeMemNode("int"));
    heap.allocate(2, new TypeMemNode("int"));
    heap.allocate(3, new TypeMemNode("boolean"));
    heap.allocate(1, new TypeMemNode("int"));
    System.out.println(heap.print());
  }
}
