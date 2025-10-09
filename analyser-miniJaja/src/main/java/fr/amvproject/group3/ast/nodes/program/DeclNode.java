package fr.amvproject.group3.ast.nodes.program;

import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.nodes.abstracts.Ident;

public class DeclNode implements AstNode {
    public final Ident ident;

    public DeclNode(Ident ident) {
        this.ident = ident;
    }

    @Override
    public String toString() {
        return "DeclNode(" + ident + ")";
    }
}