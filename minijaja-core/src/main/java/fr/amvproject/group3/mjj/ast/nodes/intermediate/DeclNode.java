package fr.amvproject.group3.mjj.ast.nodes.intermediate;

import fr.amvproject.group3.mjj.ast.AstNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.IdentNode;

/*
 * Var | Methode
 */
public abstract class DeclNode extends AstNode {
    private final IdentNode identNode;

    public DeclNode(IdentNode identNode) {
        super(identNode.getLine(), identNode.getColumn());
        this.identNode = identNode;
    }

    public IdentNode getIdent() {
        return identNode;
    }
}
