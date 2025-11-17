package fr.amvproject.group3.mjj.ast.nodes.instrs;

import fr.amvproject.group3.mjj.ast.AstNode;
import fr.amvproject.group3.mjj.ast.AstVisitor;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.ExpNode;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.InstrNode;

public class ReturnNode extends InstrNode {
    private final ExpNode expNode;

    public ReturnNode(ExpNode expNode) {
        this.expNode = expNode;
    }

    public ExpNode getExp() {
        return expNode;
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof ReturnNode returnNode) {
            return this.expNode.sameAs(returnNode.expNode);
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitReturn(this);
    }
}
