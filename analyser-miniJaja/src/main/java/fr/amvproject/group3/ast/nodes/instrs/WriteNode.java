package fr.amvproject.group3.ast.nodes.instrs;

import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.exprs.ExpNode;

public class WriteNode extends InstrNode {
    private final ExpNode exp;
    private final boolean isLn;

    public WriteNode(ExpNode exp, boolean isLn) {
        this.exp = exp;
        this.isLn = isLn;
    }

    public ExpNode getExp() {
        return exp;
    }

    public boolean isLn() {
        return isLn;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitWrite(this);
    }
}