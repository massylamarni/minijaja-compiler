package fr.amvproject.group3.antlr;

import fr.amvproject.group3.JajaCodeLexer;
import fr.amvproject.group3.JajaCodeParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;

public class JajaCodeParserTest {

    @Test
    public void givenSimpleProgram_whenParse_thenCorrect() {
        String input = "10 init";
        JajaCodeLexer lexer = new JajaCodeLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JajaCodeParser parser = new JajaCodeParser(tokens);

        ParseTree tree = parser.program();

        assertNotNull(tree);
        String treeText = tree.toStringTree(parser);
        assertThat(treeText, containsString("10"));
        assertThat(treeText, containsString("init"));
    }

    @Test
    public void givenSwapInstruction_whenParse_thenCorrect() {
        String input = "swap";
        JajaCodeLexer lexer = new JajaCodeLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JajaCodeParser parser = new JajaCodeParser(tokens);

        ParseTree tree = parser.instr();

        assertNotNull(tree);
        String treeText = tree.toStringTree(parser);
        assertThat(treeText, containsString("swap"));
    }

    @Test
    public void givenPushInstruction_whenParse_thenCorrect() {
        String input = "push(5)";
        JajaCodeLexer lexer = new JajaCodeLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JajaCodeParser parser = new JajaCodeParser(tokens);

        ParseTree tree = parser.instr();

        assertNotNull(tree);
        String treeText = tree.toStringTree(parser);
        assertThat(treeText, containsString("push"));
        assertThat(treeText, containsString("5"));
    }

    @Test
    public void givenNewInstruction_whenParse_thenCorrect() {
        String input = "new(foo, int, var, 10)";
        JajaCodeLexer lexer = new JajaCodeLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JajaCodeParser parser = new JajaCodeParser(tokens);

        ParseTree tree = parser.instr();

        assertNotNull(tree);
        String treeText = tree.toStringTree(parser);
        assertThat(treeText, containsString("new"));
        assertThat(treeText, containsString("foo"));
        assertThat(treeText, containsString("int"));
        assertThat(treeText, containsString("var"));
        assertThat(treeText, containsString("10"));
    }

    @Test
    public void givenNewArrayInstruction_whenParse_thenCorrect() {
        String input = "newarray(foo, int)";
        JajaCodeLexer lexer = new JajaCodeLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JajaCodeParser parser = new JajaCodeParser(tokens);

        ParseTree tree = parser.instr();

        assertNotNull(tree);
        String treeText = tree.toStringTree(parser);
        assertThat(treeText, containsString("newarray"));
        assertThat(treeText, containsString("foo"));
        assertThat(treeText, containsString("int"));
    }

    @Test
    public void givenInvokeInstruction_whenParse_thenCorrect() {
        String input = "invoke(foo)";
        JajaCodeLexer lexer = new JajaCodeLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JajaCodeParser parser = new JajaCodeParser(tokens);

        ParseTree tree = parser.instr();

        assertNotNull(tree);
        String treeText = tree.toStringTree(parser);
        assertThat(treeText, containsString("invoke"));
        assertThat(treeText, containsString("foo"));
    }

    @Test
    public void givenReturnInstruction_whenParse_thenCorrect() {
        String input = "return";
        JajaCodeLexer lexer = new JajaCodeLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JajaCodeParser parser = new JajaCodeParser(tokens);

        ParseTree tree = parser.instr();

        assertNotNull(tree);
        String treeText = tree.toStringTree(parser);
        assertThat(treeText, containsString("return"));
    }

    @Test
    public void givenLoadInstruction_whenParse_thenCorrect() {
        String input = "load(foo)";
        JajaCodeLexer lexer = new JajaCodeLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JajaCodeParser parser = new JajaCodeParser(tokens);

        ParseTree tree = parser.instr();

        assertNotNull(tree);
        String treeText = tree.toStringTree(parser);
        assertThat(treeText, containsString("load"));
        assertThat(treeText, containsString("foo"));
    }

    @Test
    public void givenAloadInstruction_whenParse_thenCorrect() {
        String input = "aload(foo)";
        JajaCodeLexer lexer = new JajaCodeLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JajaCodeParser parser = new JajaCodeParser(tokens);

        ParseTree tree = parser.instr();

        assertNotNull(tree);
        String treeText = tree.toStringTree(parser);
        assertThat(treeText, containsString("aload"));
        assertThat(treeText, containsString("foo"));
    }

    @Test
    public void givenStoreInstruction_whenParse_thenCorrect() {
        String input = "store(foo)";
        JajaCodeLexer lexer = new JajaCodeLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JajaCodeParser parser = new JajaCodeParser(tokens);

        ParseTree tree = parser.instr();

        assertNotNull(tree);
        String treeText = tree.toStringTree(parser);
        assertThat(treeText, containsString("store"));
        assertThat(treeText, containsString("foo"));
    }

    @Test
    public void givenAstoreInstruction_whenParse_thenCorrect() {
        String input = "astore(foo)";
        JajaCodeLexer lexer = new JajaCodeLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JajaCodeParser parser = new JajaCodeParser(tokens);

        ParseTree tree = parser.instr();

        assertNotNull(tree);
        String treeText = tree.toStringTree(parser);
        assertThat(treeText, containsString("astore"));
        assertThat(treeText, containsString("foo"));
    }

    @Test
    public void givenWriteInstruction_whenParse_thenCorrect() {
        String input = "write";
        JajaCodeLexer lexer = new JajaCodeLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JajaCodeParser parser = new JajaCodeParser(tokens);

        ParseTree tree = parser.instr();

        assertNotNull(tree);
        String treeText = tree.toStringTree(parser);
        assertThat(treeText, containsString("write"));
    }

    @Test
    public void givenWritelnInstruction_whenParse_thenCorrect() {
        String input = "writeln";
        JajaCodeLexer lexer = new JajaCodeLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JajaCodeParser parser = new JajaCodeParser(tokens);

        ParseTree tree = parser.instr();

        assertNotNull(tree);
        String treeText = tree.toStringTree(parser);
        assertThat(treeText, containsString("writeln"));
    }

    @Test
    public void givenLengthInstruction_whenParse_thenCorrect() {
        String input = "length(foo)";
        JajaCodeLexer lexer = new JajaCodeLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JajaCodeParser parser = new JajaCodeParser(tokens);

        ParseTree tree = parser.instr();

        assertNotNull(tree);
        String treeText = tree.toStringTree(parser);
        assertThat(treeText, containsString("length"));
        assertThat(treeText, containsString("foo"));
    }

    @Test
    public void givenIfInstruction_whenParse_thenCorrect() {
        String input = "if(10)";
        JajaCodeLexer lexer = new JajaCodeLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JajaCodeParser parser = new JajaCodeParser(tokens);

        ParseTree tree = parser.instr();

        assertNotNull(tree);
        String treeText = tree.toStringTree(parser);
        assertThat(treeText, containsString("if"));
        assertThat(treeText, containsString("10"));
    }

    @Test
    public void givenGotoInstruction_whenParse_thenCorrect() {
        String input = "goto(10)";
        JajaCodeLexer lexer = new JajaCodeLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JajaCodeParser parser = new JajaCodeParser(tokens);

        ParseTree tree = parser.instr();

        assertNotNull(tree);
        String treeText = tree.toStringTree(parser);
        assertThat(treeText, containsString("goto"));
        assertThat(treeText, containsString("10"));
    }

    @Test
    public void givenIncInstruction_whenParse_thenCorrect() {
        String input = "inc(foo)";
        JajaCodeLexer lexer = new JajaCodeLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JajaCodeParser parser = new JajaCodeParser(tokens);

        ParseTree tree = parser.instr();

        assertNotNull(tree);
        String treeText = tree.toStringTree(parser);
        assertThat(treeText, containsString("inc"));
        assertThat(treeText, containsString("foo"));
    }

    @Test
    public void givenAincInstruction_whenParse_thenCorrect() {
        String input = "ainc(foo)";
        JajaCodeLexer lexer = new JajaCodeLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JajaCodeParser parser = new JajaCodeParser(tokens);

        ParseTree tree = parser.instr();

        assertNotNull(tree);
        String treeText = tree.toStringTree(parser);
        assertThat(treeText, containsString("ainc"));
        assertThat(treeText, containsString("foo"));
    }

    @Test
    public void givenOperInstruction_whenParse_thenCorrect() {
        String input = "add";
        JajaCodeLexer lexer = new JajaCodeLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JajaCodeParser parser = new JajaCodeParser(tokens);

        ParseTree tree = parser.instr();

        assertNotNull(tree);
        String treeText = tree.toStringTree(parser);
        assertThat(treeText, containsString("add"));
    }

    @Test
    public void givenNopInstruction_whenParse_thenCorrect() {
        String input = "nop";
        JajaCodeLexer lexer = new JajaCodeLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JajaCodeParser parser = new JajaCodeParser(tokens);

        ParseTree tree = parser.instr();

        assertNotNull(tree);
        String treeText = tree.toStringTree(parser);
        assertThat(treeText, containsString("nop"));
    }

    @Test
    public void givenJCStopInstruction_whenParse_thenCorrect() {
        String input = "jcstop";
        JajaCodeLexer lexer = new JajaCodeLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JajaCodeParser parser = new JajaCodeParser(tokens);

        ParseTree tree = parser.instr();

        assertNotNull(tree);
        String treeText = tree.toStringTree(parser);
        assertThat(treeText, containsString("jcstop"));
    }

    @Test
    public void givenIdent_whenParse_thenCorrect() {
        String input = "foo";
        JajaCodeLexer lexer = new JajaCodeLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JajaCodeParser parser = new JajaCodeParser(tokens);

        ParseTree tree = parser.ident();

        assertNotNull(tree);
        String treeText = tree.toStringTree(parser);
        assertThat(treeText, containsString("foo"));
    }

    @Test
    public void givenValeur_whenParse_thenCorrect() {
        String input = "5";
        JajaCodeLexer lexer = new JajaCodeLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JajaCodeParser parser = new JajaCodeParser(tokens);

        ParseTree tree = parser.valeur();

        assertNotNull(tree);
        String treeText = tree.toStringTree(parser);
        assertThat(treeText, containsString("5"));
    }

    @Test
    public void givenAdresse_whenParse_thenCorrect() {
        String input = "10";
        JajaCodeLexer lexer = new JajaCodeLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JajaCodeParser parser = new JajaCodeParser(tokens);

        ParseTree tree = parser.adresse();

        assertNotNull(tree);
        String treeText = tree.toStringTree(parser);
        assertThat(treeText, containsString("10"));
    }

    @Test
    public void givenOper1_whenParse_thenCorrect() {
        String input = "not";
        JajaCodeLexer lexer = new JajaCodeLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JajaCodeParser parser = new JajaCodeParser(tokens);

        ParseTree tree = parser.oper1();

        assertNotNull(tree);
        String treeText = tree.toStringTree(parser);
        assertThat(treeText, containsString("not"));
    }

    @Test
    public void givenOper2_whenParse_thenCorrect() {
        String input = "add";
        JajaCodeLexer lexer = new JajaCodeLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JajaCodeParser parser = new JajaCodeParser(tokens);

        ParseTree tree = parser.oper2();

        assertNotNull(tree);
        String treeText = tree.toStringTree(parser);
        assertThat(treeText, containsString("add"));
    }

    @Test
    public void givenType_whenParse_thenCorrect() {
        String input = "int";
        JajaCodeLexer lexer = new JajaCodeLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JajaCodeParser parser = new JajaCodeParser(tokens);

        ParseTree tree = parser.type();

        assertNotNull(tree);
        String treeText = tree.toStringTree(parser);
        assertThat(treeText, containsString("int"));
    }

    @Test
    public void givenSorte_whenParse_thenCorrect() {
        String input = "var";
        JajaCodeLexer lexer = new JajaCodeLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JajaCodeParser parser = new JajaCodeParser(tokens);

        ParseTree tree = parser.sorte();

        assertNotNull(tree);
        String treeText = tree.toStringTree(parser);
        assertThat(treeText, containsString("var"));
    }
}
