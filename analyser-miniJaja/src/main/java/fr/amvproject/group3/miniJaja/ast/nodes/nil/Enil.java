package fr.amvproject.group3.miniJaja.ast.nodes.nil;

import fr.amvproject.group3.miniJaja.ast.AstNode;
import fr.amvproject.group3.miniJaja.ast.AstVisitor;

// Null params
public class Enil extends AstNode {
  @Override
  public boolean sameAs(AstNode other) {
      return other instanceof Enil;
  }

  @Override
  public <T> T accept(AstVisitor<T> visitor) {
    return null;
  }
}
