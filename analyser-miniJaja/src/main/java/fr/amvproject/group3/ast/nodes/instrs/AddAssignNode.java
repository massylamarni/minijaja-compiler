package fr.amvproject.group3.ast.nodes.instrs;

import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.abstracts.Ident;
import fr.amvproject.group3.ast.nodes.exprs.ExpNode;

public class AddAssignNode extends InstrNode {
    private final Ident ident;
    private final ExpNode exp;

    public AddAssignNode(Ident ident, ExpNode exp) {
        this.ident = ident;
        this.exp = exp;
    }

    public Ident getIdent() {
        return ident;
    }

    public ExpNode getExp() {
        return exp;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitAddAssign(this);
    }
}