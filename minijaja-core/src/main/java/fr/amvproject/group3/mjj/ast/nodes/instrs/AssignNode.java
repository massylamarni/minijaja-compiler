package fr.amvproject.group3.mjj.ast.nodes.instrs;

import org.antlr.v4.runtime.Token;

import fr.amvproject.group3.mjj.ast.AstNode;
import fr.amvproject.group3.mjj.ast.AstVisitor;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.ExpNode;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.InstrNode;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.LeftValueNode;

public class AssignNode extends InstrNode {
    private final LeftValueNode leftValueNode;   // Variable ou case de tableau
    private final Token assignToken;
    private final ExpNode expNode;              // Expression assignée

    public AssignNode(LeftValueNode leftValueNode, Token assignToken, ExpNode expNode) {
        super(assignToken.getLine(), assignToken.getCharPositionInLine());
        this.leftValueNode = leftValueNode;
        this.assignToken = assignToken;
        this.expNode = expNode;
    }

    public LeftValueNode getLeftValueNode() {
        return leftValueNode;
    }

    public Token getAssignToken() {
      return assignToken;
    }

    public ExpNode getExp() {
        return expNode;
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof AssignNode assignNode) {
            return this.leftValueNode.sameAs(assignNode.leftValueNode) && this.expNode.sameAs(assignNode.expNode);
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitAssign(this);
    }
}
