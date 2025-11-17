package fr.amvproject.group3.jjc.ast.nodes.literals;

import fr.amvproject.group3.jjc.ast.AstNode;
import fr.amvproject.group3.jjc.ast.AstVisitor;
import fr.amvproject.group3.jjc.ast.nodes.intermediate.InstrNode;

public class ReturnLiteral extends InstrNode {
  @Override
  public boolean sameAs(AstNode other) {
    return other instanceof ReturnLiteral;
  }

  @Override
  public <T> T accept(AstVisitor<T> visitor) {
    return visitor.visitReturn(this);
  }
}
