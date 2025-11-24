package fr.amvproject.group3.jjc.ast.nodes.instrs;

import org.antlr.v4.runtime.Token;

import fr.amvproject.group3.jjc.ast.AstNode;
import fr.amvproject.group3.jjc.ast.AstVisitor;
import fr.amvproject.group3.jjc.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.jjc.ast.nodes.intermediate.InstrNode;
import fr.amvproject.group3.jjc.utils.NodeOperations.MockToken;

public class IncNode extends InstrNode {
    private final Token incToken;
    private final IdentNode identNode;
    private final boolean isA;

    public IncNode(Token incToken, IdentNode identNode, boolean isA) {
        super(incToken.getLine(), incToken.getCharPositionInLine());
        this.incToken = incToken;
        this.identNode = identNode;
        this.isA = isA;
    }

    public IncNode(IdentNode identNode, boolean isA) {
        super(0, 0);
        this.incToken = new MockToken("inc", 0, 0);
        this.identNode = identNode;
        this.isA = isA;
    }

    public Token getIncToken() {
      return incToken;
    }

    public IdentNode getIdent() {
        return identNode;
    }

    public boolean isA() {
        return isA;
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof IncNode incNode) {
            return this.identNode.sameAs(incNode.identNode);
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitInc(this);
    }
}
