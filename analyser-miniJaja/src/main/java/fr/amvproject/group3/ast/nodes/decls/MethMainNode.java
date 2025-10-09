package fr.amvproject.group3.ast.nodes.decls;

import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.nodes.program.DeclsNode;
import fr.amvproject.group3.ast.nodes.instrs.InstrsNode;

public class MethMainNode implements AstNode {
    public final DeclsNode decls;
    public final InstrsNode instrs;

    public MethMainNode(DeclsNode decls, InstrsNode instrs) {
        this.decls = decls;
        this.instrs = instrs;
    }

    @Override
    public String toString() {
        return "(MAIN " + decls + " " + instrs + ")";
    }
}