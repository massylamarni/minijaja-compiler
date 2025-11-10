package fr.amvproject.group3.interpreter;

import java.util.ArrayList;
import java.util.List;

import fr.amvproject.group3.jjc.ast.AstVisitor;
import fr.amvproject.group3.jjc.ast.nodes.finals.AddressNode;
import fr.amvproject.group3.jjc.ast.nodes.finals.BoolLiteralNode;
import fr.amvproject.group3.jjc.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.jjc.ast.nodes.finals.IntLiteralNode;
import fr.amvproject.group3.jjc.ast.nodes.finals.StringLiteralNode;
import fr.amvproject.group3.jjc.ast.nodes.instrs.GotoNode;
import fr.amvproject.group3.jjc.ast.nodes.instrs.IfNode;
import fr.amvproject.group3.jjc.ast.nodes.instrs.IncNode;
import fr.amvproject.group3.jjc.ast.nodes.instrs.InvokeNode;
import fr.amvproject.group3.jjc.ast.nodes.instrs.LengthNode;
import fr.amvproject.group3.jjc.ast.nodes.instrs.LoadNode;
import fr.amvproject.group3.jjc.ast.nodes.instrs.NewArrayNode;
import fr.amvproject.group3.jjc.ast.nodes.instrs.NewNode;
import fr.amvproject.group3.jjc.ast.nodes.instrs.PushNode;
import fr.amvproject.group3.jjc.ast.nodes.instrs.StoreNode;
import fr.amvproject.group3.jjc.ast.nodes.literals.InitLiteral;
import fr.amvproject.group3.jjc.ast.nodes.literals.JcstopLiteral;
import fr.amvproject.group3.jjc.ast.nodes.literals.NopLiteral;
import fr.amvproject.group3.jjc.ast.nodes.literals.PopLiteral;
import fr.amvproject.group3.jjc.ast.nodes.literals.ReturnLiteral;
import fr.amvproject.group3.jjc.ast.nodes.literals.SwapLiteral;
import fr.amvproject.group3.jjc.ast.nodes.literals.WriteLiteral;
import fr.amvproject.group3.jjc.ast.nodes.other.BinaryOpNode;
import fr.amvproject.group3.jjc.ast.nodes.other.ObjNode;
import fr.amvproject.group3.jjc.ast.nodes.other.TypeNode;
import fr.amvproject.group3.jjc.ast.nodes.other.UnaryOpNode;
import fr.amvproject.group3.jjc.ast.nodes.program.ClassNode;
import fr.amvproject.group3.jjc.ast.nodes.program.ProgramNode;

public class JajaCodeInterpreter implements AstVisitor<Void> {

    private final ExecutionState state;
    private final List<ExecutionStep> executionSteps = new ArrayList<>();
    private int stepCounter = 0;

    public JajaCodeInterpreter() {
        this.state = new ExecutionState();
    }

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

    public List<ExecutionStep> getExecutionSteps() {
        return new ArrayList<>(executionSteps);
    }

    public ExecutionStep getLastExecutionStep() {
        if (executionSteps.isEmpty()) {
            return null;
        }
        return executionSteps.get(executionSteps.size() - 1);
    }

    public int getStepCount() {
        return stepCounter;
    }

    @Override
    public Void visitProgram(ProgramNode node) {
        if (node == null) return null;
        state.init();
        return null;
    }

    @Override
    public Void visitClass(ClassNode node) {
        if (node == null) return null;
        if (node.getInstr() != null) {
            node.getInstr().accept(this);
        }
        return null;
    }

    // [init] : <m,a> ⊢ init –» <[], a+1>
    @Override
    public Void visitInit(InitLiteral literal) {
        state.init();
        recordStep("init");
        return null;
    }

    // [jcstop] : <m,a> ⊢ jcstop –» <m, ⊥>
    @Override
    public Void visitJcstop(JcstopLiteral literal) {
        state.stop();
        recordStep("jcstop");
        return null;
    }

    // [nop] : <m,a> ⊢ nop –» <m, a+1>
    @Override
    public Void visitNop(NopLiteral literal) {
        state.incrementAddress();
        recordStep("nop");
        return null;
    }

    // [swap] : <q1.q2.m,a> ⊢ swap –» <q2.q1.m, a+1>
    @Override
    public Void visitSwap(SwapLiteral literal) {
        state.swap();
        state.incrementAddress();
        recordStep("swap");
        return null;
    }

    // [pop] : <<i,v,o,t>.m,a>- pop –» <m, a+1>
    @Override
    public Void visitPop(PopLiteral literal) {
        state.pop();
        state.incrementAddress();
        recordStep("pop");
        return null;
    }

    // [return] : <<w ,a1,cst,*>.m,a> ⊢ return –» <m, a1>
    @Override
    public Void visitReturn(ReturnLiteral literal) {
        if (state.isEmpty()) {
            throw new RuntimeException("Stack is empty at return");
        }

        int returnAddress = -1;
        ExecutionState.StackValue returnValue = null;

        while (!state.isEmpty()) {
            ExecutionState.StackValue item = state.pop();

            if ("__frame__".equals(item.getName())) {
                returnAddress = ((Number) item.getValue()).intValue();
                if (!state.isEmpty()) {
                    returnValue = state.pop();
                }
                break;
            }
        }

        if (returnAddress == -1) {
            throw new RuntimeException("No frame marker found for return");
        }

        if (returnValue != null) {
            state.push(returnValue.getValue());
        }

        state.setAddressCounter(returnAddress);
        recordStep("return");
        return null;
    }

    // [write] : <<w, v, cst,*>.m,a> ⊢ write –» <Afficher(v,m),a+1>
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

    // [push]: <m,a> ⊢ push(v) –» <<w, v, cst,*>.m, a+1>
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

    // [newV] : <m,a> ⊢ new(i,t,var,s) –» <IdentVal(i,t,m,s),a+1>
    // [newV0]: <<w,v,cst,*>.m,a> ⊢ new(i,t,var,0) –» <IdentVal(i,t,m,v),a+1>
    // [newC] : <<w,v, cst,*>.m,a> ⊢ new(i,t,cst,0) –» <DeclCst(i,v,t,m),a+1>
    // [newM] : <<w, v, cst,*>.m,a> ⊢ new(i,t,meth,0) –» <DeclMeth(i, v, t, m),a+1>
    @Override
    public Void visitNew(NewNode node) {
        if (node == null) return null;

        String identName = node.getIdent().getName();
        String objType = node.getObj().getName();
        String typeStr = node.getType().getName();

        Object value = null;
        if (node.getValue() != null) {
            value = extractValueFromNode(node.getValue());
        }

        Object originalValue = value;

        if (value instanceof Integer && (Integer) value == 0) {
            ExecutionState.StackValue stackVal = state.pop();
            value = stackVal.getValue();

            if ("meth".equals(objType) && value instanceof Integer && (Integer) value == 0) {
                value = state.getAddressCounter() + 2;
            }
        }

        if ("var".equals(objType) && originalValue != null && !originalValue.equals(0)) {
            ExecutionState.StackValue top = state.peek();
            if (top != null && "__frame__".equals(top.getName())) {
                state.swap();
                state.pop();
                state.push(identName, value, objType, typeStr);
                state.swap();
                state.incrementAddress();
                recordStep("new(" + identName + "," + typeStr + "," + objType + ")");
                return null;
            }
        }

        state.push(identName, value, objType, typeStr);
        state.incrementAddress();
        recordStep("new(" + identName + "," + typeStr + "," + objType + ")");

        return null;
    }

    // [newarray]: <<w, v, cst,*>.m,a> ⊢ newarray(i, t) –» <DeclTab(i, v, t, m), a+1>
    @Override
    public Void visitNewArray(NewArrayNode node) {
        if (node == null) return null;

        String identName = node.getIdent().getName();
        String typeStr = node.getType().getName();

        ExecutionState.StackValue sizeValue = state.pop();
        int size = ((Number) sizeValue.getValue()).intValue();

        state.push(identName, size, "tab", typeStr);
        state.incrementAddress();
        recordStep("newarray(" + identName + "," + typeStr + "[" + size + "])");

        return null;
    }

    // [invoke]: <m,a> ⊢ invoke(i) –» <<w, a+1,cst,*>.m, Val(i, m)>
    @Override
    public Void visitInvoke(InvokeNode node) {
        if (node == null) return null;

        String methodName = node.getIdent().getName();
        int returnAddress = state.getAddressCounter() + 1;

        state.push("__frame__", returnAddress, "cst", "void");

        Object methodValue = state.getValue(methodName);

        int methodAddress;
        if (methodValue instanceof Integer) {
            methodAddress = (Integer) methodValue;
        } else {
            throw new RuntimeException("Method " + methodName + " value is not an integer address: " + methodValue);
        }

        state.setAddressCounter(methodAddress);
        recordStep("invoke(" + methodName + ")");

        return null;
    }

    // [length] : <m,a> ⊢ length(i) –» <<w,long(I,m),cst,*>, a+1>
    @Override
    public Void visitLength(LengthNode node) {
        if (node == null) return null;

        String identName = node.getIdent().getName();

        int size = state.getArrayLength(identName);

        state.push(size);
        state.incrementAddress();
        recordStep("length");

        return null;
    }

    // [load]: <m,a> ⊢ load(i) –» <<w, Val(i,m), cst,*>.m, a+1>
    @Override
    public Void visitLoad(LoadNode node) {
        if (node == null) return null;

        String identName = node.getIdent().getName();
        state.incrementAddress();

        if (node.isA()) {
            // [aload]: <<w, ind, cst,*>.m,a> ⊢ aload(i) –»<<w, ValT(i, ind, m), cst,*>.m, a+1>
            ExecutionState.StackValue index = state.pop();
            int idx = ((Number) index.getValue()).intValue();

            Object elementValue = state.getArrayValue(identName, idx);
            state.push(elementValue);
            recordStep("aload(" + identName + "[" + idx + "])");
        } else {
            Object value = state.getValue(identName);
            state.push(value);
            recordStep("load(" + identName + ")");
        }

        return null;
    }

    // [store]: <<w, v, cst,*>.m,a> ⊢ store(i) –» <AffecterVal(i,v,m), a+1>
    @Override
    public Void visitStore(StoreNode node) {
        if (node == null) return null;

        String identName = node.getIdent().getName();
        ExecutionState.StackValue value = state.pop();

        if (node.isA()) {
            // [astore]: <<w, v, cst,*>.<w, ind, cst,*>.m,a> ⊢ astore(i) –» <AffecterValT(i, ind, v,m), a+1>
            ExecutionState.StackValue indexValue = state.pop();
            int idx = ((Number) indexValue.getValue()).intValue();

            state.setArrayValue(identName, idx, value.getValue());
            recordStep("astore(" + identName + "[" + idx + "]=" + value.getValue() + ")");
        } else {
            state.setValue(identName, value.getValue());
            recordStep("store(" + identName + "=" + value.getValue() + ")");
        }

        state.incrementAddress();
        return null;
    }

    // [goto]: <m,a> ⊢ goto(a1) –» <m, a1>
    @Override
    public Void visitGoto(GotoNode node) {
        if (node == null) return null;

        int address = node.getAddress().getValue();
        state.setAddressCounter(address);
        recordStep("goto(" + address + ")");

        return null;
    }

    // [iftrue]: <<w,true, cst, *>.m,a> ⊢ if(a1) –» <m,a1>
    // [iffalse]: <<w,false, cst, *>.m,a> ⊢ if(a1) –» <m,a+1>
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

    // [inc]: <<w,v, cst,*>.m,a> ⊢ inc(i) –» <AffecterVal(i,Val(i,m)+v,m), a+1>
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

    @Override
    public Void visitIntLiteral(IntLiteralNode node) {
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

    @Override
    public Void visitObj(ObjNode node) {
        return null;
    }

    @Override
    public Void visitType(TypeNode node) {
        return null;
    }

    // [op1]: <<w, v1,cst,*>.m,a> ⊢ oper1 –» <<w, oper1 v1, cst,*>.m, a+1>
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

    // [op2]: <<w, v2,cst,*>.<w, v1,cst,*>.m,a>⊢oper2 –» <<w, v1 oper2 v2, cst,*>.m, a+1>
    @Override
    public Void visitBinaryOp(BinaryOpNode node) {
        if (node == null) return null;

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

    public ExecutionState getState() {
        return state;
    }
}
