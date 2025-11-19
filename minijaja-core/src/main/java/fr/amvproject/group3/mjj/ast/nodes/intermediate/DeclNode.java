package fr.amvproject.group3.mjj.ast.nodes.intermediate;

import fr.amvproject.group3.mjj.ast.AstNode;
import fr.amvproject.group3.mjj.ast.AstVisitor;
import fr.amvproject.group3.mjj.ast.nodes.finals.IdentNode;

/*
 * Var | Methode
 */
public class DeclNode extends AstNode {
    private final IdentNode identNode;

    public DeclNode(IdentNode identNode) {
        super(identNode.getLine(), identNode.getColumn());
        this.identNode = identNode;
    }

    public IdentNode getIdent() {
        return identNode;
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof DeclNode declNode) {
            return this.identNode.sameAs(declNode.identNode);
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitDecl(this);
    }
}
