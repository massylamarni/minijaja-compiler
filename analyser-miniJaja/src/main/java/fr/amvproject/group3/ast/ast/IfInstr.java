package fr.amvproject.group3.ast.ast;

public class IfInstr extends Instr {
    public final Exp cond;
    public final Instrs thenInstrs;
    public final Instrs elseInstrs;

    public IfInstr(Exp cond, Instrs thenInstrs, Instrs elseInstrs) {
        this.cond = cond;
        this.thenInstrs = thenInstrs;
        this.elseInstrs = elseInstrs;
    }

    @Override
    public String toString() {
        return "(SI " + cond + " " + thenInstrs + " " + elseInstrs + ")";
    }
}