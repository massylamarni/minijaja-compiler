package fr.amvproject.group3.mjj.ast.nodes.program;

import org.antlr.v4.runtime.Token;

import fr.amvproject.group3.mjj.ast.AstNode;
import fr.amvproject.group3.mjj.ast.AstVisitor;
import fr.amvproject.group3.mjj.ast.nodes.abstracts.DeclsNode;
import fr.amvproject.group3.mjj.ast.nodes.decls.MainNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.IdentNode;

public class ClassNode extends AstNode {
    private final Token classToken;
    private final IdentNode identNode;
    private final DeclsNode declsNode;
    private final MainNode mainNode;

    public ClassNode(Token classToken, IdentNode identNode, DeclsNode declsNode, MainNode mainNode) {
        super(identNode.getLine(), identNode.getColumn());
        this.classToken = classToken;
        this.identNode = identNode;
        this.declsNode = declsNode;
        this.mainNode = mainNode;
    }

    public Token getClassToken() {
      return classToken;
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
