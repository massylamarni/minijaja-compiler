package fr.amvproject.group3.ast.nodes.intermediate;

import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.finals.IdentNode;

/* 
 * Var | Methode
 */
public class DeclNode extends AstNode {
    private final IdentNode ident;

    public DeclNode(IdentNode ident) {
        this.ident = ident;
    }

    public IdentNode getIdent() {
        return ident;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitDecl(this);
    }
}