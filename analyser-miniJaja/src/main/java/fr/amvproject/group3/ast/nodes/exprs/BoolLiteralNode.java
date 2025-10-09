package fr.amvproject.group3.ast.nodes.exprs;

public class BoolLiteralNode extends ExpNode {
    public final boolean value;
    public BoolLiteralNode(boolean value) { this.value = value; }

    @Override
    public String toString() { return value ? "vrai" : "faux"; }
}