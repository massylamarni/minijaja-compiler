package fr.amvproject.group3.jjc.ast.nodes.finals;

import org.antlr.v4.runtime.Token;

import fr.amvproject.group3.jjc.ast.AstNode;
import fr.amvproject.group3.jjc.ast.AstVisitor;
import fr.amvproject.group3.jjc.utils.NodeOperations.MockToken;

public class IdentNode extends AstNode {
    private final Token identToken;

    public IdentNode(Token identToken) {
        super(identToken.getLine(), identToken.getCharPositionInLine());
        this.identToken = identToken;
    }

    public IdentNode(String literal, int line, int column) {
        super(line, column);
        identToken = new MockToken(literal, line, column);
    }

    public Token getIdentToken() {
      return identToken;
    }

    public String getName() {
        return identToken.getText();
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof IdentNode identNode) {
            return this.getName().equals(identNode.getName());
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof IdentNode)) return false;
        IdentNode other = (IdentNode) obj;
        return this.getName().equals(other.getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitIdent(this);
    }
}
