package fr.amvproject.group3.mjj.ast.nodes.intermediate;

import fr.amvproject.group3.mjj.ast.nodes.finals.IdentNode;

/*
 * Cst | Var | Table
 */
public abstract class VarDeclNode extends DeclNode {
    public VarDeclNode(IdentNode identNode) {
        super(identNode);
    }
}
