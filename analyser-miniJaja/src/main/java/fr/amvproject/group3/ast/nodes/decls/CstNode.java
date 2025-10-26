package fr.amvproject.group3.ast.nodes.decls;

import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.ast.nodes.intermediate.ExpNode;
import fr.amvproject.group3.ast.nodes.intermediate.VarDeclNode;
import fr.amvproject.group3.ast.nodes.other.TypeNode;

public class CstNode extends VarDeclNode {
  private final ExpNode initExp;

  public CstNode(TypeNode type, IdentNode ident) {
    super(ident, type);
    this.initExp = null;
  }

  public CstNode(TypeNode type, IdentNode ident, ExpNode initExp) {
    super(ident, type);
    this.initExp = initExp;
  }

  public ExpNode getInitExp() {
    return initExp;
  }

  @Override
  public <T> T accept(AstVisitor<T> visitor) {
    return visitor.visitCst(this);
  }
}
