package fr.amvproject.group3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.*;
import fr.amvproject.group3.jjc.ast.AstNode;
import fr.amvproject.group3.jjc.ast.AstBuilderVisitor;
import fr.amvproject.group3.jjc.ast.nodes.intermediate.InstrNode;
import fr.amvproject.group3.jjc.ast.nodes.program.ClassNode;
import fr.amvproject.group3.jjc.ast.nodes.program.ProgramNode;
import fr.amvproject.group3.interpreter.ExecutionStep;
import fr.amvproject.group3.interpreter.JajaCodeInterpreter;


public class JajacodeInterpreterCaller {
    private StringBuilder sb = new StringBuilder();


    public void executeProgram(AstNode ast, JajaCodeInterpreter interpreter) {
        if (ast instanceof ProgramNode programNode) {
            Map<Integer, InstrNode> instructionMap = new HashMap<>();
            List<ClassNode> classes = programNode.getClasses();

            for (ClassNode classNode : classes) {
                if (classNode != null && classNode.getAddress() != null && classNode.getInstr() != null) {
                    instructionMap.put(classNode.getAddress().getValue(), classNode.getInstr());
                }
            }

            int maxIterations = 10000;
            int iterations = 0;

            while (!interpreter.getState().isStopped() && iterations < maxIterations) {
                int currentAddress = interpreter.getState().getAddressCounter();
                InstrNode instruction = instructionMap.get(currentAddress);

                if (instruction == null) {
                    System.err.println("Warning: No instruction at address " + currentAddress);
                    break;
                }

                instruction.accept(interpreter);
                iterations++;
            }

            if (iterations >= maxIterations) {
                System.err.println("Warning: Maximum iterations reached");
            }
        }
    }

    public void printExecutionSteps(JajaCodeInterpreter interpreter) {
        List<ExecutionStep> steps = interpreter.getExecutionSteps();

        if (steps.isEmpty()) {
            sb.append("\n");
            sb.append("No steps executed");
            return;
        }

        for (ExecutionStep step : steps) {
            sb.append("\n");
            sb.append("Step " + step.getStepNumber() + ": " + step.getInstructionExecuted());
            sb.append("\n");
            sb.append("  Address Counter: " + step.getAddressCounter());
            List<String> stackState = step.getStackState();
            if (stackState.isEmpty()) {
                sb.append("\n");
                sb.append("  Stack: [[Empty]]");
            } else {
                sb.append("\n");
                sb.append("  Stack: [" + String.join(", ", stackState) + "]");
            }

            String heapState = step.getHeapState();
            if (heapState == null || heapState.isEmpty() || heapState.equals("[Heap managed by miniJaja Stack]")) {
                sb.append("\n");
                sb.append("  Heap: [No allocations]");
            } else {
                sb.append("\n");
                sb.append("  Heap: " + heapState);
            }

            if (step.isStopped()) {
                sb.append("\n");
                sb.append("  STATUS: STOPPED");
            }

        }
    }

    public void printFinalState(JajaCodeInterpreter interpreter) {
        List<String> stackStrings = interpreter.getState().getStackStateAsStrings();
        if (stackStrings.isEmpty()) {
            sb.append("\n");
            sb.append("Stack: [[Empty]]");
        } else {
            sb.append("\n");
            sb.append("Stack: " + String.join(", ", stackStrings));
        }

        String heapState = interpreter.getState().getHeapStateAsString();
        sb.append("\n");
        sb.append("Heap: " + heapState);
        sb.append("\n");
        sb.append("Address Counter: " + interpreter.getState().getAddressCounter());
        sb.append("\n");
        sb.append("Stopped: " + interpreter.getState().isStopped());
    }

/*
    public void execute(String input) {

        JajaCodeLexer lexer = new JajaCodeLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JajaCodeParser parser = new JajaCodeParser(tokens);
        JajaCodeParser.ProgramContext tree = parser.program();

        JajaCodeAstBuilder builder = new JajaCodeAstBuilder();
        AstNode ast = builder.visit(tree);

        JajaCodeInterpreter interpreter = new JajaCodeInterpreter();

        sb.append("\n");
        sb.append("✓ Interpreter created");
        
        sb.append("\n");
        sb.append("--- Executing Program ---");
        

        if (ast != null) {
            executeProgram(ast, interpreter);

            
            sb.append("\n");
            sb.append("--- Execution Steps ---");
            printExecutionSteps(interpreter);

            
            sb.append("\n");
            sb.append("--- Final State ---");
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

        sb.append("\n");
        sb.append("===============================================");
        sb.append("\n");
        sb.append("JajaCode Interpreter - Semantic Execution");
        sb.append("\n");
        sb.append("===============================================");
        

        try {
            CharStream input = CharStreams.fromString(inputText);
            JajaCodeLexer lexer = new JajaCodeLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            JajaCodeParser parser = new JajaCodeParser(tokens);
            JajaCodeParser.ProgramContext tree = parser.program();

            AstBuilderVisitor builder = new AstBuilderVisitor();
            AstNode ast = builder.visit(tree);

            sb.append("\n");
            sb.append("✓ Program parsed successfully");

            JajaCodeInterpreter interpreter = new JajaCodeInterpreter();

            sb.append("\n");
            sb.append("✓ Interpreter created");
            
            sb.append("\n");
            sb.append("--- Executing Program ---");
            

            if (ast != null) {
                executeProgram(ast, interpreter);

                
                sb.append("\n");
                sb.append("--- Execution Steps ---");
                printExecutionSteps(interpreter);

                
                sb.append("\n");
                sb.append("--- Final State ---");
                printFinalState(interpreter);
            }

            
            sb.append("\n");
            sb.append("===============================================");
            sb.append("\n");
            sb.append("Execution finished successfully!");
            sb.append("\n");
            sb.append("===============================================");

            String output = interpreter.getState().getOutput();
            if (!output.isEmpty()) {
                sb.append("\n");
                sb.append("--- Program Output ---");
                sb.append("\n");
                sb.append(output);
                
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String getString() {
        return sb.toString();
    }

}
