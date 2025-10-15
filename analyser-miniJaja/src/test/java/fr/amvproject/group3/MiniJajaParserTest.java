package fr.amvproject.group3;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MiniJajaParserTest {


    @Test
    public void givenSimpleOperation__whenParse__thenCorrectTree() {
        // Given
        String input = "1 + 1";

        CharStream cs = CharStreams.fromString(input);
        MiniJajaLexer lexer = new MiniJajaLexer(cs);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MiniJajaParser parser = new MiniJajaParser(tokens);
        // When
        ParseTree tree = parser.exp();
        // Then
        assertThat(tree, notNullValue());
        MiniJajaParser.ExpOrContext expOr = (MiniJajaParser.ExpOrContext) tree.getChild(0);
        MiniJajaParser.ExpAndContext expAnd = (MiniJajaParser.ExpAndContext) expOr.getChild(0);
        MiniJajaParser.ExpEqContext expEq = (MiniJajaParser.ExpEqContext) expAnd.getChild(0);
        MiniJajaParser.ExpRelContext expRel = (MiniJajaParser.ExpRelContext) expEq.getChild(0);
        MiniJajaParser.ExpAddContext expAdd = (MiniJajaParser.ExpAddContext) expRel.getChild(0);

        assertThat(expAdd.getChildCount(), is(3));
        assertThat(expAdd.getChild(0).getText(), is("1"));
        assertThat(expAdd.getChild(1).getText(), is("+"));
        assertThat(expAdd.getChild(2).getText(), is("1"));
    }

    @Test
    public void givenAssign__whenParse__thenCorrectTree() {
        String input = "x = 5;";
        MiniJajaParser parser = new MiniJajaParser(new CommonTokenStream(new MiniJajaLexer(CharStreams.fromString(input))));
        ParseTree tree = parser.instr();

        assertThat(tree, notNullValue());
        assertThat(tree.getChild(0).getText(), is("x"));
        assertThat(tree.getChild(1).getText(), is("="));
        assertThat(tree.getChild(2).getText(), is("5"));
    }

    @Test
    public void givenVarDeclaration__whenParse__thenCorrectTree() {
        String input = "int y = 10;";
        MiniJajaParser parser = new MiniJajaParser(new CommonTokenStream(new MiniJajaLexer(CharStreams.fromString(input))));
        ParseTree tree = parser.var();

        assertThat(tree, notNullValue());
        assertThat(tree.getChild(0).getText(), is("int"));
        assertThat(tree.getChild(1).getText(), is("y"));
        assertThat(tree.getChild(2).getChild(0).getText(), is("="));
        assertThat(tree.getChild(2).getChild(1).getText(), is("10"));
    }

    @Test
    public void givenMethodCall__whenParse__thenCorrect() {
        String input = "meth(1, 2)";
        MiniJajaParser parser = new MiniJajaParser(new CommonTokenStream(new MiniJajaLexer(CharStreams.fromString(input))));
        ParseTree tree = parser.instr();

        assertThat(tree.getChild(0).getText(), is("meth"));
        assertThat(tree.getChild(1).getText(), is("("));
        assertThat(tree.getChild(2).getText(), is("1,2"));
        assertThat(tree.getChild(3).getText(), is(")"));
    }

    @Test
    public void givenComplexExpression__whenParse__thenCorrect() {
        // Given
        String input = "(1 + 2) * 3";
        MiniJajaParser parser = new MiniJajaParser(
                new CommonTokenStream(new MiniJajaLexer(CharStreams.fromString(input)))
        );

        // When
        MiniJajaParser.ExpContext expCtx = parser.exp();

        // Then
        assertThat(expCtx, notNullValue());

        MiniJajaParser.ExpMulContext expMulCtx =
                expCtx.expOr().expAnd(0).expEq(0).expRel(0).expAdd().expMul(0);

        assertThat(expMulCtx.expUnary().size(), is(2));
        assertThat(expMulCtx.expUnary(0).getText(), is("(1+2)"));
        assertThat(expMulCtx.expUnary(1).getText(), is("3"));
    }


    @Test
    public void givenIfStatement__whenParse__thenCorrect() {
        // Given
        String input = "if (x > 5) { x = 1; }";
        MiniJajaParser parser = new MiniJajaParser(
                new CommonTokenStream(new MiniJajaLexer(CharStreams.fromString(input)))
        );

        // When
        MiniJajaParser.InstrContext instrCtx = parser.instr();

        // Then
        assertNotNull(instrCtx.IF());
        assertEquals("if", instrCtx.IF().getText());

        MiniJajaParser.ExpContext cond = instrCtx.exp();
        assertNotNull(cond);

        MiniJajaParser.ExpEqContext expEq = cond
                .expOr()
                .expAnd(0)
                .expEq(0);

        assertThat(expEq.getChild(0).getText(), is("x"));
        assertThat(expEq.getChild(1).getText(), is(">"));
        assertThat(expEq.getChild(2).getText(), is("5"));

        MiniJajaParser.InstrsContext instrsCtx = instrCtx.instrs(0);
        assertNotNull(instrsCtx);

        // Première (et seule) instruction à l’intérieur du bloc
        MiniJajaParser.InstrContext innerInstr = instrsCtx.instr(0);
        assertNotNull(innerInstr);

        assertThat(innerInstr.ident1().getText(), is("x"));
        assertThat(innerInstr.ASSIGN().getText(), is("="));
        assertThat(innerInstr.exp().getText(), is("1"));
    }


    @Test
    public void givenWhileLoop_whenParse_thenCorrect() {
        // Given
        String input = "while (x > 10) { x = x + 1; }";
        MiniJajaParser parser = new MiniJajaParser(
                new CommonTokenStream(new MiniJajaLexer(CharStreams.fromString(input)))
        );

        // When
        MiniJajaParser.InstrContext instrCtx = parser.instr();

        // Then
        assertThat(instrCtx, notNullValue());
        assertThat(instrCtx.WHILE(), notNullValue());
        assertThat(instrCtx.WHILE().getText(), is("while"));

        MiniJajaParser.ExpContext cond = instrCtx.exp();
        assertThat(cond, notNullValue());

        MiniJajaParser.ExpEqContext expEq =
                cond.expOr().expAnd(0).expEq(0);

        List<String> condParts = expEq.children.stream()
                .map(ParseTree::getText)
                .collect(Collectors.toList());

        assertThat(condParts, hasItems("x", ">", "10"));

        MiniJajaParser.InstrsContext instrsCtx = instrCtx.instrs(0);
        assertThat(instrsCtx, notNullValue());

        MiniJajaParser.InstrContext innerInstr = instrsCtx.instr(0);
        assertThat(innerInstr, notNullValue());

        assertThat(innerInstr.ident1().getText(), is("x"));
        assertThat(innerInstr.ASSIGN().getText(), is("="));

        MiniJajaParser.ExpContext rhs = innerInstr.exp();
        MiniJajaParser.ExpAddContext rhsAdd =
                rhs.expOr().expAnd(0).expEq(0).expRel(0).expAdd();

        List<String> rhsParts = rhsAdd.children.stream()
                .map(ParseTree::getText)
                .collect(Collectors.toList());

        assertThat(rhsParts, hasItems("x", "+", "1"));
    }


    @Test
    public void givenMethodDeclaration_whenParse_thenCorrect() {
        // Given
        String input = "int sum(int a, int b) { int r; r = a + b; }";
        MiniJajaParser parser = new MiniJajaParser(
                new CommonTokenStream(new MiniJajaLexer(CharStreams.fromString(input)))
        );

        // When
        MiniJajaParser.MethodeContext methodeCtx = parser.methode();

        // Then
        assertThat(methodeCtx, notNullValue());
        assertThat(methodeCtx.ident().getText(), is("sum"));
        assertThat(methodeCtx.typemeth().getText(), is("int"));

        MiniJajaParser.EntetesContext paramsCtx = methodeCtx.entetes();
        assertThat(paramsCtx, notNullValue());

        List<MiniJajaParser.EnteteContext> enteteList = paramsCtx.entete();
        assertThat(enteteList.size(), is(2));

        MiniJajaParser.EnteteContext param1 = enteteList.get(0);
        assertThat(param1.type().getText(), is("int"));
        assertThat(param1.ident().getText(), is("a"));

        MiniJajaParser.EnteteContext param2 = enteteList.get(1);
        assertThat(param2.type().getText(), is("int"));
        assertThat(param2.ident().getText(), is("b"));

        MiniJajaParser.VarsContext varsCtx = methodeCtx.vars();
        assertThat(varsCtx, notNullValue());

        MiniJajaParser.VarContext varR = varsCtx.var(0);
        assertThat(varR.typemeth().getText(), is("int"));
        assertThat(varR.ident().getText(), is("r"));

        MiniJajaParser.InstrsContext instrsCtx = methodeCtx.instrs();
        assertThat(instrsCtx, notNullValue());

        MiniJajaParser.InstrContext assignInstr = instrsCtx.instr(0);
        assertThat(assignInstr.ident1().getText(), is("r"));
        assertThat(assignInstr.ASSIGN().getText(), is("="));

        MiniJajaParser.ExpContext rhs = assignInstr.exp();
        MiniJajaParser.ExpAddContext rhsAdd =
                rhs.expOr().expAnd(0).expEq(0).expRel(0).expAdd();

        List<String> rhsParts = rhsAdd.children.stream()
                .map(ParseTree::getText)
                .collect(Collectors.toList());

        assertThat(rhsParts, hasItems("a", "+", "b"));
    }


    @Test
    public void givenSimpleProgram_whenParse_thenCorrect() {
        String input = """
                    class Test {
                        int x;
                        main {
                            int y;
                            y = 5;
                        }
                    }
                """;

        MiniJajaParser parser = new MiniJajaParser(
                new CommonTokenStream(new MiniJajaLexer(CharStreams.fromString(input)))
        );

        MiniJajaParser.ProgramContext programCtx = parser.program();
        assertThat(programCtx, notNullValue());

        assertThat(programCtx.classe(), notNullValue());
        assertThat(programCtx.classe(0).ident().getText(), is("Test"));

        MiniJajaParser.DeclsContext declsCtx = programCtx.classe(0).decls();
        assertThat(declsCtx, notNullValue());

        MiniJajaParser.DeclContext varXDecl = declsCtx.decl(0);
        MiniJajaParser.VarContext varX = varXDecl.var();
        assertThat(varX.typemeth().getText(), is("int"));
        assertThat(varX.ident().getText(), is("x"));

        MiniJajaParser.MethmainContext mainCtx = programCtx.classe(0).methmain();
        assertThat(mainCtx, notNullValue());

        MiniJajaParser.VarsContext mainVars = mainCtx.vars();
        MiniJajaParser.VarContext varY = mainVars.var(0);
        assertThat(varY.typemeth().getText(), is("int"));
        assertThat(varY.ident().getText(), is("y"));

        MiniJajaParser.InstrsContext mainInstrs = mainCtx.instrs();
        MiniJajaParser.InstrContext assignInstr = mainInstrs.instr(0);
        assertThat(assignInstr.ident1().getText(), is("y"));
        assertThat(assignInstr.ASSIGN().getText(), is("="));
        assertThat(assignInstr.exp().getText(), is("5"));
    }


}
