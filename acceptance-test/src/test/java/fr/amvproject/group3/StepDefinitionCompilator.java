package fr.amvproject.group3;

import fr.amvproject.group3.jjc.InterpreterVisitor;
import fr.amvproject.group3.jjc.ast.nodes.program.ProgramNode;
import fr.amvproject.group3.mjj.ast.AstNode;
import fr.amvproject.group3.mjj.utils.AstOperations;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static fr.amvproject.group3.mjj.utils.NodeOperations.MockToken;

public class StepDefinitionCompilator {

    BaseStepsUtility baseStepsUtility;

    public StepDefinitionCompilator() {
        this.baseStepsUtility = BaseStepsUtility.getInstance();
    }


    @When("Compilation du programme")
    public void compilationDuProgramme() {
        AstNode ast = AstOperations.getAstFromString(baseStepsUtility.currentFileContent).ast;
        CompilerVisitor compilerVisitor = new CompilerVisitor();
        baseStepsUtility.currentCompileCode = new ProgramNode(ast.accept(compilerVisitor), new MockToken("", 1, 0));
    }

    @And("Execution du programme compilé")
    public void executionDuProgrammeCompilé() {
        baseStepsUtility.originalOut = System.out;
        baseStepsUtility.outputCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baseStepsUtility.outputCaptor));
        // When
        InterpreterVisitor interpreter = new InterpreterVisitor();
        try {
            baseStepsUtility.currentCompileCode.accept(interpreter);
        }finally {
            System.setOut(baseStepsUtility.originalOut);
        }
        baseStepsUtility.executionResultJajacode = baseStepsUtility.outputCaptor.toString().trim();
    }


}
