package fr.amvproject.group3;

import fr.amvproject.group3.CompilerVisitor;
import fr.amvproject.group3.mjj.analyser.SemanticVisitor;
import fr.amvproject.group3.exceptions.SemanticException;
import fr.amvproject.group3.jjc.ast.nodes.program.ProgramNode;
import fr.amvproject.group3.mjj.ast.AstNode;
import fr.amvproject.group3.mjj.utils.AstOperations;
import fr.amvproject.group3.mjj.utils.AstOperations.AstWrapper;
import fr.amvproject.group3.mjj.utils.NodeOperations.MockToken;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;


public class StepDefinitionErrors {

    private final BaseStepsUtility base = BaseStepsUtility.getInstance();
    private Exception compileException;

    @When("J'essaie de compiler le programme")
    public void jessaieDeCompilerLeProgramme() {
        try {
            fr.amvproject.group3.mjj.utils.AstOperations.AstWrapper wrapper = fr.amvproject.group3.mjj.utils.AstOperations.getAstFromString(base.currentFileContent);
            if (wrapper.lexicalErrorsListener.hasErrors() || wrapper.syntaxicErrorsListener.hasErrors()) {
                throw new RuntimeException("Erreur de syntaxe ou lexicale détectée");
            }
            AstNode ast = wrapper.ast;
            if (ast == null) {
                throw new RuntimeException("AST non généré en raison d'erreurs");
            }

            try {
                SemanticVisitor semanticVisitor = new SemanticVisitor();
                ast.accept(semanticVisitor);
            } catch (SemanticException se) {
                compileException = se;
                return;
            }

            CompilerVisitor compilerVisitor = new CompilerVisitor();
            base.currentCompileCode = new ProgramNode(ast.accept(compilerVisitor), new MockToken("", 1, 0));
            compileException = null;
        } catch (Exception e) {

            compileException = e;
        }
    }

    @Then("Une erreur est donnée")
    public void uneErreurEstDonnee() {
        assertThat("Une erreur de compilation doit avoir eu lieu", compileException, is(notNullValue()));
    }
}
