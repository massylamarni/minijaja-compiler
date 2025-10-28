package fr.amvproject.group3.ast.nodes.intermediate;

import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.finals.IdentNode;

/* 
 * Var | Methode
 */
public class DeclNode extends AstNode {
    private final IdentNode identNode;

    public DeclNode(IdentNode identNode) {
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