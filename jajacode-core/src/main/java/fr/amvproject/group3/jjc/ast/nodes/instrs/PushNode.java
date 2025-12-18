package fr.amvproject.group3.jjc.ast.nodes.instrs;

import org.antlr.v4.runtime.Token;

import fr.amvproject.group3.jjc.ast.AstNode;
import fr.amvproject.group3.jjc.ast.AstVisitor;
import fr.amvproject.group3.jjc.ast.nodes.intermediate.InstrNode;
import fr.amvproject.group3.jjc.ast.nodes.intermediate.ValueNode;
import fr.amvproject.group3.jjc.utils.NodeOperations.MockToken;

public class PushNode extends InstrNode {
    private final Token pushToken;
    private final ValueNode valueNode;

    public PushNode(Token pushToken, ValueNode valueNode) {
        super(pushToken.getLine(), pushToken.getCharPositionInLine());
        this.pushToken = pushToken;
        this.valueNode = valueNode;
    }

    public PushNode(ValueNode valueNode) {
        super(0, 0);
        this.pushToken = new MockToken("push", 0, 0);
        this.valueNode = valueNode;
    }

    public Token getPushToken() {
      return pushToken;
    }

    public ValueNode getValue() {
        return valueNode;
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof PushNode pushNode) {
            return this.valueNode.sameAs(pushNode.valueNode);
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitPush(this);
    }
}
