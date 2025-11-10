package fr.amvproject.group3;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Before;
import org.junit.Test;

import fr.amvproject.group3.jjc.ast.AstNode;
import fr.amvproject.group3.jjc.ast.JajaCodeAstBuilder;
import fr.amvproject.group3.jjc.ast.nodes.intermediate.InstrNode;
import fr.amvproject.group3.jjc.ast.nodes.program.ClassNode;
import fr.amvproject.group3.jjc.ast.nodes.program.ProgramNode;
import fr.amvproject.group3.interpreter.ExecutionStep;
import fr.amvproject.group3.interpreter.JajaCodeInterpreter;

public class JajaCodeInterpreterTest {

    private JajaCodeInterpreter interpreter;

    @Before
    public void setUp() {
        interpreter = new JajaCodeInterpreter();
    }

    private ProgramNode parseFile(String filename) throws Exception {
        CharStream input = CharStreams.fromFileName(filename);
        JajaCodeLexer lexer = new JajaCodeLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JajaCodeParser parser = new JajaCodeParser(tokens);
        JajaCodeParser.ProgramContext tree = parser.program();

        JajaCodeAstBuilder builder = new JajaCodeAstBuilder();
        AstNode ast = builder.visit(tree);

        assertNotNull("AST should not be null", ast);
        assertTrue("AST should be a ProgramNode", ast instanceof ProgramNode);

        return (ProgramNode) ast;
    }

    private void executeProgram(ProgramNode programNode, JajaCodeInterpreter interpreter) {
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
                break;
            }

            instruction.accept(interpreter);
            iterations++;
        }

        assertTrue("Program should stop", interpreter.getState().isStopped());
        assertTrue("Should not exceed max iterations", iterations < maxIterations);
    }

    @Test
    public void testSimpleArithmetic() throws Exception {
        ProgramNode program = parseFile("src/test/resources/demo.jjc");
        executeProgram(program, interpreter);

        String output = interpreter.getState().getOutput();
        assertEquals("Output should be 25", "25\n", output);

        List<ExecutionStep> steps = interpreter.getExecutionSteps();
        assertTrue("Should have execution steps", steps.size() > 0);
    }

    @Test
    public void testFunctionCallWithParameters() throws Exception {
        ProgramNode program = parseFile("src/test/resources/demo_full.jjc");
        executeProgram(program, interpreter);

        String output = interpreter.getState().getOutput();
        assertEquals("Output should be four lines with 6", "6\n6\n6\n6\n", output);

        assertTrue("Program should have stopped", interpreter.getState().isStopped());
        assertTrue("Stack should be empty at end", interpreter.getState().getStackStateAsStrings().isEmpty());
    }

    @Test
    public void testInitInstruction() throws Exception {
        ProgramNode program = parseFile("src/test/resources/demo.jjc");
        executeProgram(program, interpreter);

        List<ExecutionStep> steps = interpreter.getExecutionSteps();
        ExecutionStep firstStep = steps.get(0);

        assertEquals("First instruction should be init", "init", firstStep.getInstructionExecuted());
        assertEquals("Address counter should increment to 2", 2, firstStep.getAddressCounter());
    }

    @Test
    public void testStackOperations() throws Exception {
        ProgramNode program = parseFile("src/test/resources/demo.jjc");
        executeProgram(program, interpreter);

        List<ExecutionStep> steps = interpreter.getExecutionSteps();

        ExecutionStep pushStep1 = steps.get(1);
        assertEquals("Should push 10", "push(10)", pushStep1.getInstructionExecuted());
        assertEquals("Stack should have 1 element", 1, pushStep1.getStackState().size());

        ExecutionStep pushStep2 = steps.get(2);
        assertEquals("Should push 20", "push(20)", pushStep2.getInstructionExecuted());
        assertEquals("Stack should have 2 elements", 2, pushStep2.getStackState().size());
    }

    @Test
    public void testBinaryOperations() throws Exception {
        ProgramNode program = parseFile("src/test/resources/demo.jjc");
        executeProgram(program, interpreter);

        List<ExecutionStep> steps = interpreter.getExecutionSteps();

        boolean foundAdd = false;
        boolean foundSub = false;

        for (ExecutionStep step : steps) {
            if (step.getInstructionExecuted().equals("binaryOp(ADD)")) {
                foundAdd = true;
            }
            if (step.getInstructionExecuted().equals("binaryOp(SUB)")) {
                foundSub = true;
            }
        }

        assertTrue("Should have ADD operation", foundAdd);
        assertTrue("Should have SUB operation", foundSub);
    }

    @Test
    public void testArrayOperations() throws Exception {
        ProgramNode program = parseFile("src/test/resources/demo_full.jjc");
        executeProgram(program, interpreter);

        List<ExecutionStep> steps = interpreter.getExecutionSteps();

        boolean foundNewArray = false;
        boolean foundArrayStore = false;

        for (ExecutionStep step : steps) {
            if (step.getInstructionExecuted().startsWith("newarray")) {
                foundNewArray = true;
                assertFalse("Heap should not be empty after array allocation",
                    step.getHeapState().equals("[No allocations]"));
            }
            if (step.getInstructionExecuted().startsWith("astore")) {
                foundArrayStore = true;
                assertTrue("Heap should contain array data",
                    step.getHeapState().contains("Array@"));
            }
        }

        assertTrue("Should have array allocation", foundNewArray);
        assertTrue("Should have array store", foundArrayStore);
    }

    @Test
    public void testMethodInvocation() throws Exception {
        ProgramNode program = parseFile("src/test/resources/demo_full.jjc");
        executeProgram(program, interpreter);

        List<ExecutionStep> steps = interpreter.getExecutionSteps();

        boolean foundInvoke = false;
        boolean foundReturn = false;

        for (ExecutionStep step : steps) {
            if (step.getInstructionExecuted().startsWith("invoke")) {
                foundInvoke = true;
            }
            if (step.getInstructionExecuted().equals("return")) {
                foundReturn = true;
            }
        }

        assertTrue("Should have method invocation", foundInvoke);
        assertTrue("Should have return", foundReturn);
    }

    @Test
    public void testVariableOperations() throws Exception {
        ProgramNode program = parseFile("src/test/resources/demo_full.jjc");
        executeProgram(program, interpreter);

        List<ExecutionStep> steps = interpreter.getExecutionSteps();

        boolean foundNew = false;
        boolean foundLoad = false;
        boolean foundStore = false;
        boolean foundInc = false;

        for (ExecutionStep step : steps) {
            String instr = step.getInstructionExecuted();
            if (instr.startsWith("new(")) {
                foundNew = true;
            }
            if (instr.startsWith("load(")) {
                foundLoad = true;
            }
            if (instr.startsWith("store(")) {
                foundStore = true;
            }
            if (instr.startsWith("inc(")) {
                foundInc = true;
            }
        }

        assertTrue("Should have variable declaration", foundNew);
        assertTrue("Should have variable load", foundLoad);
        assertTrue("Should have variable store", foundStore);
        assertTrue("Should have increment", foundInc);
    }

    @Test
    public void testControlFlow() throws Exception {
        ProgramNode program = parseFile("src/test/resources/demo_full.jjc");
        executeProgram(program, interpreter);

        List<ExecutionStep> steps = interpreter.getExecutionSteps();

        boolean foundGoto = false;
        boolean foundIf = false;

        for (ExecutionStep step : steps) {
            if (step.getInstructionExecuted().startsWith("goto")) {
                foundGoto = true;
            }
            if (step.getInstructionExecuted().startsWith("if(")) {
                foundIf = true;
            }
        }

        assertTrue("Should have goto", foundGoto);
        assertTrue("Should have if", foundIf);
    }

    @Test
    public void testWriteOperations() throws Exception {
        ProgramNode program = parseFile("src/test/resources/demo.jjc");
        executeProgram(program, interpreter);

        String output = interpreter.getState().getOutput();
        assertFalse("Output should not be empty", output.isEmpty());
        assertTrue("Output should contain newline", output.contains("\n"));
    }

    @Test
    public void testExecutionStepCount() throws Exception {
        ProgramNode program = parseFile("src/test/resources/demo.jjc");
        executeProgram(program, interpreter);

        int stepCount = interpreter.getStepCount();
        List<ExecutionStep> steps = interpreter.getExecutionSteps();

        assertEquals("Step count should match execution steps", stepCount, steps.size());
        assertTrue("Should have multiple steps", stepCount > 5);
    }

    @Test
    public void testFinalStateEmpty() throws Exception {
        ProgramNode program = parseFile("src/test/resources/demo.jjc");
        executeProgram(program, interpreter);

        assertTrue("Program should be stopped",
            interpreter.getState().isStopped());
        assertFalse("Should have execution steps",
            interpreter.getExecutionSteps().isEmpty());
    }

    @Test
    public void testHeapAllocation() throws Exception {
        ProgramNode program = parseFile("src/test/resources/demo_full.jjc");
        executeProgram(program, interpreter);

        String heapState = interpreter.getState().getHeapStateAsString();
        assertTrue("Heap should have allocations", heapState.contains("Array@"));
        assertTrue("Heap should show array size", heapState.contains("size=4"));
        assertTrue("Heap should show array type", heapState.contains("type=entier"));
    }

    @Test
    public void testAddressCounterProgression() throws Exception {
        ProgramNode program = parseFile("src/test/resources/demo.jjc");
        executeProgram(program, interpreter);

        List<ExecutionStep> steps = interpreter.getExecutionSteps();

        for (ExecutionStep step : steps) {
            int currentAddress = step.getAddressCounter();
            assertTrue("Address counter should progress", currentAddress >= 0);
        }
    }
}
