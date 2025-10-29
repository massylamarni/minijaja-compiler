package fr.amvproject.group3;

import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.JajaCodeAstBuilder;
import fr.amvproject.group3.ast.PrintVisitor;
import fr.amvproject.group3.utils.StringSaver;

import org.antlr.v4.runtime.*;

public class App {
    public static void main(String[] args) throws Exception {
        CharStream input = CharStreams.fromFileName("analyser-JajaCode/src/test/resources/td.jjc");
        JajaCodeLexer lexer = new JajaCodeLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JajaCodeParser parser = new JajaCodeParser(tokens);
        JajaCodeParser.ProgramContext tree = parser.program();
        JajaCodeAstBuilder builder = new JajaCodeAstBuilder();
        AstNode ast = builder.visit(tree);

        // Print AST to file
        PrintVisitor printer = new PrintVisitor();
        String printedProgram = ast.accept(printer);
        StringSaver.save(printedProgram, "printedProgram.jjc");

        // Rebuild AST from printed program
        CharStream printedInput = CharStreams.fromString(printedProgram);
        JajaCodeLexer printedLexer = new JajaCodeLexer(printedInput);
        CommonTokenStream printedTokens = new CommonTokenStream(printedLexer);
        JajaCodeParser printedParser = new JajaCodeParser(printedTokens);
        JajaCodeParser.ProgramContext printedTree = printedParser.program();
        AstNode printedAst = builder.visit(printedTree);

        ast.sameAs(printedAst);
    }
}