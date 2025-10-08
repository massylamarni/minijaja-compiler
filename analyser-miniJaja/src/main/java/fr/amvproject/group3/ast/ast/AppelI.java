package fr.amvproject.group3.ast.ast;

import java.util.List;

public class AppelI extends Instr {
    public final Ident methodName;
    public final List<Exp> args;

    public AppelI(Ident methodName, List<Exp> args) {
        this.methodName = methodName;
        this.args = args;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(APPELI ").append(methodName).append(" (LISTEXP");
        for (Exp arg : args) sb.append(" ").append(arg);
        sb.append("))");
        return sb.toString();
    }
}