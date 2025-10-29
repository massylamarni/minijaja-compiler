package fr.amvproject.group3.interpreter;

import java.util.ArrayList;
import java.util.List;

import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.finals.AddressNode;
import fr.amvproject.group3.ast.nodes.finals.BoolLiteralNode;
import fr.amvproject.group3.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.ast.nodes.finals.IntLiteralNode;
import fr.amvproject.group3.ast.nodes.finals.StringLiteralNode;
import fr.amvproject.group3.ast.nodes.instrs.GotoNode;
import fr.amvproject.group3.ast.nodes.instrs.IfNode;
import fr.amvproject.group3.ast.nodes.instrs.IncNode;
import fr.amvproject.group3.ast.nodes.instrs.InvokeNode;
import fr.amvproject.group3.ast.nodes.instrs.LengthNode;
import fr.amvproject.group3.ast.nodes.instrs.LoadNode;
import fr.amvproject.group3.ast.nodes.instrs.NewArrayNode;
import fr.amvproject.group3.ast.nodes.instrs.NewNode;
import fr.amvproject.group3.ast.nodes.instrs.PushNode;
import fr.amvproject.group3.ast.nodes.instrs.StoreNode;
import fr.amvproject.group3.ast.nodes.literals.InitLiteral;
import fr.amvproject.group3.ast.nodes.literals.JcstopLiteral;
import fr.amvproject.group3.ast.nodes.literals.NopLiteral;
import fr.amvproject.group3.ast.nodes.literals.PopLiteral;
import fr.amvproject.group3.ast.nodes.literals.ReturnLiteral;
import fr.amvproject.group3.ast.nodes.literals.SwapLiteral;
import fr.amvproject.group3.ast.nodes.literals.WriteLiteral;
import fr.amvproject.group3.ast.nodes.other.BinaryOpNode;
import fr.amvproject.group3.ast.nodes.other.ObjNode;
import fr.amvproject.group3.ast.nodes.other.TypeNode;
import fr.amvproject.group3.ast.nodes.other.UnaryOpNode;
import fr.amvproject.group3.ast.nodes.program.ClassNode;
import fr.amvproject.group3.ast.nodes.program.ProgramNode;

/**
 * JajaCodeInterpreter implements the semantic interpretation rules for JajaCode.
 * It uses the visitor pattern to execute each instruction according to the rules.
 */
public class JajaCodeInterpreter implements AstVisitor<Void> {

    private final ExecutionState state;
    private final List<ExecutionStep> executionSteps = new ArrayList<>();
    private int stepCounter = 0;

    public JajaCodeInterpreter() {
        this.state = new ExecutionState();
    }

    /**
     * Records the current execution state as an ExecutionStep
     */
    private void recordStep(String instructionName) {
        ExecutionStep step = new ExecutionStep(
            stepCounter++,
            state.getAddressCounter(),
            instructionName,
            state.getStackStateAsStrings(),
            state.getHeapStateAsString(),
            state.getOutput(),
            state.isStopped(),
            null
        );
        executionSteps.add(step);
    }

    /**
     * Gets all recorded execution steps
     */
    public List<ExecutionStep> getExecutionSteps() {
        return new ArrayList<>(executionSteps);
    }

    /**
     * Gets the most recent execution step
     */
    public ExecutionStep getLastExecutionStep() {
        if (executionSteps.isEmpty()) {
            return null;
        }
        return executionSteps.get(executionSteps.size() - 1);
    }

    /**
     * Gets total number of steps executed
     */
    public int getStepCount() {
        return stepCounter;
    }

    // ==================== Program Execution ====================

    @Override
    public Void visitProgram(ProgramNode node) {
        if (node == null) return null;
        
        // A program consists of a list of classes/instructions
        System.out.println("Starting JajaCode program execution...");
        
        // Initialize memory
        state.init();
        
        // Execute the program
        // The actual execution would be done by a separate execution engine
        // that processes instructions in order
        
        return null;
    }

    @Override
    public Void visitClass(ClassNode node) {
        if (node == null) return null;
        
        // Execute instructions in class
        if (node.getInstr() != null) {
            node.getInstr().accept(this);
        }
        
        return null;
    }

    // ==================== Literal Instructions ====================

    /**
     * [init] : <m,a> ⊢ init –» <[], a+1>
     */
    @Override
    public Void visitInit(InitLiteral literal) {
        state.init();
        state.incrementAddress();
        recordStep("init");
        return null;
    }

    /**
     * [jcstop] : <m,a> ⊢ jcstop –» <m, ⊥>
     */
    @Override
    public Void visitJcstop(JcstopLiteral literal) {
        state.stop();
        recordStep("jcstop");
        return null;
    }

    /**
     * [nop] : <m,a> ⊢ nop –» <m, a+1>
     */
    @Override
    public Void visitNop(NopLiteral literal) {
        state.incrementAddress();
        recordStep("nop");
        return null;
    }

    /**
     * [swap] : <q1.q2.m,a> ⊢ swap –» <q2.q1.m, a+1>
     */
    @Override
    public Void visitSwap(SwapLiteral literal) {
        state.swap();
        state.incrementAddress();
        recordStep("swap");
        return null;
    }

    /**
     * [pop] : <<i,v,o,t>.m,a>- pop –» <m, a+1>
     */
    @Override
    public Void visitPop(PopLiteral literal) {
        state.pop();
        state.incrementAddress();
        recordStep("pop");
        return null;
    }

    /**
     * [return] : <<w ,a1,cst,*>.m,a> ⊢ return –» <m, a1>
     */
    @Override
    public Void visitReturn(ReturnLiteral literal) {
        // Pop the frame marker which contains the return address
        ExecutionState.StackValue frame = state.pop();
        if ("frame".equals(frame.getObjectType())) {
            state.setAddressCounter(frame.getReturnAddress());
        }
        recordStep("return");
        return null;
    }

    /**
     * [write] : <<w, v, cst,*>.m,a> ⊢ write –» <Afficher(v,m),a+1>
     */
    @Override
    public Void visitWrite(WriteLiteral literal) {
        if (!state.isEmpty()) {
            ExecutionState.StackValue top = state.peek();
            if (literal.isLn()) {
                state.appendOutputLn(top.getValue());
            } else {
                state.appendOutput(top.getValue());
            }
        }
        state.incrementAddress();
        recordStep("write" + (literal.isLn() ? "ln" : ""));
        return null;
    }

    // ==================== Stack Operations ====================

    /**
     * [push]: <m,a> ⊢ push(v) –» <<w, v, cst,*>.m, a+1>
     */
    @Override
    public Void visitPush(PushNode node) {
        if (node != null && node.getValue() != null) {
            Object value = extractValueFromNode(node.getValue());
            if (value == null) {
                throw new RuntimeException("Push instruction: value cannot be null");
            }
            state.push(value);
            state.incrementAddress();
            recordStep("push(" + value + ")");
        } else {
            throw new RuntimeException("Push instruction: missing value");
        }
        return null;
    }

    // ==================== Variable/Constant/Array/Method Operations ====================

    /**
     * [newV] : <m,a> ⊢ new(i,t,var,s) –» <IdentVal(i,t,m,s),a+1>
     * [newC] : <<w,v, cst,*>.m,a> ⊢ new(i,t,cst,0) –» <DeclCst(i,v,t,m),a+1>
     * [newM] : <<w, v, cst,*>.m,a> ⊢ new(i,t,meth,0) –» <DeclMeth(i, v, t, m),a+1>
     */
    @Override
    public Void visitNew(NewNode node) {
        if (node == null) return null;

        String identName = node.getIdent().getName();
        String objType = node.getObj().getName();  // "var" or "meth"
        String typeStr = node.getType().getName();  // "entier" or "boolean"

        Object value = null;
        if (node.getValue() != null) {
            value = extractValueFromNode(node.getValue());
        }

        // Push the new variable onto the stack
        state.push(identName, value, objType, typeStr);
        state.incrementAddress();
        recordStep("new(" + identName + "," + typeStr + "," + objType + ")");

        return null;
    }

    /**
     * [newarray]: <<w, v, cst,*>.m,a> ⊢ newarray(i, t) –» <DeclTab(i, v, t, m), a+1>
     */
    @Override
    public Void visitNewArray(NewArrayNode node) {
        if (node == null) return null;

        String identName = node.getIdent().getName();
        String typeStr = node.getType().getName();

        // Get array size from top of stack (should be pushed before)
        ExecutionState.StackValue sizeValue = state.pop();
        int size = ((Number) sizeValue.getValue()).intValue();

        // Allocate array in heap and get reference
        int heapRef = state.getHeapManager().allocate(size, typeStr);

        // Push array reference onto stack
        state.push(identName, heapRef, "tab", typeStr);
        state.incrementAddress();
        recordStep("newarray(" + identName + "," + typeStr + "[" + size + "])");

        return null;
    }

    /**
     * [invoke]: <m,a> ⊢ invoke(i) –» <<w, a+1,cst,*>.m, Val(i, m)>
     */
    @Override
    public Void visitInvoke(InvokeNode node) {
        if (node == null) return null;

        String methodName = node.getIdent().getName();
        int returnAddress = state.getAddressCounter() + 1;

        // Push frame marker containing return address
        state.push("__frame__", returnAddress, "frame", "void");

        // Find method and jump to it
        // In a real implementation, this would look up the method in the program
        // and set the address counter accordingly
        // For now, we'll just set a placeholder
        System.out.println("Invoking method: " + methodName);
        recordStep("invoke(" + methodName + ")");

        return null;
    }

    /**
     * [length] : <m,a> ⊢ length(i) –» <<w,long(I,m),cst,*>, a+1>
     */
    @Override
    public Void visitLength(LengthNode node) {
        if (node == null) return null;
        
        // Get the array reference from stack
        ExecutionState.StackValue arrayRef = state.pop();
        int ref = ((Number) arrayRef.getValue()).intValue();

        // Get array size from heap
        int size = state.getHeapManager().getSize(ref);

        // Push size onto stack
        state.push(size);
        state.incrementAddress();
        recordStep("length");

        return null;
    }

    // ==================== Load/Store Operations ====================

    /**
     * [load]: <m,a> ⊢ load(i) –» <<w, Val(i,m), cst,*>.m, a+1>
     */
    @Override
    public Void visitLoad(LoadNode node) {
        if (node == null) return null;

        String identName = node.getIdent().getName();
        
        if (node.isA()) {
            // [aload]: <<w, ind, cst,*>.m,a> ⊢ aload(i) –»<<w, ValT(i, ind, m), cst,*>.m, a+1>
            ExecutionState.StackValue index = state.pop();
            int idx = ((Number) index.getValue()).intValue();
            
            Object arrayValue = state.getValue(identName);
            int ref = ((Number) arrayValue).intValue();
            
            Object elementValue = state.getHeapManager().get(ref, idx);
            state.push(elementValue);
            recordStep("aload(" + identName + "[" + idx + "])");
        } else {
            // Regular load
            Object value = state.getValue(identName);
            state.push(value);
            recordStep("load(" + identName + ")");
        }
        
        state.incrementAddress();
        return null;
    }

    /**
     * [store]: <<w, v, cst,*>.m,a> ⊢ store(i) –» <AffecterVal(i,v,m), a+1>
     */
    @Override
    public Void visitStore(StoreNode node) {
        if (node == null) return null;

        String identName = node.getIdent().getName();
        ExecutionState.StackValue value = state.pop();

        if (node.isA()) {
            // [astore]: <<w, v, cst,*>.<w, ind, cst,*>.m,a> ⊢ astore(i) –» <AffecterValT(i, ind, v,m), a+1>
            ExecutionState.StackValue indexValue = state.pop();
            int idx = ((Number) indexValue.getValue()).intValue();
            
            Object arrayValue = state.getValue(identName);
            int ref = ((Number) arrayValue).intValue();
            
            state.getHeapManager().set(ref, idx, value.getValue());
            recordStep("astore(" + identName + "[" + idx + "]=" + value.getValue() + ")");
        } else {
            // Regular store
            state.setValue(identName, value.getValue());
            recordStep("store(" + identName + "=" + value.getValue() + ")");
        }
        
        state.incrementAddress();
        return null;
    }

    // ==================== Jump Operations ====================

    /**
     * [goto]: <m,a> ⊢ goto(a1) –» <m, a1>
     */
    @Override
    public Void visitGoto(GotoNode node) {
        if (node == null) return null;

        int address = node.getAddress().getValue();
        state.setAddressCounter(address);
        recordStep("goto(" + address + ")");

        return null;
    }

    /**
     * [iftrue]: <<w,true, cst, *>.m,a> ⊢ if(a1) –» <m,a1>
     * [iffalse]: <<w,false, cst, *>.m,a> ⊢ if(a1) –» <m,a+1>
     */
    @Override
    public Void visitIf(IfNode node) {
        if (node == null) return null;

        ExecutionState.StackValue condition = state.pop();
        boolean cond = ((Boolean) condition.getValue());

        if (cond) {
            int address = node.getAddress().getValue();
            state.setAddressCounter(address);
            recordStep("if(true," + address + ")");
        } else {
            state.incrementAddress();
            recordStep("if(false)");
        }

        return null;
    }

    // ==================== Increment Operations ====================

    /**
     * [inc]: <<w,v, cst,*>.m,a> ⊢ inc(i) –» <AffecterVal(i,Val(i,m)+v,m), a+1>
     */
    @Override
    public Void visitInc(IncNode node) {
        if (node == null) return null;

        String identName = node.getIdent().getName();
        ExecutionState.StackValue incrementValue = state.pop();

        Object currentValue = state.getValue(identName);
        
        Number result;
        if (currentValue instanceof Integer && incrementValue.getValue() instanceof Integer) {
            result = (Integer) currentValue + ((Integer) incrementValue.getValue());
        } else {
            result = ((Number) currentValue).doubleValue() + ((Number) incrementValue.getValue()).doubleValue();
        }

        state.setValue(identName, result);
        state.incrementAddress();
        recordStep("inc(" + identName + "=" + result + ")");

        return null;
    }

    // ==================== Literal Values ====================

    @Override
    public Void visitIntLiteral(IntLiteralNode node) {
        // This is handled in visitPush and other operations that need the actual value
        return null;
    }

    @Override
    public Void visitBoolLiteral(BoolLiteralNode node) {
        return null;
    }

    @Override
    public Void visitStringLiteral(StringLiteralNode node) {
        return null;
    }

    @Override
    public Void visitIdent(IdentNode node) {
        return null;
    }

    @Override
    public Void visitAddress(AddressNode node) {
        return null;
    }

    // ==================== Other Operations ====================

    @Override
    public Void visitObj(ObjNode node) {
        return null;
    }

    @Override
    public Void visitType(TypeNode node) {
        return null;
    }

    /**
     * [op1]: <<w, v1,cst,*>.m,a> ⊢ oper1 –» <<w, oper1 v1, cst,*>.m, a+1>
     */
    @Override
    public Void visitUnaryOp(UnaryOpNode node) {
        if (node == null) return null;

        ExecutionState.StackValue operand = state.pop();
        UnaryOpNode.UnaryOpName operator = node.getOp();

        Object result = applyUnaryOp(operator, operand.getValue());

        state.push(result);
        state.incrementAddress();
        recordStep("unaryOp(" + operator + ")");

        return null;
    }

    /**
     * [op2]: <<w, v2,cst,*>.<w, v1,cst,*>.m,a>⊢oper2 –» <<w, v1 oper2 v2, cst,*>.m, a+1>
     */
    @Override
    public Void visitBinaryOp(BinaryOpNode node) {
        if (node == null) return null;

        // Pop in reverse order (LIFO)
        ExecutionState.StackValue v2 = state.pop();
        ExecutionState.StackValue v1 = state.pop();

        if (v1 == null || v2 == null) {
            throw new RuntimeException("Binary operation: insufficient values on stack");
        }

        BinaryOpNode.BinaryOpName operator = node.getOp();
        Object result = applyBinaryOp(operator, v1.getValue(), v2.getValue());

        state.push(result);
        state.incrementAddress();
        recordStep("binaryOp(" + operator + ")");

        return null;
    }

    /**
     * Extract actual value from nodes
     */
    public Object extractValueFromNode(Object node) {
        if (node instanceof IntLiteralNode intNode) {
            return intNode.getValue();
        } else if (node instanceof BoolLiteralNode boolNode) {
            return boolNode.getValue();
        } else if (node instanceof StringLiteralNode strNode) {
            return strNode.getValue();
        }
        return node;
    }

    /**
     * Apply unary operator
     */
    private Object applyUnaryOp(UnaryOpNode.UnaryOpName operator, Object operand) {
        if (operator == UnaryOpNode.UnaryOpName.NOT) {
            if (operand instanceof Boolean) {
                return !(Boolean) operand;
            } else {
                throw new RuntimeException("NOT operator requires boolean operand, got: " + operand.getClass().getSimpleName());
            }
        } else if (operator == UnaryOpNode.UnaryOpName.NEG) {
            if (operand instanceof Number) {
                return -((Number) operand).doubleValue();
            } else {
                throw new RuntimeException("NEG operator requires numeric operand, got: " + operand.getClass().getSimpleName());
            }
        }
        throw new RuntimeException("Unknown unary operator: " + operator);
    }

    /**
     * Apply binary operator
     */
    private Object applyBinaryOp(BinaryOpNode.BinaryOpName operator, Object left, Object right) {
        if (left == null || right == null) {
            throw new RuntimeException("Binary operation: null operand(s). left=" + left + ", right=" + right + ", operator=" + operator);
        }
        
        if (left instanceof Integer && right instanceof Integer) {
            int l = (Integer) left;
            int r = (Integer) right;
            
            if (operator == BinaryOpNode.BinaryOpName.ADD) return l + r;
            if (operator == BinaryOpNode.BinaryOpName.SUB) return l - r;
            if (operator == BinaryOpNode.BinaryOpName.MUL) return l * r;
            if (operator == BinaryOpNode.BinaryOpName.DIV) {
                if (r == 0) throw new RuntimeException("Division by zero");
                return l / r;
            }
            if (operator == BinaryOpNode.BinaryOpName.CMP) {
                if (r == 0) throw new RuntimeException("Modulo by zero");
                return l % r;
            }
            if (operator == BinaryOpNode.BinaryOpName.SUP) return l > r;
            if (operator == BinaryOpNode.BinaryOpName.AND) return l == 1 && r == 1;
            if (operator == BinaryOpNode.BinaryOpName.OR) return l == 1 || r == 1;
            
            throw new RuntimeException("Unknown binary operator for int: " + operator);
        } else if (left instanceof Boolean && right instanceof Boolean) {
            boolean l = (Boolean) left;
            boolean r = (Boolean) right;
            if (operator == BinaryOpNode.BinaryOpName.AND) {
                return l && r;
            } else if (operator == BinaryOpNode.BinaryOpName.OR) {
                return l || r;
            }
            throw new RuntimeException("Unknown binary operator for boolean: " + operator);
        } else {
            // Numeric operations
            try {
                double l = ((Number) left).doubleValue();
                double r = ((Number) right).doubleValue();
                
                if (operator == BinaryOpNode.BinaryOpName.ADD) return l + r;
                if (operator == BinaryOpNode.BinaryOpName.SUB) return l - r;
                if (operator == BinaryOpNode.BinaryOpName.MUL) return l * r;
                if (operator == BinaryOpNode.BinaryOpName.DIV) {
                    if (r == 0) throw new RuntimeException("Division by zero");
                    return l / r;
                }
                if (operator == BinaryOpNode.BinaryOpName.CMP) {
                    if (r == 0) throw new RuntimeException("Modulo by zero");
                    return l % r;
                }
                if (operator == BinaryOpNode.BinaryOpName.SUP) return l > r;
                
                throw new RuntimeException("Unknown binary operator: " + operator);
            } catch (ClassCastException e) {
                throw new RuntimeException("Type mismatch in binary operation: left=" + left.getClass().getSimpleName() + 
                        ", right=" + right.getClass().getSimpleName() + ", operator=" + operator, e);
            }
        }
    }

    /**
     * Get execution state for testing
     */
    public ExecutionState getState() {
        return state;
    }
}
