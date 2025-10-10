package fr.amvproject.group3.ast.nodes.program;

import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.abstracts.Ident;
import fr.amvproject.group3.ast.nodes.decls.MethMainNode;

public class ClasseNode extends AstNode {
    private final Ident ident;
    private final DeclsNode decls;
    private final MethMainNode main;

    public ClasseNode(Ident ident, DeclsNode decls, MethMainNode main) {
        this.ident = ident;
        this.decls = decls;
        this.main = main;
    }

    public Ident getIdent() {
        return ident;
    }

    public DeclsNode getDecls() {
        return decls;
    }

    public MethMainNode getMain() {
        return main;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitClasse(this);
    }
}