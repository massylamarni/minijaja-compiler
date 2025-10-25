package fr.amvproject.group3.ast.nodes.program;

import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.abstracts.DeclsNode;
import fr.amvproject.group3.ast.nodes.decls.MainNode;
import fr.amvproject.group3.ast.nodes.finals.IdentNode;

public class ClassNode extends AstNode {
    private final IdentNode ident;
    private final DeclsNode decls;
    private final MainNode main;

    public ClassNode(IdentNode ident, DeclsNode decls, MainNode main) {
        this.ident = ident;
        this.decls = decls;
        this.main = main;
    }

    public IdentNode getIdent() {
        return ident;
    }

    public DeclsNode getDecls() {
        return decls;
    }

    public MainNode getMain() {
        return main;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitClass(this);
    }
}