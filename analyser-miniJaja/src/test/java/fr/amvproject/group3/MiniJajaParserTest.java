package fr.amvproject.group3;
/*
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MiniJajaParserTest {


    @Test
    public void given__when__then() {
        String input = "1 + 1";

        CharStream cs = CharStreams.fromString(input);
        MiniJajaLexer lexer = new MiniJajaLexer(cs);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MiniJajaParser parser = new MiniJajaParser(tokens);

        ParseTree tree = parser.exp();
        assertNotNull(tree);
        MiniJajaParser.ExpAndContext expAnd = (MiniJajaParser.ExpAndContext) tree.getChild(0);
        MiniJajaParser.ExpEqContext expEq = (MiniJajaParser.ExpEqContext) expAnd.getChild(0);
        MiniJajaParser.ExpAddContext expAdd = (MiniJajaParser.ExpAddContext) expEq.getChild(0);

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
*/