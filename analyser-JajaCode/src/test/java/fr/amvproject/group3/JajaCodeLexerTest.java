package fr.amvproject.group3;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;

import java.util.List;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class JajaCodeLexerTest {


    @Test
    public void givenInit__whenGenerateTokens__thenCorrectLexer() {
        String input = "1 init";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(),is(2));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.NOMBRE));
        assertThat(tokens.get(0).getText(), is("1"));

        assertThat(tokens.get(1).getType(),is(JajaCodeLexer.INIT));
        assertThat( tokens.get(1).getText(),is("init"));

    }

    @Test
    public void givenPush__whenGenerateTokens__thenCorrectLexer() {
        String input = "1 push(0)";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(),is(5));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.NOMBRE));
        assertThat(tokens.get(0).getText(), is("1"));

        assertThat(tokens.get(1).getType(),is(JajaCodeLexer.PUSH));
        assertThat( tokens.get(1).getText(),is("push"));

        assertThat(tokens.get(2).getType(), is(JajaCodeLexer.LPAREN));
        assertThat(tokens.get(2).getText(), is("("));

        assertThat(tokens.get(3).getType(), is(JajaCodeLexer.NOMBRE));
        assertThat(tokens.get(3).getText(), is("0"));

        assertThat(tokens.get(4).getType(), is(JajaCodeLexer.RPAREN));
        assertThat(tokens.get(4).getText(), is(")"));

    }

    @Test
    public void givenGoto__whenGenerateTokens__thenCorrectLexer() {
        String input = "6 goto(11)";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(),is(5));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.NOMBRE));
        assertThat(tokens.get(0).getText(), is("6"));

        assertThat(tokens.get(1).getType(),is(JajaCodeLexer.GOTO));
        assertThat( tokens.get(1).getText(),is("goto"));

        assertThat(tokens.get(2).getType(), is(JajaCodeLexer.LPAREN));
        assertThat(tokens.get(2).getText(), is("("));

        assertThat(tokens.get(3).getType(), is(JajaCodeLexer.NOMBRE));
        assertThat(tokens.get(3).getText(), is("11"));

        assertThat(tokens.get(4).getType(), is(JajaCodeLexer.RPAREN));
        assertThat(tokens.get(4).getText(), is(")"));

    }

    @Test
    public void givenStore__whenGenerateTokens__thenCorrectLexer() {
        String input = "12 store(x)";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(),is(5));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.NOMBRE));
        assertThat(tokens.get(0).getText(), is("12"));

        assertThat(tokens.get(1).getType(),is(JajaCodeLexer.STORE));
        assertThat( tokens.get(1).getText(),is("store"));

        assertThat(tokens.get(2).getType(), is(JajaCodeLexer.LPAREN));
        assertThat(tokens.get(2).getText(), is("("));

        assertThat(tokens.get(3).getType(), is(JajaCodeLexer.IDENTIFICATEUR));
        assertThat(tokens.get(3).getText(), is("x"));

        assertThat(tokens.get(4).getType(), is(JajaCodeLexer.RPAREN));
        assertThat(tokens.get(4).getText(), is(")"));

    }

    @Test
    public void givenInvoke__whenGenerateTokens__thenCorrectLexer() {
        String input = "14 invoke(f)";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(),is(5));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.NOMBRE));
        assertThat(tokens.get(0).getText(), is("14"));

        assertThat(tokens.get(1).getType(),is(JajaCodeLexer.INVOKE));
        assertThat( tokens.get(1).getText(),is("invoke"));

        assertThat(tokens.get(2).getType(), is(JajaCodeLexer.LPAREN));
        assertThat(tokens.get(2).getText(), is("("));

        assertThat(tokens.get(3).getType(), is(JajaCodeLexer.IDENTIFICATEUR));
        assertThat(tokens.get(3).getText(), is("f"));

        assertThat(tokens.get(4).getType(), is(JajaCodeLexer.RPAREN));
        assertThat(tokens.get(4).getText(), is(")"));

    }
    @Test
    public void givenInc__whenGenerateTokens__thenCorrectLexer() {
        String input = "17 inc(x)";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(),is(5));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.NOMBRE));
        assertThat(tokens.get(0).getText(), is("17"));

        assertThat(tokens.get(1).getType(),is(JajaCodeLexer.INC));
        assertThat( tokens.get(1).getText(),is("inc"));

        assertThat(tokens.get(2).getType(), is(JajaCodeLexer.LPAREN));
        assertThat(tokens.get(2).getText(), is("("));

        assertThat(tokens.get(3).getType(), is(JajaCodeLexer.IDENTIFICATEUR));
        assertThat(tokens.get(3).getText(), is("x"));

        assertThat(tokens.get(4).getType(), is(JajaCodeLexer.RPAREN));
        assertThat(tokens.get(4).getText(), is(")"));

    }

    @Test
    public void givenSwap__whenGenerateTokens__thenCorrectLexer() {
        String input = "9 swap";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(),is(2));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.NOMBRE));
        assertThat(tokens.get(0).getText(), is("9"));

        assertThat(tokens.get(1).getType(),is(JajaCodeLexer.SWAP));
        assertThat( tokens.get(1).getText(),is("swap"));

    }
    @Test
    public void givenJcstop__whenGenerateTokens__thenCorrectLexer() {
        String input = "24 jcstop";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(),is(2));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.NOMBRE));
        assertThat(tokens.get(0).getText(), is("24"));

        assertThat(tokens.get(1).getType(),is(JajaCodeLexer.JCSTOP));
        assertThat( tokens.get(1).getText(),is("jcstop"));

    }

    @Test
    public void givenComment__whenGenerateTokens__thenCorrectLexer() {
        String input = "9 swap // adr retour ↔ val. résultat";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(),is(2));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.NOMBRE));
        assertThat(tokens.get(0).getText(), is("9"));

        assertThat(tokens.get(1).getType(),is(JajaCodeLexer.SWAP));
        assertThat( tokens.get(1).getText(),is("swap"));

    }

    @Test
    public void givenPop__whenGenerateTokens__thenCorrectLexer() {
        String input = "17 pop";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(),is(2));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.NOMBRE));
        assertThat(tokens.get(0).getText(), is("17"));

        assertThat(tokens.get(1).getType(),is(JajaCodeLexer.POP));
        assertThat( tokens.get(1).getText(),is("pop"));

    }

    @Test
    public void givenReturn__whenGenerateTokens__thenCorrectLexer() {
        String input = "10 return";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(),is(2));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.NOMBRE));
        assertThat(tokens.get(0).getText(), is("10"));

        assertThat(tokens.get(1).getType(),is(JajaCodeLexer.RETURN));
        assertThat( tokens.get(1).getText(),is("return"));

    }

    @Test
    public void givenLoad__whenGenerateTokens__thenCorrectLexer() {
        String input = "8 load(p)";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(),is(5));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.NOMBRE));
        assertThat(tokens.get(0).getText(), is("8"));

        assertThat(tokens.get(1).getType(),is(JajaCodeLexer.LOAD));
        assertThat( tokens.get(1).getText(),is("load"));

        assertThat(tokens.get(2).getType(), is(JajaCodeLexer.LPAREN));
        assertThat(tokens.get(2).getText(), is("("));

        assertThat(tokens.get(3).getType(), is(JajaCodeLexer.IDENTIFICATEUR));
        assertThat(tokens.get(3).getText(), is("p"));

        assertThat(tokens.get(4).getType(), is(JajaCodeLexer.RPAREN));
        assertThat(tokens.get(4).getText(), is(")"));

    }

    @Test
    public void givenNew__whenGenerateTokens__thenCorrectLexer() {
        String input = "3 new(x,entier,var,0)";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(),is(11));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.NOMBRE));
        assertThat(tokens.get(0).getText(), is("3"));

        assertThat(tokens.get(1).getType(),is(JajaCodeLexer.NEW));
        assertThat( tokens.get(1).getText(),is("new"));

        assertThat(tokens.get(2).getType(), is(JajaCodeLexer.LPAREN));
        assertThat(tokens.get(2).getText(), is("("));

        assertThat(tokens.get(3).getType(), is(JajaCodeLexer.IDENTIFICATEUR));
        assertThat(tokens.get(3).getText(), is("x"));

        assertThat(tokens.get(4).getType(), is(JajaCodeLexer.COMMA));
        assertThat(tokens.get(4).getText(), is(","));

        assertThat(tokens.get(5).getType(), is(JajaCodeLexer.INT));
        assertThat(tokens.get(5).getText(), is("entier"));

        assertThat(tokens.get(6).getType(), is(JajaCodeLexer.COMMA));
        assertThat(tokens.get(6).getText(), is(","));

        assertThat(tokens.get(7).getType(), is(JajaCodeLexer.VAR));
        assertThat(tokens.get(7).getText(), is("var"));

        assertThat(tokens.get(8).getType(), is(JajaCodeLexer.COMMA));
        assertThat(tokens.get(8).getText(), is(","));

        assertThat(tokens.get(9).getType(), is(JajaCodeLexer.NOMBRE));
        assertThat(tokens.get(9).getText(), is("0"));

        assertThat(tokens.get(10).getType(), is(JajaCodeLexer.RPAREN));
        assertThat(tokens.get(10).getText(), is(")"));

    }

}
