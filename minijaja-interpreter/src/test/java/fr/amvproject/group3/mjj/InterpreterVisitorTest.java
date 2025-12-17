package fr.amvproject.group3.mjj;


import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

public class InterpreterVisitorTest {

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

        // When
        Interpreter interpreter = new Interpreter(code);
        interpreter.run();        
        // Then
        String output = interpreter.getOutputBuffer().get(0);
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

        // When
        Interpreter interpreter = new Interpreter(code);
        interpreter.run();        
        // Then
        String output = interpreter.getOutputBuffer().get(0);
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

        Interpreter interpreter = new Interpreter(code);
        interpreter.run();        
        // Then
        String output = interpreter.getOutputBuffer().get(0);
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

        Interpreter interpreter = new Interpreter(code);
        interpreter.run();        

        // Then
        String output = interpreter.getOutputBuffer().get(0);
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

        Interpreter interpreter = new Interpreter(code);
        interpreter.run();        

        // Then
        String output = interpreter.getOutputBuffer().get(0);
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

        Interpreter interpreter = new Interpreter(code);
        interpreter.run();        

        // Then
        String output = interpreter.getOutputBuffer().get(0);
        assertThat(output, is("true"));
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

        Interpreter interpreter = new Interpreter(code);
        interpreter.run();        

        // Then
        String output = interpreter.getOutputBuffer().get(0);
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

        Interpreter interpreter = new Interpreter(code);
        interpreter.run();        

        // Then
        String output = interpreter.getOutputBuffer().get(0);
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

        Interpreter interpreter = new Interpreter(code);
        interpreter.run();        

        // Then
        String output = interpreter.getOutputBuffer().get(0);
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

        Interpreter interpreter = new Interpreter(code);
        interpreter.run();        

        // Then
        String output = interpreter.getOutputBuffer().get(0);
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

        Interpreter interpreter = new Interpreter(code);
        interpreter.run();        

        // Then
        String output = interpreter.getOutputBuffer().get(0);
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

        Interpreter interpreter = new Interpreter(code);
        interpreter.run();        

        // Then
        String output = interpreter.getOutputBuffer().get(0);
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

        Interpreter interpreter = new Interpreter(code);
        interpreter.run();        

        // Then
        String output = interpreter.getOutputBuffer().get(0);
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

        Interpreter interpreter = new Interpreter(code);
        interpreter.run();        

        // Then
        String output = interpreter.getOutputBuffer().get(0);
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

        Interpreter interpreter = new Interpreter(code);
        interpreter.run();        

        // Then
        String output = interpreter.getOutputBuffer().get(0);
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

        Interpreter interpreter = new Interpreter(code);
        interpreter.run();        

        // Then
        String output = interpreter.getOutputBuffer().get(0);
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

        Interpreter interpreter = new Interpreter(code);
        interpreter.run();        

        // Then
        String output = interpreter.getOutputBuffer().get(0);
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

        Interpreter interpreter = new Interpreter(code);
        interpreter.run();        

        // Then
        String output = interpreter.getOutputBuffer().get(0);
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

        Interpreter interpreter = new Interpreter(code);
        interpreter.run();        

        // Then
        String output = interpreter.getOutputBuffer().get(0);
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

        Interpreter interpreter = new Interpreter(code);
        interpreter.run();        

        // Then
        String output = interpreter.getOutputBuffer().get(0);
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

        Interpreter interpreter = new Interpreter(code);
        interpreter.run();        

        // Then
        String output = interpreter.getOutputBuffer().get(0);
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

        Interpreter interpreter = new Interpreter(code);
        interpreter.run();        

        // Then
        String output = interpreter.getOutputBuffer().get(0);
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

        Interpreter interpreter = new Interpreter(code);
        interpreter.run();        

        // Then
        String output = interpreter.getOutputBuffer().get(0);
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

        Interpreter interpreter = new Interpreter(code);
        interpreter.run();        

        // Then
        String output = interpreter.getOutputBuffer().get(0);
        assertThat(output, is("10"));
    }

    @Test
    public void testTabSimple() {
        // Given
        String code = """
                class C {
                    int x = 5 ;
                    int nt[3] ;
    
                main {
                    nt[0] = x ;
                    write(nt[0]);
                }
                }
                
                """;

        Interpreter interpreter = new Interpreter(code);
        interpreter.run();        

        // Then
        String output = interpreter.getOutputBuffer().get(0);
        assertThat(output, is("5"));
    }

    @Test
    public void testTabBoolean() {
        // Given
        String code = """
                class C {
                    boolean nt[2] ;
    
                main {
                    nt[0] = true ;
                    nt[1] = false ;
                    write(nt[0]);
                }
                }
                
                """;

        Interpreter interpreter = new Interpreter(code);
        interpreter.run();        

        // Then
        String output = interpreter.getOutputBuffer().get(0);
        assertThat(output, is("true"));
    }

    @Test
    public void testTabInLoop() {
        // Given
        String code = """
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
                
                """;

        Interpreter interpreter = new Interpreter(code);
        interpreter.run();        

        // Then
        String output = interpreter.getOutputBuffer().get(0);
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

        Interpreter interpreter = new Interpreter(code);
        interpreter.run();        

        // Then
        String output = interpreter.getOutputBuffer().get(0);
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

        Interpreter interpreter = new Interpreter(code);
        interpreter.run();        

        // Then
        String output = interpreter.getOutputBuffer().get(0);
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

        Interpreter interpreter = new Interpreter(code);
        interpreter.run();
        String output = interpreter.getOutputBuffer().get(0);
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

        Interpreter interpreter = new Interpreter(code);
        interpreter.run();
        String output = interpreter.getOutputBuffer().get(0);
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

        Interpreter interpreter = new Interpreter(code);
        interpreter.run();
        String output1 = interpreter.getOutputBuffer().get(0);
        String output2 = interpreter.getOutputBuffer().get(1);
        String output3 = interpreter.getOutputBuffer().get(2);

        assertThat(output1, is("0"));
        assertThat(output2, is("10"));
        assertThat(output3, is("11"));
    }

    @Test
    public void testLengthSimple() {
        // Given
        String code = """
        class C {
            int nt[5];
            
        main {
            int size = length(nt);
            write(size);
        }
        }
        """;

        Interpreter interpreter = new Interpreter(code);
        interpreter.run();
        

        // Then
        String output = interpreter.getOutputBuffer().get(0);
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

        Interpreter interpreter = new Interpreter(code);
        interpreter.run();        

        // Then
        String output = interpreter.getOutputBuffer().get(0);
        assertThat(output, is("10"));
    }

    // ######################## ERROR HANDLING ########################

    @Test
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
        // When
        Interpreter interpreter = new Interpreter(code);
        interpreter.run();
        assertTrue(interpreter.getRuntimeErrorsCollector().hasErrors());

        // Then - exception should be thrown
    }

    @Test
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
        // When
        Interpreter interpreter = new Interpreter(code);
        interpreter.run();
        assertTrue(interpreter.getRuntimeErrorsCollector().hasErrors());

        // Then - exception should be thrown
    }

    @Test
    public void givenArrayIndexOutOfBounds__whenInterpret__thenThrowRuntimeException() throws Exception {
        // Given
        String code = """
            class Test {
                int nt[3];
                main {
                    nt[5] = 10;
                    write(nt[5]);
                }
            }
            """;
        // When
        Interpreter interpreter = new Interpreter(code);
        interpreter.run();
        assertTrue(interpreter.getRuntimeErrorsCollector().hasErrors());

        // Then - exception should be thrown
    }

    @Test
    public void givenNegativeArrayIndex__whenInterpret__thenThrowRuntimeException() throws Exception {
        // Given
        String code = """
            class Test {
                int nt[3];
                main {
                    int value = nt[-1];
                    write(value);
                }
            }
            """;
        // When
        Interpreter interpreter = new Interpreter(code);
        interpreter.run();
        assertTrue(interpreter.getRuntimeErrorsCollector().hasErrors());

        // Then - exception should be thrown
    }

    @Test
    public void givenArrayIndexOutOfBoundsInLoop__whenInterpret__thenThrowRuntimeException() throws Exception {
        // Given
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
        // When
        Interpreter interpreter = new Interpreter(code);
        interpreter.run();
        assertTrue(interpreter.getRuntimeErrorsCollector().hasErrors());

        // Then - exception should be thrown
    }

    @Test
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
        // When
        Interpreter interpreter = new Interpreter(code);
        interpreter.run();
        assertTrue(interpreter.getRuntimeErrorsCollector().hasErrors());

        // Then - exception should be thrown after LOOP_LIMIT iterations
    }

    @Test
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
        // When
        Interpreter interpreter = new Interpreter(code);
        interpreter.run();
        assertTrue(interpreter.getRuntimeErrorsCollector().hasErrors());

        // Then - exception should be thrown after LOOP_LIMIT iterations
    }

    @Test
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
        // When
        Interpreter interpreter = new Interpreter(code);
        interpreter.run();
        assertTrue(interpreter.getRuntimeErrorsCollector().hasErrors());

        // Then - exception should be thrown after RECURSION_LIMIT calls
    }

    @Test
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
        // When
        Interpreter interpreter = new Interpreter(code);
        interpreter.run();
        assertTrue(interpreter.getRuntimeErrorsCollector().hasErrors());

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
         Interpreter interpreter = new Interpreter(code);
         interpreter.run();
        

        // Then
        String output = interpreter.getOutputBuffer().get(0);
        assertThat(output, is("120"));
    }
}
