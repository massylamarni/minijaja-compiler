package fr.amvproject.group3.ast.nodes.abstracts;

import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.AstVisitor;

public class Ident extends AstNode {
    private final String name;
    private final int line;
    
    public Ident(String name, int line) {
        this.name = name;
        this.line = line;   // -1 is used for temp stack nodes
    }
    
    public String getName() {
        return name;
    }

    public int getLine() {
        return line;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitIdent(this);
    }
}