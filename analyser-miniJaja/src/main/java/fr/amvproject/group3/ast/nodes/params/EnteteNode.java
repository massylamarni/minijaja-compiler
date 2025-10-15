package fr.amvproject.group3.ast.nodes.params;

import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.abstracts.Ident;
import fr.amvproject.group3.ast.nodes.other.TypeNode;

public class EnteteNode extends AstNode {
    private final TypeNode type;
    private final Ident ident;

    public EnteteNode(TypeNode type, Ident ident) {
        this.type = type;
        this.ident = ident;
    }

    public TypeNode getType() {
        return type;
    }

    public Ident getIdent() {
        return ident;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitEntete(this);
    }
}