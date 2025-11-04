package fr.amvproject.group3.jajaCode.ast.nodes.instrs;

import fr.amvproject.group3.jajaCode.ast.AstNode;
import fr.amvproject.group3.jajaCode.ast.AstVisitor;
import fr.amvproject.group3.jajaCode.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.jajaCode.ast.nodes.intermediate.InstrNode;
import fr.amvproject.group3.jajaCode.ast.nodes.other.TypeNode;

public class NewArrayNode extends InstrNode {
    private final IdentNode identNode;
    private final TypeNode typeNode;

    public NewArrayNode(IdentNode identNode, TypeNode typeNode) {
        this.identNode = identNode;
        this.typeNode = typeNode;
    }

    public IdentNode getIdent() {
        return identNode;
    }

    public TypeNode getType() {
        return typeNode;
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof NewArrayNode newArrayNode) {
            return this.identNode.sameAs(newArrayNode.identNode) && this.typeNode.sameAs(newArrayNode.typeNode);
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitNewArray(this);
    }
}
