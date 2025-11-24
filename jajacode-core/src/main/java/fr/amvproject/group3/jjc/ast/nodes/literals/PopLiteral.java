package fr.amvproject.group3.jjc.ast.nodes.literals;

import org.antlr.v4.runtime.Token;

import fr.amvproject.group3.jjc.ast.AstNode;
import fr.amvproject.group3.jjc.ast.AstVisitor;
import fr.amvproject.group3.jjc.ast.nodes.intermediate.InstrNode;
import fr.amvproject.group3.jjc.utils.NodeOperations.MockToken;

public class PopLiteral extends InstrNode {
  private final Token popToken;

  public PopLiteral(Token popToken) {
    super(popToken.getLine(), popToken.getCharPositionInLine());
    this.popToken = popToken;
  }

  public PopLiteral() {
        super(0, 0);
        popToken = new MockToken("pop", 0, 0);
    }

  public Token getPopToken() {
    return popToken;
  }

  @Override
  public boolean sameAs(AstNode other) {
    return other instanceof PopLiteral;
  }

  @Override
  public <T> T accept(AstVisitor<T> visitor) {
    return visitor.visitPop(this);
  }
}
