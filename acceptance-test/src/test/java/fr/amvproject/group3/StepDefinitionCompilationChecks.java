package fr.amvproject.group3;

import io.cucumber.java.en.Then;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

public class StepDefinitionCompilationChecks {

    private final BaseStepsUtility base = BaseStepsUtility.getInstance();

    @Then("La compilation doit réussir")
    public void laCompilationDoitRéussir() {
        assertThat("Le code compilé doit être présent après compilation", base.currentCompileCode, notNullValue());
    }
}
