package fr.amvproject.group3;

import fr.amvproject.group3.jjc.ast.PrintVisitor;
import fr.amvproject.group3.jjc.ast.nodes.program.ProgramNode;
import fr.amvproject.group3.mjj.ast.AstNode;
import fr.amvproject.group3.mjj.utils.AstOperations;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class CompilerVisitorIntegrationTest {


    private String loadResource(String filename) throws IOException {
        InputStream is = getClass().getClassLoader()
                .getResourceAsStream(filename);
        assertThat(is, is(notNullValue()));

        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }


    @Test
    public void givenMjjFileDeclaration_whenCompile_thenOutputIsCorrect() throws Exception {
        //Given
        String source = loadResource("test1.mjj");
        AstNode ast = AstOperations.getAstFromString(source);
        CompilerVisitor compilerVisitor = new CompilerVisitor();
        String goal = """
                1 init
                2 push(0)
                2 new(x, entier, var, 0)
                3 push(10)
                4 new(y, entier, var, 0)
                5 push(0)
                6 swap
                7 pop
                8 swap
                9 pop
                10 pop
                11 jcstop
                """;

        //When
        String jajacode = "";
        ProgramNode programNode = new ProgramNode(ast.accept(compilerVisitor));
        PrintVisitor printVisitor = new PrintVisitor();
        jajacode = programNode.accept(printVisitor);

        //  Then
        assertThat(jajacode.trim(),is(goal.trim()));
    }


    @Test
    public void givenMjjFileAddition_whenCompile_thenOutputIsCorrect() throws Exception {
        //Given
        String source = loadResource("test2.mjj");
        AstNode ast = AstOperations.getAstFromString(source);
        CompilerVisitor compilerVisitor = new CompilerVisitor();
        String goal = """
                1 init
                2 push(0)
                3 new(a, entier, var, 0)
                4 push(0)
                5 new(b, entier, var, 0)
                6 push(3)
                7 store(a)
                8 load(a)
                9 push(5)
                10 add
                11 store(b)
                12 push(0)
                13 swap
                14 pop
                15 swap
                16 pop
                17 pop
                18 jcstop
                """;

        //When
        String jajacode = "";
        ProgramNode programNode = new ProgramNode(ast.accept(compilerVisitor));
        PrintVisitor printVisitor = new PrintVisitor();
        jajacode = programNode.accept(printVisitor);

        //  Then
        assertThat(jajacode.trim(),is(goal.trim()));
    }

    @Test
    public void givenMjjFileIfElse_whenCompile_thenOutputIsCorrect() throws Exception {
        //Given
        String source = loadResource("test3.mjj");
        AstNode ast = AstOperations.getAstFromString(source);
        CompilerVisitor compilerVisitor = new CompilerVisitor();
        String goal = """
                1 init
                2 push(0)
                3 new(a, entier, var, 0)
                4 push(false)
                5 new(b, boolean, var, 0)
                6 push(1)
                7 store(a)
                8 load(a)
                9 push(0)
                10 sup
                11 store(flag)
                12 load(flag)
                13 if(17)
                14 push(20)
                15 store(a)
                16 goto(19)
                17 push(10)
                18 store(a)
                19 push(0)
                20 swap
                21 pop
                22 swap
                23 pop
                24 pop
                25 jcstop
                """;

        //When
        String jajacode = "";
        ProgramNode programNode = new ProgramNode(ast.accept(compilerVisitor));
        PrintVisitor printVisitor = new PrintVisitor();
        jajacode = programNode.accept(printVisitor);

        //  Then
        assertThat(jajacode.trim(),is(goal.trim()));
    }


    @Test
    public void givenMjjFileWhile_whenCompile_thenOutputIsCorrect() throws Exception {
        //Given
        String source = loadResource("test4.mjj");
        AstNode ast = AstOperations.getAstFromString(source);
        CompilerVisitor compilerVisitor = new CompilerVisitor();
        String goal = """
                1 init
                2 push(0)
                3 new(x, entier, var, 0)
                4 push(5)
                5 store(x)
                6 load(x)
                7 push(0)
                8 sup
                9 nop
                10 if(...)
                11 load(x)
                12 push(1)
                13 sub
                14 store(x)
                15 goto(6)
                16 push(0)
                17 swap
                18 pop
                19 pop
                20 jcstop
                """;

        //When
        String jajacode = "";
        ProgramNode programNode = new ProgramNode(ast.accept(compilerVisitor));
        PrintVisitor printVisitor = new PrintVisitor();
        jajacode = programNode.accept(printVisitor);

        //  Then
        assertThat(jajacode.trim(),is(goal.trim()));
    }

    @Test
    public void givenMjjFileMeth_whenCompile_thenOutputIsCorrect() throws Exception {
        //Given
        String source = loadResource("test5.mjj");
        AstNode ast = AstOperations.getAstFromString(source);
        CompilerVisitor compilerVisitor = new CompilerVisitor();
        String goal = """
                1 init
                2 push(0)
                3 new(x,entier, var, 0)
                4 push(7)
                5 new(f,entier, meth, 0)
                6 goto (...)
                7 new(p,entier, var, 0)
                8 load(p)
                9 swap
                10 return
                11 push(3)
                12 store(x)
                13 load(x)
                14 load(x)
                15 invoke(f)
                16 swap
                17 pop
                18 add
                19 store(x)
                20 push(0)
                21 swap
                22 pop
                23 swap
                24 pop
                25 pop
                26 jcstop
                """;

        //When
        String jajacode = "";
        ProgramNode programNode = new ProgramNode(ast.accept(compilerVisitor));
        PrintVisitor printVisitor = new PrintVisitor();
        jajacode = programNode.accept(printVisitor);

        //  Then
        assertThat(jajacode.trim(),is(goal.trim()));
    }


}
