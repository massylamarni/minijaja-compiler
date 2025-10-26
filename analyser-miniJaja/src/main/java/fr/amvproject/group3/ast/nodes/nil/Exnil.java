package fr.amvproject.group3.ast.nodes.nil;

import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.AstVisitor;

// Null expression list
public class Exnil extends AstNode {
  @Override
  public <T> T accept(AstVisitor<T> visitor) {
    return null;
  }
}