package fr.amvproject.group3.ast.nodes.program;

import java.util.ArrayList;
import java.util.List;


import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.AstVisitor;

public class DeclsNode extends AstNode {
    private final List<DeclNode> decls = new ArrayList<>();
    
    public void add(DeclNode d) {
        decls.add(d);
    }

    public List<DeclNode> getDecls() {
        return decls;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitDecls(this);
    }
}