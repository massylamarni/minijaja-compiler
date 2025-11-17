package fr.amvproject.group3.mjj.ast.nodes.nil;

import fr.amvproject.group3.mjj.ast.AstNode;
import fr.amvproject.group3.mjj.ast.AstVisitor;

// Null instruction
public class Inil extends AstNode {
  @Override
  public boolean sameAs(AstNode other) {
      return other instanceof Inil;
  }

  @Override
  public <T> T accept(AstVisitor<T> visitor) {
    return null;
  }
}
