package fr.amvproject.group3.jjc.ast.nodes.instrs;

import fr.amvproject.group3.jjc.ast.AstNode;
import fr.amvproject.group3.jjc.ast.AstVisitor;
import fr.amvproject.group3.jjc.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.jjc.ast.nodes.intermediate.InstrNode;

public class StoreNode extends InstrNode {
    private final IdentNode identNode;
    private final boolean isA;

    public StoreNode(IdentNode identNode, boolean isA) {
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
        if (other instanceof StoreNode storeNode) {
            return this.identNode.sameAs(storeNode.identNode) && this.isA == storeNode.isA;
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitStore(this);
    }
}
