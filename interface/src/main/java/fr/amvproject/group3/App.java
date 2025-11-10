package fr.amvproject.group3;

import org.antlr.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import fr.amvproject.group3.jjc.ast.AstNode;
import fr.amvproject.group3.jjc.ast.JajaCodeAstBuilder;
import fr.amvproject.group3.jjc.ast.PrintVisitor;
import fr.amvproject.group3.jjc.ast.ProgramGenerator;
import fr.amvproject.group3.utils.StringSaver;
import fr.amvproject.group3.mjj.MemoryVisitor;
import fr.amvproject.group3.mjj.SymbolTable;
import fr.amvproject.group3.mjj.ast.MiniJajaAstBuilder;

public class App {
  public static void main(String[] args) throws Exception {

    /* JajaCode */

    // Program generation
    ProgramGenerator generator = new ProgramGenerator();
    generator.generateProgram();
    generator.print("main");

    // Ast building
    JajaCodeLexer jjc_lexer = new JajaCodeLexer(CharStreams.fromFileName("jajacode-core/src/test/resources/td.jjc"));
    CommonTokenStream jjc_tokens = new CommonTokenStream(jjc_lexer);
    JajaCodeParser jjc_parser = new JajaCodeParser(jjc_tokens);
    JajaCodeParser.ProgramContext jjc_tree = jjc_parser.program();
    JajaCodeAstBuilder jjc_builder = new JajaCodeAstBuilder();
    AstNode jjc_ast = jjc_builder.visit(jjc_tree);

    // Print AST to file
    PrintVisitor jjc_printer = new PrintVisitor();
    String jjc_printedProgram = jjc_ast.accept(jjc_printer);
    StringSaver.save(jjc_printedProgram, "printedProgram.jjc", "main");

    /* MiniJaja */
    MiniJajaLexer mjj_lexer = new MiniJajaLexer(CharStreams.fromFileName("minijaja-core/src/test/resources/light.mjj"));
    CommonTokenStream mjj_tokens = new CommonTokenStream(mjj_lexer);
    MiniJajaParser mjj_parser = new MiniJajaParser(mjj_tokens);
    MiniJajaParser.ProgramContext mjj_tree = mjj_parser.program();
    MiniJajaAstBuilder mjj_builder = new MiniJajaAstBuilder();
    fr.amvproject.group3.mjj.ast.AstNode mjj_ast = mjj_builder.visit(mjj_tree);

    // Print AST to file
    fr.amvproject.group3.mjj.ast.PrintVisitor mjj_printer = new fr.amvproject.group3.mjj.ast.PrintVisitor();
    String mjj_printedProgram = mjj_ast.accept(mjj_printer);
    StringSaver.save(mjj_printedProgram, "printedProgram.mjj", "main");

    /* Symbol table */
    MemoryVisitor memoryVisitor = new MemoryVisitor();
    mjj_ast.accept(memoryVisitor);
    SymbolTable symbolTable = memoryVisitor.getSymbolTable();
    symbolTable.print();

    }
}
