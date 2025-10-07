package fr.amvproject.group3.ast.ast;

public class Incrementation extends Instr {
    public final Ident ident;
    public Incrementation(Ident ident) { this.ident = ident; }

    @Override
    public String toString() {
        return "(INCREMENT " + ident + ")";
    }
}
