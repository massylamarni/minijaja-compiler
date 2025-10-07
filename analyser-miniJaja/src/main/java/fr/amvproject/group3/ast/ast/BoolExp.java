package fr.amvproject.group3.ast.ast;

public class BoolExp extends Exp {
    public final boolean value;
    public BoolExp(boolean value) { this.value = value; }

    @Override
    public String toString() { return value ? "vrai" : "faux"; }
}