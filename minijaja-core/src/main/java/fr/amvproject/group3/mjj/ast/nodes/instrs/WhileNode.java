package fr.amvproject.group3.mjj.ast.nodes.instrs;

import org.antlr.v4.runtime.Token;

import fr.amvproject.group3.mjj.ast.AstNode;
import fr.amvproject.group3.mjj.ast.AstVisitor;
import fr.amvproject.group3.mjj.ast.nodes.abstracts.InstrsNode;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.ExpNode;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.InstrNode;

public class WhileNode extends InstrNode {
    private final Token whileToken;
    private final ExpNode condExp;
    private final InstrsNode instrsNode;

    public WhileNode(Token whileToken, ExpNode condExp, InstrsNode instrsNode) {
        super(whileToken.getLine(), whileToken.getCharPositionInLine());
        this.whileToken = whileToken;
        this.condExp = condExp;
        this.instrsNode = instrsNode;
    }

    public Token getWhileToken() {
      return whileToken;
    }

    public ExpNode getCondExp() {
        return condExp;
    }

    public InstrsNode getInstrs() {
        return instrsNode;
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof WhileNode whileNode) {
            return this.condExp.sameAs(whileNode.condExp) && this.instrsNode.sameAs(whileNode.instrsNode);
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitWhile(this);
    }
}
