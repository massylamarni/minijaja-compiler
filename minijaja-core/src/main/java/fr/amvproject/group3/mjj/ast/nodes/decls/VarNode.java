package fr.amvproject.group3.mjj.ast.nodes.decls;

import org.antlr.v4.runtime.Token;

import fr.amvproject.group3.mjj.ast.AstNode;
import fr.amvproject.group3.mjj.ast.AstVisitor;
import fr.amvproject.group3.mjj.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.ExpNode;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.VarDeclNode;
import fr.amvproject.group3.mjj.ast.nodes.other.TypeMethNode;

public class VarNode extends VarDeclNode {
    private final Token assignToken;
    private final TypeMethNode typeMethNode;
    private final ExpNode initExp;

    public VarNode(TypeMethNode typeMethNode, IdentNode ident) {
      super(ident);
      this.typeMethNode = typeMethNode;
      this.initExp = null;
      this.assignToken = null;
    }

    public VarNode(TypeMethNode typeMethNode, IdentNode ident, Token assignToken, ExpNode initExp) {
      super(ident);
      this.assignToken = assignToken;
      this.typeMethNode = typeMethNode;
      this.initExp = initExp;
    }

    public Token getAssignToken() {
      return assignToken;
    }

    public ExpNode getInitExp() {
        return initExp;
    }

    public TypeMethNode getType() {
      return typeMethNode;
    }

    @Override
    public boolean sameAs(AstNode other) {
      if (other instanceof VarNode varNode) {
        if (this.initExp == null || varNode.initExp == null) {
          return (this.initExp == varNode.initExp) &&
            (this.getIdent().sameAs(varNode.getIdent())) &&
            (this.typeMethNode.sameAs(varNode.typeMethNode));
        }
        return this.getIdent().sameAs(varNode.getIdent()) &&
          this.typeMethNode.sameAs(varNode.typeMethNode) &&
          this.initExp.sameAs(varNode.initExp);
      }
      return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitVar(this);
    }
}
