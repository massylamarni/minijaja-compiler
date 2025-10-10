package fr.amvproject.group3.ast.nodes.exprs;

import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.abstracts.Ident;

public class MethodCallExpNode extends ExpNode {
    private final Ident ident;
    private final ListExpNode listexp;

    public MethodCallExpNode(Ident ident, ListExpNode listexp) {
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
        return visitor.visitMethodCallExp(this);
    }
}