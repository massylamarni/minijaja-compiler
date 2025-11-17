package fr.amvproject.group3.mjj.ast.nodes.finals;

import fr.amvproject.group3.mjj.ast.AstNode;
import fr.amvproject.group3.mjj.ast.AstVisitor;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.LeftValueNode;

public class IdentNode extends LeftValueNode {
    private final String name;
    private final int line;

    public IdentNode(String name, int line) {
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
    public String evaluate() {
        return name;
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof IdentNode identNode) {
            return this.name.equals(identNode.name);
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || !(obj instanceof IdentNode)) return false;
        IdentNode other = (IdentNode) obj;
        return this.name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitIdent(this);
    }
}
