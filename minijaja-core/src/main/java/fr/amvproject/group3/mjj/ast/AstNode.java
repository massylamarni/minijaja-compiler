package fr.amvproject.group3.mjj.ast;


public abstract class AstNode {
    public abstract <T> T accept(AstVisitor<T> visitor);
    public abstract boolean sameAs(AstNode other);
}
