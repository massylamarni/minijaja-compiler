package fr.amvproject.group3.ast.nodes.exprs;

import fr.amvproject.group3.ast.nodes.abstracts.Ident;
import fr.amvproject.group3.ast.nodes.other.LeftValueNode;

/**
 * Accès à un élément d’un tableau
 * Exemple : tab[i]
 */
public class ArrayAccessNode extends LeftValueNode {

    private Ident ident;
    private ExpNode indexExpr; // expression d’index (i, 2, etc.)

    public ArrayAccessNode(Ident ident, ExpNode indexExpr) {
        this.ident = ident;
        this.indexExpr = indexExpr;
    }

    public Ident getIdent() {
        return ident;
    }

    public ExpNode getIndexExpr() {
        return indexExpr;
    }

    @Override
    public String toString() {
        return ident + "[" + indexExpr + "]";
    }
}
