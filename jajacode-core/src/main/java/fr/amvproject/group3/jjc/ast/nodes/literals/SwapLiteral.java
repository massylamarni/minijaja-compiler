package fr.amvproject.group3.jjc.ast.nodes.literals;

import org.antlr.v4.runtime.Token;

import fr.amvproject.group3.jjc.ast.AstNode;
import fr.amvproject.group3.jjc.ast.AstVisitor;
import fr.amvproject.group3.jjc.ast.nodes.intermediate.InstrNode;

public class SwapLiteral extends InstrNode {
  private final Token swapToken;

  public SwapLiteral(Token swapToken) {
    super(swapToken.getLine(), swapToken.getCharPositionInLine());
    this.swapToken = swapToken;
  }

  public Token getSwapToken() {
    return swapToken;
  }

  @Override
  public boolean sameAs(AstNode other) {
    return other instanceof SwapLiteral;
  }

  @Override
  public <T> T accept(AstVisitor<T> visitor) {
    return visitor.visitSwap(this);
  }
}
