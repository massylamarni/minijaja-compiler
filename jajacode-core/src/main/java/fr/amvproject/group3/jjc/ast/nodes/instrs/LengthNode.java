package fr.amvproject.group3.jjc.ast.nodes.instrs;

import org.antlr.v4.runtime.Token;

import fr.amvproject.group3.jjc.ast.AstNode;
import fr.amvproject.group3.jjc.ast.AstVisitor;
import fr.amvproject.group3.jjc.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.jjc.ast.nodes.intermediate.InstrNode;
import fr.amvproject.group3.jjc.utils.NodeOperations.MockToken;

public class LengthNode extends InstrNode {
    private final Token lengthToken;
    private final IdentNode identNode;

    public LengthNode(Token lengthToken, IdentNode identNode) {
        super(lengthToken.getLine(), lengthToken.getCharPositionInLine());
        this.lengthToken = lengthToken;
        this.identNode = identNode;
    }

    public LengthNode(IdentNode identNode) {
        super(0, 0);
        this.lengthToken = new MockToken("length", 0, 0);
        this.identNode = identNode;
    }

    public Token getLengthToken() {
      return lengthToken;
    }

    public IdentNode getIdent() {
        return identNode;
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof LengthNode lengthNode) {
            return this.identNode.sameAs(lengthNode.identNode);
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitLength(this);
    }
}
