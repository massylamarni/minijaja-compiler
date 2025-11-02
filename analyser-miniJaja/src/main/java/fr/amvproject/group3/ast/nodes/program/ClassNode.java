package fr.amvproject.group3.ast.nodes.program;

import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.abstracts.DeclsNode;
import fr.amvproject.group3.ast.nodes.decls.MainNode;
import fr.amvproject.group3.ast.nodes.finals.IdentNode;

public class ClassNode extends AstNode {
    private final IdentNode identNode;
    private final DeclsNode declsNode;
    private final MainNode mainNode;

    public ClassNode(IdentNode identNode, DeclsNode declsNode, MainNode mainNode) {
        this.identNode = identNode;
        this.declsNode = declsNode;
        this.mainNode = mainNode;
    }

    public IdentNode getIdent() {
        return identNode;
    }

    public DeclsNode getDecls() {
        return declsNode;
    }

    public MainNode getMain() {
        return mainNode;
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof ClassNode classNode) {
            return this.identNode.sameAs(classNode.identNode) &&
                this.declsNode.sameAs(classNode.declsNode) &&
                this.mainNode.sameAs(classNode.mainNode);
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitClass(this);
    }
}