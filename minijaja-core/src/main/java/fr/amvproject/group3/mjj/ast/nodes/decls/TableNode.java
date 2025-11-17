package fr.amvproject.group3.mjj.ast.nodes.decls;

import fr.amvproject.group3.mjj.ast.AstNode;
import fr.amvproject.group3.mjj.ast.AstVisitor;
import fr.amvproject.group3.mjj.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.ExpNode;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.VarDeclNode;
import fr.amvproject.group3.mjj.ast.nodes.other.TypeMethNode;

public class TableNode extends VarDeclNode {
  private final ExpNode sizeExp;
  private final TypeMethNode typeMethNode;

  public TableNode(TypeMethNode typeMethNode, IdentNode ident, ExpNode sizeExp) {
    super(ident);
    this.typeMethNode = typeMethNode;
    this.sizeExp = sizeExp;
  }

  public TypeMethNode getType() {
    return typeMethNode;
  }

  public ExpNode getSizeExp() {
    return sizeExp;
  }

  @Override
  public boolean sameAs(AstNode other) {
     if (other instanceof TableNode tableNode) {
        return this.getIdent().sameAs(tableNode.getIdent()) &&
          this.typeMethNode.sameAs(tableNode.typeMethNode) &&
          this.sizeExp.sameAs(tableNode.sizeExp);
      }
      return false;
  }

  @Override
  public <T> T accept(AstVisitor<T> visitor) {
    return visitor.visitTable(this);
  }
}
