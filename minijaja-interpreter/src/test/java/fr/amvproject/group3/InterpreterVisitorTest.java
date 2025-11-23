package fr.amvproject.group3;


import fr.amvproject.group3.mjj.ast.AstBuilderVisitor;
import fr.amvproject.group3.mjj.ast.nodes.program.ProgramNode;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Ignore;
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
        AstBuilderVisitor builder = new AstBuilderVisitor();

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


    @Ignore("This check is made at compilation, it is unnecessary here")
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
                    };
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
                    };
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
                    };
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
                    };
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

    @Test
    public void givenFunction__whenExecute__thenCorrectResult() {
        // Given
        String code = """
                class C {
                    int x = 5 ;
                
                int fct(int var) {
                    int y = 5 + var;
                    write(y) ;
                    return y ;
                } ;
                main {
                    fct(x);
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


    @Test
    public void givenFunction__whenExecute__thenAffectResultAndPrint() {
        // Given
        String code = """
                class C {
                    int x = 5 ;
                
                int fct(int var) {
                    int y = 5 + var;
                    return y ;
                } ;
                main {
                    x = fct(x);
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

    @Test
    public void givenFunction__whenExecute__thenPrintResult() {
        // Given
        String code = """
                class C {
                    int x = 5 ;
                
                int fct(int var) {
                    int y = 5 + var;
                    return y ;
                } ;
                main {
                    int res = fct(x);
                    write(res);
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

    @Test
    public void testTabSimple() {
        // Given
        String code = """
                class C {
                    int x = 5 ;
                    int tab[3] ;
    
                main {
                    tab[0] = x ;
                    write(tab[0]);
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

    @Test
    public void testTabBoolean() {
        // Given
        String code = """
                class C {
                    boolean tab[2] ;
    
                main {
                    tab[0] = true ;
                    tab[1] = false ;
                    write(tab[0]);
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
    public void testTabInLoop() {
        // Given
        String code = """
                class C {
                    int tab[3] ;
                    int i = 0 ;
    
                main {
                    while (3 > i) {
                        tab[i] = i * 10 ;
                        i++ ;
                    };
                    write(tab[2]);
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
        assertThat(output, is("20"));
    }

    @Test
    public void testTabAffectation() {
        // Given
        String code = """
                class C {
                    int tab1[1] ;
                    int tab2[1] ;
    
                main {
                    tab1[0] = 10 ;
                    tab2[0] += tab1[0] ;
                    write(tab2[0]);
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

    @Test
    public void testTabIncrement() {
        // Given
        String code = """
                class C {
                    int tab1[1] ;
                    
    
                main {
                    tab1[0] = 10 ;
                    tab1[0]++ ;
                    write(tab1[0]);
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
    public void testIncVariable() {
        String code = """
        class C {
            main {
                int x = 5;
                x++;
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
            program.accept(interpreter);
        } finally {
            System.setOut(originalOut);
        }

        String output = outContent.toString().trim();
        assertThat(output, is("6"));
    }

    @Test
    public void testTabAffectationSimple() {
        String code = """
            class C {
                int tab1[1];
                
            main {
                tab1[0] = 10;
                write(tab1[0]);
            }
            }
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
        String code = """
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
        String code = """
        class C {
            int tab[5];
            
        main {
            int size = length(tab);
            write(size);
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
        assertThat(interpreter.getStack().getQueue().size(), is(0));
    }

    @Test
    public void testMethodeHard() {
        // Given
        String code = """
                class C {
                    int x = 5 ;
                    
                int fct2(int var2) {
                    int z =  var2 * 2 ;
                    return z ;
                } ;
                
                int fct(int var) {
                    int y = fct2(var);
                    return y ;
                } ;
                
                main {
                    int res1 = fct(x);
                    write(res1);
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

    // ######################## ERROR HANDLING ########################

    @Test(expected = fr.amvproject.group3.exceptions.RuntimeException.class)
    public void givenDivisionByZero__whenInterpret__thenThrowRuntimeException() throws Exception {
        // Given
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
        ProgramNode program = buildAstFromCode(code);

        // When
        InterpreterVisitor interpreter = new InterpreterVisitor();
        program.accept(interpreter);

        // Then - exception should be thrown
    }

    @Test(expected = fr.amvproject.group3.exceptions.RuntimeException.class)
    public void givenDivisionByZeroInExpression__whenInterpret__thenThrowRuntimeException() throws Exception {
        // Given
        String code = """
            class Test {
                main {
                    int result = 5 / (3 - 3);
                    write(result);
                }
            }
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
        String code = """
            class Test {
                int tab[3];
                main {
                    tab[5] = 10;
                    write(tab[5]);
                }
            }
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
        String code = """
            class Test {
                int tab[3];
                main {
                    int value = tab[-1];
                    write(value);
                }
            }
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
        String code = """
            class Test {
                int tab[3];
                int i = 0;
                main {
                    while (5 > i) {
                        tab[i] = i * 10;
                        i++;
                    };
                }
            }
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
        ProgramNode program = buildAstFromCode(code);

        // When
        InterpreterVisitor interpreter = new InterpreterVisitor();
        program.accept(interpreter);

        // Then - exception should be thrown after LOOP_LIMIT iterations
    }

    @Test(expected = fr.amvproject.group3.exceptions.RuntimeException.class)
    public void givenInfiniteLoopWithIncrement__whenInterpret__thenThrowRuntimeException() throws Exception {
        // Given
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
        ProgramNode program = buildAstFromCode(code);

        // When
        InterpreterVisitor interpreter = new InterpreterVisitor();
        program.accept(interpreter);

        // Then - exception should be thrown after LOOP_LIMIT iterations
    }

    @Test(expected = fr.amvproject.group3.exceptions.RuntimeException.class)
    public void givenInfiniteRecursion__whenInterpret__thenThrowRuntimeException() throws Exception {
        // Given
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
        ProgramNode program = buildAstFromCode(code);

        // When
        InterpreterVisitor interpreter = new InterpreterVisitor();
        program.accept(interpreter);

        // Then - exception should be thrown after RECURSION_LIMIT calls
    }

    @Test(expected = fr.amvproject.group3.exceptions.RuntimeException.class)
    public void givenInfiniteRecursionWithoutCondition__whenInterpret__thenThrowRuntimeException() throws Exception {
        // Given
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
        ProgramNode program = buildAstFromCode(code);

        // When
        InterpreterVisitor interpreter = new InterpreterVisitor();
        program.accept(interpreter);

        // Then - exception should be thrown after RECURSION_LIMIT calls
    }

    @Test
    public void givenValidRecursion__whenInterpret__thenCorrectResult() throws Exception {
        // Given
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
