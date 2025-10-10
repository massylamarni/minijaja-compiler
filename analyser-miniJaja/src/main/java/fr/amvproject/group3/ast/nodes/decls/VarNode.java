package fr.amvproject.group3.ast.nodes.decls;

import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.abstracts.Ident;
import fr.amvproject.group3.ast.nodes.exprs.ExpNode;
import fr.amvproject.group3.ast.nodes.other.TypeNode;
import fr.amvproject.group3.ast.nodes.program.DeclNode;

public class VarNode extends DeclNode {
    private final TypeNode type;
    private final ExpNode sizeExp;     // si tableau, expression donnant la taille (null sinon)
    private final ExpNode initExp;     // valeur initiale (null si pas d'affectation)
    private final boolean isFinal;      // true si 'final'

    // Constructeur pour variable simple
    public VarNode(TypeNode type, Ident ident) {
      super(ident);
      this.type = type;
      this.sizeExp = null;
      this.initExp = null;
      this.isFinal = false;
    }

    // Constructeur complet
    public VarNode(TypeNode type, Ident ident, ExpNode sizeExp, ExpNode initExp, boolean isFinal) {
      super(ident);
      this.type = type;
      this.sizeExp = sizeExp;
      this.initExp = initExp;
      this.isFinal = isFinal;
    }

    public TypeNode getType() {
        return type;
    }

    public ExpNode getSizeExp() {
        return sizeExp;
    }

    public ExpNode getInitExp() {
        return initExp;
    }

    public boolean isFinal() {
        return isFinal;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitVar(this);
    }
}
