package fr.amvproject.group3.ast.ast;

public class Ident implements ASTNode {
    public final String name;
    public Ident(String name) { this.name = name; }
    @Override
    public String toString() { return name; }
}