package fr.amvproject.group3.ast.nodes.decls;

import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.ast.nodes.intermediate.DeclNode;
import fr.amvproject.group3.ast.nodes.intermediate.ExpNode;
import fr.amvproject.group3.ast.nodes.other.TypeNode;

public class TableNode extends DeclNode {
  private final TypeNode type;
  private final ExpNode sizeExp;

  public TableNode(TypeNode type, IdentNode ident, ExpNode sizeExp) {
    super(ident);
    this.type = type;
    this.sizeExp = sizeExp;
  }

  public TypeNode getType() {
    return type;
  }

  public ExpNode getSizeExp() {
    return sizeExp;
  }

  @Override
  public <T> T accept(AstVisitor<T> visitor) {
    return visitor.visitTable(this);
  }
}
