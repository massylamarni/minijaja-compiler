package fr.amvproject.group3.jjc.ast.nodes.literals;

import org.antlr.v4.runtime.Token;

import fr.amvproject.group3.jjc.ast.AstNode;
import fr.amvproject.group3.jjc.ast.AstVisitor;
import fr.amvproject.group3.jjc.ast.nodes.intermediate.InstrNode;

public class InitLiteral extends InstrNode {
  private final Token initToken;

  public InitLiteral(Token initToken) {
    super(initToken.getLine(), initToken.getCharPositionInLine());
    this.initToken = initToken;
  }

  public Token getInitToken() {
    return initToken;
  }

  @Override
  public boolean sameAs(AstNode other) {
    return other instanceof InitLiteral;
  }

  @Override
  public <T> T accept(AstVisitor<T> visitor) {
    return visitor.visitInit(this);
  }
}
