package fr.amvproject.group3.ast.ast;


public class MainBlock implements ASTNode {
    public final Decls decls;
    public final Instrs instrs;

    public MainBlock(Decls decls, Instrs instrs) {
        this.decls = decls;
        this.instrs = instrs;
    }

    @Override
    public String toString() {
        return "(MAIN " + decls + " " + instrs + ")";
    }
}