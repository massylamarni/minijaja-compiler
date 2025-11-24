package fr.amvproject.group3.jjc.ast.nodes.literals;

import org.antlr.v4.runtime.Token;

import fr.amvproject.group3.jjc.ast.AstNode;
import fr.amvproject.group3.jjc.ast.AstVisitor;
import fr.amvproject.group3.jjc.ast.nodes.intermediate.InstrNode;
import fr.amvproject.group3.jjc.utils.NodeOperations.MockToken;

public class JcstopLiteral extends InstrNode {
  private final Token jcstopToken;

  public JcstopLiteral(Token jcstopToken) {
    super(jcstopToken.getLine(), jcstopToken.getCharPositionInLine());
    this.jcstopToken = jcstopToken;
  }

  public JcstopLiteral() {
        super(0, 0);
        jcstopToken = new MockToken("jcstop", 0, 0);
    }

  public Token getJcstopToken() {
    return jcstopToken;
  }

  @Override
  public boolean sameAs(AstNode other) {
    return other instanceof JcstopLiteral;
  }

  @Override
  public <T> T accept(AstVisitor<T> visitor) {
    return visitor.visitJcstop(this);
  }
}
