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
    private final IdentNode identNode;
    private final ExpNode indexExp; // expression d’index (i, 2, etc.)

    public ArrayAccessNode(IdentNode identNode, ExpNode indexExp) {
        super(identNode.getLine(), identNode.getColumn());
        this.identNode = identNode;
        this.indexExp = indexExp;
    }

    public IdentNode getIdent() {
        return identNode;
    }

    public ExpNode getIndexExp() {
        return indexExp;
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof ArrayAccessNode arrayAccessNode) {
            return this.identNode.sameAs(arrayAccessNode.identNode) && this.indexExp.sameAs(arrayAccessNode.indexExp);
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitArrayAccess(this);
    }
}
