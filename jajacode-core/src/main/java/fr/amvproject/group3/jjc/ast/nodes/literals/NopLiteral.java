package fr.amvproject.group3.jjc.ast.nodes.literals;

import org.antlr.v4.runtime.Token;

import fr.amvproject.group3.jjc.ast.AstNode;
import fr.amvproject.group3.jjc.ast.AstVisitor;
import fr.amvproject.group3.jjc.ast.nodes.intermediate.InstrNode;

public class NopLiteral extends InstrNode {
  private final Token nopToken;

  public NopLiteral(Token nopToken) {
    super(nopToken.getLine(), nopToken.getCharPositionInLine());
    this.nopToken = nopToken;
  }

  public Token getNopToken() {
    return nopToken;
  }

  @Override
  public boolean sameAs(AstNode other) {
    return other instanceof NopLiteral;
  }

  @Override
  public <T> T accept(AstVisitor<T> visitor) {
    return visitor.visitNop(this);
  }
}
