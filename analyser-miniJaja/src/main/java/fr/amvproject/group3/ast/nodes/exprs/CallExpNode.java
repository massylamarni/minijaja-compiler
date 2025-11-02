package fr.amvproject.group3.ast.nodes.exprs;

import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.abstracts.ListExpNode;
import fr.amvproject.group3.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.ast.nodes.intermediate.ExpNode;

public class CallExpNode extends ExpNode {
    private final IdentNode identNode;
    private final ListExpNode listExpNode;

    public CallExpNode(IdentNode identNode, ListExpNode listExpNode) {
        this.identNode = identNode;
        this.listExpNode = listExpNode;
    }

    public IdentNode getIdent() {
        return identNode;
    }

    public ListExpNode getListExp() {
        return listExpNode;
    }

    @Override
    public String evaluate() {
        StringBuilder sb = new StringBuilder(identNode.getName());
        for (ExpNode expNode : listExpNode.getList()) {
            sb.append(expNode.evaluate());
        }
        return sb.toString();
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof CallExpNode callExpNode) {
            return this.identNode.sameAs(callExpNode.identNode) && this.listExpNode.sameAs(callExpNode.listExpNode);
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitCallExp(this);
    }
}