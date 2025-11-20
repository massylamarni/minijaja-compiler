package fr.amvproject.group3;

import fr.amvproject.group3.interpreter.JajaCodeInterpreter;
import fr.amvproject.group3.jjc.ast.nodes.program.ProgramNode;
import fr.amvproject.group3.mjj.ast.AstNode;
import fr.amvproject.group3.mjj.utils.AstOperations;
import io.cucumber.java.ParameterType;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static fr.amvproject.group3.mjj.utils.NodeOperations.MockToken;

public class StepDefinitionCompilator {

    String currentFileContent;
    ProgramNode currentCompileCode;
    String currentFilename;

    ByteArrayOutputStream outputCaptor;
    PrintStream originalOut;
    String executionResult;

    @ParameterType("([\\w\\-./\\\\]+\\.mjj)")
    public String mjjfile(String filename) {
        return filename;
    }

    private String loadResource(String filename) throws IOException {
        InputStream is = getClass().getClassLoader()
                .getResourceAsStream("acceptance/" + filename);
        assertThat(is, is(notNullValue()));

        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }

    private String loadExpected(String baseName) throws IOException {
        String expectedFile = baseName.replace(".mjj", ".out");
        return loadResource(expectedFile).trim();
    }

    @Given("Un programme Minijaja du fichier {mjjfile}")
    public void unProgrammeMinijajaDuFichierMjj(String mjjfile) throws IOException {
        currentFilename = mjjfile;
        currentFileContent = loadResource(mjjfile);
    }

    @When("Compilation du programme")
    public void compilationDuProgramme() {
        AstNode ast = AstOperations.getAstFromString(currentFileContent);
        CompilerVisitor compilerVisitor = new CompilerVisitor();
        currentCompileCode = new ProgramNode(ast.accept(compilerVisitor), new MockToken("", 1, 0));
    }

    @And("Execution du programme compilé")
    public void executionDuProgrammeCompilé() {
        originalOut = System.out;
        outputCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputCaptor));
        try {
            JajaCodeExecuter.executeProgram(currentCompileCode, new JajaCodeInterpreter());
        } finally {
            System.setOut(originalOut);
        }
        executionResult = outputCaptor.toString().trim();
    }

    @Then("le résultat final doit être correct")
    public void leRésultatFinalDoitÊtreCorrect() throws IOException {
        String expected = loadExpected(currentFilename);

        assertThat(
                "La sortie du programme ne correspond pas au fichier .out attendu",
                executionResult,
                is(expected)
        );
    }

    @And("Execution du programme minijaja")
    public void executionDuProgrammeMinijaja() {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("Le résultat des deux exécutions doit être similaire")
    public void leRésultatDesDeuxExécutionsDoitÊtreSimilaire() {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }
}
