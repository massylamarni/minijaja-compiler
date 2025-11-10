package fr.amvproject.group3.mjj.ast.nodes.nil;

import fr.amvproject.group3.mjj.ast.AstNode;
import fr.amvproject.group3.mjj.ast.AstVisitor;

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
