package fr.amvproject.group3.ast.nodes.exprs;

public class BinaryExpNode extends ExpNode {
    public final String op;
    public final ExpNode left, right;

    public BinaryExpNode(String op, ExpNode left, ExpNode right) {
        this.op = op;
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "(" + op + " " + left + " " + right + ")";
    }
}