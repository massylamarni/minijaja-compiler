package fr.amvproject.group3.ast.ast;

public class UnaryExp extends Exp {
    public final String op;
    public final Exp expr;

    public UnaryExp(String op, Exp expr) {
        this.op = op;
        this.expr = expr;
    }

    @Override
    public String toString() {
        return "(" + op + " " + expr + ")";
    }
}