package fr.amvproject.group3;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MiniJaJaParserTest {


    @Test
    public void given__when__then() {
        String input = "1 + 1";

        CharStream cs = CharStreams.fromString(input);
        MiniJaJaLexer lexer = new MiniJaJaLexer(cs);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MiniJaJaParser parser = new MiniJaJaParser(tokens);

        ParseTree tree = parser.exp();
        assertNotNull(tree);
        MiniJaJaParser.ExpAndContext expAnd = (MiniJaJaParser.ExpAndContext) tree.getChild(0);
        MiniJaJaParser.ExpEqContext expEq = (MiniJaJaParser.ExpEqContext) expAnd.getChild(0);
        MiniJaJaParser.ExpAddContext expAdd = (MiniJaJaParser.ExpAddContext) expEq.getChild(0);

        // Vérifie la structure de l'opération + :
        assertEquals(3, expAdd.getChildCount());   // gauche, +, droite
        assertEquals("1", expAdd.getChild(0).getText());
        assertEquals("+", expAdd.getChild(1).getText());
        assertEquals("1", expAdd.getChild(2).getText());
        // Given

        // When

        // Then

    }

}
