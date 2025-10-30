package fr.amvproject.group3.ast.nodes.instrs;

import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.ast.nodes.intermediate.InstrNode;

public class InvokeNode extends InstrNode {
    private final IdentNode identNode;

    public InvokeNode(IdentNode identNode) {
        this.identNode = identNode;
    }

    public IdentNode getIdent() {
        return identNode;
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof InvokeNode invokeNode) {
            return this.identNode.sameAs(invokeNode.identNode);
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitInvoke(this);
    }
}