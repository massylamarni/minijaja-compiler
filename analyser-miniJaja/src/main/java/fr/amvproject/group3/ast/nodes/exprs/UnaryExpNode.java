package fr.amvproject.group3.ast.nodes.exprs;

public class UnaryExpNode extends ExpNode {
    public final String op;
    public final ExpNode exp;

    public UnaryExpNode(String op, ExpNode exp) {
        this.op = op;
        this.exp = exp;
    }

    @Override
    public String toString() {
        return "(" + op + " " + exp + ")";
    }
}