package fr.amvproject.group3.ast.nodes.instrs;

import fr.amvproject.group3.ast.AstVisitor;
import fr.amvproject.group3.ast.nodes.abstracts.Ident;

public class IncNode extends InstrNode {
    private final Ident ident;
    
    public IncNode(Ident ident) {
        this.ident = ident;
    }

    public Ident getIdent() {
        return ident;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitInc(this);
    }
}
