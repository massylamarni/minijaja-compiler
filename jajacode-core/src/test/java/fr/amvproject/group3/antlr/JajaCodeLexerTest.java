package fr.amvproject.group3.antlr;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;
import org.junit.Test;

import java.util.List;

import fr.amvproject.group3.JajaCodeLexer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class JajaCodeLexerTest {

    @Test
    public void givenInit__whenGenerateTokens__thenCorrectLexer() {
        String input = "1 init";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(), is(3));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.NOMBRE));
        assertThat(tokens.get(0).getText(), is("1"));

        assertThat(tokens.get(1).getType(), is(JajaCodeLexer.WS));
        assertThat(tokens.get(1).getText(), is(" "));

        assertThat(tokens.get(2).getType(), is(JajaCodeLexer.INIT));
        assertThat(tokens.get(2).getText(), is("init"));
    }

    @Test
    public void givenPush__whenGenerateTokens__thenCorrectLexer() {
        String input = "1 push(0)";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(), is(6));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.NOMBRE));
        assertThat(tokens.get(0).getText(), is("1"));

        assertThat(tokens.get(1).getType(), is(JajaCodeLexer.WS));
        assertThat(tokens.get(1).getText(), is(" "));

        assertThat(tokens.get(2).getType(), is(JajaCodeLexer.PUSH));
        assertThat(tokens.get(2).getText(), is("push"));

        assertThat(tokens.get(3).getType(), is(JajaCodeLexer.LPAREN));
        assertThat(tokens.get(3).getText(), is("("));

        assertThat(tokens.get(4).getType(), is(JajaCodeLexer.NOMBRE));
        assertThat(tokens.get(4).getText(), is("0"));

        assertThat(tokens.get(5).getType(), is(JajaCodeLexer.RPAREN));
        assertThat(tokens.get(5).getText(), is(")"));
    }

    @Test
    public void givenGoto__whenGenerateTokens__thenCorrectLexer() {
        String input = "6 goto(11)";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(), is(6));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.NOMBRE));
        assertThat(tokens.get(0).getText(), is("6"));

        assertThat(tokens.get(1).getType(), is(JajaCodeLexer.WS));
        assertThat(tokens.get(1).getText(), is(" "));

        assertThat(tokens.get(2).getType(), is(JajaCodeLexer.GOTO));
        assertThat(tokens.get(2).getText(), is("goto"));

        assertThat(tokens.get(3).getType(), is(JajaCodeLexer.LPAREN));
        assertThat(tokens.get(3).getText(), is("("));

        assertThat(tokens.get(4).getType(), is(JajaCodeLexer.NOMBRE));
        assertThat(tokens.get(4).getText(), is("11"));

        assertThat(tokens.get(5).getType(), is(JajaCodeLexer.RPAREN));
        assertThat(tokens.get(5).getText(), is(")"));
    }

    @Test
    public void givenStore__whenGenerateTokens__thenCorrectLexer() {
        String input = "12 store(x)";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(), is(6));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.NOMBRE));
        assertThat(tokens.get(0).getText(), is("12"));

        assertThat(tokens.get(1).getType(), is(JajaCodeLexer.WS));
        assertThat(tokens.get(1).getText(), is(" "));

        assertThat(tokens.get(2).getType(), is(JajaCodeLexer.STORE));
        assertThat(tokens.get(2).getText(), is("store"));

        assertThat(tokens.get(3).getType(), is(JajaCodeLexer.LPAREN));
        assertThat(tokens.get(3).getText(), is("("));

        assertThat(tokens.get(4).getType(), is(JajaCodeLexer.IDENTIFICATEUR));
        assertThat(tokens.get(4).getText(), is("x"));

        assertThat(tokens.get(5).getType(), is(JajaCodeLexer.RPAREN));
        assertThat(tokens.get(5).getText(), is(")"));
    }

    @Test
    public void givenInvoke__whenGenerateTokens__thenCorrectLexer() {
        String input = "14 invoke(f)";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(), is(6));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.NOMBRE));
        assertThat(tokens.get(0).getText(), is("14"));

        assertThat(tokens.get(1).getType(), is(JajaCodeLexer.WS));
        assertThat(tokens.get(1).getText(), is(" "));

        assertThat(tokens.get(2).getType(), is(JajaCodeLexer.INVOKE));
        assertThat(tokens.get(2).getText(), is("invoke"));

        assertThat(tokens.get(3).getType(), is(JajaCodeLexer.LPAREN));
        assertThat(tokens.get(3).getText(), is("("));

        assertThat(tokens.get(4).getType(), is(JajaCodeLexer.IDENTIFICATEUR));
        assertThat(tokens.get(4).getText(), is("f"));

        assertThat(tokens.get(5).getType(), is(JajaCodeLexer.RPAREN));
        assertThat(tokens.get(5).getText(), is(")"));
    }

    @Test
    public void givenInc__whenGenerateTokens__thenCorrectLexer() {
        String input = "17 inc(x)";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(), is(6));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.NOMBRE));
        assertThat(tokens.get(0).getText(), is("17"));

        assertThat(tokens.get(1).getType(), is(JajaCodeLexer.WS));
        assertThat(tokens.get(1).getText(), is(" "));

        assertThat(tokens.get(2).getType(), is(JajaCodeLexer.INC));
        assertThat(tokens.get(2).getText(), is("inc"));

        assertThat(tokens.get(3).getType(), is(JajaCodeLexer.LPAREN));
        assertThat(tokens.get(3).getText(), is("("));

        assertThat(tokens.get(4).getType(), is(JajaCodeLexer.IDENTIFICATEUR));
        assertThat(tokens.get(4).getText(), is("x"));

        assertThat(tokens.get(5).getType(), is(JajaCodeLexer.RPAREN));
        assertThat(tokens.get(5).getText(), is(")"));
    }

    @Test
    public void givenSwap__whenGenerateTokens__thenCorrectLexer() {
        String input = "9 swap";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(), is(3));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.NOMBRE));
        assertThat(tokens.get(0).getText(), is("9"));

        assertThat(tokens.get(1).getType(), is(JajaCodeLexer.WS));
        assertThat(tokens.get(1).getText(), is(" "));

        assertThat(tokens.get(2).getType(), is(JajaCodeLexer.SWAP));
        assertThat(tokens.get(2).getText(), is("swap"));
    }

    @Test
    public void givenJcstop__whenGenerateTokens__thenCorrectLexer() {
        String input = "24 jcstop";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(), is(3));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.NOMBRE));
        assertThat(tokens.get(0).getText(), is("24"));

        assertThat(tokens.get(1).getType(), is(JajaCodeLexer.WS));
        assertThat(tokens.get(1).getText(), is(" "));

        assertThat(tokens.get(2).getType(), is(JajaCodeLexer.JCSTOP));
        assertThat(tokens.get(2).getText(), is("jcstop"));
    }

    @Test
    public void givenComment__whenGenerateTokens__thenCorrectLexer() {
        String input = "9 swap // adr retour ↔ val. résultat";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(), is(4));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.NOMBRE));
        assertThat(tokens.get(0).getText(), is("9"));

        assertThat(tokens.get(1).getType(), is(JajaCodeLexer.WS));
        assertThat(tokens.get(1).getText(), is(" "));

        assertThat(tokens.get(2).getType(), is(JajaCodeLexer.SWAP));
        assertThat(tokens.get(2).getText(), is("swap"));

        assertThat(tokens.get(3).getType(), is(JajaCodeLexer.WS));
        assertThat(tokens.get(3).getText(), is(" "));
    }

    @Test
    public void givenPop__whenGenerateTokens__thenCorrectLexer() {
        String input = "17 pop";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(), is(3));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.NOMBRE));
        assertThat(tokens.get(0).getText(), is("17"));

        assertThat(tokens.get(1).getType(), is(JajaCodeLexer.WS));
        assertThat(tokens.get(1).getText(), is(" "));

        assertThat(tokens.get(2).getType(), is(JajaCodeLexer.POP));
        assertThat(tokens.get(2).getText(), is("pop"));
    }

    @Test
    public void givenReturn__whenGenerateTokens__thenCorrectLexer() {
        String input = "10 return";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(), is(3));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.NOMBRE));
        assertThat(tokens.get(0).getText(), is("10"));

        assertThat(tokens.get(1).getType(), is(JajaCodeLexer.WS));
        assertThat(tokens.get(1).getText(), is(" "));

        assertThat(tokens.get(2).getType(), is(JajaCodeLexer.RETURN));
        assertThat(tokens.get(2).getText(), is("return"));
    }

    @Test
    public void givenLoad__whenGenerateTokens__thenCorrectLexer() {
        String input = "8 load(p)";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(), is(6));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.NOMBRE));
        assertThat(tokens.get(0).getText(), is("8"));

        assertThat(tokens.get(1).getType(), is(JajaCodeLexer.WS));
        assertThat(tokens.get(1).getText(), is(" "));

        assertThat(tokens.get(2).getType(), is(JajaCodeLexer.LOAD));
        assertThat(tokens.get(2).getText(), is("load"));

        assertThat(tokens.get(3).getType(), is(JajaCodeLexer.LPAREN));
        assertThat(tokens.get(3).getText(), is("("));

        assertThat(tokens.get(4).getType(), is(JajaCodeLexer.IDENTIFICATEUR));
        assertThat(tokens.get(4).getText(), is("p"));

        assertThat(tokens.get(5).getType(), is(JajaCodeLexer.RPAREN));
        assertThat(tokens.get(5).getText(), is(")"));
    }

    @Test
    public void givenNew__whenGenerateTokens__thenCorrectLexer() {
        String input = "3 new(x,entier,var,0)";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(), is(12));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.NOMBRE));
        assertThat(tokens.get(0).getText(), is("3"));

        assertThat(tokens.get(1).getType(), is(JajaCodeLexer.WS));
        assertThat(tokens.get(1).getText(), is(" "));

        assertThat(tokens.get(2).getType(), is(JajaCodeLexer.NEW));
        assertThat(tokens.get(2).getText(), is("new"));

        assertThat(tokens.get(3).getType(), is(JajaCodeLexer.LPAREN));
        assertThat(tokens.get(3).getText(), is("("));

        assertThat(tokens.get(4).getType(), is(JajaCodeLexer.IDENTIFICATEUR));
        assertThat(tokens.get(4).getText(), is("x"));

        assertThat(tokens.get(5).getType(), is(JajaCodeLexer.COMMA));
        assertThat(tokens.get(5).getText(), is(","));

        assertThat(tokens.get(6).getType(), is(JajaCodeLexer.INT));
        assertThat(tokens.get(6).getText(), is("entier"));

        assertThat(tokens.get(7).getType(), is(JajaCodeLexer.COMMA));
        assertThat(tokens.get(7).getText(), is(","));

        assertThat(tokens.get(8).getType(), is(JajaCodeLexer.VAR));
        assertThat(tokens.get(8).getText(), is("var"));

        assertThat(tokens.get(9).getType(), is(JajaCodeLexer.COMMA));
        assertThat(tokens.get(9).getText(), is(","));

        assertThat(tokens.get(10).getType(), is(JajaCodeLexer.NOMBRE));
        assertThat(tokens.get(10).getText(), is("0"));

        assertThat(tokens.get(11).getType(), is(JajaCodeLexer.RPAREN));
        assertThat(tokens.get(11).getText(), is(")"));
    }

    @Test
    public void givenAload__whenGenerateTokens__thenCorrectLexer() {
        String input = "10 aload(x)";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(), is(6));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.NOMBRE));
        assertThat(tokens.get(0).getText(), is("10"));

        assertThat(tokens.get(1).getType(), is(JajaCodeLexer.WS));
        assertThat(tokens.get(1).getText(), is(" "));

        assertThat(tokens.get(2).getType(), is(JajaCodeLexer.ALOAD));
        assertThat(tokens.get(2).getText(), is("aload"));

        assertThat(tokens.get(3).getType(), is(JajaCodeLexer.LPAREN));
        assertThat(tokens.get(3).getText(), is("("));

        assertThat(tokens.get(4).getType(), is(JajaCodeLexer.IDENTIFICATEUR));
        assertThat(tokens.get(4).getText(), is("x"));

        assertThat(tokens.get(5).getType(), is(JajaCodeLexer.RPAREN));
        assertThat(tokens.get(5).getText(), is(")"));
    }

    @Test
    public void givenAstore__whenGenerateTokens__thenCorrectLexer() {
        String input = "11 astore(x)";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(), is(6));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.NOMBRE));
        assertThat(tokens.get(0).getText(), is("11"));

        assertThat(tokens.get(1).getType(), is(JajaCodeLexer.WS));
        assertThat(tokens.get(1).getText(), is(" "));

        assertThat(tokens.get(2).getType(), is(JajaCodeLexer.ASTORE));
        assertThat(tokens.get(2).getText(), is("astore"));

        assertThat(tokens.get(3).getType(), is(JajaCodeLexer.LPAREN));
        assertThat(tokens.get(3).getText(), is("("));

        assertThat(tokens.get(4).getType(), is(JajaCodeLexer.IDENTIFICATEUR));
        assertThat(tokens.get(4).getText(), is("x"));

        assertThat(tokens.get(5).getType(), is(JajaCodeLexer.RPAREN));
        assertThat(tokens.get(5).getText(), is(")"));
    }

    @Test
    public void givenLength__whenGenerateTokens__thenCorrectLexer() {
        String input = "13 length(x)";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(), is(6));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.NOMBRE));
        assertThat(tokens.get(0).getText(), is("13"));

        assertThat(tokens.get(1).getType(), is(JajaCodeLexer.WS));
        assertThat(tokens.get(1).getText(), is(" "));

        assertThat(tokens.get(2).getType(), is(JajaCodeLexer.LENGHT));
        assertThat(tokens.get(2).getText(), is("length"));

        assertThat(tokens.get(3).getType(), is(JajaCodeLexer.LPAREN));
        assertThat(tokens.get(3).getText(), is("("));

        assertThat(tokens.get(4).getType(), is(JajaCodeLexer.IDENTIFICATEUR));
        assertThat(tokens.get(4).getText(), is("x"));

        assertThat(tokens.get(5).getType(), is(JajaCodeLexer.RPAREN));
        assertThat(tokens.get(5).getText(), is(")"));
    }

    @Test
    public void givenIf__whenGenerateTokens__thenCorrectLexer() {
        String input = "16 if(10)";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(), is(6));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.NOMBRE));
        assertThat(tokens.get(0).getText(), is("16"));

        assertThat(tokens.get(1).getType(), is(JajaCodeLexer.WS));
        assertThat(tokens.get(1).getText(), is(" "));

        assertThat(tokens.get(2).getType(), is(JajaCodeLexer.IF));
        assertThat(tokens.get(2).getText(), is("if"));

        assertThat(tokens.get(3).getType(), is(JajaCodeLexer.LPAREN));
        assertThat(tokens.get(3).getText(), is("("));

        assertThat(tokens.get(4).getType(), is(JajaCodeLexer.NOMBRE));
        assertThat(tokens.get(4).getText(), is("10"));

        assertThat(tokens.get(5).getType(), is(JajaCodeLexer.RPAREN));
        assertThat(tokens.get(5).getText(), is(")"));
    }

    @Test
    public void givenAinc__whenGenerateTokens__thenCorrectLexer() {
        String input = "15 ainc(x)";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(), is(6));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.NOMBRE));
        assertThat(tokens.get(0).getText(), is("15"));

        assertThat(tokens.get(1).getType(), is(JajaCodeLexer.WS));
        assertThat(tokens.get(1).getText(), is(" "));

        assertThat(tokens.get(2).getType(), is(JajaCodeLexer.AINC));
        assertThat(tokens.get(2).getText(), is("ainc"));

        assertThat(tokens.get(3).getType(), is(JajaCodeLexer.LPAREN));
        assertThat(tokens.get(3).getText(), is("("));

        assertThat(tokens.get(4).getType(), is(JajaCodeLexer.IDENTIFICATEUR));
        assertThat(tokens.get(4).getText(), is("x"));

        assertThat(tokens.get(5).getType(), is(JajaCodeLexer.RPAREN));
        assertThat(tokens.get(5).getText(), is(")"));
    }

    @Test
    public void givenWrite__whenGenerateTokens__thenCorrectLexer() {
        String input = "24 write";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(), is(3));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.NOMBRE));
        assertThat(tokens.get(0).getText(), is("24"));

        assertThat(tokens.get(1).getType(), is(JajaCodeLexer.WS));
        assertThat(tokens.get(1).getText(), is(" "));

        assertThat(tokens.get(2).getType(), is(JajaCodeLexer.WRITE));
        assertThat(tokens.get(2).getText(), is("write"));
    }

    @Test
    public void givenWriteln__whenGenerateTokens__thenCorrectLexer() {
        String input = "25 writeln";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(), is(3));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.NOMBRE));
        assertThat(tokens.get(0).getText(), is("25"));

        assertThat(tokens.get(1).getType(), is(JajaCodeLexer.WS));
        assertThat(tokens.get(1).getText(), is(" "));

        assertThat(tokens.get(2).getType(), is(JajaCodeLexer.WRITELN));
        assertThat(tokens.get(2).getText(), is("writeln"));
    }

    @Test
    public void givenNewArray__whenGenerateTokens__thenCorrectLexer() {
        String input = "5 newarray(x, entier)";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(), is(9));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.NOMBRE));
        assertThat(tokens.get(0).getText(), is("5"));

        assertThat(tokens.get(1).getType(), is(JajaCodeLexer.WS));
        assertThat(tokens.get(1).getText(), is(" "));

        assertThat(tokens.get(2).getType(), is(JajaCodeLexer.NEWARRAY));
        assertThat(tokens.get(2).getText(), is("newarray"));

        assertThat(tokens.get(3).getType(), is(JajaCodeLexer.LPAREN));
        assertThat(tokens.get(3).getText(), is("("));

        assertThat(tokens.get(4).getType(), is(JajaCodeLexer.IDENTIFICATEUR));
        assertThat(tokens.get(4).getText(), is("x"));

        assertThat(tokens.get(5).getType(), is(JajaCodeLexer.COMMA));
        assertThat(tokens.get(5).getText(), is(","));

        assertThat(tokens.get(6).getType(), is(JajaCodeLexer.WS));
        assertThat(tokens.get(6).getText(), is(" "));

        assertThat(tokens.get(7).getType(), is(JajaCodeLexer.INT));
        assertThat(tokens.get(7).getText(), is("entier"));

        assertThat(tokens.get(8).getType(), is(JajaCodeLexer.RPAREN));
        assertThat(tokens.get(8).getText(), is(")"));
    }

    @Test
    public void givenTrue__whenGenerateTokens__thenCorrectLexer() {
        String input = "true";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(), is(1));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.TRUE));
        assertThat(tokens.get(0).getText(), is("true"));
    }

    @Test
    public void givenFalse__whenGenerateTokens__thenCorrectLexer() {
        String input = "false";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(), is(1));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.FALSE));
        assertThat(tokens.get(0).getText(), is("false"));
    }

    @Test
    public void givenBoolean__whenGenerateTokens__thenCorrectLexer() {
        String input = "boolean";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(), is(1));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.BOOLEAN));
        assertThat(tokens.get(0).getText(), is("boolean"));
    }

    @Test
    public void givenVar__whenGenerateTokens__thenCorrectLexer() {
        String input = "var";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(), is(1));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.VAR));
        assertThat(tokens.get(0).getText(), is("var"));
    }

    @Test
    public void givenMeth__whenGenerateTokens__thenCorrectLexer() {
        String input = "meth";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(), is(1));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.METH));
        assertThat(tokens.get(0).getText(), is("meth"));
    }

    @Test
    public void givenCst__whenGenerateTokens__thenCorrectLexer() {
        String input = "cst";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(), is(1));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.CST));
        assertThat(tokens.get(0).getText(), is("cst"));
    }

    @Test
    public void givenTab__whenGenerateTokens__thenCorrectLexer() {
        String input = "tab";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(), is(1));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.TAB));
        assertThat(tokens.get(0).getText(), is("tab"));
    }

    @Test
    public void givenNeg__whenGenerateTokens__thenCorrectLexer() {
        String input = "neg";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(), is(1));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.NEG));
        assertThat(tokens.get(0).getText(), is("neg"));
    }

    @Test
    public void givenNot__whenGenerateTokens__thenCorrectLexer() {
        String input = "not";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(), is(1));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.NOT));
        assertThat(tokens.get(0).getText(), is("not"));
    }

    @Test
    public void givenAdd__whenGenerateTokens__thenCorrectLexer() {
        String input = "add";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(), is(1));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.ADD));
        assertThat(tokens.get(0).getText(), is("add"));
    }

    @Test
    public void givenSub__whenGenerateTokens__thenCorrectLexer() {
        String input = "sub";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(), is(1));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.SUB));
        assertThat(tokens.get(0).getText(), is("sub"));
    }

    @Test
    public void givenMul__whenGenerateTokens__thenCorrectLexer() {
        String input = "mul";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(), is(1));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.MUL));
        assertThat(tokens.get(0).getText(), is("mul"));
    }

    @Test
    public void givenDiv__whenGenerateTokens__thenCorrectLexer() {
        String input = "div";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(), is(1));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.DIV));
        assertThat(tokens.get(0).getText(), is("div"));
    }

    @Test
    public void givenCmp__whenGenerateTokens__thenCorrectLexer() {
        String input = "cmp";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(), is(1));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.CMP));
        assertThat(tokens.get(0).getText(), is("cmp"));
    }

    @Test
    public void givenSup__whenGenerateTokens__thenCorrectLexer() {
        String input = "sup";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(), is(1));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.SUP));
        assertThat(tokens.get(0).getText(), is("sup"));
    }

    @Test
    public void givenOr__whenGenerateTokens__thenCorrectLexer() {
        String input = "or";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(), is(1));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.OR));
        assertThat(tokens.get(0).getText(), is("or"));
    }

    @Test
    public void givenAnd__whenGenerateTokens__thenCorrectLexer() {
        String input = "and";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(), is(1));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.AND));
        assertThat(tokens.get(0).getText(), is("and"));
    }

    @Test
    public void givenNop__whenGenerateTokens__thenCorrectLexer() {
        String input = "nop";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(), is(1));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.NOP));
        assertThat(tokens.get(0).getText(), is("nop"));
    }

    @Test
    public void givenComma__whenGenerateTokens__thenCorrectLexer() {
        String input = ",";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(), is(1));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.COMMA));
        assertThat(tokens.get(0).getText(), is(","));
    }

    @Test
    public void givenLparen__whenGenerateTokens__thenCorrectLexer() {
        String input = "(";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(), is(1));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.LPAREN));
        assertThat(tokens.get(0).getText(), is("("));
    }

    @Test
    public void givenRparen__whenGenerateTokens__thenCorrectLexer() {
        String input = ")";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(), is(1));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.RPAREN));
        assertThat(tokens.get(0).getText(), is(")"));
    }

    @Test
    public void givenString__whenGenerateTokens__thenCorrectLexer() {
        String input = "\"Hello, World!\"";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(), is(1));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.STRING));
        assertThat(tokens.get(0).getText(), is("\"Hello, World!\""));
    }

    @Test
    public void givenIdentificateur__whenGenerateTokens__thenCorrectLexer() {
        String input = "myVariable";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(), is(1));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.IDENTIFICATEUR));
        assertThat(tokens.get(0).getText(), is("myVariable"));
    }

    @Test
    public void givenNombre__whenGenerateTokens__thenCorrectLexer() {
        String input = "12345";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(), is(1));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.NOMBRE));
        assertThat(tokens.get(0).getText(), is("12345"));
    }

    @Test
    public void givenCommentBlock__whenGenerateTokens__thenCorrectLexer() {
        String input = "/* This is a block comment */";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(), is(1));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.COMMENT_BLOCK));
        assertThat(tokens.get(0).getText(), is("/* This is a block comment */"));
    }

    @Test
    public void givenVide__whenGenerateTokens__thenCorrectLexer() {
        String input = "vide";

        CharStream charStream = CharStreams.fromString(input);
        JajaCodeLexer lexer = new JajaCodeLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(), is(1));

        assertThat(tokens.get(0).getType(), is(JajaCodeLexer.VIDE));
        assertThat(tokens.get(0).getText(), is("vide"));
    }
}
