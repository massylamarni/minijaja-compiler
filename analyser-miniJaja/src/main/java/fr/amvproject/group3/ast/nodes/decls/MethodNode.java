package fr.amvproject.group3.ast.nodes.decls;

import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.abstracts.ParamsNode;
import fr.amvproject.group3.ast.nodes.abstracts.InstrsNode;
import fr.amvproject.group3.ast.nodes.abstracts.VarsNode;
import fr.amvproject.group3.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.ast.nodes.intermediate.DeclNode;
import fr.amvproject.group3.ast.nodes.other.TypeMethNode;

public class MethodNode extends DeclNode {
    private final TypeMethNode returnType;
    private final ParamsNode paramsNode;
    private final VarsNode varsNode;
    private final InstrsNode instrsNode;

    public MethodNode(TypeMethNode returnType, IdentNode ident, ParamsNode paramsNode, VarsNode varsNode, InstrsNode instrsNode) {
        super(ident);
        this.returnType = returnType;
        this.paramsNode = paramsNode;
        this.varsNode = varsNode;
        this.instrsNode = instrsNode;
    }

    public TypeMethNode getReturnType() {
        return returnType;
    }

    public ParamsNode getParams() {
        return paramsNode;
    }

    public VarsNode getVars() {
        return varsNode;
    }

    public InstrsNode getInstrs() {
        return instrsNode;
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof MethodNode methodNode) {
            return this.getIdent().sameAs(methodNode.getIdent()) &&
                this.returnType.sameAs(methodNode.returnType) &&
                this.paramsNode.sameAs(methodNode.paramsNode) &&
                this.varsNode.sameAs(methodNode.varsNode) &&
                this.instrsNode.sameAs(methodNode.instrsNode);
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitMethod(this);
    }
}