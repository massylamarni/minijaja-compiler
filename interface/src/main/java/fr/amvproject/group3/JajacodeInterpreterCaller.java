package fr.amvproject.group3;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.*;
import fr.amvproject.group3.jajaCode.ast.AstNode;
import fr.amvproject.group3.jajaCode.ast.JajaCodeAstBuilder;
import fr.amvproject.group3.interpreter.JajaCodeInterpreter;

import static fr.amvproject.group3.JajaCodeExecuter.*;


public class JajacodeInterpreterCaller {

/*
    public void execute(String input) {

        JajaCodeLexer lexer = new JajaCodeLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JajaCodeParser parser = new JajaCodeParser(tokens);
        JajaCodeParser.ProgramContext tree = parser.program();

        JajaCodeAstBuilder builder = new JajaCodeAstBuilder();
        AstNode ast = builder.visit(tree);

        JajaCodeInterpreter interpreter = new JajaCodeInterpreter();

        System.out.println("✓ Interpreter created");
        System.out.println();
        System.out.println("--- Executing Program ---");
        System.out.println();

        if (ast != null) {
            executeProgram(ast, interpreter);

            System.out.println();
            System.out.println("--- Execution Steps ---");
            printExecutionSteps(interpreter);

            System.out.println();
            System.out.println("--- Final State ---");
            printFinalState(interpreter);
        }


        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText("Ceci est une popup simple !");
        alert.showAndWait();
    }
    */

    public void execute(String inputText) {

        System.out.println("===============================================");
        System.out.println("JajaCode Interpreter - Semantic Execution");
        System.out.println("===============================================");
        System.out.println();

        try {
            CharStream input = CharStreams.fromString(inputText);
            JajaCodeLexer lexer = new JajaCodeLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            JajaCodeParser parser = new JajaCodeParser(tokens);
            JajaCodeParser.ProgramContext tree = parser.program();

            JajaCodeAstBuilder builder = new JajaCodeAstBuilder();
            AstNode ast = builder.visit(tree);

            System.out.println("✓ Program parsed successfully");

            JajaCodeInterpreter interpreter = new JajaCodeInterpreter();

            System.out.println("✓ Interpreter created");
            System.out.println();
            System.out.println("--- Executing Program ---");
            System.out.println();

            if (ast != null) {
                executeProgram(ast, interpreter);

                System.out.println();
                System.out.println("--- Execution Steps ---");
                printExecutionSteps(interpreter);

                System.out.println();
                System.out.println("--- Final State ---");
                printFinalState(interpreter);
            }

            System.out.println();
            System.out.println("===============================================");
            System.out.println("Execution finished successfully!");
            System.out.println("===============================================");

            String output = interpreter.getState().getOutput();
            if (!output.isEmpty()) {
                System.out.println("--- Program Output ---");
                System.out.print(output);
                System.out.println();
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
