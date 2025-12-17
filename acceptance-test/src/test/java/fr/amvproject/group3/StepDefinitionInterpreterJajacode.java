package fr.amvproject.group3;

import fr.amvproject.group3.jjc.Interpreter;
import fr.amvproject.group3.jjc.ast.nodes.program.ProgramNode;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then; // Added for @Then annotation

import java.io.ByteArrayOutputStream;
import java.io.IOException; // Added for IOException
import java.io.PrintStream;

import static org.hamcrest.MatcherAssert.assertThat; // Added for assertThat
import static org.hamcrest.Matchers.*; // Added for Hamcrest matchers

public class StepDefinitionInterpreterJajacode {

    private final BaseStepsUtility base = BaseStepsUtility.getInstance();
    private final BaseStepsUtilityJcc jccState = BaseStepsUtilityJcc.getInstance();



    @When("Interpretation du programme jajacode")
    public void interpretationDuProgrammeJajacode() {
        jccState.originalOut = System.out;
        jccState.outputCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(jccState.outputCaptor));
        try {
            if (jccState.currentProgramContent == null || jccState.currentProgramContent.isEmpty()) {
                throw new IllegalStateException("Le programme JajaCode n'a pas été chargé.");
            }

            Interpreter interpreter = new Interpreter(jccState.currentProgramContent);
            interpreter.run();

            jccState.executionResult = jccState.outputCaptor.toString().trim();
        } catch (Exception e) {
            jccState.lastException = e;
        } finally {
            System.setOut(jccState.originalOut);
        }
    }



     @Then("Une erreur d'exécution doit être détectée")
    public void uneErreurDExécutionDoitÊtreDétectée() {
        assertThat(jccState.lastException, is(notNullValue()));
    }

}
