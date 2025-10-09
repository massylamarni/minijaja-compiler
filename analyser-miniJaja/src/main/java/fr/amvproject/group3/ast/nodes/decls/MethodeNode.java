package fr.amvproject.group3.ast.nodes.decls;

import java.util.List;
import java.util.ArrayList;

import fr.amvproject.group3.ast.nodes.abstracts.Ident;
import fr.amvproject.group3.ast.nodes.instrs.InstrsNode;
import fr.amvproject.group3.ast.nodes.other.TypeNode;
import fr.amvproject.group3.ast.nodes.program.DeclsNode;
import fr.amvproject.group3.ast.nodes.program.DeclNode;
import fr.amvproject.group3.ast.nodes.params.EnteteNode;

public class MethodeNode extends DeclNode {
    public final TypeNode returnType;
    public final Ident ident;
    public final List<EnteteNode> params;
    public final DeclsNode vars;
    public final InstrsNode instrs;

    public MethodeNode(String returnType, Ident ident, List<EnteteNode> params, DeclsNode vars, InstrsNode instrs) {
        super(ident);
        this.returnType = new TypeNode(returnType);
        this.ident = ident;
        this.params = params != null ? params : new ArrayList<>();
        this.vars = vars != null ? vars : new DeclsNode();
        this.instrs = instrs != null ? instrs : new InstrsNode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(METHODE ").append(returnType).append(" ").append(ident).append(" ");
        sb.append("(ENTETES ");
        for (EnteteNode e : params) sb.append(e).append(" ");
        sb.append(") ");
        sb.append(vars).append(" ").append(instrs).append(")");
        return sb.toString();
    }
}