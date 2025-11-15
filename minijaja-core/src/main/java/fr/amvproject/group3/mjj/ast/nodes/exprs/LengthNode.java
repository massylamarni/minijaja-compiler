package fr.amvproject.group3.mjj.ast.nodes.exprs;

import fr.amvproject.group3.mjj.ast.AstNode;
import fr.amvproject.group3.mjj.ast.AstVisitor;
import fr.amvproject.group3.mjj.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.ExpNode;

public class LengthNode extends ExpNode {
    private final IdentNode identNode;

    public LengthNode(IdentNode identNode) {
        this.identNode = identNode;
    }

    public IdentNode getIdent() {
        return identNode;
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof LengthNode lengthNode) {
            return this.identNode.sameAs(lengthNode.identNode);
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitLength(this);
    }
}
