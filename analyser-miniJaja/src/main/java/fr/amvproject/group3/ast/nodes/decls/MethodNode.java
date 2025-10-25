package fr.amvproject.group3.ast.nodes.decls;

import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.abstracts.ParamsNode;
import fr.amvproject.group3.ast.nodes.abstracts.InstrsNode;
import fr.amvproject.group3.ast.nodes.abstracts.VarsNode;
import fr.amvproject.group3.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.ast.nodes.intermediate.DeclNode;
import fr.amvproject.group3.ast.nodes.other.TypeNode;

public class MethodNode extends DeclNode {
    private final TypeNode returnType;
    private final ParamsNode params;
    private final VarsNode vars;
    private final InstrsNode instrs;

    public MethodNode(TypeNode returnType, IdentNode ident, ParamsNode params, VarsNode vars, InstrsNode instrs) {
        super(ident);
        this.returnType = returnType;
        this.params = params;
        this.vars = vars;
        this.instrs = instrs;
    }

    public TypeNode getReturnType() {
        return returnType;
    }

    public ParamsNode getParams() {
        return params;
    }

    public VarsNode getVars() {
        return vars;
    }

    public InstrsNode getInstrs() {
        return instrs;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitMethod(this);
    }
}