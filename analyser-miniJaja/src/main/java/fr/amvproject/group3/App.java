package fr.amvproject.group3;

import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.MiniJajaAstBuilder;
import fr.amvproject.group3.ast.PrintVisitor;
import fr.amvproject.group3.memory.MemoryVisitor;
import fr.amvproject.group3.memory.SymbolTable;

import org.antlr.v4.runtime.*;


public class App {
    public static void main(String[] args) throws Exception {
        
        CharStream input = CharStreams.fromFileName("analyser-miniJaja/test_files/light.mj");
        MiniJajaLexer lexer = new MiniJajaLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MiniJajaParser parser = new MiniJajaParser(tokens);
        MiniJajaParser.ProgramContext tree = parser.program();
        MiniJajaAstBuilder builder = new MiniJajaAstBuilder();
        AstNode ast = builder.visit(tree);

        PrintVisitor printer = new PrintVisitor();
        printer.save(ast.accept(printer));

        
        MemoryVisitor memoryVisitor = new MemoryVisitor();
        ast.accept(memoryVisitor);
        SymbolTable symbolTable = memoryVisitor.getSymbolTable();
        symbolTable.printTable();
    }
}