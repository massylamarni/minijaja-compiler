package fr.amvproject.group3;

import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.MiniJajaAstBuilder;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.CharStream;


public class App {
    public static void main(String[] args) throws Exception {
        CharStream input = CharStreams.fromFileName("/home/dhk/Desktop/UFC/Compilation/groupe-3/analyser-miniJaja/test.mj");
        MiniJajaLexer lexer = new MiniJajaLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MiniJajaParser parser = new MiniJajaParser(tokens);

        MiniJajaParser.ProgramContext tree = parser.program();
        MiniJajaAstBuilder builder = new MiniJajaAstBuilder();
        AstNode ast = builder.visit(tree);

        System.out.println("=== AST ===");
        System.out.println(ast);
    }
}