package fr.amvproject.group3.ast.nodes.program;

import java.util.ArrayList;

import fr.amvproject.group3.ast.AstNode;

public class DeclsNode implements AstNode {
    public final java.util.List<DeclNode> decls = new ArrayList<>();
    public void add(DeclNode d) { decls.add(d); }

    @Override
    public String toString() {
        if (decls.isEmpty()) return "(DECLS vnil)";
        StringBuilder sb = new StringBuilder("(DECLS ");
        for (DeclNode d : decls) sb.append(d).append(" ");
        sb.append(")");
        return sb.toString();
    }
}