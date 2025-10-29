package fr.amvproject.group3.interpreter;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * ExecutionState manages the memory and address counter for JajaCode execution.
 * Implements the semantic interpretation < MEM, ADR > ⊢ JCODE –» < MEM, ADR >
 */
public class ExecutionState {
    /**
     * Represents a stack value with metadata: <w, v, cst, *>
     */
    public static class StackValue {
        private final String name;           // identifier name
        private Object value;                // the actual value
        private final String objectType;     // "var", "cst", "vcst", "tab", "meth", "frame"
        private final String typeStr;        // int, boolean, void
        private int returnAddress;           // for frames: where to return to

        public StackValue(String name, Object value, String objectType, String typeStr) {
            this.name = name;
            this.value = value;
            this.objectType = objectType;
            this.typeStr = typeStr;
            this.returnAddress = -1;
        }

        public String getName() {
            return name;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public String getObjectType() {
            return objectType;
        }

        public String getTypeStr() {
            return typeStr;
        }

        public int getReturnAddress() {
            return returnAddress;
        }

        public void setReturnAddress(int returnAddress) {
            this.returnAddress = returnAddress;
        }

        @Override
        public String toString() {
            return String.format("<%s, %s, %s, %s>", name, value, objectType, typeStr);
        }
    }

    // Memory (stack): represented as a deque of StackValues
    private final Deque<StackValue> memory = new ArrayDeque<>();

    // Address counter: current instruction address
    private int addressCounter = 0;

    // Stop flag: when jcstop is executed
    private boolean stopped = false;

    // Heap management for arrays
    private final MemoryManager heapManager = new MemoryManager();

    // Output management
    private final OutputManager outputManager = new OutputManager();

    /**
     * Initialize memory: <m,a> ⊢ init –» <[], a+1>
     */
    public void init() {
        memory.clear();
        addressCounter++;
    }

    /**
     * Stop execution: <m,a> ⊢ jcstop –» <m, ⊥>
     */
    public void stop() {
        stopped = true;
    }

    public boolean isStopped() {
        return stopped;
    }

    /**
     * Increment address counter: for nop and other instructions
     */
    public void incrementAddress() {
        addressCounter++;
    }

    /**
     * Set address counter to specific value: for goto and if instructions
     */
    public void setAddressCounter(int address) {
        addressCounter = address;
    }

    /**
     * Get current address counter
     */
    public int getAddressCounter() {
        return addressCounter;
    }

    /**
     * Push a value onto the stack: <m,a> ⊢ push(v) –» <<w, v, cst,*>.m, a+1>
     */
    public void push(Object value) {
        memory.push(new StackValue("", value, "cst", "int"));
    }

    /**
     * Push a named stack value: for new, load, etc.
     */
    public void push(String name, Object value, String objectType, String typeStr) {
        memory.push(new StackValue(name, value, objectType, typeStr));
    }

    /**
     * Pop from stack: <<i,v,o,t>.m,a>- pop –» <m, a+1>
     */
    public StackValue pop() {
        if (memory.isEmpty()) {
            throw new RuntimeException("Stack underflow on pop");
        }
        StackValue popped = memory.pop();
        
        // If popped value is an array, remove from heap
        if ("tab".equals(popped.getObjectType()) && popped.getValue() instanceof Integer) {
            heapManager.remove((Integer) popped.getValue());
        }
        
        return popped;
    }

    /**
     * Peek at top of stack without removing
     */
    public StackValue peek() {
        if (memory.isEmpty()) {
            throw new RuntimeException("Stack empty on peek");
        }
        return memory.peek();
    }

    /**
     * Check if stack is empty
     */
    public boolean isEmpty() {
        return memory.isEmpty();
    }

    /**
     * Swap top two stack values: <q1.q2.m,a> ⊢ swap –» <q2.q1.m, a+1>
     */
    public void swap() {
        if (memory.size() < 2) {
            throw new RuntimeException("Cannot swap: stack has less than 2 elements");
        }
        StackValue first = memory.pop();
        StackValue second = memory.pop();
        memory.push(first);
        memory.push(second);
    }

    /**
     * Find and return value of identifier from stack
     */
    public Object getValue(String identName) {
        for (StackValue sv : memory) {
            if (sv.getName().equals(identName)) {
                return sv.getValue();
            }
        }
        throw new RuntimeException("Identifier not found: " + identName);
    }

    /**
     * Find and update value of identifier in stack
     */
    public void setValue(String identName, Object value) {
        for (StackValue sv : memory) {
            if (sv.getName().equals(identName)) {
                sv.setValue(value);
                return;
            }
        }
        throw new RuntimeException("Identifier not found: " + identName);
    }

    /**
     * Get heap manager for array operations
     */
    public MemoryManager getHeapManager() {
        return heapManager;
    }

    /**
     * Append to output buffer
     */
    public void appendOutput(Object value) {
        outputManager.write(value);
    }

    /**
     * Append to output buffer with newline
     */
    public void appendOutputLn(Object value) {
        outputManager.writeln(value);
    }

    /**
     * Get output buffer content
     */
    public String getOutput() {
        return outputManager.getOutput();
    }

    /**
     * Print execution state for debugging
     */
    public void printState() {
        System.out.println("=== Execution State ===");
        System.out.println("Address Counter: " + addressCounter);
        System.out.println("Stopped: " + stopped);
        System.out.println("Stack:");
        if (memory.isEmpty()) {
            System.out.println("  [Empty]");
        } else {
            int i = memory.size();
            for (StackValue sv : memory) {
                System.out.println("  [" + i + "] " + sv);
                i--;
            }
        }
        System.out.println("Heap: " + heapManager.toString());
        String outputStr = getOutput();
        System.out.println("Output: " + (outputStr.length() > 0 ? outputStr : "[Empty]"));
        System.out.println("=======================");
    }

    /**
     * Get current stack state as list of strings for step tracking
     */
    public List<String> getStackStateAsStrings() {
        List<String> stackStrings = new ArrayList<>();
        if (memory.isEmpty()) {
            stackStrings.add("[Empty]");
        } else {
            for (StackValue sv : memory) {
                stackStrings.add(sv.toString());
            }
        }
        return stackStrings;
    }

    /**
     * Get current heap state as string
     */
    public String getHeapStateAsString() {
        return heapManager.toString();
    }
}
