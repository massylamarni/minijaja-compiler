package fr.amvproject.group3.ast.nodes.decls;

import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.abstracts.InstrsNode;
import fr.amvproject.group3.ast.nodes.abstracts.VarsNode;

public class MainNode extends AstNode {
    private final VarsNode vars;
    private final InstrsNode instrs;

    public MainNode(VarsNode vars, InstrsNode instrs) {
        this.vars = vars;
        this.instrs = instrs;
    }

    public VarsNode getVars() {
        return vars;
    }

    public InstrsNode getInstrs() {
        return instrs;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitMain(this);
    }
}