package fr.amvproject.group3.mjj.ast.nodes.decls;

import org.antlr.v4.runtime.Token;

import fr.amvproject.group3.mjj.ast.AstNode;
import fr.amvproject.group3.mjj.ast.AstVisitor;
import fr.amvproject.group3.mjj.ast.nodes.abstracts.InstrsNode;
import fr.amvproject.group3.mjj.ast.nodes.abstracts.VarsNode;

public class MainNode extends AstNode {
    private final Token mainToken;
    private final VarsNode varsNode;
    private final InstrsNode instrsNode;

    public MainNode(Token mainToken, VarsNode varsNode, InstrsNode instrsNode) {
        super(mainToken.getLine(), mainToken.getCharPositionInLine());
        this.mainToken = mainToken;
        this.varsNode = varsNode;
        this.instrsNode = instrsNode;
    }

    public Token getMainToken() {
      return mainToken;
    }

    public VarsNode getVars() {
        return varsNode;
    }

    public InstrsNode getInstrs() {
        return instrsNode;
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof MainNode mainNode) {
            return this.varsNode.sameAs(mainNode.varsNode) && this.instrsNode.sameAs(mainNode.instrsNode);
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitMain(this);
    }
}
