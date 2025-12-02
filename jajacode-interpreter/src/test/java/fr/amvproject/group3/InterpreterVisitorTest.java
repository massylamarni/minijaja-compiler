package fr.amvproject.group3;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import fr.amvproject.group3.jjc.InterpreterVisitor;
import fr.amvproject.group3.jjc.ast.AstBuilderVisitor;
import fr.amvproject.group3.jjc.ast.nodes.program.ProgramNode;

public class InterpreterVisitorTest {

    private ProgramNode buildAstFromCode(String code) {
        JajaCodeLexer lexer = new JajaCodeLexer(CharStreams.fromString(code));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JajaCodeParser parser = new JajaCodeParser(tokens);

        ParseTree tree = parser.program();
        AstBuilderVisitor builder = new AstBuilderVisitor();

        return (ProgramNode) builder.visit(tree);
    }

    // ######################## ARIMTHMETIC ########################

    @Test
    public void givenSimpleAdditionCode__whenInterpret__thenStackHaveResult() throws Exception {
        // Given
        /*
            class Test {
                int x = 2;
                int y = 3;
                main {
                    x = x + y;
                    write(x);
                }
            }
        */
        String code = """
            1 init
            2 push(2)
            3 new(x, entier, var, 0)
            4 push(3)
            5 new(y, entier, var, 0)
            6 load(x)
            7 load(y)
            8 add
            9 store(x)
            10 load(x)
            11 write
            12 push(0)
            13 swap
            14 pop
            15 swap
            16 pop
            17 pop
            18 jcstop
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
        /*
            class Test {
                int x = 2;
                int y = 3;
                main {
                    x = x - y;
                    write(x);
                }
            }
        */
        String code = """
            1 init
            2 push(2)
            3 new(x, entier, var, 0)
            4 push(3)
            5 new(y, entier, var, 0)
            6 load(x)
            7 load(y)
            8 sub
            9 store(x)
            10 load(x)
            11 write
            12 push(0)
            13 swap
            14 pop
            15 swap
            16 pop
            17 pop
            18 jcstop
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
        /*
            class Test {
                int x = 2;
                int y = 3;
                main {
                    x = x * y;
                    write(x);
                }
            }
        */
        String code = """
            1 init
            2 push(2)
            3 new(x, entier, var, 0)
            4 push(3)
            5 new(y, entier, var, 0)
            6 load(x)
            7 load(y)
            8 mul
            9 store(x)
            10 load(x)
            11 write
            12 push(0)
            13 swap
            14 pop
            15 swap
            16 pop
            17 pop
            18 jcstop
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
        /*
            class Test {
                int x = 9;
                int y = 3;
                main {
                    x = x / y;
                    write(x);
                }
            }
        */
        String code = """
            1 init
            2 push(9)
            3 new(x, entier, var, 0)
            4 push(3)
            5 new(y, entier, var, 0)
            6 load(x)
            7 load(y)
            8 div
            9 store(x)
            10 load(x)
            11 write
            12 push(0)
            13 swap
            14 pop
            15 swap
            16 pop
            17 pop
            18 jcstop
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
        /*
            class Test {
                int x = 3;
                int y = 9;
                main {
                    boolean z = x > y;
                    write(z);
                }
            }
        */
        String code = """
            1 init
            2 push(3)
            3 new(x, entier, var, 0)
            4 push(9)
            5 new(y, entier, var, 0)
            6 load(x)
            7 load(y)
            8 sup
            9 new(z, boolean, var, 0)
            10 load(z)
            11 write
            12 push(0)
            13 swap
            14 pop
            15 swap
            16 pop
            17 swap
            18 pop
            19 pop
            20 jcstop
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
        /*
            class Test {
                int x = 9;
                int y = 3;
                main {
                    boolean z = x > y;
                    write(z);
                }
            }
        */
        String code = """
            1 init
            2 push(9)
            3 new(x, entier, var, 0)
            4 push(3)
            5 new(y, entier, var, 0)
            6 load(x)
            7 load(y)
            8 sup
            9 new(z, boolean, var, 0)
            10 load(z)
            11 write
            12 push(0)
            13 swap
            14 pop
            15 swap
            16 pop
            17 swap
            18 pop
            19 pop
            20 jcstop
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

    // ######################## COMPARAISON EQUALS ########################

    @Test
    public void givenSimpleEqualComparaisonInt__whenInterpret__thenStackHaveResultFalse() throws Exception {
        // Given
        /*
            class Test {
                int x = 9;
                int y = 3;
                main {
                    boolean z = x == y;
                    write(z);
                }
            }
        */
        String code = """
            1 init
            2 push(9)
            3 new(x, entier, var, 0)
            4 push(3)
            5 new(y, entier, var, 0)
            6 load(x)
            7 load(y)
            8 cmp
            9 new(z, boolean, var, 0)
            10 load(z)
            11 write
            12 push(0)
            13 swap
            14 pop
            15 swap
            16 pop
            17 swap
            18 pop
            19 pop
            20 jcstop
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
        /*
            class Test {
                int x = 9;
                int y = 9;
                main {
                    boolean z = x == y;
                    write(z);
                }
            }
        */
        String code = """
            1 init
            2 push(9)
            3 new(x, entier, var, 0)
            4 push(9)
            5 new(y, entier, var, 0)
            6 load(x)
            7 load(y)
            8 cmp
            9 new(z, boolean, var, 0)
            10 load(z)
            11 write
            12 push(0)
            13 swap
            14 pop
            15 swap
            16 pop
            17 swap
            18 pop
            19 pop
            20 jcstop
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
        /*
            class Test {
                boolean x = true;
                boolean y = false;
                main {
                    boolean z = x == y;
                    write(z);
                }
            }
        */
        String code = """
            1 init
            2 push(true)
            3 new(x, boolean, var, 0)
            4 push(false)
            5 new(y, boolean, var, 0)
            6 load(x)
            7 load(y)
            8 cmp
            9 new(z, boolean, var, 0)
            10 load(z)
            11 write
            12 push(0)
            13 swap
            14 pop
            15 swap
            16 pop
            17 swap
            18 pop
            19 pop
            20 jcstop
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
        /*
            class Test {
                boolean x = true;
                boolean y = true;
                main {
                    boolean z = x == y;
                    write(z);
                }
            }
        */
        String code = """
            1 init
            2 push(true)
            3 new(x, boolean, var, 0)
            4 push(true)
            5 new(y, boolean, var, 0)
            6 load(x)
            7 load(y)
            8 cmp
            9 new(z, boolean, var, 0)
            10 load(z)
            11 write
            12 push(0)
            13 swap
            14 pop
            15 swap
            16 pop
            17 swap
            18 pop
            19 pop
            20 jcstop
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
        /*
            class Test {
                int x = 1;
                main {
                    if(x == 0){
                    x+=1;
                    }
                    else {
                    x+=10;
                    };
                    write(x);
                }
            }
        */
        String code = """
            1 init
            2 push(1)
            3 new(x, entier, var, 0)
            4 load(x)
            5 push(0)
            6 cmp
            7 if(11)
            8 push(10)
            9 inc(x)
            10 goto(13)
            11 push(1)
            12 inc(x)
            13 load(x)
            14 write
            15 push(0)
            16 swap
            17 pop
            18 pop
            19 jcstop
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
        /*
            class Test {
                int x = 0;
                main {
                    if(x == 0){
                    x+=1;
                    };
                    write(x);
                }
            }
        */
        String code = """
            1 init
            2 push(0)
            3 new(x, entier, var, 0)
            4 load(x)
            5 push(0)
            6 cmp
            7 if(9)
            8 goto(11)
            9 push(1)
            10 inc(x)
            11 load(x)
            12 write
            13 push(0)
            14 swap
            15 pop
            16 pop
            17 jcstop
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
        /*
            class Test {
                int x = 1;
                main {
                    if(x == 0){
                    x+=1;
                    };
                    write(x);
                }
            }
        */
        String code = """
            1 init
            2 push(1)
            3 new(x, entier, var, 0)
            4 load(x)
            5 push(0)
            6 cmp
            7 if(9)
            8 goto(11)
            9 push(1)
            10 inc(x)
            11 load(x)
            12 write
            13 push(0)
            14 swap
            15 pop
            16 pop
            17 jcstop
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
        /*
            class Test {
                int x = 0;
                main {
                    if(x == 0){
                    x+=1;
                    }
                    else {
                    x+=10;
                    };
                    if(x == 10){
                    x = 20;
                    }
                    else {
                    x += 1;
                    };
                    write(x);
                }
            }
        */
        String code = """
            1 init
            2 push(0)
            3 new(x, entier, var, 0)
            4 load(x)
            5 push(0)
            6 cmp
            7 if(11)
            8 push(10)
            9 inc(x)
            10 goto(13)
            11 push(1)
            12 inc(x)
            13 load(x)
            14 push(10)
            15 cmp
            16 if(20)
            17 push(1)
            18 inc(x)
            19 goto(22)
            20 push(20)
            21 store(x)
            22 load(x)
            23 write
            24 push(0)
            25 swap
            26 pop
            27 pop
            28 jcstop
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
        /*
            class Test {
                int x = 0;
                main {
                   while(10 > x){
                   x+=1;
                    };
                    write(x);
                }
            }
        */
        String code = """
            1 init
            2 push(0)
            3 new(x, entier, var, 0)
            4 push(10)
            5 load(x)
            6 sup
            7 not
            8 if(12)
            9 push(1)
            10 inc(x)
            11 goto(4)
            12 load(x)
            13 write
            14 push(0)
            15 swap
            16 pop
            17 pop
            18 jcstop
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
        /*
            class Test {
                boolean x = true;
                boolean y = true;
                main {
                    boolean z = x && y;
                    write(z);
                }
            }
        */
        String code = """
            1 init
            2 push(true)
            3 new(x, boolean, var, 0)
            4 push(true)
            5 new(y, boolean, var, 0)
            6 load(x)
            7 load(y)
            8 and
            9 new(z, boolean, var, 0)
            10 load(z)
            11 write
            12 push(0)
            13 swap
            14 pop
            15 swap
            16 pop
            17 swap
            18 pop
            19 pop
            20 jcstop
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
        /*
            class Test {
                boolean x = true;
                boolean y = false;
                main {
                    boolean z = x && y;
                    write(z);
                }
            }
        */
        String code = """
            1 init
            2 push(true)
            3 new(x, boolean, var, 0)
            4 push(false)
            5 new(y, boolean, var, 0)
            6 load(x)
            7 load(y)
            8 and
            9 new(z, boolean, var, 0)
            10 load(z)
            11 write
            12 push(0)
            13 swap
            14 pop
            15 swap
            16 pop
            17 swap
            18 pop
            19 pop
            20 jcstop
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
        /*
            class Test {
                boolean x = true;
                boolean y = true;
                main {
                    boolean z = x || y;
                    write(z);
                }
            }
        */
        String code = """
            1 init
            2 push(true)
            3 new(x, boolean, var, 0)
            4 push(true)
            5 new(y, boolean, var, 0)
            6 load(x)
            7 load(y)
            8 or
            9 new(z, boolean, var, 0)
            10 load(z)
            11 write
            12 push(0)
            13 swap
            14 pop
            15 swap
            16 pop
            17 swap
            18 pop
            19 pop
            20 jcstop
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
        /*
            class Test {
                boolean x = true;
                boolean y = false;
                main {
                    boolean z = x || y;
                    write(z);
                }
            }
        */
        String code = """
            1 init
            2 push(true)
            3 new(x, boolean, var, 0)
            4 push(false)
            5 new(y, boolean, var, 0)
            6 load(x)
            7 load(y)
            8 or
            9 new(z, boolean, var, 0)
            10 load(z)
            11 write
            12 push(0)
            13 swap
            14 pop
            15 swap
            16 pop
            17 swap
            18 pop
            19 pop
            20 jcstop
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
        /*
            class Test {
                boolean x = true;
                main {
                    boolean z = !(x);
                    write(z);
                }
            }
        */
        String code = """
            1 init
            2 push(true)
            3 new(x, boolean, var, 0)
            4 load(x)
            5 not
            6 new(z, boolean, var, 0)
            7 load(z)
            8 write
            9 push(0)
            10 swap
            11 pop
            12 swap
            13 pop
            14 pop
            15 jcstop
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
        /*
            class Test {
                boolean x = false;
                main {
                    boolean z = !(x);
                    write(z);
                }
            }
        */
        String code = """
            1 init
            2 push(false)
            3 new(x, boolean, var, 0)
            4 load(x)
            5 not
            6 new(z, boolean, var, 0)
            7 load(z)
            8 write
            9 push(0)
            10 swap
            11 pop
            12 swap
            13 pop
            14 pop
            15 jcstop
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
    public void givenFunction__whenExecute__thenCorrectResult() {
        // Given
        /*
            class C {
                    int x = 5 ;
                
                int fct(int v) {
                    int y = 5 + v;
                    write(y) ;
                    return y ;
                } ;
                main {
                    fct(x);
                }
            }
        */
        String code = """
            1 init
            2 push(5)
            3 new(x, entier, var, 0)
            4 push(7)
            5 new(fct, entier, meth, 0)
            6 goto(19)
            7 new(v, entier, var, 1)
            8 push(5)
            9 load(v)
            10 add
            11 new(y, entier, var, 0)
            12 load(y)
            13 write
            14 load(y)
            15 swap
            16 pop
            17 swap
            18 return
            19 load(x)
            20 invoke(fct)
            21 swap
            22 pop
            23 pop
            24 push(0)
            25 swap
            26 pop
            27 swap
            28 pop
            29 pop
            30 jcstop
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


    @Test
    public void givenFunction__whenExecute__thenAffectResultAndPrint() {
        // Given
        /*
            class C {
                    int x = 5 ;
                
                int fct(int v) {
                    int y = 5 + v;
                    return y ;
                } ;
                main {
                    x = fct(x);
                    write(x);
                }
            }
        */
        String code = """
            1 init
            2 push(5)
            3 new(x, entier, var, 0)
            4 push(7)
            5 new(fct, entier, meth, 0)
            6 goto(17)
            7 new(v, entier, var, 1)
            8 push(5)
            9 load(v)
            10 add
            11 new(y, entier, var, 0)
            12 load(y)
            13 swap
            14 pop
            15 swap
            16 return
            17 load(x)
            18 invoke(fct)
            19 swap
            20 pop
            21 store(x)
            22 load(x)
            23 write
            24 push(0)
            25 swap
            26 pop
            27 swap
            28 pop
            29 pop
            30 jcstop            
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

    @Test
    public void givenFunction__whenExecute__thenPrintResult() {
        // Given
        /*
            class C {
                    int x = 5 ;
                
                int fct(int v) {
                    int y = 5 + v;
                    return y ;
                } ;
                main {
                    int res = fct(x);
                    write(res);
                }
            }
        */
        String code = """
            1 init
            2 push(5)
            3 new(x, entier, var, 0)
            4 push(7)
            5 new(fct, entier, meth, 0)
            6 goto(17)
            7 new(v, entier, var, 1)
            8 push(5)
            9 load(v)
            10 add
            11 new(y, entier, var, 0)
            12 load(y)
            13 swap
            14 pop
            15 swap
            16 return
            17 load(x)
            18 invoke(fct)
            19 swap
            20 pop
            21 new(res, entier, var, 0)
            22 load(res)
            23 write
            24 push(0)
            25 swap
            26 pop
            27 swap
            28 pop
            29 swap
            30 pop
            31 pop
            32 jcstop
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

    @Test
    public void testTabSimple() {
        // Given
        /*
            class C {
                    int x = 5 ;
                    int nt[3] ;
    
                main {
                    nt[0] = x ;
                    write(nt[0]);
                }
            }
        */
        String code = """
            1 init
            2 push(5)
            3 new(x, entier, var, 0)
            4 push(3)
            5 newarray(nt, entier)
            6 push(0)
            7 load(x)
            8 astore(nt)
            9 push(0)
            10 aload(nt)
            11 write
            12 push(0)
            13 swap
            14 pop
            15 swap
            16 pop
            17 pop
            18 jcstop                
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

    @Test
    public void testTabBoolean() {
        // Given
        /*
            class C {
                    boolean nt[2] ;
    
                main {
                    nt[0] = true ;
                    nt[1] = false ;
                    write(nt[0]);
                }
            }
        */
        String code = """
            1 init
            2 push(2)
            3 newarray(nt, boolean)
            4 push(0)
            5 push(true)
            6 astore(nt)
            7 push(1)
            8 push(false)
            9 astore(nt)
            10 push(0)
            11 aload(nt)
            12 write
            13 push(0)
            14 swap
            15 pop
            16 pop
            17 jcstop                
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
    public void testTabInLoop() {
        // Given
        /*
            class C {
                    int nt[3] ;
                    int i = 0 ;
    
                main {
                    while (3 > i) {
                        nt[i] = i * 10 ;
                        i++ ;
                    };
                    write(nt[2]);
                }
            }
        */
        String code = """
            1 init
            2 push(3)
            3 newarray(nt, entier)
            4 push(0)
            5 new(i, entier, var, 0)
            6 push(3)
            7 load(i)
            8 sup
            9 not
            10 if(19)
            11 load(i)
            12 load(i)
            13 push(10)
            14 mul
            15 astore(nt)
            16 push(1)
            17 inc(i)
            18 goto(6)
            19 push(2)
            20 aload(nt)
            21 write
            22 push(0)
            23 swap
            24 pop
            25 swap
            26 pop
            27 pop
            28 jcstop
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
        assertThat(output, is("20"));
    }

    @Test
    public void testTabAffectation() {
        // Given
        /*
            class C {
                    int tab1[1] ;
                    int tab2[1] ;
    
                main {
                    tab1[0] = 10 ;
                    tab2[0] += tab1[0] ;
                    write(tab2[0]);
                }
            }
        */
        String code = """
            1 init
            2 push(1)
            3 newarray(tab1, entier)
            4 push(1)
            5 newarray(tab2, entier)
            6 push(0)
            7 push(10)
            8 astore(tab1)
            9 push(0)
            10 push(0)
            11 aload(tab1)
            12 ainc(tab2)
            13 push(0)
            14 aload(tab2)
            15 write
            16 push(0)
            17 swap
            18 pop
            19 swap
            20 pop
            21 pop
            22 jcstop
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

    @Test
    public void testTabIncrement() {
        // Given
        /*
            class C {
                    int tab1[1] ;
                    
    
                main {
                    tab1[0] = 10 ;
                    tab1[0]++ ;
                    write(tab1[0]);
                }
            }
        */
        String code = """
            1 init
            2 push(1)
            3 newarray(tab1, entier)
            4 push(0)
            5 push(10)
            6 astore(tab1)
            7 push(0)
            8 push(1)
            9 ainc(tab1)
            10 push(0)
            11 aload(tab1)
            12 write
            13 push(0)
            14 swap
            15 pop
            16 pop
            17 jcstop
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
    public void testIncVariable() {
        /*
            class C {
                main {
                    int x = 5;
                    x++;
                    write(x);
                }
            }
        */
        String code = """
            1 init
            2 push(5)
            3 new(x, entier, var, 0)
            4 push(1)
            5 inc(x)
            6 load(x)
            7 write
            8 push(0)
            9 swap
            10 pop
            11 pop
            12 jcstop
        """;

        ProgramNode program = buildAstFromCode(code);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        InterpreterVisitor interpreter = new InterpreterVisitor();

        try {
            program.accept(interpreter);
        } finally {
            System.setOut(originalOut);
        }

        String output = outContent.toString().trim();
        assertThat(output, is("6"));
    }

    @Test
    public void testTabAffectationSimple() {
        /*
            class C {
                int tab1[1];
                
                main {
                    tab1[0] = 10;
                    write(tab1[0]);
                }
            }
        */
        String code = """
            1 init
            2 push(1)
            3 newarray(tab1, entier)
            4 push(0)
            5 push(10)
            6 astore(tab1)
            7 push(0)
            8 aload(tab1)
            9 write
            10 push(0)
            11 swap
            12 pop
            13 pop
            14 jcstop
        """;

        ProgramNode program = buildAstFromCode(code);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        InterpreterVisitor interpreter = new InterpreterVisitor();

        try {
            program.accept(interpreter);
        } finally {
            System.setOut(originalOut);
        }

        String output = outContent.toString().trim();
        assertThat(output, is("10"));
    }

    @Test
    public void testDebugTabIncrement() {
        /*
            class C {
                int tab1[1];
                
                main {
                    write(tab1[0]);    // 1. Valeur initiale
                    tab1[0] = 10;
                    write(tab1[0]);    // 2. Après affectation
                    tab1[0]++;
                    write(tab1[0]);    // 3. Après incrément
                }
            }
        */
        String code = """
            1 init
            2 push(1)
            3 newarray(tab1, entier)
            4 push(0)
            5 aload(tab1)
            6 write
            7 push(0)
            8 push(10)
            9 astore(tab1)
            10 push(0)
            11 aload(tab1)
            12 write
            13 push(0)
            14 push(1)
            15 ainc(tab1)
            16 push(0)
            17 aload(tab1)
            18 write
            19 push(0)
            20 swap
            21 pop
            22 pop
            23 jcstop
        """;

        ProgramNode program = buildAstFromCode(code);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        InterpreterVisitor interpreter = new InterpreterVisitor();

        try {
            program.accept(interpreter);
        } finally {
            System.setOut(originalOut);
        }

        String output = outContent.toString().trim();
        System.out.println("DEBUG OUTPUT: [" + output + "]");

        assertThat(output, is("01011"));
    }

    @Test
    public void testLengthSimple() {
        // Given
        /*
            class C {
                int nt[5];
                
                main {
                    int size = length(nt);
                    write(size);
                }
            }
        */
        String code = """
            1 init
            2 push(5)
            3 newarray(nt, entier)
            4 length(nt)
            5 new(size, entier, var, 0)
            6 load(size)
            7 write
            8 push(0)
            9 swap
            10 pop
            11 swap
            12 pop
            13 pop
            14 jcstop
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

    @Test
    public void testMethodeHard() {
        // Given
        /*
        String code = """
                class C {
                    int x = 5 ;

                int fct2(int v2) {
                    int z =  v2 * 2 ;
                    return z ;
                } ;

                int fct(int v1) {
                    int y = fct2(v1);
                    return y ;
                } ;

                main {
                    int res1 = fct(x);
                    write(res1);
                }
                }
                
                """;
        */
        String code = """ 
                1 init
                2 push(5)
                3 new(x, entier, var, 0)
                4 push(7)
                5 new(fct2, entier, meth, 0)
                6 goto(17)
                7 new(v2, entier, var, 1)
                8 load(v2)
                9 push(2)
                10 mul
                11 new(z, entier, var, 0)
                12 load(z)
                13 swap
                14 pop
                15 swap
                16 return
                17 push(20)
                18 new(fct, entier, meth, 0)
                19 goto(31)
                20 new(v1, entier, var, 1)
                21 load(v1)
                22 invoke(fct2)
                23 swap
                24 pop
                25 new(y, entier, var, 0)
                26 load(y)
                27 swap
                28 pop
                29 swap
                30 return
                31 load(x)
                32 invoke(fct)
                33 swap
                34 pop
                35 new(res1, entier, var, 0)
                36 load(res1)
                37 write
                38 push(0)
                39 swap
                40 pop
                41 swap
                42 pop
                43 swap
                44 pop
                45 swap
                46 pop
                47 pop
                48 jcstop
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

    @Test(expected = fr.amvproject.group3.exceptions.RuntimeException.class)
    public void givenDivisionByZero__whenInterpret__thenThrowRuntimeException() throws Exception {
        // Given
        /*
        String code = """
            class Test {
                int x = 10;
                int y = 0;
                main {
                    x = x / y;
                    write(x);
                }
            }
            """;
        */
        String code = """
            1 init
            2 push(10)
            3 new(x, entier, var, 0)
            4 push(0)
            5 new(y, entier, var, 0)
            6 load(x)
            7 load(y)
            8 div
            9 store(x)
            10 load(x)
            11 write
            12 push(0)
            13 swap
            14 pop
            15 swap
            16 pop
            17 pop
            18 jcstop
            """;
        ProgramNode program = buildAstFromCode(code);

        // When
        InterpreterVisitor interpreter = new InterpreterVisitor();
        program.accept(interpreter);

        // Then - exception should be thrown
    }

    @Test(expected = fr.amvproject.group3.exceptions.RuntimeException.class)
    public void givenDivisionByZeroInExpression__whenInterpret__thenThrowRuntimeException() throws Exception {
        // Given
        /*
        String code = """
            class Test {
                main {
                    int result = 5 / (3 - 3);
                    write(result);
                }
            }
            """;

         */
        String code = """
                1 init
                2 push(5)
                3 push(3)
                4 push(3)
                5 sub
                6 div
                7 new(result, entier, var, 0)
                8 load(result)
                9 write
                10 push(0)
                11 swap
                12 pop
                13 pop
                14 jcstop
                """;
        ProgramNode program = buildAstFromCode(code);

        // When
        InterpreterVisitor interpreter = new InterpreterVisitor();
        program.accept(interpreter);

        // Then - exception should be thrown
    }

    @Test(expected = fr.amvproject.group3.exceptions.RuntimeException.class)
    public void givenArrayIndexOutOfBounds__whenInterpret__thenThrowRuntimeException() throws Exception {
        // Given
        /*
        String code = """
            class Test {
                int nt[3];
                main {
                    nt[5] = 10;
                    write(nt[5]);
                }
            }
            """;

         */
        String code = """
                1 init
                2 push(3)
                3 newarray(nt, entier)
                4 push(5)
                5 push(10)
                6 astore(nt)
                7 push(5)
                8 aload(nt)
                9 write
                10 push(0)
                11 swap
                12 pop
                13 pop
                14 jcstop
            """;
        ProgramNode program = buildAstFromCode(code);

        // When
        InterpreterVisitor interpreter = new InterpreterVisitor();
        program.accept(interpreter);

        // Then - exception should be thrown
    }

    @Test(expected = fr.amvproject.group3.exceptions.RuntimeException.class)
    public void givenNegativeArrayIndex__whenInterpret__thenThrowRuntimeException() throws Exception {
        // Given
        /*
        String code = """
            class Test {
                int nt[3];
                main {
                    int value = nt[-1];
                    write(value);
                }
            }
            """;

         */
        String code = """
                1 init
                2 push(3)
                3 newarray(nt, entier)
                4 push(1)
                5 neg
                6 aload(nt)
                7 new(value, entier, var, 0)
                8 load(value)
                9 write
                10 push(0)
                11 swap
                12 pop
                13 swap
                14 pop
                15 pop
                16 jcstop
            """;
        ProgramNode program = buildAstFromCode(code);

        // When
        InterpreterVisitor interpreter = new InterpreterVisitor();
        program.accept(interpreter);

        // Then - exception should be thrown
    }

    @Test(expected = fr.amvproject.group3.exceptions.RuntimeException.class)
    public void givenArrayIndexOutOfBoundsInLoop__whenInterpret__thenThrowRuntimeException() throws Exception {
        // Given
        /*
        String code = """
            class Test {
                int nt[3];
                int i = 0;
                main {
                    while (5 > i) {
                        nt[i] = i * 10;
                        i++;
                    };
                }
            }
            """;

         */
        String code = """
            1 init
            2 push(3)
            3 newarray(nt, entier)
            4 push(0)
            5 new(i, entier, var, 0)
            6 push(5)
            7 load(i)
            8 sup
            9 not
            10 if(19)
            11 load(i)
            12 load(i)
            13 push(10)
            14 mul
            15 astore(nt)
            16 push(1)
            17 inc(i)
            18 goto(6)
            19 push(0)
            20 swap
            21 pop
            22 swap
            23 pop
            24 pop
            25 jcstop
            """;
        ProgramNode program = buildAstFromCode(code);

        // When
        InterpreterVisitor interpreter = new InterpreterVisitor();
        program.accept(interpreter);

        // Then - exception should be thrown
    }

    @Test(expected = fr.amvproject.group3.exceptions.RuntimeException.class)
    public void givenInfiniteLoop__whenInterpret__thenThrowRuntimeException() throws Exception {
        // Given
        /*
        String code = """
            class Test {
                int x = 0;
                main {
                    while (x == 0) {
                        x = 0;
                    };
                    write(x);
                }
            }
            """;
        */
        String code = """
                1 init
                2 push(0)
                3 new(x, entier, var, 0)
                4 load(x)
                5 push(0)
                6 cmp
                7 not
                8 if(12)
                9 push(0)
                10 store(x)
                11 goto(4)
                12 load(x)
                13 write
                14 push(0)
                15 swap
                16 pop
                17 pop
                18 jcstop """;
        ProgramNode program = buildAstFromCode(code);

        // When
        InterpreterVisitor interpreter = new InterpreterVisitor();
        program.accept(interpreter);

        // Then - exception should be thrown after LOOP_LIMIT iterations
    }

    @Test(expected = fr.amvproject.group3.exceptions.RuntimeException.class)
    public void givenInfiniteLoopWithIncrement__whenInterpret__thenThrowRuntimeException() throws Exception {
        // Given
        /*
        String code = """
            class Test {
                int x = 1;
                main {
                    while (x > 0) {
                        x++;
                    };
                    write(x);
                }
            }
            """;

         */
        String code = """
             1 init
             2 push(1)
             3 new(x, entier, var, 0)
             4 load(x)
             5 push(0)
             6 sup
             7 not
             8 if(12)
             9 push(1)
             10 inc(x)
             11 goto(4)
             12 load(x)
             13 write
             14 push(0)
             15 swap
             16 pop
             17 pop
             18 jcstop
                
            """;
        ProgramNode program = buildAstFromCode(code);

        // When
        InterpreterVisitor interpreter = new InterpreterVisitor();
        program.accept(interpreter);

        // Then - exception should be thrown after LOOP_LIMIT iterations
    }

    @Test(expected = fr.amvproject.group3.exceptions.RuntimeException.class)
    public void givenInfiniteRecursion__whenInterpret__thenThrowRuntimeException() throws Exception {
        // Given
        /*
        String code = """
            class Test {
                int fct(int n) {
                    return fct(n + 1);
                };
                main {
                    int result = fct(0);
                    write(result);
                }
            }
            """;

         */
        String code = """
                1 init
                2 push(5)
                3 new(fct, entier, meth, 0)
                4 goto(14)
                5 new(n, entier, var, 1)
                6 load(n)
                7 push(1)
                8 add
                9 invoke(fct)
                10 swap
                11 pop
                12 swap
                13 return
                14 push(0)
                15 invoke(fct)
                16 swap
                17 pop
                18 new(result, entier, var, 0)
                19 load(result)
                20 write
                21 push(0)
                22 swap
                23 pop
                24 swap
                25 pop
                26 pop
                27 jcstop
                """;
        ProgramNode program = buildAstFromCode(code);

        // When
        InterpreterVisitor interpreter = new InterpreterVisitor();
        program.accept(interpreter);

        // Then - exception should be thrown after RECURSION_LIMIT calls
    }

    @Test(expected = fr.amvproject.group3.exceptions.RuntimeException.class)
    public void givenInfiniteRecursionWithoutCondition__whenInterpret__thenThrowRuntimeException() throws Exception {
        // Given
        /*
        String code = """
            class Test {
                int factorial(int n) {
                    return n * factorial(n - 1);
                };
                main {
                    int result = factorial(5);
                    write(result);
                }
            }
            """;

         */
        String code = """
                1 init
                2 push(5)
                3 new(factorial, entier, meth, 0)
                4 goto(16)
                5 new(n, entier, var, 1)
                6 load(n)
                7 load(n)
                8 push(1)
                9 sub
                10 invoke(factorial)
                11 swap
                12 pop
                13 mul
                14 swap
                15 return
                16 push(5)
                17 invoke(factorial)
                18 swap
                19 pop
                20 new(result, entier, var, 0)
                21 load(result)
                22 write
                23 push(0)
                24 swap
                25 pop
                26 swap
                27 pop
                28 pop
                29 jcstop
                """;
        ProgramNode program = buildAstFromCode(code);

        // When
        InterpreterVisitor interpreter = new InterpreterVisitor();
        program.accept(interpreter);

        // Then - exception should be thrown after RECURSION_LIMIT calls
    }

    @Test
    public void givenValidRecursion__whenInterpret__thenCorrectResult() throws Exception {
        // Given
        /*
        String code = """
            class Test {
                int factorial(int n) {
                    if (n == 0) {
                        return 1;
                    } else {
                        return n * factorial(n - 1);
                    };
                };
                main {
                    int result = factorial(5);
                    write(result);
                }
            }
            """;

         */
        String code = """
                1 init
                2 push(5)
                3 new(factorial, entier, meth, 0)
                4 goto(22)
                5 new(n, entier, var, 1)
                6 load(n)
                7 push(0)
                8 cmp
                9 if(19)
                10 load(n)
                11 load(n)
                12 push(1)
                13 sub
                14 invoke(factorial)
                15 swap
                16 pop
                17 mul
                18 goto(20)
                19 push(1)
                20 swap
                21 return
                22 push(5)
                23 invoke(factorial)
                24 swap
                25 pop
                26 new(result, entier, var, 0)
                27 load(result)
                28 write
                29 push(0)
                30 swap
                31 pop
                32 swap
                33 pop
                34 pop
                35 jcstop
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
        assertThat(output, is("120"));
    }

}

