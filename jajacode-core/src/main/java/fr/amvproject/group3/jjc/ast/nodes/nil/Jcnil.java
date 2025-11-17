package fr.amvproject.group3.jjc.ast.nodes.nil;

import fr.amvproject.group3.jjc.ast.AstNode;
import fr.amvproject.group3.jjc.ast.AstVisitor;

public class Jcnil extends AstNode {
  @Override
  public boolean sameAs(AstNode other) {
    return other instanceof Jcnil;
  }

  @Override
  public <T> T accept(AstVisitor<T> visitor) {
    return null;
  }
}
