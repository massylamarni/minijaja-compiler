package fr.amvproject.group3;

import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.MiniJajaAstBuilder;
import fr.amvproject.group3.ast.PrintVisitor;
import fr.amvproject.group3.memory.MemoryVisitor;
import fr.amvproject.group3.memory.SymbolTable;
import fr.amvproject.group3.utils.StringSaver;

import org.antlr.v4.runtime.*;

public class App {
    public static void main(String[] args) throws Exception {
        CharStream input = CharStreams.fromFileName("analyser-miniJaja/src/test/resources/light.mjj");
        MiniJajaLexer lexer = new MiniJajaLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MiniJajaParser parser = new MiniJajaParser(tokens);
        MiniJajaParser.ProgramContext tree = parser.program();
        MiniJajaAstBuilder builder = new MiniJajaAstBuilder();
        AstNode ast = builder.visit(tree);

        // Print AST to file
        PrintVisitor printer = new PrintVisitor();
        String printedProgram = ast.accept(printer);
        StringSaver.save(printedProgram, "printedProgram.mjj", "main");

        // Rebuild AST from printed program
        CharStream printedInput = CharStreams.fromString(printedProgram);
        MiniJajaLexer printedLexer = new MiniJajaLexer(printedInput);
        CommonTokenStream printedTokens = new CommonTokenStream(printedLexer);
        MiniJajaParser printedParser = new MiniJajaParser(printedTokens);
        MiniJajaParser.ProgramContext printedTree = printedParser.program();
        AstNode printedAst = builder.visit(printedTree);

        ast.sameAs(printedAst);

        MemoryVisitor memoryVisitor = new MemoryVisitor();
        ast.accept(memoryVisitor);
        SymbolTable symbolTable = memoryVisitor.getSymbolTable();
        symbolTable.print();
    }
}