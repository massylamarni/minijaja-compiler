package fr.amvproject.group3;

import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.MiniJajaAstBuilder;
import fr.amvproject.group3.ast.PrintVisitor;
import fr.amvproject.group3.memory.MemoryVisitor;
import fr.amvproject.group3.memory.SymbolTable;
import java.util.logging.Logger;

import org.antlr.v4.runtime.*;


public class App {
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());
    public static void main(String[] args) throws Exception {
        
        CharStream input = CharStreams.fromFileName("analyser-miniJaja/test_files/light.mj");
        MiniJajaLexer lexer = new MiniJajaLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MiniJajaParser parser = new MiniJajaParser(tokens);
        MiniJajaParser.ProgramContext tree = parser.program();
        MiniJajaAstBuilder builder = new MiniJajaAstBuilder();
        AstNode ast = builder.visit(tree);

        PrintVisitor printer = new PrintVisitor();
        String result = ast.accept(printer);
        LOGGER.info(result);

        
        MemoryVisitor memoryVisitor = new MemoryVisitor();
        ast.accept(memoryVisitor);
        SymbolTable symbolTable = memoryVisitor.getSymbolTable();
        symbolTable.printTable();
        
    }
}