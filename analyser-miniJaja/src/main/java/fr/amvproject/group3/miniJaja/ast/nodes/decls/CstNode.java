package fr.amvproject.group3.miniJaja.ast.nodes.decls;

import fr.amvproject.group3.miniJaja.ast.AstNode;
import fr.amvproject.group3.miniJaja.ast.AstVisitor;
import fr.amvproject.group3.miniJaja.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.miniJaja.ast.nodes.intermediate.ExpNode;
import fr.amvproject.group3.miniJaja.ast.nodes.intermediate.VarDeclNode;
import fr.amvproject.group3.miniJaja.ast.nodes.other.TypeNode;

public class CstNode extends VarDeclNode {
  private final ExpNode initExp;
  private final TypeNode typeNode;

  public CstNode(TypeNode typeNode, IdentNode identNode) {
    super(identNode);
    this.typeNode = typeNode;
    this.initExp = null;
  }

  public CstNode(TypeNode typeNode, IdentNode identNode, ExpNode initExp) {
    super(identNode);
    this.typeNode = typeNode;
    this.initExp = initExp;
  }

  public TypeNode getType() {
      return typeNode;
    }

  public ExpNode getInitExp() {
    return initExp;
  }

  @Override
  public boolean sameAs(AstNode other) {
      if (other instanceof CstNode cstNode) {
        if (this.initExp == null || cstNode.initExp == null) {
          return (this.initExp == cstNode.initExp) &&
            (this.getIdent().sameAs(cstNode.getIdent())) &&
            (this.typeNode.sameAs(cstNode.typeNode));
        }
        return this.getIdent().sameAs(cstNode.getIdent()) &&
          this.typeNode.sameAs(cstNode.typeNode) &&
          this.initExp.sameAs(cstNode.initExp);
      }
      return false;
  }

  @Override
  public <T> T accept(AstVisitor<T> visitor) {
    return visitor.visitCst(this);
  }
}
