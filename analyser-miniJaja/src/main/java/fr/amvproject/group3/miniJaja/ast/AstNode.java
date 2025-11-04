package fr.amvproject.group3.miniJaja.ast;

public abstract class AstNode {
    public abstract <T> T accept(AstVisitor<T> visitor);
    public abstract boolean sameAs(AstNode other);
}
