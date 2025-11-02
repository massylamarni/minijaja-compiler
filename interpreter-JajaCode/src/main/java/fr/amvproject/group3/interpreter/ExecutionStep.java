package fr.amvproject.group3.interpreter;

import java.util.ArrayList;
import java.util.List;

public class ExecutionStep {
    private final int stepNumber;
    private final int addressCounter;
    private final String instructionExecuted;
    private final List<String> stackState;
    private final String heapState;
    private final String outputSoFar;
    private final boolean stopped;
    private final String error;

    public ExecutionStep(int stepNumber, int addressCounter, String instructionExecuted,
                         List<String> stackState, String heapState, String outputSoFar,
                         boolean stopped, String error) {
        this.stepNumber = stepNumber;
        this.addressCounter = addressCounter;
        this.instructionExecuted = instructionExecuted;
        this.stackState = new ArrayList<>(stackState);
        this.heapState = heapState;
        this.outputSoFar = outputSoFar;
        this.stopped = stopped;
        this.error = error;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public int getAddressCounter() {
        return addressCounter;
    }

    public String getInstructionExecuted() {
        return instructionExecuted;
    }

    public List<String> getStackState() {
        return new ArrayList<>(stackState);
    }

    public String getHeapState() {
        return heapState;
    }

    public String getOutputSoFar() {
        return outputSoFar;
    }

    public boolean isStopped() {
        return stopped;
    }

    public String getError() {
        return error;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Step ").append(stepNumber).append(" ===\n");
        sb.append("Instruction: ").append(instructionExecuted).append("\n");
        sb.append("Address Counter: ").append(addressCounter).append("\n");
        sb.append("Stack: ").append(stackState).append("\n");
        if (!heapState.isEmpty()) {
            sb.append("Heap: ").append(heapState).append("\n");
        }
        if (!outputSoFar.isEmpty()) {
            sb.append("Output: ").append(outputSoFar).append("\n");
        }
        if (error != null) {
            sb.append("Error: ").append(error).append("\n");
        }
        sb.append("\n");
        return sb.toString();
    }
}
