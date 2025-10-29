package fr.amvproject.group3;

import java.util.List;
import org.antlr.v4.runtime.*;
import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.JajaCodeAstBuilder;
import fr.amvproject.group3.ast.nodes.program.ProgramNode;
import fr.amvproject.group3.ast.nodes.program.ClassNode;
import fr.amvproject.group3.ast.nodes.intermediate.InstrNode;
import fr.amvproject.group3.interpreter.JajaCodeInterpreter;
import fr.amvproject.group3.interpreter.ExecutionStep;

/**
 * JajaCode Interpreter Application
 * 
 * This application demonstrates the execution of JajaCode programs
 * using the semantic interpretation rules defined in the interpreter.
 */
public class App {
    
    public static void main(String[] args) throws Exception {
        // Determine input file
        String inputFile;
        if (args.length > 0) {
            inputFile = args[0];
        } else {
            // Use demo.jjc by default
            inputFile = "interpreter-JajaCode/src/test/resources/demo_full.jjc";
        }
        
        System.out.println("===============================================");
        System.out.println("JajaCode Interpreter - Semantic Execution");
        System.out.println("===============================================");
        System.out.println("Input file: " + inputFile);
        System.out.println();
        
        try {
            // Parse the JajaCode program
            CharStream input = CharStreams.fromFileName(inputFile);
            JajaCodeLexer lexer = new JajaCodeLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            JajaCodeParser parser = new JajaCodeParser(tokens);
            JajaCodeParser.ProgramContext tree = parser.program();
            
            // Build AST
            JajaCodeAstBuilder builder = new JajaCodeAstBuilder();
            AstNode ast = builder.visit(tree);
            
            System.out.println("✓ Program parsed successfully");
            
            // Create interpreter
            JajaCodeInterpreter interpreter = new JajaCodeInterpreter();
            
            System.out.println("✓ Interpreter created");
            System.out.println();
            
            // Execute the program
            System.out.println("--- Executing Program ---");
            System.out.println();
            if (ast != null) {
                // Execute all instructions in order
                executeProgram(ast, interpreter);
                
                // Print execution steps
                System.out.println();
                System.out.println("--- Execution Steps ---");
                printExecutionSteps(interpreter);
                
                // Print final state
                System.out.println();
                System.out.println("--- Final State ---");
                printFinalState(interpreter);
                
                // Print output
                String output = interpreter.getState().getOutput();
                if (!output.isEmpty()) {
                    System.out.println();
                    System.out.println("--- Program Output ---");
                    System.out.print(output);
                    System.out.println();
                }
            }
            
            System.out.println();
            System.out.println("===============================================");
            System.out.println("Execution finished successfully!");
            System.out.println("===============================================");
            
        } catch (Exception e) {
            System.err.println("Error executing program:");
            e.printStackTrace();
        }
    }

    /**
     * Execute a program by visiting all classes and their instructions
     */
    private static void executeProgram(AstNode ast, JajaCodeInterpreter interpreter) {
        if (ast instanceof ProgramNode programNode) {
            // Get all classes from the program
            List<ClassNode> classes = programNode.getClasses();
            
            // Execute each class
            for (ClassNode classNode : classes) {
                if (classNode != null) {
                    // Get the instruction from the class
                    InstrNode instr = classNode.getInstr();
                    if (instr != null) {
                        // Execute the instruction
                        instr.accept(interpreter);
                        
                        // Check if we should stop
                        if (interpreter.getState().isStopped()) {
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Print all execution steps with their state information
     */
    private static void printExecutionSteps(JajaCodeInterpreter interpreter) {
        List<ExecutionStep> steps = interpreter.getExecutionSteps();
        
        if (steps.isEmpty()) {
            System.out.println("No steps executed");
            return;
        }
        
        for (ExecutionStep step : steps) {
            System.out.println("Step " + step.getStepNumber() + ": " + step.getInstructionExecuted());
            System.out.println("  Address Counter: " + step.getAddressCounter());
            
            // Print stack state
            List<String> stackState = step.getStackState();
            System.out.println("  Stack: [" + String.join(", ", stackState) + "]");
            
            // Print heap state if not empty
            String heapState = step.getHeapState();
            if (heapState != null && !heapState.isEmpty()) {
                System.out.println("  Heap: " + heapState);
            }
            
            // Print output accumulated so far
            String output = step.getOutputSoFar();
            if (output != null && !output.isEmpty()) {
                System.out.println("  Output: " + output.replace("\n", "\\n"));
            }
            
            if (step.isStopped()) {
                System.out.println("  STATUS: STOPPED");
            }
            
            System.out.println();
        }
    }

    /**
     * Print the final execution state
     */
    private static void printFinalState(JajaCodeInterpreter interpreter) {
        System.out.println("Stack: " + String.join(", ", interpreter.getState().getStackStateAsStrings()));
        System.out.println("Heap: " + interpreter.getState().getHeapStateAsString());
        System.out.println("Address Counter: " + interpreter.getState().getAddressCounter());
        System.out.println("Stopped: " + interpreter.getState().isStopped());
    }
}
