package fr.amvproject.group3.jjc.ast.nodes.instrs;

import org.antlr.v4.runtime.Token;

import fr.amvproject.group3.jjc.ast.AstNode;
import fr.amvproject.group3.jjc.ast.AstVisitor;
import fr.amvproject.group3.jjc.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.jjc.ast.nodes.intermediate.InstrNode;

public class LoadNode extends InstrNode {
    private final Token loadToken;
    private final IdentNode identNode;
    private final boolean isA;

    public LoadNode(Token loadToken, IdentNode identNode, boolean isA) {
        super(loadToken.getLine(), loadToken.getCharPositionInLine());
        this.loadToken = loadToken;
        this.identNode = identNode;
        this.isA = isA;
    }

    public Token getLoadToken() {
      return loadToken;
    }

    public IdentNode getIdent() {
        return identNode;
    }

    public boolean isA() {
        return isA;
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof LoadNode loadNode) {
            return this.identNode.sameAs(loadNode.identNode);
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitLoad(this);
    }
}
