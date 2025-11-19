package fr.amvproject.group3.jjc.ast.nodes.instrs;

import org.antlr.v4.runtime.Token;

import fr.amvproject.group3.jjc.ast.AstNode;
import fr.amvproject.group3.jjc.ast.AstVisitor;
import fr.amvproject.group3.jjc.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.jjc.ast.nodes.intermediate.InstrNode;

public class StoreNode extends InstrNode {
    private final Token storeToken;
    private final IdentNode identNode;
    private final boolean isA;

    public StoreNode(Token storeToken, IdentNode identNode, boolean isA) {
        super(storeToken.getLine(), storeToken.getCharPositionInLine());
        this.storeToken = storeToken;
        this.identNode = identNode;
        this.isA = isA;
    }

    public Token getStoreToken() {
      return storeToken;
    }

    public IdentNode getIdent() {
        return identNode;
    }

    public boolean isA() {
        return isA;
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof StoreNode storeNode) {
            return this.identNode.sameAs(storeNode.identNode) && this.isA == storeNode.isA;
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitStore(this);
    }
}
