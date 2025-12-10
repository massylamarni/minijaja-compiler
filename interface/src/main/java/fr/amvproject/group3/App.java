package fr.amvproject.group3;

import java.nio.file.Files;
import java.nio.file.Paths;

import fr.amvproject.group3.jjc.ast.AstNode;
import fr.amvproject.group3.jjc.ast.PrintVisitor;
import fr.amvproject.group3.jjc.utils.AstOperations;
import fr.amvproject.group3.mjj.analyser.SemanticVisitor;
import fr.amvproject.group3.mjj.analyser.SymbolTable;
import fr.amvproject.group3.mjj.Interpreter;
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

    /* Symbol table */

    // SemanticVisitor memoryVisitor = new SemanticVisitor();
    // mjjAst.accept(memoryVisitor);
    // SymbolTable symbolTable = memoryVisitor.getSymbolTable();
    // symbolTable.print();

    /* Compilation */

    String programToCompile = """
        class C {
                    int nt[3] ;
                    int i = 0 ;
    
                main {
                    while (3 > i) {
                        nt[i] = i * 10 ;
                        i++ ;
                    };
                    write(nt[2]);
                }
            }
        """;
    Compiler compiler = new Compiler(programToCompile);
    compiler.compile();
    System.out.println(compiler.getCompiledProgram());

    /* Interpretation */

    String programPath = "minijaja-core/src/test/resources/fact.mjj";
    String mjjProgram =  Files.readString(Paths.get(programPath));
    Interpreter mjjInterpreter = new Interpreter(mjjProgram);
    mjjInterpreter.addBreakPoint(7);
    mjjInterpreter.addBreakPoint(10);
    mjjInterpreter.addBreakPoint(20);
    mjjInterpreter.runWithDebug();
    mjjInterpreter.resume();
    mjjInterpreter.resume();
    mjjInterpreter.resume();
    mjjInterpreter.stepForward();
    mjjInterpreter.stepForward();
    mjjInterpreter.stepForward();
    mjjInterpreter.stepBackward();
    mjjInterpreter.stepBackward();
    mjjInterpreter.stepBackward();
    mjjInterpreter.stepForward();
    mjjInterpreter.stepBackward();
    mjjInterpreter.stepForward();
    mjjInterpreter.stepBackward();
    mjjInterpreter.stepForward();
    mjjInterpreter.stepBackward();

    System.out.println("----------------------------------------------");

    String jjcProgram = """
            1 init
            2 push(1)
            3 newarray(tab1, entier)
            4 push(0)
            5 aload(tab1)
            6 write
            7 push(0)
            8 push(10)
            9 astore(tab1)
            10 push(0)
            11 aload(tab1)
            12 write
            13 push(0)
            14 push(1)
            15 ainc(tab1)
            16 push(0)
            17 aload(tab1)
            18 write
            19 push(0)
            20 swap
            21 pop
            22 pop
            23 jcstop
        """;
    fr.amvproject.group3.jjc.Interpreter jjcInterpreter = new fr.amvproject.group3.jjc.Interpreter(jjcProgram);
    jjcInterpreter.addBreakPoint(7);
    jjcInterpreter.addBreakPoint(10);
    jjcInterpreter.addBreakPoint(20);
    jjcInterpreter.runWithDebug();
    jjcInterpreter.resume();
    jjcInterpreter.stepForward();
    jjcInterpreter.stepForward();
    jjcInterpreter.restart();
    jjcInterpreter.runWithDebug();
    jjcInterpreter.resume();
    jjcInterpreter.stepForward();
    jjcInterpreter.stepForward();
    jjcInterpreter.stepForward();
    jjcInterpreter.stepBackward();
    jjcInterpreter.stepBackward();
    jjcInterpreter.stepBackward();
    jjcInterpreter.stepForward();
    jjcInterpreter.stepBackward();
    jjcInterpreter.stepForward();
    jjcInterpreter.stepBackward();
    jjcInterpreter.stepForward();
    jjcInterpreter.stepBackward();
  }
}
