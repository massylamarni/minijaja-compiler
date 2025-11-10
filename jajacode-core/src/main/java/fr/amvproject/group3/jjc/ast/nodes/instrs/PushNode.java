package fr.amvproject.group3.jjc.ast.nodes.instrs;

import fr.amvproject.group3.jjc.ast.AstNode;
import fr.amvproject.group3.jjc.ast.AstVisitor;
import fr.amvproject.group3.jjc.ast.nodes.intermediate.InstrNode;
import fr.amvproject.group3.jjc.ast.nodes.intermediate.ValueNode;

public class PushNode extends InstrNode {
    private final ValueNode valueNode;

    public PushNode(ValueNode valueNode) {
        this.valueNode = valueNode;
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
