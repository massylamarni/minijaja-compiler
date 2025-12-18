package fr.amvproject.group3.jjc.ast.nodes.instrs;

import org.antlr.v4.runtime.Token;

import fr.amvproject.group3.jjc.ast.AstNode;
import fr.amvproject.group3.jjc.ast.AstVisitor;
import fr.amvproject.group3.jjc.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.jjc.ast.nodes.intermediate.InstrNode;
import fr.amvproject.group3.jjc.utils.NodeOperations.MockToken;

public class InvokeNode extends InstrNode {
    private final Token invokeToken;
    private final IdentNode identNode;

    public InvokeNode(Token invokeToken, IdentNode identNode) {
        super(invokeToken.getLine(), invokeToken.getCharPositionInLine());
        this.invokeToken = invokeToken;
        this.identNode = identNode;
    }

    public InvokeNode(IdentNode identNode) {
        super(0, 0);
        this.invokeToken = new MockToken("invoke", 0, 0);
        this.identNode = identNode;
    }

    public Token getInvokeToken() {
      return invokeToken;
    }

    public IdentNode getIdent() {
        return identNode;
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof InvokeNode invokeNode) {
            return this.identNode.sameAs(invokeNode.identNode);
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitInvoke(this);
    }
}
