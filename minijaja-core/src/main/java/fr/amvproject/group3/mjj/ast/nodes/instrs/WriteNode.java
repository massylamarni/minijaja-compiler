package fr.amvproject.group3.mjj.ast.nodes.instrs;

import fr.amvproject.group3.mjj.ast.AstNode;
import fr.amvproject.group3.mjj.ast.AstVisitor;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.ExpNode;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.InstrNode;

public class WriteNode extends InstrNode {
    private final ExpNode expNode;
    private final boolean isLn;

    public WriteNode(ExpNode expNode, boolean isLn) {
        this.expNode = expNode;
        this.isLn = isLn;
    }

    public ExpNode getExp() {
        return expNode;
    }

    public boolean isLn() {
        return isLn;
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof WriteNode writeNode) {
            return this.expNode.sameAs(writeNode.expNode) && this.isLn == writeNode.isLn;
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitWrite(this);
    }
}
