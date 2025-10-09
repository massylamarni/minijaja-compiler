package fr.amvproject.group3.ast.nodes.program;

import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.nodes.abstracts.Ident;
import fr.amvproject.group3.ast.nodes.decls.MethMainNode;

public class ClasseNode implements AstNode {
    public final Ident ident;
    public final DeclsNode decls;
    public final MethMainNode main;

    public ClasseNode(Ident ident, DeclsNode decls, MethMainNode main) {
        this.ident = ident;
        this.decls = decls;
        this.main = main;
    }

    @Override
    public String toString() {
        return "(CLASSE " + ident + " " + decls + " " + main + ")";
    }
}