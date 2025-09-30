package fr.amvproject.group3.ast.nodes.decls;

import fr.amvproject.group3.ast.*;

public class ConstDeclNode extends AstNode {
    private final String type;
    private final String name;
    private final AstNode value;

    public ConstDeclNode(String type, String name, AstNode value) {
        this.type = type;
        this.name = name;
        this.value = value;
    }

    public String getType() { return type; }
    public String getName() { return name; }
    public AstNode getValue() { return value; }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitConstDecl(this);
    }
}