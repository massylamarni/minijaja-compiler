package fr.amvproject.group3;

import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class BaseSteps {

    private BaseStepsUtility baseStepsUtility;

    public BaseSteps() {
        this.baseStepsUtility = BaseStepsUtility.getInstance();
    }

    @ParameterType("([\\w\\-./\\\\]+\\.mjj)")
    public String mjjfile(String filename) {
        return filename;
    }

    @Given("Un programme Minijaja du fichier {mjjfile}")
    public void unProgrammeMinijajaDuFichierMjj(String mjjfile) throws IOException {
        if (baseStepsUtility == null) {
            baseStepsUtility = new BaseStepsUtility();
        }
        baseStepsUtility.currentFilename = mjjfile;
        baseStepsUtility.currentFileContent = baseStepsUtility.loadResource(mjjfile);
    }

    @Then("le résultat final doit être correct")
    public void leRésultatFinalDoitÊtreCorrect() throws IOException {
        if (baseStepsUtility == null) {
            baseStepsUtility = new BaseStepsUtility();
        }
        if (baseStepsUtility.lastException != null) {
            StringWriter sw = new StringWriter();
            baseStepsUtility.lastException.printStackTrace(new PrintWriter(sw));
            throw new AssertionError("Une exception est survenue pendant l'exécution/compilation:\n" + sw.toString());
        }
        if (fr.amvproject.group3.BaseStepsUtilityJcc.getInstance().lastException != null) {
            StringWriter sw = new StringWriter();
            fr.amvproject.group3.BaseStepsUtilityJcc.getInstance().lastException.printStackTrace(new PrintWriter(sw));
            throw new AssertionError("Une exception est survenue pendant l'exécution JajaCode:\n" + sw.toString());
        }

        String expected = baseStepsUtility.loadExpected(baseStepsUtility.currentFilename);
        expected = expected.replace("\r\n", "\n").replace("\r", "\n");
        if (baseStepsUtility.executionResultMiniJaja != null) {
            String actual = baseStepsUtility.executionResultMiniJaja.replace("\r\n", "\n").replace("\r", "\n");
            assertThat(
                    "La sortie du programme ne correspond pas au fichier .out attendu",
                    actual,
                    is(expected)
            );
        }
        else {
            String actual = baseStepsUtility.executionResultJajacode.replace("\r\n", "\n").replace("\r", "\n");
            assertThat(
                    "La sortie du programme ne correspond pas au fichier .out attendu",
                    actual,
                    is(expected)
            );
        }

    }

    @Then("Le résultat des deux exécutions doit être similaire")
    public void leRésultatDesDeuxExécutionsDoitÊtreSimilaire() {
       assertThat(baseStepsUtility.executionResultJajacode, is(baseStepsUtility.executionResultMiniJaja));
    }
}
