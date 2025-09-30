package fr.amvproject.group3.ast.nodes.decls;

import fr.amvproject.group3.ast.*;

public class VarDeclNode extends AstNode {
    private final String type;
    private final String name;
    private final AstNode initExpr; // peut être null

    public VarDeclNode(String type, String name, AstNode initExpr) {
        this.type = type;
        this.name = name;
        this.initExpr = initExpr;
    }

    public String getType() { return type; }
    public String getName() { return name; }
    public AstNode getInitExpr() { return initExpr; }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitVarDecl(this);
    }
}