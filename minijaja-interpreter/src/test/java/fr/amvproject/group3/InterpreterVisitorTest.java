package fr.amvproject.group3;


import fr.amvproject.group3.MiniJajaLexer;
import fr.amvproject.group3.MiniJajaParser;
import fr.amvproject.group3.mjj.ast.MiniJajaAstBuilder;
import fr.amvproject.group3.mjj.ast.nodes.program.ProgramNode;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;
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
    public void givenSimpleAdditionCode__whenInterpret__thenStackHaveResult() throws Exception {
        // Given
        String code = """
            class Test {
                int x = 2;
                int y = 3;
                main {
                    x = x + y;
                    write(x);
                }
            }
            """;

        ProgramNode program = buildAstFromCode(code);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        // When
        InterpreterVisitor interpreter = new InterpreterVisitor();
        try {
            program.accept(interpreter);
        }finally {
            System.setOut(originalOut);
        }

        // Then
        String output = outContent.toString().trim();
        assertThat(output, is("5"));

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
                    write(x);
                }
            }
            """;

        ProgramNode program = buildAstFromCode(code);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        // When
        InterpreterVisitor interpreter = new InterpreterVisitor();
        try {
            program.accept(interpreter);
        }finally {
            System.setOut(originalOut);
        }

        // Then
        String output = outContent.toString().trim();
        assertThat(output, is("-1"));

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
        assertThat(output, is("6"));

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
        assertThat(output, is("3"));

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
                    write(z);
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
        assertThat(output, is("false"));
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
                    write(z);
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
        assertThat(output, is("true"));
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
                    write(z);
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
            assertThat(e.getMessage(), is("Type error: arithmetic operator requires integer operands, got Boolean and Boolean"));
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
                    write(z);
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
        assertThat(output, is("false"));
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
                    write(z);
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
        assertThat(output, is("true"));
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
                    write(z);
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
        assertThat(output, is("false"));
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
                    write(z);
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
        assertThat(output, is("true"));
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
        assertThat(output, is("11"));
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
        assertThat(output, is("1"));
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
        assertThat(output, is("1"));
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
        assertThat(output, is("2"));

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
        assertThat(output, is("10"));

    }

    // ######################## COMPARAISON LOGICAL CONNECTOR ########################

    @Test
    public void givenSimpleAndComparaisonBool__whenInterpret__thenStackHaveResultTrue() throws Exception {
        // Given
        String code = """
            class Test {
                boolean x = true;
                boolean y = true;
                main {
                    boolean z = x && y;
                    write(z);
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
        assertThat(output, is("true"));
    }

    @Test
    public void givenSimpleAndComparaisonBool__whenInterpret__thenStackHaveResultFalse() throws Exception {
        // Given
        String code = """
            class Test {
                boolean x = true;
                boolean y = false;
                main {
                    boolean z = x && y;
                    write(z);
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
        assertThat(output, is("false"));
    }


    @Test
    public void givenSimpleOrComparaisonBool__whenInterpret__thenStackHaveResultTrue() throws Exception {
        // Given
        String code = """
            class Test {
                boolean x = true;
                boolean y = true;
                main {
                    boolean z = x || y;
                    write(z);
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
        assertThat(output, is("true"));
    }

    @Test
    public void givenSimpleOrComparaisonBoolTrueFalse__whenInterpret__thenStackHaveResultTrue() throws Exception {
        // Given
        String code = """
            class Test {
                boolean x = true;
                boolean y = false;
                main {
                    boolean z = x || y;
                    write(z);
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
        assertThat(output, is("true"));
    }


    @Test
    public void givenSimpleNotComparaisonBool__whenInterpret__thenStackHaveResultFalse() throws Exception {
        // Given
        String code = """
            class Test {
                boolean x = true;
                main {
                    boolean z = !(x);
                    write(z);
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
        assertThat(output, is("false"));
    }

    @Test
    public void givenSimpleNotComparaisonBool__whenInterpret__thenStackHaveResultTrue() throws Exception {
        // Given
        String code = """
            class Test {
                boolean x = false;
                main {
                    boolean z = !(x);
                    write(z);
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
        assertThat(output, is("true"));
    }

}
