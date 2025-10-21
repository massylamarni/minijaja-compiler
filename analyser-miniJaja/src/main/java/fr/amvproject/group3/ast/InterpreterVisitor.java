package fr.amvproject.group3.ast;

import java.util.ArrayList;
import java.util.List;

import fr.amvproject.group3.ast.nodes.abstracts.*;
import fr.amvproject.group3.ast.nodes.decls.*;
import fr.amvproject.group3.ast.nodes.exprs.*;
import fr.amvproject.group3.ast.nodes.instrs.*;
import fr.amvproject.group3.ast.nodes.other.*;
import fr.amvproject.group3.ast.nodes.params.*;
import fr.amvproject.group3.ast.nodes.program.*;
import fr.amvproject.group3.memory.Stack;

public class InterpreterVisitor implements AstVisitor<Object> {
    private final Stack stack = new Stack();
    private boolean returnFlag = false;
    private Object returnValue = null;

    public Stack getStack() {
        return stack;
    }

    // ========== PROGRAM ==========
    @Override
    public Object visitProgram(ProgramNode node) {
        for (ClasseNode classe : node.getClasses()) {
            classe.accept(this);
        }
        return null;
    }

    @Override
    public Object visitClasse(ClasseNode node) {
        node.getDecls().accept(this);
        node.getMain().accept(this);
        return null;
    }

    // ========== DECLARATIONS ==========
    @Override
    public Object visitDecls(DeclsNode node) {
        for (DeclNode decl : node.getDecls()) {
            decl.accept(this);
        }
        return null;
    }

    @Override
    public Object visitDecl(DeclNode node) {
        if (node instanceof VarNode) {
            ((VarNode) node).accept(this);
        } else if (node instanceof MethodeNode) {
            ((MethodeNode) node).accept(this);
        }
        return null;
    }

    @Override
    public Object visitVars(VarsNode node) {
        for (VarNode var : node.getList()) {
            var.accept(this);
        }
        return null;
    }

    @Override
    public Object visitVar(VarNode node) {
        String varObj = node.getVarObj();
        Object value = null;

        // Evaluate init expression if present
        if (node.getInitExp() != null) {
            value = node.getInitExp().accept(this);
        } else {
            // Default values if no init expression
            String type = node.getType().getType().getName();
            if ("INT".equals(type)) {
                value = 0;
            } else if ("BOOLEAN".equals(type)) {
                value = false;
            }
        }

        if ("var".equals(varObj)) {
            stack.declVar(node.getIdent(), value, node.getType());
        } else if ("cst".equals(varObj)) {
            stack.declCst(node.getIdent(), value, node.getType(), false);
        } else if ("tab".equals(varObj)) {
            // Evaluate array size
            Object sizeObj = node.getSizeExp().accept(this);
            int size = ((Number) sizeObj).intValue();
            Object[] arr = new Object[size];
            stack.declTab(node.getIdent(), arr, node.getType());
        }
        return null;
    }

    @Override
    public Object visitMethode(MethodeNode node) {
        stack.declMeth(node.getIdent(), node.getReturnType(), node.getEntetes(), node.getVars(), node.getInstrs());
        return null;
    }

    @Override
    public Object visitMethMain(MethMainNode node) {
        node.getVars().accept(this);
        node.getInstrs().accept(this);
        return null;
    }

    @Override
    public Object visitEntetes(EntetesNode node) {
        // Entetes are processed during method declaration, not separately
        return null;
    }

    @Override
    public Object visitEntete(EnteteNode node) {
        return null;
    }

    // ========== INSTRUCTIONS ==========
    @Override
    public Object visitInstrs(InstrsNode node) {
        for (InstrNode instr : node.getList()) {
            if (returnFlag) break; // Stop if return statement encountered
            instr.accept(this);
        }
        return null;
    }

    @Override
    public Object visitInstr(InstrNode node) {
        return null;
    }

    @Override
    public Object visitAssign(AssignNode node) {
        Object value = node.getExp().accept(this);

        if (node.getLeftValue() instanceof VarRefNode) {
            VarRefNode varRef = (VarRefNode) node.getLeftValue();
            stack.affecterVal(varRef.getIdent(), value);
        } else if (node.getLeftValue() instanceof ArrayAccessNode) {
            ArrayAccessNode arrayAccess = (ArrayAccessNode) node.getLeftValue();
            int index = ((Number) arrayAccess.getIndexExp().accept(this)).intValue();
            stack.affecterValT(arrayAccess.getIdent(), index, value);
        }
        return null;
    }

    @Override
    public Object visitAddAssign(AddAssignNode node) {
        Object currentValue;
        if (node.getLeftValueNode() instanceof VarRefNode) {
            VarRefNode varRef = (VarRefNode) node.getLeftValueNode();
            currentValue = stack.val(varRef.getIdent());
        } else if (node.getLeftValueNode() instanceof ArrayAccessNode) {
            ArrayAccessNode arrayAccess = (ArrayAccessNode) node.getLeftValueNode();
            int index = ((Number) arrayAccess.getIndexExp().accept(this)).intValue();
            currentValue = stack.valT(arrayAccess.getIdent(), index);
        } else {
            throw new RuntimeException("Unexpected left value in +=");
        }

        Object addValue = node.getExp().accept(this);

        //int result = ((Number) currentValue).intValue() + ((Number) addValue).intValue();
        int result = ((Number) addValue).intValue();

        if (node.getLeftValueNode() instanceof VarRefNode) {
            VarRefNode varRef = (VarRefNode) node.getLeftValueNode();
            stack.affecterVal(varRef.getIdent(), result);
        } else if (node.getLeftValueNode() instanceof ArrayAccessNode) {
            ArrayAccessNode arrayAccess = (ArrayAccessNode) node.getLeftValueNode();
            int index = ((Number) arrayAccess.getIndexExp().accept(this)).intValue();
            stack.affecterValT(arrayAccess.getIdent(), index, result);
        }
        return null;
    }

    @Override
    public Object visitInc(IncNode node) {
        Object currentValue = stack.val(node.getIdent());
        int newValue = ((Number) currentValue).intValue() + 1;
        stack.affecterVal(node.getIdent(), newValue);
        return null;
    }

    @Override
    public Object visitLength(LengthNode node) {
        Object ref = stack.val(node.getIdent());
        if (ref instanceof Integer) {
            // It's a reference to array on heap
            return 0; // TODO: implement proper array length retrieval
        }
        return 0;
    }

    @Override
    public Object visitIf(IfNode node) {
        Object condValue = node.getCondExp().accept(this);
        boolean condition = (boolean) condValue;

        if (condition) {
            node.getThenInstrs().accept(this);
        } else if (node.getElseInstrs() != null) {
            node.getElseInstrs().accept(this);
        }
        return null;
    }

    @Override
    public Object visitWhile(WhileNode node) {
        while ((boolean) node.getCondExp().accept(this)) {
            if (returnFlag) break;
            node.getInstrs().accept(this);
        }
        return null;
    }

    @Override
    public Object visitWrite(WriteNode node) {
        Object value = node.getExp().accept(this);
        if (node.isLn()) {
            System.out.println(value);
        } else {
            System.out.print(value);
        }
        return null;
    }

    @Override
    public Object visitRetour(RetourNode node) {
        returnValue = node.getExp().accept(this);
        returnFlag = true;
        return returnValue;
    }

    // ========== EXPRESSIONS ==========
    @Override
    public Object visitListExp(ListExpNode node) {
        List<Object> values = new ArrayList<>();
        for (ExpNode exp : node.getList()) {
            values.add(exp.accept(this));
        }
        return values;
    }

    @Override
    public Object visitBinaryExp(BinaryExpNode node) {
        String op = node.getOp().getOp().getName();

        // Short-circuit evaluation for logical operators
        if ("&&".equals(op)) {
            Object left = node.getLeftExp().accept(this);
            if (left == null) {
                throw new RuntimeException("Left operand is null for && operator");
            }
            if (!(Boolean) left) {
                return false; // N'évalue pas le côté droit
            }
            Object right = node.getRightExp().accept(this);
            if (right == null) {
                throw new RuntimeException("Right operand is null for && operator");
            }
            return (Boolean) right;
        } else if ("||".equals(op)) {
            Object left = node.getLeftExp().accept(this);
            if (left == null) {
                throw new RuntimeException("Left operand is null for || operator");
            }
            if ((Boolean) left) {
                return true; // N'évalue pas le côté droit
            }
            Object right = node.getRightExp().accept(this);
            if (right == null) {
                throw new RuntimeException("Right operand is null for || operator");
            }
            return (Boolean) right;
        }

        // For other operators, evaluate both sides
        Object left = node.getLeftExp().accept(this);
        Object right = node.getRightExp().accept(this);

        switch (op) {
            case "+":
                return ((Number) left).intValue() + ((Number) right).intValue();
            case "-":
                return ((Number) left).intValue() - ((Number) right).intValue();
            case "*":
                return ((Number) left).intValue() * ((Number) right).intValue();
            case "/":
                int divisor = ((Number) right).intValue();
                if (divisor == 0) throw new RuntimeException("Division by zero");
                return ((Number) left).intValue() / divisor;
            case "==":
                return left.equals(right);
            case ">":
                return ((Number) left).intValue() > ((Number) right).intValue();
            default:
                throw new RuntimeException("Unknown binary operator: " + op);
        }
    }

    @Override
    public Object visitUnaryExp(UnaryExpNode node) {
        Object operand = node.getExp().accept(this);
        String op = node.getOp().getOp().getName();

        if (operand == null) {
            throw new RuntimeException("Operand is null for unary operator: " + op);
        }

        switch (op) {
            case "-":
                return -((Number) operand).intValue();
            case "!":
                return !(boolean) operand;
            default:
                throw new RuntimeException("Unknown unary operator: " + op);
        }
    }

    @Override
    public Object visitIntLiteral(IntLiteralNode node) {
        return node.getValue();
    }

    @Override
    public Object visitBoolLiteral(BoolLiteralNode node) {
        return node.getValue();
    }

    @Override
    public Object visitStringLiteral(StringLiteralNode node) {
        return node.getValue();
    }

    @Override
    public Object visitVarRef(VarRefNode node) {
        return stack.val(node.getIdent());
    }

    @Override
    public Object visitArrayAccess(ArrayAccessNode node) {
        int index = ((Number) node.getIndexExp().accept(this)).intValue();
        return stack.valT(node.getIdent(), index);
    }

    @Override
    public Object visitMethodCallExp(MethodCallExpNode node) {
        List<Object> args = (List<Object>) node.getListExp().accept(this);
        return stack.callMethod(node.getIdent(), args);
    }

    @Override
    public Object visitMethodCallInstr(MethodCallInstrNode node) {
        List<Object> args = (List<Object>) node.getListExp().accept(this);
        stack.callMethod(node.getIdent(), args);
        return null;
    }

    // ========== OTHER NODES ==========
    @Override
    public Object visitType(TypeNode node) {
        return node;
    }

    @Override
    public Object visitBinaryOp(BinaryOpNode node) {
        return node;
    }

    @Override
    public Object visitUnaryOp(UnaryOpNode node) {
        return node;
    }

    @Override
    public Object visitIdent(Ident node) {
        return node.getName();
    }
}
