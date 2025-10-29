package fr.amvproject.group3.ast.nodes.literals;

import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.AstVisitor;

public class WriteLiteral extends AstNode {
  private final boolean isLn;

  public WriteLiteral(boolean isLn) {
    this.isLn = isLn;
  }

  public boolean isLn() {
    return isLn;
  }

  @Override
  public boolean sameAs(AstNode other) {
    return other instanceof WriteLiteral;
  }

  @Override
  public <T> T accept(AstVisitor<T> visitor) {
    return null;
  }
}
