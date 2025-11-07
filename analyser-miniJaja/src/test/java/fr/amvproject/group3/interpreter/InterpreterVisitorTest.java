package fr.amvproject.group3.interpreter;

import fr.amvproject.group3.MiniJajaLexer;
import fr.amvproject.group3.MiniJajaParser;
import fr.amvproject.group3.ast.InterpreterVisitor;
import fr.amvproject.group3.ast.MiniJajaAstBuilder;
import fr.amvproject.group3.ast.nodes.program.ProgramNode;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class InterpreterVisitorTest {


    private ProgramNode buildAstFromCode(String code) {
        MiniJajaLexer lexer = new MiniJajaLexer(CharStreams.fromString(code));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MiniJajaParser parser = new MiniJajaParser(tokens);

        ParseTree tree = parser.program();
        MiniJajaAstBuilder builder = new MiniJajaAstBuilder();

        return (ProgramNode) builder.visit(tree);
    }

    // ######################## ARIMTHMETIC ########################

    @Test
    public void testSimplestCase__declareAndPrintInt() throws Exception {
        String code = """
                class Test {
                    int x = 2;
                    int y = 3;
                    main {
                        x = x+ y;
                        write(x);
                    }
                }
                """;

        ProgramNode program = buildAstFromCode(code);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        InterpreterVisitor interpreter = new InterpreterVisitor();

        try {
            // When
            program.accept(interpreter);
        } finally {
            System.setOut(originalOut);
        }

        // Then
        String output = outContent.toString().trim();
        assertThat(output, is("5"));
    }
}
