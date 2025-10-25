package fr.amvproject.group3.ast.nodes.instrs;

import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.ast.nodes.intermediate.InstrNode;

public class IncNode extends InstrNode {
    private final IdentNode ident;
    
    public IncNode(IdentNode ident) {
        this.ident = ident;
    }

    public IdentNode getIdent() {
        return ident;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitInc(this);
    }
}
