package fr.amvproject.group3.ast.nodes.instrs;

import fr.amvproject.group3.ast.*;

public class WhileNode extends AstNode {
    private final AstNode condition;
    private final java.util.List<AstNode> body;

    public WhileNode(AstNode condition, java.util.List<AstNode> body) {
        this.condition = condition;
        this.body = body;
    }

    public AstNode getCondition() { return condition; }
    public java.util.List<AstNode> getBody() { return body; }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitWhile(this);
    }
}