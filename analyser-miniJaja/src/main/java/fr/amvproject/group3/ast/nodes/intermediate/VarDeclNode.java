package fr.amvproject.group3.ast.nodes.intermediate;

import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.ast.nodes.other.TypeNode;

/* 
 * Cst | Var | Table
 */
public class VarDeclNode extends DeclNode {
    private TypeNode type;

    public VarDeclNode(IdentNode ident, TypeNode type) {
        super(ident);
        this.type = type;
    }

    public TypeNode getType() {
      return type;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitVarDecl(this);
    }
}