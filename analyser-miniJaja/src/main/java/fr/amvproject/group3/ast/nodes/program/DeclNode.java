package fr.amvproject.group3.ast.nodes.program;

import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.abstracts.Ident;

/* 
 * Var | Methode
 */
public class DeclNode extends AstNode {
    private final Ident ident;

    public DeclNode(Ident ident) {
        this.ident = ident;
    }

    public Ident getIdent() {
        return ident;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitDecl(this);
    }
}