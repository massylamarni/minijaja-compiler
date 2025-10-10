package fr.amvproject.group3.ast.nodes.abstracts;

import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.AstVisitor;

public class Ident extends AstNode {
    private final String name;
    
    public Ident(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitIdent(this);
    }
}