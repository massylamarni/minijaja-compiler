package fr.amvproject.group3.ast.nodes.literals;

import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.AstVisitor;

public class ReturnLiteral extends AstNode {
  @Override
  public boolean sameAs(AstNode other) {
    return other instanceof ReturnLiteral;
  }

  @Override
  public <T> T accept(AstVisitor<T> visitor) {
    return null;
  }
}
