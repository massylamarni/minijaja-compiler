package fr.amvproject.group3.ast.nodes.instrs;

import fr.amvproject.group3.ast.nodes.abstracts.Ident;
import fr.amvproject.group3.ast.nodes.exprs.ArrayAccessNode;
import fr.amvproject.group3.ast.nodes.exprs.ExpNode;
import fr.amvproject.group3.ast.nodes.exprs.VarRefNode;
import fr.amvproject.group3.ast.nodes.other.LeftValueNode;

public class AssignNode extends InstrNode {

    private LeftValueNode target;   // Variable ou case de tableau
    private ExpNode exp;           // Expression assignée

    public AssignNode(LeftValueNode target, ExpNode exp) {
        this.target = target;
        this.exp = exp;
    }

    public LeftValueNode getTarget() {
        return target;
    }

    public Ident getIdent() {
        if (target instanceof VarRefNode) {
            VarRefNode varRef = (VarRefNode) target;
            return varRef.getIdent();
        }
        return ((ArrayAccessNode) target).getIdent();
    }

    public ExpNode getIndex() {
        if (target instanceof ArrayAccessNode) {
            ArrayAccessNode arrAccess = (ArrayAccessNode) target;
            return arrAccess.getIndexExpr();
        }
        return null; // Pas un accès tableau ou index non constant
    }

    public ExpNode getExp() {
        return exp;
    }

    @Override
    public String toString() {
        return target + " = " + exp;
    }
}
