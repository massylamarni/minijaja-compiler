package fr.amvproject.group3.jjc.ast.nodes.literals;

import fr.amvproject.group3.jjc.ast.AstNode;
import fr.amvproject.group3.jjc.ast.AstVisitor;
import fr.amvproject.group3.jjc.ast.nodes.intermediate.InstrNode;

public class PopLiteral extends InstrNode {
  @Override
  public boolean sameAs(AstNode other) {
    return other instanceof PopLiteral;
  }

  @Override
  public <T> T accept(AstVisitor<T> visitor) {
    return visitor.visitPop(this);
  }
}
