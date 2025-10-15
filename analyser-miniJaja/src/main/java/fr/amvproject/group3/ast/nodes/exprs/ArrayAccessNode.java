package fr.amvproject.group3.ast.nodes.exprs;

import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.abstracts.Ident;
import fr.amvproject.group3.ast.nodes.other.LeftValueNode;

/**
 * Accès à un élément d’un tableau
 * Exemple : tab[i]
 */
public class ArrayAccessNode extends LeftValueNode {
    private final Ident ident;
    private final ExpNode indexExp; // expression d’index (i, 2, etc.)

    public ArrayAccessNode(Ident ident, ExpNode indexExp) {
        this.ident = ident;
        this.indexExp = indexExp;
    }

    public Ident getIdent() {
        return ident;
    }

    public ExpNode getIndexExp() {
        return indexExp;
    }

    @Override
    public String evaluate() {
        return ident.getName() + "[ " + indexExp.evaluate() + " ]";
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitArrayAccess(this);
    }
}
