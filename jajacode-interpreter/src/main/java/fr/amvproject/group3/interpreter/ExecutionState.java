package fr.amvproject.group3.interpreter;

import java.util.ArrayList;
import java.util.List;

import fr.amvproject.group3.jjc.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.jjc.ast.nodes.other.TypeNode;
import fr.amvproject.group3.jjc.utils.NodeOperations.MockToken;
import fr.amvproject.group3.nodes.ObjNode;
import fr.amvproject.group3.jjc.Stack;
import fr.amvproject.group3.jjc.Quad;

public class ExecutionState {

    public static class StackValue {
        private final String name;           // identifier name
        private Object value;                // the actual value
        private final ObjNode objectType;     // "var", "cst", "tab"
        private final TypeNode typeStr;        // int, boolean, void
        private int returnAddress;           // for frames: where to return to

        public StackValue(String name, Object value, ObjNode objectType, TypeNode typeStr) {
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

        public ObjNode getObjectType() {
            return objectType;
        }

        public TypeNode getTypeStr() {
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

    private final Stack stack = new Stack();
    private int addressCounter = 1;
    private boolean stopped = false;
    private final OutputManager outputManager = new OutputManager();

    private String inferType(Object value) {
        if (value instanceof Boolean) {
            return "boolean";
        } else if (value instanceof Integer || value instanceof Number) {
            return "int";
        } else {
            return "void";
        }
    }

    private String normalizeTypeName(String typeStr) {
        if (typeStr == null) {
            return "void";
        }
        return switch (typeStr.toLowerCase()) {
            case "entier" -> "int";
            case "booleen" -> "boolean";
            case "vide" -> "void";
            default -> typeStr; // already normalized or unknown
        };
    }

    // <m,a> ⊢ init –» <[], a+1>
    public void init() {
        addressCounter++;
    }

    // <m,a> ⊢ jcstop –» <m, ⊥>
    public void stop() {
        stopped = true;
    }

    public boolean isStopped() {
        return stopped;
    }

    public void incrementAddress() {
        addressCounter++;
    }

    public void setAddressCounter(int address) {
        addressCounter = address;
    }

    public int getAddressCounter() {
        return addressCounter;
    }

    // <m,a> ⊢ push(v) –» <<w, v, cst,*>.m, a+1>
    public void push(Object value) {
        // Push a constant value with empty identifier
        MockToken identToken = new MockToken("", -1, 0);
        IdentNode emptyIdent = new IdentNode(identToken);
        String typeStr = inferType(value);
        MockToken typeToken = new MockToken(typeStr, 0, 0);
        TypeNode type = new TypeNode(typeToken);
        stack.declCst(emptyIdent, value, type);
    }

    public void push(String name, Object value, String objectType, String typeStr) {
        MockToken identToken = new MockToken(name, -1, 0);
        IdentNode ident = new IdentNode(identToken);
        String normalizedType = normalizeTypeName(typeStr);
        MockToken typeToken = new MockToken(normalizedType, 0, 0);
        TypeNode type = new TypeNode(typeToken);

        switch (objectType) {
            case "var":
                stack.declVar(ident, value, type);
                break;
            case "cst":
                stack.declCst(ident, value, type);
                break;
            case "tab":
                if (value instanceof Integer integerValue) {
                    stack.declTab(ident, integerValue , type);
                } else {
                    throw new RuntimeException("Array declaration requires integer size");
                }
                break;
            case "meth":
                Quad methQuad = new Quad(ident, value, new ObjNode("meth"), type);
                stack.push(methQuad);
                break;
            default:
                stack.declCst(ident, value, type);
        }
    }

    // <<i,v,o,t>.m,a>- pop –» <m, a+1>
    public StackValue pop() {
        Quad quad = stack.pop();

        String name = quad.getIdent() != null ? quad.getIdent().getName() : "";
        String typeStr = quad.getType() != null ? quad.getType().getName() : "void";

        MockToken typeToken = new MockToken(typeStr, 0, 0);
        return new StackValue(name, quad.getValue(), quad.getObj(), new TypeNode(typeToken));
    }

    public StackValue peek() {
        Quad quad = stack.pop();

        String name = quad.getIdent() != null ? quad.getIdent().getName() : "";
        String typeStr = quad.getType() != null ? quad.getType().getName() : "void";

        pushQuadBack(quad);

        MockToken typeToken = new MockToken(typeStr, 0, 0);
        return new StackValue(name, quad.getValue(), quad.getObj(), new TypeNode(typeToken));
    }

    public boolean isEmpty() {
        try {
            Quad quad = stack.pop();
            pushQuadBack(quad);
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    // <q1.q2.m,a> ⊢ swap –» <q2.q1.m, a+1>
    public void swap() {
        stack.swap();
    }

    public Object getValue(String identName) {
        MockToken identToken = new MockToken(identName, -1, 0);
        IdentNode ident = new IdentNode(identToken);

        if (isEmpty()) {
            throw new RuntimeException("Stack is empty, cannot find identifier: " + identName);
        }

        Quad topQuad = stack.pop();
        Object value = stack.val(ident);
        pushQuadBack(topQuad);

        if (value == null) {
            throw new RuntimeException("Identifier not found: " + identName);
        }
        return value;
    }

    public void setValue(String identName, Object value) {
        if (isEmpty()) {
            throw new RuntimeException("Stack is empty, cannot set identifier: " + identName);
        }

        java.util.List<Quad> queue = stack.getQueue();
        boolean found = false;

        for (Quad quad : queue) {
            if (quad.getIdent() != null && identName.equals(quad.getIdent().getName())) {
                // quad.getValue() = value; TODO: fix this
                found = true;
                break;
            }
        }

        if (!found) {
            throw new RuntimeException("Variable not found on stack: " + identName);
        }
    }

    private void pushQuadBack(Quad quad) {
        if (quad == null) return;
        switch (quad.getObj().getObj()) {
            case VAR:
                stack.declVar(quad.getIdent(), quad.getValue(), quad.getType());
                break;
            case TAB:
                if (quad.getValue() instanceof Integer integerQuadValue) {
                    stack.declTab(quad.getIdent(), integerQuadValue, quad.getType());
                }
                break;
            default:
                stack.declCst(quad.getIdent(), quad.getValue(), quad.getType());
        }
    }

    public Stack getStack() {
        return stack;
    }

    public Object getArrayValue(String identName, int index) {
        MockToken identToken = new MockToken(identName, -1, 0);
        IdentNode ident = new IdentNode(identToken);
        Quad quad = stack.pop();
        Object result = null;
        if (quad != null && "tab".equals(quad.getObj())) {
            result = stack.valT(ident, index);
        }
        pushQuadBack(quad);
        return result;
    }

    public void setArrayValue(String identName, int index, Object value) {
        MockToken identToken = new MockToken(identName, -1, 0);
        IdentNode ident = new IdentNode(identToken);
        Quad quad = stack.pop();
        stack.affecterValT(ident, index, value);
        pushQuadBack(quad);
    }

    public int getArrayLength(String identName) {
        MockToken identToken = new MockToken(identName, -1, 0);
        IdentNode ident = new IdentNode(identToken);
        Quad quad = stack.pop();
        Integer length = stack.longT(ident);
        pushQuadBack(quad);
        return length != null ? length : 0;
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

    public void printState() {
        System.out.println("=== Execution State ===");
        System.out.println("Address Counter: " + addressCounter);
        System.out.println("Stopped: " + stopped);
        System.out.println("Stack:");
        stack.print();
        String outputStr = getOutput();
        System.out.println("Output: " + (outputStr.length() > 0 ? outputStr : "[Empty]"));
        System.out.println("=======================");
    }

    public List<String> getStackStateAsStrings() {
        List<String> stackStrings = new ArrayList<>();

        java.util.List<Quad> queue = stack.getQueue();

        if (queue.isEmpty()) {
            return stackStrings;
        }

        for (Quad quad : queue) {
            String identName = quad.getIdent() != null ? quad.getIdent().getName() : "";
            String valueStr = quad.getValue() != null ? quad.getValue().toString() : "null";
            String obj = quad.getObj() != null ? quad.getObj().getName() : "unknown";
            String typeStr = quad.getType() != null ? quad.getType().getName() : "void";

            stackStrings.add(String.format("<%s, %s, %s, %s>", identName, valueStr, obj, typeStr));
        }

        return stackStrings;
    }

    public String getHeapStateAsString() {
        java.util.List<Object> heapQueue = stack.getHeap().getQueue();
        int nextAddress = stack.getHeap().getNextAddress();

        if (nextAddress == 0 || heapQueue.isEmpty()) {
            return "[No allocations]";
        }

        StringBuilder sb = new StringBuilder();
        int address = 0;
        boolean firstBlock = true;

        while (address < nextAddress) {
            if (heapQueue.get(address) instanceof Integer size && size > 0) {
                if (!firstBlock) {
                    sb.append("\n  ");
                }
                firstBlock = false;

                String type = "unknown";
                List<Object> elements = new ArrayList<>();

                for (int i = 1; i <= size && (address + i) < heapQueue.size(); i++) {
                    Object element = heapQueue.get(address + i);
                    elements.add(element);

                    if (i == 1) {
                        if (element instanceof Integer) {
                            type = "entier";
                        } else if (element instanceof Boolean) {
                            type = "booleen";
                        }
                    }
                }

                sb.append(String.format("Array@%d [size=%d, type=%s]: %s",
                    address, size, type, elements));
                address += size + 1;
            } else {
                address++;
            }
        }

        return sb.length() > 0 ? sb.toString() : "[No allocations]";
    }
}
