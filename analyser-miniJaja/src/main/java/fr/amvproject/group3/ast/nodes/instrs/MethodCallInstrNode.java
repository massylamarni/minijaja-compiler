package fr.amvproject.group3.ast.nodes.instrs;

import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.abstracts.Ident;
import fr.amvproject.group3.ast.nodes.exprs.ListExpNode;

public class MethodCallInstrNode extends InstrNode {
    private final Ident ident;
    private final ListExpNode listexp;

    public MethodCallInstrNode(Ident ident, ListExpNode listexp) {
        this.ident = ident;
        this.listexp = listexp;
    }

    public Ident getIdent() {
        return ident;
    }

    public ListExpNode getListExp() {
        return listexp;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitMethodCallInstr(this);
    }
}