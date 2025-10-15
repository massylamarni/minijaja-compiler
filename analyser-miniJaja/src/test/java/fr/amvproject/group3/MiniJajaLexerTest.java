package fr.amvproject.group3;

import org.antlr.v4.runtime.*;

import java.util.List;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MiniJajaLexerTest {

    @Test
    public void givenSimpleIntAssign__whenGenerateTokens__thenCorrectLexer() {
        String input = "int y = 10;";

        CharStream charStream = CharStreams.fromString(input);
        MiniJajaLexer lexer = new MiniJajaLexer(charStream);
        List<? extends Token> tokens = lexer.getAllTokens();

        assertThat(tokens.size(),is(5));

        assertThat(tokens.get(0).getType(), is(MiniJajaLexer.INT));
        assertThat(tokens.get(0).getText(), is("int"));

        assertThat(tokens.get(1).getType(),is(MiniJajaLexer.IDENTIFICATEUR));
        assertThat( tokens.get(1).getText(),is("y"));

        assertThat(tokens.get(2).getType(),is(MiniJajaLexer.ASSIGN));
        assertThat(tokens.get(2).getText(),is("="));

        assertThat(tokens.get(3).getType(),is(MiniJajaLexer.NOMBRE));
        assertThat(tokens.get(3).getText(),is("10"));

        assertThat(tokens.get(4).getType(),is(MiniJajaLexer.SEMI));
        assertThat(tokens.get(4).getText(),is(";"));

    }

    @Test
    public void givenCommentBlock__whenGenerateTokens__thenCorrectLexer() {
        // Given
        String input = "/********************************************/\n" +
                "/* Programme MiniJaja                       */\n" +
                "/* Validation de toutes les instructions\t*/\n" +
                "/* sans tableau, ni fonction                */\n" +
                "/********************************************/";

        CharStream charStream = CharStreams.fromString(input);
        MiniJajaLexer lexer = new MiniJajaLexer(charStream);
        // When
        List<? extends Token> tokens = lexer.getAllTokens();
        // Then
        assertThat(tokens.size(),is(0));
    }

    @Test
    public void givenCommentLine__whenGenerateTokens__thenCorrectLexer() {
        // Given
        String input = "//commentaire miniJaja sur une seule ligne\n" +
                "int x;";

        CharStream charStream = CharStreams.fromString(input);
        MiniJajaLexer lexer = new MiniJajaLexer(charStream);
        // When
        List<? extends Token> tokens = lexer.getAllTokens();
        // Then
        assertThat(tokens.size(),is(3));
    }

    @Test
    public void givenEmptyClass__whenGenerateTokens__thenCorrectLexer() {
        // Given
        String input = "class C {}";

        CharStream charStream = CharStreams.fromString(input);
        MiniJajaLexer lexer = new MiniJajaLexer(charStream);
        // When
        List<? extends Token> tokens = lexer.getAllTokens();
        // Then
        assertThat(tokens.size(),is(4));

        assertThat(tokens.get(0).getType(), is(MiniJajaLexer.CLASS));
        assertThat(tokens.get(0).getText(), is("class"));

        assertThat(tokens.get(1).getType(),is(MiniJajaLexer.IDENTIFICATEUR));
        assertThat( tokens.get(1).getText(),is("C"));

        assertThat(tokens.get(2).getType(),is(MiniJajaLexer.LBRACE));
        assertThat(tokens.get(2).getText(),is("{"));

        assertThat(tokens.get(3).getType(),is(MiniJajaLexer.RBRACE));
        assertThat(tokens.get(3).getText(),is("}"));

    }

    @Test
    public void givenFinalVar__whenGenerateTokens__thenCorrectLexer() {
        // Given
        String input = "final int VAL = 5;";

        CharStream charStream = CharStreams.fromString(input);
        MiniJajaLexer lexer = new MiniJajaLexer(charStream);
        // When
        List<? extends Token> tokens = lexer.getAllTokens();
        // Then
        assertThat(tokens.size(),is(6));

        assertThat(tokens.get(0).getType(), is(MiniJajaLexer.FINAL));
        assertThat(tokens.get(0).getText(), is("final"));

        assertThat(tokens.get(1).getType(),is(MiniJajaLexer.INT));
        assertThat( tokens.get(1).getText(),is("int"));

        assertThat(tokens.get(2).getType(),is(MiniJajaLexer.IDENTIFICATEUR));
        assertThat(tokens.get(2).getText(),is("VAL"));

        assertThat(tokens.get(3).getType(),is(MiniJajaLexer.ASSIGN));
        assertThat(tokens.get(3).getText(),is("="));

        assertThat(tokens.get(4).getType(),is(MiniJajaLexer.NOMBRE));
        assertThat(tokens.get(4).getText(),is("5"));

        assertThat(tokens.get(5).getType(),is(MiniJajaLexer.SEMI));
        assertThat(tokens.get(5).getText(),is(";"));
    }


    @Test
    public void givenBoolVar__whenGenerateTokens__thenCorrectLexer() {
        // Given
        String input = "boolean b1 = true;";

        CharStream charStream = CharStreams.fromString(input);
        MiniJajaLexer lexer = new MiniJajaLexer(charStream);
        // When
        List<? extends Token> tokens = lexer.getAllTokens();
        // Then
        assertThat(tokens.size(),is(5));

        assertThat(tokens.get(0).getType(), is(MiniJajaLexer.BOOLEAN));
        assertThat(tokens.get(0).getText(), is("boolean"));

        assertThat(tokens.get(1).getType(),is(MiniJajaLexer.IDENTIFICATEUR));
        assertThat(tokens.get(1).getText(),is("b1"));

        assertThat(tokens.get(2).getType(),is(MiniJajaLexer.ASSIGN));
        assertThat(tokens.get(2).getText(),is("="));

        assertThat(tokens.get(3).getType(),is(MiniJajaLexer.TRUE));
        assertThat(tokens.get(3).getText(),is("true"));

        assertThat(tokens.get(4).getType(),is(MiniJajaLexer.SEMI));
        assertThat(tokens.get(4).getText(),is(";"));
    }

    @Test
    public void givenMain__whenGenerateTokens__thenCorrectLexer() {
        // Given
        String input = "main {}";

        CharStream charStream = CharStreams.fromString(input);
        MiniJajaLexer lexer = new MiniJajaLexer(charStream);
        // When
        List<? extends Token> tokens = lexer.getAllTokens();
        // Then
        assertThat(tokens.size(),is(3));

        assertThat(tokens.get(0).getType(), is(MiniJajaLexer.MAIN));
        assertThat(tokens.get(0).getText(), is("main"));

        assertThat(tokens.get(1).getType(),is(MiniJajaLexer.LBRACE));
        assertThat(tokens.get(1).getText(),is("{"));

        assertThat(tokens.get(2).getType(),is(MiniJajaLexer.RBRACE));
        assertThat(tokens.get(2).getText(),is("}"));
    }
    @Test
    public void givenWriteLn__whenGenerateTokens__thenCorrectLexer() {
        // Given
        String input = "writeln(x);";

        CharStream charStream = CharStreams.fromString(input);
        MiniJajaLexer lexer = new MiniJajaLexer(charStream);
        // When
        List<? extends Token> tokens = lexer.getAllTokens();
        // Then
        assertThat(tokens.size(),is(5));

        assertThat(tokens.get(0).getType(), is(MiniJajaLexer.WRITELN));
        assertThat(tokens.get(0).getText(), is("writeln"));

        assertThat(tokens.get(1).getType(),is(MiniJajaLexer.LPAREN));
        assertThat(tokens.get(1).getText(),is("("));

        assertThat(tokens.get(2).getType(),is(MiniJajaLexer.IDENTIFICATEUR));
        assertThat(tokens.get(2).getText(),is("x"));

        assertThat(tokens.get(3).getType(),is(MiniJajaLexer.RPAREN));
        assertThat(tokens.get(3).getText(),is(")"));

        assertThat(tokens.get(4).getType(),is(MiniJajaLexer.SEMI));
        assertThat(tokens.get(4).getText(),is(";"));
    }


    @Test
    public void givenWrite__whenGenerateTokens__thenCorrectLexer() {
        // Given
        String input = "write(\"x (15) = \");";

        CharStream charStream = CharStreams.fromString(input);
        MiniJajaLexer lexer = new MiniJajaLexer(charStream);
        // When
        List<? extends Token> tokens = lexer.getAllTokens();
        // Then
        assertThat(tokens.size(),is(5));

        assertThat(tokens.get(0).getType(), is(MiniJajaLexer.WRITE));
        assertThat(tokens.get(0).getText(), is("write"));

        assertThat(tokens.get(1).getType(),is(MiniJajaLexer.LPAREN));
        assertThat(tokens.get(1).getText(),is("("));

        assertThat(tokens.get(2).getType(),is(MiniJajaLexer.STRING));
        assertThat(tokens.get(2).getText(),is("\"x (15) = \""));

        assertThat(tokens.get(3).getType(),is(MiniJajaLexer.RPAREN));
        assertThat(tokens.get(3).getText(),is(")"));

        assertThat(tokens.get(4).getType(),is(MiniJajaLexer.SEMI));
        assertThat(tokens.get(4).getText(),is(";"));
    }

    @Test
    public void givenElse__whenGenerateTokens__thenCorrectLexer() {
        // Given
        String input = "else {x++;}";

        CharStream charStream = CharStreams.fromString(input);
        MiniJajaLexer lexer = new MiniJajaLexer(charStream);
        // When
        List<? extends Token> tokens = lexer.getAllTokens();
        // Then
        assertThat(tokens.size(),is(6));

        assertThat(tokens.get(0).getType(), is(MiniJajaLexer.ELSE));
        assertThat(tokens.get(0).getText(), is("else"));

        assertThat(tokens.get(1).getType(),is(MiniJajaLexer.LBRACE));
        assertThat(tokens.get(1).getText(),is("{"));

        assertThat(tokens.get(2).getType(),is(MiniJajaLexer.IDENTIFICATEUR));
        assertThat(tokens.get(2).getText(),is("x"));

        assertThat(tokens.get(3).getType(),is(MiniJajaLexer.INC));
        assertThat(tokens.get(3).getText(),is("++"));

        assertThat(tokens.get(4).getType(),is(MiniJajaLexer.SEMI));
        assertThat(tokens.get(4).getText(),is(";"));

        assertThat(tokens.get(5).getType(),is(MiniJajaLexer.RBRACE));
        assertThat(tokens.get(5).getText(),is("}"));
    }


    @Test
    public void givenIf__whenGenerateTokens__thenCorrectLexer() {
        // Given
        String input = "if (b1 && (b1 || (!b2))) {}";

        CharStream charStream = CharStreams.fromString(input);
        MiniJajaLexer lexer = new MiniJajaLexer(charStream);
        // When
        List<? extends Token> tokens = lexer.getAllTokens();
        // Then
        assertThat(tokens.size(),is(15));

        assertThat(tokens.get(0).getType(), is(MiniJajaLexer.IF));
        assertThat(tokens.get(0).getText(), is("if"));

        assertThat(tokens.get(1).getType(),is(MiniJajaLexer.LPAREN));
        assertThat(tokens.get(1).getText(),is("("));

        assertThat(tokens.get(2).getType(),is(MiniJajaLexer.IDENTIFICATEUR));
        assertThat(tokens.get(2).getText(),is("b1"));

        assertThat(tokens.get(3).getType(),is(MiniJajaLexer.AND));
        assertThat(tokens.get(3).getText(),is("&&"));

        assertThat(tokens.get(4).getType(),is(MiniJajaLexer.LPAREN));
        assertThat(tokens.get(4).getText(),is("("));

        assertThat(tokens.get(5).getType(),is(MiniJajaLexer.IDENTIFICATEUR));
        assertThat(tokens.get(5).getText(),is("b1"));

        assertThat(tokens.get(6).getType(),is(MiniJajaLexer.OR));
        assertThat(tokens.get(6).getText(),is("||"));

        assertThat(tokens.get(7).getType(),is(MiniJajaLexer.LPAREN));
        assertThat(tokens.get(7).getText(),is("("));

        assertThat(tokens.get(8).getType(),is(MiniJajaLexer.NOT));
        assertThat(tokens.get(8).getText(),is("!"));

        assertThat(tokens.get(9).getType(),is(MiniJajaLexer.IDENTIFICATEUR));
        assertThat(tokens.get(9).getText(),is("b2"));

        assertThat(tokens.get(10).getType(),is(MiniJajaLexer.RPAREN));
        assertThat(tokens.get(10).getText(),is(")"));

        assertThat(tokens.get(11).getType(),is(MiniJajaLexer.RPAREN));
        assertThat(tokens.get(11).getText(),is(")"));

        assertThat(tokens.get(12).getType(),is(MiniJajaLexer.RPAREN));
        assertThat(tokens.get(12).getText(),is(")"));

        assertThat(tokens.get(13).getType(),is(MiniJajaLexer.LBRACE));
        assertThat(tokens.get(13).getText(),is("{"));

        assertThat(tokens.get(14).getType(),is(MiniJajaLexer.RBRACE));
        assertThat(tokens.get(14).getText(),is("}"));
    }

    @Test
    public void givenMethod__whenGenerateTokens__thenCorrectLexer() {
        // Given
        String input = "int somme( int max) {return 1;};";

        CharStream charStream = CharStreams.fromString(input);
        MiniJajaLexer lexer = new MiniJajaLexer(charStream);
        // When
        List<? extends Token> tokens = lexer.getAllTokens();
        // Then
        assertThat(tokens.size(),is(12));

        assertThat(tokens.get(0).getType(), is(MiniJajaLexer.INT));
        assertThat(tokens.get(0).getText(), is("int"));

        assertThat(tokens.get(1).getType(),is(MiniJajaLexer.IDENTIFICATEUR));
        assertThat(tokens.get(1).getText(),is("somme"));

        assertThat(tokens.get(2).getType(),is(MiniJajaLexer.LPAREN));
        assertThat(tokens.get(2).getText(),is("("));

        assertThat(tokens.get(3).getType(),is(MiniJajaLexer.INT));
        assertThat(tokens.get(3).getText(),is("int"));

        assertThat(tokens.get(4).getType(),is(MiniJajaLexer.IDENTIFICATEUR));
        assertThat(tokens.get(4).getText(),is("max"));

        assertThat(tokens.get(5).getType(),is(MiniJajaLexer.RPAREN));
        assertThat(tokens.get(5).getText(),is(")"));

        assertThat(tokens.get(6).getType(),is(MiniJajaLexer.LBRACE));
        assertThat(tokens.get(6).getText(),is("{"));

        assertThat(tokens.get(7).getType(),is(MiniJajaLexer.RETURN));
        assertThat(tokens.get(7).getText(),is("return"));

        assertThat(tokens.get(8).getType(),is(MiniJajaLexer.NOMBRE));
        assertThat(tokens.get(8).getText(),is("1"));

        assertThat(tokens.get(9).getType(),is(MiniJajaLexer.SEMI));
        assertThat(tokens.get(9).getText(),is(";"));

        assertThat(tokens.get(10).getType(),is(MiniJajaLexer.RBRACE));
        assertThat(tokens.get(10).getText(),is("}"));

        assertThat(tokens.get(11).getType(),is(MiniJajaLexer.SEMI));
        assertThat(tokens.get(11).getText(),is(";"));
    }

    @Test
    public void givenMethodCall__whenGenerateTokens__thenCorrectLexer() {
        // Given
        String input = "f(10);";

        CharStream charStream = CharStreams.fromString(input);
        MiniJajaLexer lexer = new MiniJajaLexer(charStream);
        // When
        List<? extends Token> tokens = lexer.getAllTokens();
        // Then
        assertThat(tokens.size(),is(5));

        assertThat(tokens.get(0).getType(), is(MiniJajaLexer.IDENTIFICATEUR));
        assertThat(tokens.get(0).getText(), is("f"));

        assertThat(tokens.get(1).getType(),is(MiniJajaLexer.LPAREN));
        assertThat(tokens.get(1).getText(),is("("));

        assertThat(tokens.get(2).getType(),is(MiniJajaLexer.NOMBRE));
        assertThat(tokens.get(2).getText(),is("10"));

        assertThat(tokens.get(3).getType(),is(MiniJajaLexer.RPAREN));
        assertThat(tokens.get(3).getText(),is(")"));

        assertThat(tokens.get(4).getType(),is(MiniJajaLexer.SEMI));
        assertThat(tokens.get(4).getText(),is(";"));
    }






}
