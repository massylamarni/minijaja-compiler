package fr.amvproject.group3.ast.ast;

public class WhileInstr extends Instr {
    public final Exp cond;
    public final Instrs body;

    public WhileInstr(Exp cond, Instrs body) {
        this.cond = cond;
        this.body = body;
    }

    @Override
    public String toString() {
        return "(TANTQUE " + cond + " " + body + ")";
    }
}