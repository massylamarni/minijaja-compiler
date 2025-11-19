package fr.amvproject.group3.mjj.ast.nodes.instrs;

import org.antlr.v4.runtime.Token;

import fr.amvproject.group3.mjj.ast.AstNode;
import fr.amvproject.group3.mjj.ast.AstVisitor;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.InstrNode;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.LeftValueNode;

public class IncNode extends InstrNode {
    private final LeftValueNode leftValueNode;
    private final Token incToken;

    public IncNode(LeftValueNode leftValueNode, Token incToken) {
        super(leftValueNode.getLine(), leftValueNode.getColumn());
        this.leftValueNode = leftValueNode;
        this.incToken = incToken;
    }

    public LeftValueNode getLeftValueNode() {
        return leftValueNode;
    }

    public Token getIncToken() {
      return incToken;
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof IncNode incNode) {
            return this.leftValueNode.sameAs(incNode.leftValueNode);
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitInc(this);
    }
}
