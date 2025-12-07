package fr.amvproject.group3;

import fr.amvproject.group3.jjc.ast.PrintVisitor;
import fr.amvproject.group3.jjc.ast.nodes.program.ProgramNode;
import fr.amvproject.group3.mjj.ast.AstNode;
import fr.amvproject.group3.mjj.utils.AstOperations;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;


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
        // Generate the textual JajaCode representation of the compiled ProgramNode
        baseStepsUtility.lastException = null;
        try {
            PrintVisitor printer = new PrintVisitor();
            String jajacode = baseStepsUtility.currentCompileCode.accept(printer);
            baseStepsUtility.executionResultJajacode = (jajacode == null) ? "" : jajacode.trim();
        } catch (Exception e) {
            baseStepsUtility.lastException = e;
            baseStepsUtility.executionResultJajacode = "";
        }
        // Synchronize state with JajaCode-specific test utility so JCC steps can read the result
        try {
            BaseStepsUtilityJcc jjc = BaseStepsUtilityJcc.getInstance();
            jjc.executionResult = baseStepsUtility.executionResultJajacode;
            jjc.lastException = baseStepsUtility.lastException;
            jjc.currentCompileCode = baseStepsUtility.currentCompileCode;
        } catch (Exception ignore) {
            // defensive: don't let test-state sync break the step
        }
    }


}
