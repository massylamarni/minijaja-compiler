package fr.amvproject.group3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.antlr.v4.runtime.*;
import fr.amvproject.group3.jjc.ast.AstNode;
import fr.amvproject.group3.jjc.ast.AstBuilderVisitor;
import fr.amvproject.group3.jjc.ast.nodes.program.ProgramNode;
import fr.amvproject.group3.jjc.ast.nodes.program.ClassNode;
import fr.amvproject.group3.jjc.ast.nodes.intermediate.InstrNode;
import fr.amvproject.group3.interpreter.JajaCodeInterpreter;
import fr.amvproject.group3.interpreter.ExecutionStep;


/*
 * JajaCodeExecuter.java is just used to simulate the interpretation and display the result in the CLI
 */

public class JajaCodeExecuter {

    public static void main(String[] args) throws Exception {
        String inputFile = args.length > 0 ? args[0] : "interpreter-JajaCode/src/test/resources/demo_full.jjc";

        System.out.println("===============================================");
        System.out.println("JajaCode Interpreter - Semantic Execution");
        System.out.println("===============================================");
        System.out.println("Input file: " + inputFile);
        System.out.println();

        try {
            CharStream input = CharStreams.fromFileName(inputFile);
            JajaCodeLexer lexer = new JajaCodeLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            JajaCodeParser parser = new JajaCodeParser(tokens);
            JajaCodeParser.ProgramContext tree = parser.program();

            AstBuilderVisitor builder = new AstBuilderVisitor();
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

    public static void executeProgram(AstNode ast, JajaCodeInterpreter interpreter) {
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

    public static void printExecutionSteps(JajaCodeInterpreter interpreter) {
        List<ExecutionStep> steps = interpreter.getExecutionSteps();

        if (steps.isEmpty()) {
            System.out.println("No steps executed");
            return;
        }

        for (ExecutionStep step : steps) {
            System.out.println("Step " + step.getStepNumber() + ": " + step.getInstructionExecuted());
            System.out.println("  Address Counter: " + step.getAddressCounter());
            List<String> stackState = step.getStackState();
            if (stackState.isEmpty()) {
                System.out.println("  Stack: [[Empty]]");
            } else {
                System.out.println("  Stack: [" + String.join(", ", stackState) + "]");
            }

            String heapState = step.getHeapState();
            if (heapState == null || heapState.isEmpty() || heapState.equals("[Heap managed by miniJaja Stack]")) {
                System.out.println("  Heap: [No allocations]");
            } else {
                System.out.println("  Heap: " + heapState);
            }

            if (step.isStopped()) {
                System.out.println("  STATUS: STOPPED");
            }

            System.out.println();
        }
    }

    public static void printFinalState(JajaCodeInterpreter interpreter) {
        List<String> stackStrings = interpreter.getState().getStackStateAsStrings();
        if (stackStrings.isEmpty()) {
            System.out.println("Stack: [[Empty]]");
        } else {
            System.out.println("Stack: " + String.join(", ", stackStrings));
        }

        String heapState = interpreter.getState().getHeapStateAsString();
        System.out.println("Heap: " + heapState);
        System.out.println("Address Counter: " + interpreter.getState().getAddressCounter());
        System.out.println("Stopped: " + interpreter.getState().isStopped());
    }
}
