package fr.amvproject.group3.ast.nodes.decls;

import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.ast.nodes.intermediate.ExpNode;
import fr.amvproject.group3.ast.nodes.intermediate.VarDeclNode;
import fr.amvproject.group3.ast.nodes.other.TypeMethNode;

public class VarNode extends VarDeclNode {
    private final ExpNode initExp;
    private final TypeMethNode typeMethNode;

    public VarNode(TypeMethNode typeMethNode, IdentNode ident) {
      super(ident);
      this.typeMethNode = typeMethNode;
      this.initExp = null;
    }

    public VarNode(TypeMethNode typeMethNode, IdentNode ident, ExpNode initExp) {
      super(ident);
      this.typeMethNode = typeMethNode;
      this.initExp = initExp;
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
