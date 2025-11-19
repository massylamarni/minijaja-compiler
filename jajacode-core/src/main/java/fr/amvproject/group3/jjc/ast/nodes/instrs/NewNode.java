package fr.amvproject.group3.jjc.ast.nodes.instrs;

import org.antlr.v4.runtime.Token;

import fr.amvproject.group3.jjc.ast.AstNode;
import fr.amvproject.group3.jjc.ast.AstVisitor;
import fr.amvproject.group3.jjc.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.jjc.ast.nodes.intermediate.InstrNode;
import fr.amvproject.group3.jjc.ast.nodes.intermediate.ValueNode;
import fr.amvproject.group3.jjc.ast.nodes.other.ObjNode;
import fr.amvproject.group3.jjc.ast.nodes.other.TypeNode;

public class NewNode extends InstrNode {
    private final Token newToken;
    private final IdentNode identNode;
    private final TypeNode typeNode;
    private final ObjNode objNode;
    private final ValueNode valueNode;

    public NewNode(Token newToken, IdentNode identNode, TypeNode typeNode, ObjNode objNode, ValueNode valueNode) {
        super(newToken.getLine(), newToken.getCharPositionInLine());
        this.newToken = newToken;
        this.identNode = identNode;
        this.typeNode = typeNode;
        this.objNode = objNode;
        this.valueNode = valueNode;
    }

    public Token getNewToken() {
      return newToken;
    }

    public IdentNode getIdent() {
        return identNode;
    }

    public TypeNode getType() {
        return typeNode;
    }

    public ObjNode getObj() {
        return objNode;
    }

    public ValueNode getValue() {
        return valueNode;
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof NewNode newNode) {
            return this.identNode.sameAs(newNode.identNode) &&
                this.typeNode.sameAs(newNode.typeNode) &&
                this.objNode.sameAs(newNode.objNode) &&
                this.valueNode.sameAs(newNode.valueNode);
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitNew(this);
    }
}
