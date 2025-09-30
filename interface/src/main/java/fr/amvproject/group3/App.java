package fr.amvproject.group3;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import fr.amvproject.group3.ast.BuildAstVisitor;
import fr.amvproject.group3.ast.nodes.program.ProgramNode;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception
    {
        System.out.println( "Hello World!" );
        String input = Files.readString(Paths.get("/home/massy/Desktop/test.mjj"));
        MiniJajaLexer lexer = new MiniJajaLexer(CharStreams.fromString(input));
        //CommonTokenStream tokens = new CommonTokenStream(lexer);
        MiniJajaParser parser = new MiniJajaParser(new CommonTokenStream(lexer));
        System.out.println(parser.program());
        ParseTree tree = parser.program();
        BuildAstVisitor builder = new BuildAstVisitor();
        ProgramNode ast = (ProgramNode) builder.visit(tree);
    }
}
