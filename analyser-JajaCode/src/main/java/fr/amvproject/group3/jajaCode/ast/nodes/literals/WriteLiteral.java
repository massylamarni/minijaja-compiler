package fr.amvproject.group3.jajaCode.ast.nodes.literals;

import fr.amvproject.group3.jajaCode.ast.AstNode;
import fr.amvproject.group3.jajaCode.ast.AstVisitor;
import fr.amvproject.group3.jajaCode.ast.nodes.intermediate.InstrNode;

public class WriteLiteral extends InstrNode {
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
    return visitor.visitWrite(this);
  }
}
