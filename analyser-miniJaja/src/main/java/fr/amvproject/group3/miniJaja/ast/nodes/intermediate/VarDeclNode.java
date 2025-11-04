package fr.amvproject.group3.miniJaja.ast.nodes.intermediate;

import fr.amvproject.group3.miniJaja.ast.AstNode;
import fr.amvproject.group3.miniJaja.ast.AstVisitor;
import fr.amvproject.group3.miniJaja.ast.nodes.finals.IdentNode;

/*
 * Cst | Var | Table
 */
public class VarDeclNode extends DeclNode {

    public VarDeclNode(IdentNode identNode) {
        super(identNode);
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof VarDeclNode varDeclNode) {
            return this.getIdent().sameAs(varDeclNode.getIdent());
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitVarDecl(this);
    }
}
