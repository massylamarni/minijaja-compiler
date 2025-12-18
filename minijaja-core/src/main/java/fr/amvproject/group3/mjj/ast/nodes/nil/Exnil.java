package fr.amvproject.group3.mjj.ast.nodes.nil;

import fr.amvproject.group3.mjj.ast.AstNode;
import fr.amvproject.group3.mjj.ast.AstVisitor;

// Null expression list
public class Exnil extends AstNode {
  public Exnil(int line, int column) {
    super(line, column);
  }

  @Override
  public boolean sameAs(AstNode other) {
      return other instanceof Exnil;
  }

  @Override
  public <T> T accept(AstVisitor<T> visitor) {
    return null;
  }
}
