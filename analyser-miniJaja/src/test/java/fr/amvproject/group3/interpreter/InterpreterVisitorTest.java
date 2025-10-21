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

    // ######################## COMPARAISON SUPERIOR ########################

    @Test
    public void givenSimpleSuperiorComparaisonInt__whenInterpret__thenStackHaveResultFalse() throws Exception {
        // Given
        String code = """
            class Test {
                int x = 3;
                int y = 9;
                main {
                    boolean z = x > y;
                }
            }
            """;

        ProgramNode program = buildAstFromCode(code);
        // When
        InterpreterVisitor interpreter = new InterpreterVisitor();
        program.accept(interpreter);

        Stack stack = interpreter.getStack();
        // Then
        assertThat(stack.val(new Ident("z",1)),is(false));
    }

    @Test
    public void givenSimpleSuperiorComparaisonInt__whenInterpret__thenStackHaveResultTrue() throws Exception {
        // Given
        String code = """
            class Test {
                int x = 9;
                int y = 3;
                main {
                    boolean z = x > y;
                }
            }
            """;

        ProgramNode program = buildAstFromCode(code);
        // When
        InterpreterVisitor interpreter = new InterpreterVisitor();
        program.accept(interpreter);

        Stack stack = interpreter.getStack();
        // Then
        assertThat(stack.val(new Ident("z",1)),is(true));
    }


    @Test
    public void givenSimpleSuperiorComparaisonBool__whenInterpret__thenExceptionThrow() throws Exception {
        // Given
        String code = """
            class Test {
                boolean x = true;
                boolean y = true;
                main {
                    boolean z = x > y;
                }
            }
            """;

        ProgramNode program = buildAstFromCode(code);

        try {
            // When
            InterpreterVisitor interpreter = new InterpreterVisitor();
            program.accept(interpreter);
        } catch (RuntimeException e) {
            // Then
            assertThat(e.getMessage(), is("class java.lang.Boolean cannot be cast to class java.lang.Number " +
                    "(java.lang.Boolean and java.lang.Number are in module java.base of loader 'bootstrap')"));
        }
    }


    // ######################## COMPARAISON EQUALS ########################

    @Test
    public void givenSimpleEqualComparaisonInt__whenInterpret__thenStackHaveResultFalse() throws Exception {
        // Given
        String code = """
            class Test {
                int x = 9;
                int y = 3;
                main {
                    boolean z = x == y;
                }
            }
            """;

        ProgramNode program = buildAstFromCode(code);
        // When
        InterpreterVisitor interpreter = new InterpreterVisitor();
        program.accept(interpreter);

        Stack stack = interpreter.getStack();
        // Then
        assertThat(stack.val(new Ident("z",1)),is(false));
    }

    @Test
    public void givenSimpleEqualComparaisonInt__whenInterpret__thenStackHaveResultTrue() throws Exception {
        // Given
        String code = """
            class Test {
                int x = 9;
                int y = 9;
                main {
                    boolean z = x == y;
                }
            }
            """;

        ProgramNode program = buildAstFromCode(code);
        // When
        InterpreterVisitor interpreter = new InterpreterVisitor();
        program.accept(interpreter);

        Stack stack = interpreter.getStack();
        // Then
        assertThat(stack.val(new Ident("z",1)),is(true));
    }


    @Test
    public void givenSimpleEqualComparaisonBool__whenInterpret__thenStackHaveResultFalse() throws Exception {
        // Given
        String code = """
            class Test {
                boolean x = true;
                boolean y = false;
                main {
                    boolean z = x == y;
                }
            }
            """;

        ProgramNode program = buildAstFromCode(code);
        // When
        InterpreterVisitor interpreter = new InterpreterVisitor();
        program.accept(interpreter);

        Stack stack = interpreter.getStack();
        // Then
        assertThat(stack.val(new Ident("z",1)),is(false));
    }

    @Test
    public void givenSimpleEqualComparaisonBool__whenInterpret__thenStackHaveResultTrue() throws Exception {
        // Given
        String code = """
            class Test {
                boolean x = true;
                boolean y = true;
                main {
                    boolean z = x == y;
                }
            }
            """;

        ProgramNode program = buildAstFromCode(code);
        // When
        InterpreterVisitor interpreter = new InterpreterVisitor();
        program.accept(interpreter);

        Stack stack = interpreter.getStack();
        // Then
        assertThat(stack.val(new Ident("z",1)),is(true));
    }


    // ######################## CONDITIONAL ########################

    @Test
    public void givenSimpleIfElseCode__whenInterpret__thenStackHaveResult() throws Exception {
        // Given
        String code = """
            class Test {
                int x = 1;
                main {
                    if(x == 0){
                    x+=1;
                    }
                    else {
                    x+=10;
                    }
                }
            }
            """;

        ProgramNode program = buildAstFromCode(code);
        // When
        InterpreterVisitor interpreter = new InterpreterVisitor();
        program.accept(interpreter);

        Stack stack = interpreter.getStack();
        // Then
        assertThat(stack.val(new Ident("x",1)),is(11));
    }

    @Test
    public void givenSimpleIfCodeTrue__whenInterpret__thenStackHaveResult() throws Exception {
        // Given
        String code = """
            class Test {
                int x = 0;
                main {
                    if(x == 0){
                    x+=1;
                    }
                }
            }
            """;

        ProgramNode program = buildAstFromCode(code);
        // When
        InterpreterVisitor interpreter = new InterpreterVisitor();
        program.accept(interpreter);

        Stack stack = interpreter.getStack();
        // Then
        assertThat(stack.val(new Ident("x",1)),is(1));
    }

    @Test
    public void givenSimpleIfCodeFalse__whenInterpret__thenStackHaveResult() throws Exception {
        // Given
        String code = """
            class Test {
                int x = 1;
                main {
                    if(x == 0){
                    x+=1;
                    }
                }
            }
            """;

        ProgramNode program = buildAstFromCode(code);
        // When
        InterpreterVisitor interpreter = new InterpreterVisitor();
        program.accept(interpreter);

        Stack stack = interpreter.getStack();
        // Then
        assertThat(stack.val(new Ident("x",1)),is(1));
    }

    @Test
    public void givenMultipleIfElseCode__whenInterpret__thenStackHaveResult() throws Exception {
        // Given
        String code = """
            class Test {
                int x = 0;
                main {
                    if(x == 0){
                    x+=1;
                    }
                    else {
                    x+=10;
                    }
                    if(x == 10){
                    x = 20;
                    }
                    else {
                    x += 1;
                    }
                }
            }
            """;

        ProgramNode program = buildAstFromCode(code);
        // When
        InterpreterVisitor interpreter = new InterpreterVisitor();
        program.accept(interpreter);

        Stack stack = interpreter.getStack();
        // Then
        assertThat(stack.val(new Ident("x",1)),is(2));

    }

    @Test
    public void givenSimpleWhileCode__whenInterpret__thenStackHaveResult() throws Exception {
        // Given
        String code = """
            class Test {
                int x = 0;
                main {
                   while(10 > x){
                   x+=1;
                    }
                }
            }
            """;

        ProgramNode program = buildAstFromCode(code);
        // When
        InterpreterVisitor interpreter = new InterpreterVisitor();
        program.accept(interpreter);

        Stack stack = interpreter.getStack();
        // Then
        assertThat(stack.val(new Ident("x",1)),is(10));

    }

}
