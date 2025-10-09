package fr.amvproject.group3.ast.nodes.instrs;

import java.util.List;
import fr.amvproject.group3.ast.nodes.abstracts.Ident;
import fr.amvproject.group3.ast.nodes.exprs.ExpNode;

public class MethodCallInstrNode extends InstrNode {
    public final Ident methodName;
    public final List<ExpNode> args;

    public MethodCallInstrNode(Ident methodName, List<ExpNode> args) {
        this.methodName = methodName;
        this.args = args;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(APPELI ").append(methodName).append(" (LISTEXP");
        for (ExpNode arg : args) sb.append(" ").append(arg);
        sb.append("))");
        return sb.toString();
    }
}