package fr.amvproject.group3.ast.nodes.decls;

import fr.amvproject.group3.ast.nodes.abstracts.Ident;
import fr.amvproject.group3.ast.nodes.exprs.ExpNode;
import fr.amvproject.group3.ast.nodes.other.TypeNode;
import fr.amvproject.group3.ast.nodes.program.DeclNode;

public class VarNode extends DeclNode {
    private TypeNode type;
    private ExpNode sizeExpr;     // si tableau, expression donnant la taille (null sinon)
    private ExpNode initExpr;     // valeur initiale (null si pas d'affectation)
    private boolean isFinal;      // true si 'final'

    // Constructeur pour variable simple
    public VarNode(TypeNode type, Ident ident) {
      super(ident);
      this.type = type;
      this.sizeExpr = null;
      this.initExpr = null;
      this.isFinal = false;
    }

    // Constructeur complet
    public VarNode(TypeNode type, Ident ident, ExpNode sizeExpr, ExpNode initExpr, boolean isFinal) {
      super(ident);
      this.type = type;
      this.sizeExpr = sizeExpr;
      this.initExpr = initExpr;
      this.isFinal = isFinal;
    }

    public TypeNode getType() {
        return type;
    }

    public Ident getIdent() {
        return ident;
    }

    public ExpNode getSizeExpr() {
        return sizeExpr;
    }

    public ExpNode getInitExpr() {
        return initExpr;
    }

    public boolean isFinal() {
        return isFinal;
    }

    @Override
    public String toString() {
        String s = "";
        if (isFinal) s += "final ";
        s += type + " " + ident;
        if (sizeExpr != null) s += "[" + sizeExpr + "]";
        if (initExpr != null) s += " = " + initExpr;
        return s;
    }
}
