package fr.amvproject.group3.mjj.ast.nodes.instrs;

import fr.amvproject.group3.mjj.ast.AstNode;
import fr.amvproject.group3.mjj.ast.AstVisitor;
import fr.amvproject.group3.mjj.ast.nodes.abstracts.ListExpNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.InstrNode;

public class CallInstrNode extends InstrNode {
    private final IdentNode identNode;
    private final ListExpNode listExpNode;

    public CallInstrNode(IdentNode identNode, ListExpNode listExpNode) {
        super(identNode.getLine(), identNode.getLine());
        this.identNode = identNode;
        this.listExpNode = listExpNode;
    }

    public IdentNode getIdent() {
        return identNode;
    }

    public ListExpNode getListExp() {
        return listExpNode;
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof CallInstrNode callInstrNode) {
            return this.identNode.sameAs(callInstrNode.identNode) && this.listExpNode.sameAs(callInstrNode.listExpNode);
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitCallInstr(this);
    }
}
