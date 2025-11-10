package fr.amvproject.group3.mjj.ast.nodes.finals;

import fr.amvproject.group3.mjj.ast.AstNode;
import fr.amvproject.group3.mjj.ast.AstVisitor;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.ExpNode;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.LeftValueNode;

/**
 * Accès à un élément d’un tableau
 * Exemple : tab[i]
 */
public class ArrayAccessNode extends LeftValueNode {
    private final IdentNode ident;
    private final ExpNode indexExp; // expression d’index (i, 2, etc.)

    public ArrayAccessNode(IdentNode ident, ExpNode indexExp) {
        this.ident = ident;
        this.indexExp = indexExp;
    }

    public IdentNode getIdent() {
        return ident;
    }

    public ExpNode getIndexExp() {
        return indexExp;
    }

    @Override
    public String evaluate() {
        return ident.getName() + "[" + indexExp.evaluate() + "]";
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof ArrayAccessNode arrayAccessNode) {
            return this.ident.sameAs(arrayAccessNode.ident) && this.indexExp.sameAs(arrayAccessNode.indexExp);
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitArrayAccess(this);
    }
}
