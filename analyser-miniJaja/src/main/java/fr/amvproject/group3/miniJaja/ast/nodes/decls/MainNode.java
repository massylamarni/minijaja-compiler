package fr.amvproject.group3.miniJaja.ast.nodes.decls;

import fr.amvproject.group3.miniJaja.ast.AstNode;
import fr.amvproject.group3.miniJaja.ast.AstVisitor;
import fr.amvproject.group3.miniJaja.ast.nodes.abstracts.InstrsNode;
import fr.amvproject.group3.miniJaja.ast.nodes.abstracts.VarsNode;

public class MainNode extends AstNode {
    private final VarsNode varsNode;
    private final InstrsNode instrsNode;

    public MainNode(VarsNode varsNode, InstrsNode instrsNode) {
        this.varsNode = varsNode;
        this.instrsNode = instrsNode;
    }

    public VarsNode getVars() {
        return varsNode;
    }

    public InstrsNode getInstrs() {
        return instrsNode;
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof MainNode mainNode) {
            return this.varsNode.sameAs(mainNode.varsNode) && this.instrsNode.sameAs(mainNode.instrsNode);
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitMain(this);
    }
}
