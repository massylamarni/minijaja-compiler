package fr.amvproject.group3.ast.nodes.decls;

import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.abstracts.Ident;
import fr.amvproject.group3.ast.nodes.instrs.InstrsNode;
import fr.amvproject.group3.ast.nodes.other.TypeNode;
import fr.amvproject.group3.ast.nodes.program.DeclNode;
import fr.amvproject.group3.ast.nodes.params.EntetesNode;

public class MethodeNode extends DeclNode {
    private final TypeNode returnType;
    private final Ident ident;
    private final EntetesNode entetes;
    private final VarsNode vars;
    private final InstrsNode instrs;

    public MethodeNode(String returnType, Ident ident, EntetesNode entetes, VarsNode vars, InstrsNode instrs) {
        super(ident);
        this.returnType = new TypeNode(returnType);
        this.ident = ident;
        this.entetes = entetes != null ? entetes : new EntetesNode();
        this.vars = vars != null ? vars : new VarsNode();
        this.instrs = instrs != null ? instrs : new InstrsNode();
    }

    public TypeNode getReturnType() {
        return returnType;
    }

    public Ident getIdent() {
        return ident;
    }

    public EntetesNode getEntetes() {
        return entetes;
    }

    public VarsNode getVars() {
        return vars;
    }

    public InstrsNode getInstrs() {
        return instrs;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitMethode(this);
    }
}