package fr.amvproject.group3.ast.nodes.abstracts;

import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.AstVisitor;

public class Ident extends AstNode {
    private final String name;
    private final int line;

    public Ident(String name, int line) {
        this.name = name;
        this.line = line;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ident)) return false;
        Ident ident = (Ident) o;
        return name.equals(ident.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }
}