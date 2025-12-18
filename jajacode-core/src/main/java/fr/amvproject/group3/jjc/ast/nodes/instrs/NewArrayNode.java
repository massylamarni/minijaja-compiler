package fr.amvproject.group3.jjc.ast.nodes.instrs;

import org.antlr.v4.runtime.Token;

import fr.amvproject.group3.jjc.ast.AstNode;
import fr.amvproject.group3.jjc.ast.AstVisitor;
import fr.amvproject.group3.jjc.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.jjc.ast.nodes.intermediate.InstrNode;
import fr.amvproject.group3.jjc.ast.nodes.other.TypeNode;
import fr.amvproject.group3.jjc.utils.NodeOperations.MockToken;

public class NewArrayNode extends InstrNode {
    private final Token newArrayToken;
    private final IdentNode identNode;
    private final TypeNode typeNode;

    public NewArrayNode(Token newArrayToken, IdentNode identNode, TypeNode typeNode) {
        super(newArrayToken.getLine(), newArrayToken.getCharPositionInLine());
        this.newArrayToken = newArrayToken;
        this.identNode = identNode;
        this.typeNode = typeNode;
    }

    public NewArrayNode(IdentNode identNode, TypeNode typeNode) {
        super(0, 0);
        this.newArrayToken = new MockToken("newarray", 0, 0);
        this.identNode = identNode;
        this.typeNode = typeNode;
    }

    public Token getNewArrayToken() {
      return newArrayToken;
    }

    public IdentNode getIdent() {
        return identNode;
    }

    public TypeNode getType() {
        return typeNode;
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof NewArrayNode newArrayNode) {
            return this.identNode.sameAs(newArrayNode.identNode) && this.typeNode.sameAs(newArrayNode.typeNode);
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitNewArray(this);
    }
}
