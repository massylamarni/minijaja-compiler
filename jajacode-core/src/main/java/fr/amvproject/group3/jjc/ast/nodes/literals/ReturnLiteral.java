package fr.amvproject.group3.jjc.ast.nodes.literals;

import org.antlr.v4.runtime.Token;

import fr.amvproject.group3.jjc.ast.AstNode;
import fr.amvproject.group3.jjc.ast.AstVisitor;
import fr.amvproject.group3.jjc.ast.nodes.intermediate.InstrNode;
import fr.amvproject.group3.jjc.utils.NodeOperations.MockToken;

public class ReturnLiteral extends InstrNode {
  private final Token returnToken;

  public ReturnLiteral(Token returnToken) {
    super(returnToken.getLine(), returnToken.getCharPositionInLine());
    this.returnToken = returnToken;
  }

  public ReturnLiteral() {
        super(0, 0);
        returnToken = new MockToken("return", 0, 0);
    }

  public Token getReturnToken() {
    return returnToken;
  }

  @Override
  public boolean sameAs(AstNode other) {
    return other instanceof ReturnLiteral;
  }

  @Override
  public <T> T accept(AstVisitor<T> visitor) {
    return visitor.visitReturn(this);
  }
}
