package fr.amvproject.group3.ast.nodes.abstracts;

import fr.amvproject.group3.ast.AstNode;

public class Ident implements AstNode {
    public final String name;
    public Ident(String name) { this.name = name; }
    @Override
    public String toString() { return name; }
}