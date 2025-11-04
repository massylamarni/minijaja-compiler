package fr.amvproject.group3.jajaCode.ast.nodes.literals;

import fr.amvproject.group3.jajaCode.ast.AstNode;
import fr.amvproject.group3.jajaCode.ast.AstVisitor;
import fr.amvproject.group3.jajaCode.ast.nodes.intermediate.InstrNode;

public class NopLiteral extends InstrNode {
  @Override
  public boolean sameAs(AstNode other) {
    return other instanceof NopLiteral;
  }

  @Override
  public <T> T accept(AstVisitor<T> visitor) {
    return visitor.visitNop(this);
  }
}
