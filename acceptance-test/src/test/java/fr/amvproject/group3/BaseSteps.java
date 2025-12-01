package fr.amvproject.group3;

import io.cucumber.java.ParameterType;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import java.io.IOException;

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
        String expected = baseStepsUtility.loadExpected(baseStepsUtility.currentFilename);
        if (baseStepsUtility.executionResultMiniJaja != null) {
            assertThat(
                    "La sortie du programme ne correspond pas au fichier .out attendu",
                    baseStepsUtility.executionResultMiniJaja,
                    is(expected)
            );
        }
        else {
            assertThat(
                    "La sortie du programme ne correspond pas au fichier .out attendu",
                    baseStepsUtility.executionResultJajacode,
                    is(expected)
            );
        }

    }

    @Then("Le résultat des deux exécutions doit être similaire")
    public void leRésultatDesDeuxExécutionsDoitÊtreSimilaire() {
       assertThat(baseStepsUtility.executionResultJajacode, is(baseStepsUtility.executionResultMiniJaja));
    }
}
