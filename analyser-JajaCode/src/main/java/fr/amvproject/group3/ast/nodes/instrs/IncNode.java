package fr.amvproject.group3.ast.nodes.instrs;

import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.ast.nodes.intermediate.InstrNode;

public class IncNode extends InstrNode {
    private final IdentNode identNode;
    private final boolean isA;
    
    public IncNode(IdentNode identNode, boolean isA) {
        this.identNode = identNode;
        this.isA = isA;
    }

    public IdentNode getIdent() {
        return identNode;
    }

    public boolean isA() {
        return isA;
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof IncNode incNode) {
            return this.identNode.sameAs(incNode.identNode);
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitInc(this);
    }
}
