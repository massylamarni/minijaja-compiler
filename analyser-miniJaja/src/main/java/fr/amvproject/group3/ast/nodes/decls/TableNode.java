package fr.amvproject.group3.ast.nodes.decls;

import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.ast.nodes.intermediate.ExpNode;
import fr.amvproject.group3.ast.nodes.intermediate.VarDeclNode;
import fr.amvproject.group3.ast.nodes.other.TypeNode;

public class TableNode extends VarDeclNode {
  private final ExpNode sizeExp;

  public TableNode(TypeNode type, IdentNode ident, ExpNode sizeExp) {
    super(ident, type);
    this.sizeExp = sizeExp;
  }

  public ExpNode getSizeExp() {
    return sizeExp;
  }

  @Override
  public <T> T accept(AstVisitor<T> visitor) {
    return visitor.visitTable(this);
  }
}
