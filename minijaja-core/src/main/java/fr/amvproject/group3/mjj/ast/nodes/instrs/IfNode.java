package fr.amvproject.group3.mjj.ast.nodes.instrs;

import org.antlr.v4.runtime.Token;

import fr.amvproject.group3.mjj.ast.AstNode;
import fr.amvproject.group3.mjj.ast.AstVisitor;
import fr.amvproject.group3.mjj.ast.nodes.abstracts.InstrsNode;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.ExpNode;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.InstrNode;

public class IfNode extends InstrNode {
    private final Token ifToken;
    private final Token elseToken;
    private final ExpNode condExp;
    private final InstrsNode thenInstrs;
    private final InstrsNode elseInstrs;

    public IfNode(Token ifToken, ExpNode condExp, InstrsNode thenInstrs, Token elseToken, InstrsNode elseInstrs) {
        super(ifToken.getLine(), ifToken.getCharPositionInLine());
        this.ifToken = ifToken;
        this.elseToken = elseToken;
        this.condExp = condExp;
        this.thenInstrs = thenInstrs;
        this.elseInstrs = elseInstrs;
    }
    
    public Token getIfToken() {
      return ifToken;
    }

    public Token getElseToken() {
      return elseToken;
    }

    public ExpNode getCondExp() {
        return condExp;
    }

    public InstrsNode getThenInstrs() {
        return thenInstrs;
    }

    public InstrsNode getElseInstrs() {
        return elseInstrs;
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof IfNode ifNode) {
            if (this.thenInstrs == null || ifNode.thenInstrs == null) return this.thenInstrs == ifNode.thenInstrs;
            if (this.elseInstrs == null || ifNode.elseInstrs == null) return this.elseInstrs == ifNode.elseInstrs;
            return this.condExp.sameAs(ifNode.condExp) &&
                this.thenInstrs.sameAs(ifNode.thenInstrs) &&
                this.elseInstrs.sameAs(ifNode.elseInstrs);
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitIf(this);
    }
}
