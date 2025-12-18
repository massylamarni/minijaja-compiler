package fr.amvproject.group3;

import fr.amvproject.group3.mjj.InterpreterVisitor;
import fr.amvproject.group3.mjj.ast.AstBuilderVisitor;
import fr.amvproject.group3.mjj.ast.nodes.program.ProgramNode;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.containsString;

public class StepDefinitionInterpreterErrors {

    private final BaseStepsUtility base = BaseStepsUtility.getInstance();
    private Exception interpretationException;

    private ProgramNode buildAstFromCode(String code) {
        MiniJajaLexer lexer = new MiniJajaLexer(CharStreams.fromString(code));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MiniJajaParser parser = new MiniJajaParser(tokens);

        ParseTree tree = parser.program();
        AstBuilderVisitor builder = new AstBuilderVisitor();

        return (ProgramNode) builder.visit(tree);
    }

    @When("J'essaie d'interpreter le programme minijaja")
    public void jessaieDeInterpreterLeProgrammeMinijaja() {
        try {
            ProgramNode program = buildAstFromCode(base.currentFileContent);
            InterpreterVisitor interpreter = new InterpreterVisitor();
            program.accept(interpreter);
            interpretationException = null;
        } catch (Exception e) {
            interpretationException = e;
        }
    }

    @Then("une erreur d'interpretation est levee")
    public void uneErreurDInterpretationEstLevee() {
        assertThat("Une erreur d'interpretation doit avoir eu lieu", interpretationException, is(notNullValue()));
    }

    @Then("l'erreur d'interpretation est de type runtime")
    public void lErreurDInterpretationEstDeTypeRuntime() {
        assertThat("L'exception doit être une RuntimeException", interpretationException, instanceOf(fr.amvproject.group3.exceptions.RuntimeException.class));
    }

    @Then("l'erreur d'interpretation contient {string}")
    public void lErreurDInterpretationContient(String expected) {
        assertThat("Le message d'exception doit contenir le texte attendu", interpretationException.getMessage(), containsString(expected));
    }
}
