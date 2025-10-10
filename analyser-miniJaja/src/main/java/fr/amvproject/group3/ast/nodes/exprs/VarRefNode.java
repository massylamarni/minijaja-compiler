package fr.amvproject.group3.ast.nodes.exprs;

import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.abstracts.Ident;
import fr.amvproject.group3.ast.nodes.other.LeftValueNode;

/**
 * Référence à une variable simple
 */
public class VarRefNode extends LeftValueNode {
    private final Ident ident;

    public VarRefNode(Ident ident) {
        this.ident = ident;
    }

    public Ident getIdent() {
        return ident;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitVarRef(this);
    }
}
