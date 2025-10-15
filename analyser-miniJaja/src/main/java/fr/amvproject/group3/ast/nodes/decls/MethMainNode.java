package fr.amvproject.group3.ast.nodes.decls;

import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.instrs.InstrsNode;

public class MethMainNode extends AstNode {
    private final VarsNode vars;
    private final InstrsNode instrs;

    public MethMainNode(VarsNode vars, InstrsNode instrs) {
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
        return visitor.visitMethMain(this);
    }
}