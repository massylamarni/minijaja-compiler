package fr.amvproject.group3.ast.nodes.instrs;

import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.abstracts.ListExpNode;
import fr.amvproject.group3.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.ast.nodes.intermediate.InstrNode;

public class CallInstrNode extends InstrNode {
    private final IdentNode ident;
    private final ListExpNode listexp;

    public CallInstrNode(IdentNode ident, ListExpNode listexp) {
        this.ident = ident;
        this.listexp = listexp;
    }

    public IdentNode getIdent() {
        return ident;
    }

    public ListExpNode getListExp() {
        return listexp;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitCallInstr(this);
    }
}