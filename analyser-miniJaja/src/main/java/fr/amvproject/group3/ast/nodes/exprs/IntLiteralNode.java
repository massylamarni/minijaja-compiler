package fr.amvproject.group3.ast.nodes.exprs;

public class IntLiteralNode extends ExpNode {
    public final int value;
    public IntLiteralNode(int value) { this.value = value; }

    @Override
    public String toString() { return Integer.toString(value); }
}