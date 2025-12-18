package fr.amvproject.group3;

import fr.amvproject.group3.mjj.InterpreterVisitor;
import fr.amvproject.group3.mjj.ast.AstBuilderVisitor;
import fr.amvproject.group3.mjj.ast.nodes.program.ProgramNode;
import io.cucumber.java.en.When;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class StepDefinitionInterpreterMinijaja {


    BaseStepsUtility baseStepsUtility;

    public StepDefinitionInterpreterMinijaja() {
        this.baseStepsUtility = BaseStepsUtility.getInstance();
    }

    private ProgramNode buildAstFromCode(String code) {
        MiniJajaLexer lexer = new MiniJajaLexer(CharStreams.fromString(code));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MiniJajaParser parser = new MiniJajaParser(tokens);

        ParseTree tree = parser.program();
        AstBuilderVisitor builder = new AstBuilderVisitor();

        return (ProgramNode) builder.visit(tree);
    }

    @When("Interpretation du programme minijaja")
    public void interpretationDuProgrammeMinijaja() {

        ProgramNode program = buildAstFromCode(baseStepsUtility.currentFileContent);

        InterpreterVisitor interpreter = new InterpreterVisitor();
        program.accept(interpreter);

        baseStepsUtility.executionResultMiniJaja =
                String.join("", interpreter.getOutputBuffer()).trim();
    }

}
