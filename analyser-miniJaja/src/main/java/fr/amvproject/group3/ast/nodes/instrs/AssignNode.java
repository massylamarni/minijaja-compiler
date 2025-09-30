package fr.amvproject.group3.ast.nodes.instrs;

import fr.amvproject.group3.ast.*;

// Affectation =
public class AssignNode extends AstNode {
    private final String ident;
    private final AstNode expr;

    public AssignNode(String ident, AstNode expr) {
        this.ident = ident;
        this.expr = expr;
    }

    public String getIdent() { return ident; }
    public AstNode getExpr() { return expr; }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitAssign(this);
    }
}