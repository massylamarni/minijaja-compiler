package fr.amvproject.group3.mjj.ast.nodes.decls;

import org.antlr.v4.runtime.Token;

import fr.amvproject.group3.mjj.ast.AstNode;
import fr.amvproject.group3.mjj.ast.AstVisitor;
import fr.amvproject.group3.mjj.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.ExpNode;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.VarDeclNode;
import fr.amvproject.group3.mjj.ast.nodes.other.TypeNode;

public class CstNode extends VarDeclNode {
  private final Token finalToken;
  private final Token assignToken;
  private final TypeNode typeNode;
  private final ExpNode initExp;

  public CstNode(Token finalToken, TypeNode typeNode, IdentNode identNode) {
    super(identNode);
    this.finalToken = finalToken;
    this.typeNode = typeNode;
    this.assignToken = null;
    this.initExp = null;
  }

  public CstNode(Token finalToken, TypeNode typeNode, IdentNode identNode, Token assignToken, ExpNode initExp) {
    super(identNode);
    this.finalToken = finalToken;
    this.typeNode = typeNode;
    this.assignToken = assignToken;
    this.initExp = initExp;
  }

  public Token getFinalToken() {
    return finalToken;
  }

  public Token getAssignToken() {
    return assignToken;
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
