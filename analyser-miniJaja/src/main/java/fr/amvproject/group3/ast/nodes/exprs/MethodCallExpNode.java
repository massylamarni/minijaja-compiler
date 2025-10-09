package fr.amvproject.group3.ast.nodes.exprs;

import java.util.List;
import fr.amvproject.group3.ast.nodes.abstracts.Ident;

public class MethodCallExpNode extends ExpNode {
    public final Ident methodName;
    public final List<ExpNode> args;

    public MethodCallExpNode(Ident methodName, List<ExpNode> args) {
        this.methodName = methodName;
        this.args = args;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(APPELE ").append(methodName).append(" (LISTEXP");
        for (ExpNode arg : args) sb.append(" ").append(arg);
        sb.append("))");
        return sb.toString();
    }
}