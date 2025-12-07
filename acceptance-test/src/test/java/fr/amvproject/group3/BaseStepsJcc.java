package fr.amvproject.group3;

import fr.amvproject.group3.jjc.ExecutionContext;
import fr.amvproject.group3.JajaCodeLexer;
import fr.amvproject.group3.JajaCodeParser;
import fr.amvproject.group3.jjc.Interpreter;
import fr.amvproject.group3.jjc.InterpreterVisitor;
import fr.amvproject.group3.jjc.ast.AstBuilderVisitor;
import fr.amvproject.group3.jjc.ast.AstNode;
import fr.amvproject.group3.jjc.ast.nodes.program.ProgramNode;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import fr.amvproject.group3.exceptions.SemanticException;
import fr.amvproject.group3.exceptions.RuntimeException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class BaseStepsJcc {

    private final BaseStepsUtilityJcc jjcState = BaseStepsUtilityJcc.getInstance();
    private final BaseStepsUtility base = BaseStepsUtility.getInstance();

       @ParameterType("([\\w\\-./\\\\]+\\.jjc)")
       public String jjcfile(String jjcfile) {
           return jjcfile;
       }
   
       @ParameterType("([\\w\\-./\\\\]+\\.out)")
       public String outfile(String outfile) {
           return outfile;
       }
    @Given("Un programme JajaCode du fichier {jjcfile}")
    public void unProgrammeJajaCodeDuFichier(String jjcfile) throws Exception {
        jjcState.currentFilename = jjcfile;
        String content = jjcState.loadResource(jjcfile);
        jjcState.lastException = null;
        jjcState.currentCompileCode = null; // Clear previous state
        jjcState.currentProgramContent = content; // Store the program content

        try {
            CharStream cs = CharStreams.fromString(content);
            JajaCodeLexer lexer = new JajaCodeLexer(cs);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            JajaCodeParser parser = new JajaCodeParser(tokens);
            parser.removeErrorListeners();
            parser.addErrorListener(new org.antlr.v4.runtime.BaseErrorListener() {
                @Override
                public void syntaxError(org.antlr.v4.runtime.Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, org.antlr.v4.runtime.RecognitionException e) {
                    throw new IllegalArgumentException("Failed to parse " + jjcfile + " at line " + line + ":" + charPositionInLine + " " + msg);
                }
            });
            JajaCodeParser.ProgramContext tree = parser.program();

            AstBuilderVisitor builder = new AstBuilderVisitor();
            AstNode ast = builder.visit(tree);

            if (ast instanceof ProgramNode) {
                jjcState.currentCompileCode = (ProgramNode) ast;
            } else {
                throw new IllegalArgumentException("Fichier JajaCode non valide: " + jjcfile);
            }
        } catch (Exception e) {
            jjcState.lastException = e;
        }
    }

    @When("J'essaie d'exécuter le programme compilé")
    public void jEssaieDExécuterLeProgrammeCompilé() {
        jjcState.lastException = null;
        jjcState.originalOut = System.out;
        jjcState.outputCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(jjcState.outputCaptor));
        try {
            // The ProgramNode 'program' is no longer directly used for execution,
            // as the Interpreter now takes the raw program content.
            // If `jjcState.currentProgramContent` is null or parsing fails,
            // the Interpreter constructor will likely throw an exception.
            if (jjcState.currentProgramContent == null || jjcState.currentProgramContent.isEmpty()) {
                jjcState.lastException = new IllegalStateException("Le programme JajaCode n'a pas été chargé.");
                return; // Exit the method as there's nothing to interpret
            }

            Interpreter interpreter = new Interpreter(jjcState.currentProgramContent);
            interpreter.run();

            if (interpreter.getSemanticErrorsCollector().hasErrors()) {
                // If semantic errors occurred,
                SemanticException semanticError = interpreter.getSemanticErrorsCollector().getErrors().get(0);
                jjcState.lastException = semanticError;
            } else if (interpreter.getRuntimeErrorsCollector().hasErrors()) {
                // If runtime errors occurred,
                fr.amvproject.group3.exceptions.RuntimeException runtimeError = interpreter.getRuntimeErrorsCollector().getErrors().get(0);
                jjcState.lastException = runtimeError;
            }

            jjcState.executionResult = jjcState.outputCaptor.toString().trim();
        } catch (Exception e) {
            jjcState.lastException = e;
        } finally {
            if (jjcState.originalOut != null) {
                System.setOut(jjcState.originalOut);
            }
        }
    }
    /* 
    @Then("le résultat final doit être correct jcc")
    public void le_résultat_final_doit_être_correct() throws IOException {
        if (jjcState.lastException != null) {
            throw new AssertionError("Une exception inattendue s'est produite lors de l'interprétation: " + jjcState.lastException.getMessage(), jjcState.lastException);
        }

        String expectedOutput = base.loadExpected(jjcState.currentFilename);
        assertThat("Le résultat de l'interprétation JajaCode ne correspond pas à l'attendu.", jjcState.executionResult, is(equalTo(expectedOutput)));
    }
    */
    @Then("Une erreur de compilation doit être détectée")
    public void uneErreurDeCompilationDoitÊtreDétectée() {
        assertThat(jjcState.lastException, is(notNullValue()));
       // assertThat(jjcState.lastException, instanceOf(IllegalArgumentException.class));
    }

    @Then("le résultat final doit être correct jcc {outfile}")
    public void le_résultat_final_doit_être_correct_jcc(String outfile) throws IOException {
        if (jjcState.lastException != null) {
            throw new AssertionError("Une exception inattendue s'est produite lors de l'interprétation: " + jjcState.lastException.getMessage(), jjcState.lastException);
        }

        String expectedOutput = base.loadResource(outfile).trim();
        String actualOutput = (jjcState.executionResult == null) ? "" : jjcState.executionResult.trim();
        // Normalize line endings to avoid OS-specific differences (CRLF vs LF)
        expectedOutput = expectedOutput.replace("\r\n", "\n");
        actualOutput = actualOutput.replace("\r\n", "\n");
        assertThat("Le résultat de l'interprétation JajaCode ne correspond pas à l'attendu pour " + outfile + ".", actualOutput, is(equalTo(expectedOutput)));
    }

   
}
