package fr.amvproject.group3.ast.nodes.literals;

import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.AstVisitor;

public class JcstopLiteral extends AstNode {
  
  @Override
  public boolean sameAs(AstNode other) {
    return other instanceof JcstopLiteral;
  }

  @Override
  public <T> T accept(AstVisitor<T> visitor) {
    return null;
  }
}
