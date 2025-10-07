package fr.amvproject.group3.ast.ast;

public class IntExp extends Exp {
    public final int value;
    public IntExp(int value) { this.value = value; }

    @Override
    public String toString() { return Integer.toString(value); }
}