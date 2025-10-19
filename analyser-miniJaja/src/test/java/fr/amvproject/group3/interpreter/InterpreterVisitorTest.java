package fr.amvproject.group3.interpreter;

import fr.amvproject.group3.MiniJajaLexer;
import fr.amvproject.group3.MiniJajaParser;
import fr.amvproject.group3.ast.InterpreterVisitor;
import fr.amvproject.group3.ast.MiniJajaAstBuilder;
import fr.amvproject.group3.ast.nodes.abstracts.Ident;
import fr.amvproject.group3.ast.nodes.program.ProgramNode;
import fr.amvproject.group3.memory.Stack;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class InterpreterVisitorTest {


    private ProgramNode buildAstFromCode(String code) throws Exception {
        MiniJajaLexer lexer = new MiniJajaLexer(CharStreams.fromString(code));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MiniJajaParser parser = new MiniJajaParser(tokens);

        ParseTree tree = parser.program();
        MiniJajaAstBuilder builder = new MiniJajaAstBuilder();

        return (ProgramNode) builder.visit(tree);
    }

    @Test
    public void givenSimpleAdditionCode__whenInterpret__thenStackHaveResult() throws Exception {
        // Given
        String code = """
            class Test {
                int x = 2;
                int y = 3;
                main {
                    x = x + y;
                }
            }
            """;

        ProgramNode program = buildAstFromCode(code);
        // When
        InterpreterVisitor interpreter = new InterpreterVisitor();
        program.accept(interpreter);

        Stack stack = interpreter.getStack();
        // Then
        assertThat(stack.val(new Ident("x",1)),is(5));

    }

    @Test
    public void givenSimpleSoustractionCode__whenInterpret__thenStackHaveResult() throws Exception {
        // Given
        String code = """
            class Test {
                int x = 2;
                int y = 3;
                main {
                    x = x - y;
                }
            }
            """;

        ProgramNode program = buildAstFromCode(code);
        // When
        InterpreterVisitor interpreter = new InterpreterVisitor();
        program.accept(interpreter);

        Stack stack = interpreter.getStack();
        // Then
        assertThat(stack.val(new Ident("x",1)),is(-1));

    }


    @Test
    public void givenSimpleMultiplicationCode__whenInterpret__thenStackHaveResult() throws Exception {
        // Given
        String code = """
            class Test {
                int x = 2;
                int y = 3;
                main {
                    x = x * y;
                }
            }
            """;

        ProgramNode program = buildAstFromCode(code);
        // When
        InterpreterVisitor interpreter = new InterpreterVisitor();
        program.accept(interpreter);

        Stack stack = interpreter.getStack();
        // Then
        assertThat(stack.val(new Ident("x",1)),is(6));

    }


    @Test
    public void givenSimpleDivisionCode__whenInterpret__thenStackHaveResult() throws Exception {
        // Given
        String code = """
            class Test {
                int x = 9;
                int y = 3;
                main {
                    x = x / y;
                }
            }
            """;

        ProgramNode program = buildAstFromCode(code);
        // When
        InterpreterVisitor interpreter = new InterpreterVisitor();
        program.accept(interpreter);

        Stack stack = interpreter.getStack();
        // Then
        assertThat(stack.val(new Ident("x",1)),is(3));

    }

}
