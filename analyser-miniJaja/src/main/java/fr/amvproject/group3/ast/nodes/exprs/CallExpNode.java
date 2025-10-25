package fr.amvproject.group3.ast.nodes.exprs;

import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.abstracts.ListExpNode;
import fr.amvproject.group3.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.ast.nodes.intermediate.ExpNode;

public class CallExpNode extends ExpNode {
    private final IdentNode ident;
    private final ListExpNode listexp;

    public CallExpNode(IdentNode ident, ListExpNode listexp) {
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
    public String evaluate() {
        StringBuilder sb = new StringBuilder(ident.getName());
        for (ExpNode expNode : listexp.getList()) {
            sb.append(expNode.evaluate());
        }
        return sb.toString();
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitCallExp(this);
    }
}