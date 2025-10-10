package fr.amvproject.group3.ast.nodes.params;

import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.abstracts.Ident;

public class EnteteNode extends AstNode {
    private final String type;
    private final Ident ident;

    public EnteteNode(String type, Ident ident) {
        this.type = type;
        this.ident = ident;
    }

    public String getType() {
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