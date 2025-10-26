package fr.amvproject.group3.ast.nodes.intermediate;

import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.finals.IdentNode;

/* 
 * Cst | Var | Table
 */
public class VarDeclNode extends DeclNode {
    public VarDeclNode(IdentNode ident) {
        super(ident);
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitVarDecl(this);
    }
}