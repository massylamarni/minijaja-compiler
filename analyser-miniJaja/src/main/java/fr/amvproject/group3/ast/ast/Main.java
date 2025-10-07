package fr.amvproject.group3.ast.ast;

import fr.amvproject.group3.MiniJajaLexer;
import fr.amvproject.group3.MiniJajaParser;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.CharStream;


public class Main {
    public static void main(String[] args) throws Exception {
        CharStream input = CharStreams.fromFileName("C:\\Users\\suley\\Documents\\projetCompile2\\groupe-3\\analyser-miniJaja\\test.mj");
        MiniJajaLexer lexer = new MiniJajaLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MiniJajaParser parser = new MiniJajaParser(tokens);

        MiniJajaParser.ProgramContext tree = parser.program();
        MiniJajaASTBuilder builder = new MiniJajaASTBuilder();
        ASTNode ast = builder.visit(tree);

        System.out.println("=== AST ===");
        System.out.println(ast);
    }
}