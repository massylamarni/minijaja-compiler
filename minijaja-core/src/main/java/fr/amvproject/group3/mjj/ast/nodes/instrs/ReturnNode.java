package fr.amvproject.group3.mjj.ast.nodes.instrs;

import org.antlr.v4.runtime.Token;

import fr.amvproject.group3.mjj.ast.AstNode;
import fr.amvproject.group3.mjj.ast.AstVisitor;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.ExpNode;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.InstrNode;

public class ReturnNode extends InstrNode {
    private final Token returnToken;
    private final ExpNode expNode;

    public ReturnNode(Token returnToken, ExpNode expNode) {
        super(returnToken.getLine(), returnToken.getCharPositionInLine());
        this.returnToken = returnToken;
        this.expNode = expNode;
    }

    public Token getReturnToken() {
      return returnToken;
    }

    public ExpNode getExp() {
        return expNode;
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof ReturnNode returnNode) {
            return this.expNode.sameAs(returnNode.expNode);
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitReturn(this);
    }
}
