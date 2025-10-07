package fr.amvproject.group3.ast.ast;

import java.util.*;

public class Decls implements ASTNode {
    public final java.util.List<Decl> decls = new ArrayList<>();
    public void add(Decl d) { decls.add(d); }

    @Override
    public String toString() {
        if (decls.isEmpty()) return "(DECLS vnil)";
        StringBuilder sb = new StringBuilder("(DECLS ");
        for (Decl d : decls) sb.append(d).append(" ");
        sb.append(")");
        return sb.toString();
    }
}