package fr.amvproject.group3.ast.ast;

public class BinaryExp extends Exp {
    public final String op;
    public final Exp left, right;

    public BinaryExp(String op, Exp left, Exp right) {
        this.op = op;
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "(" + op + " " + left + " " + right + ")";
    }
}