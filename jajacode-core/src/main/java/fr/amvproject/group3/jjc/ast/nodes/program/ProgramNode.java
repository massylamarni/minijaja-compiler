package fr.amvproject.group3.jjc.ast.nodes.program;

import fr.amvproject.group3.jjc.ast.AstNode;
import fr.amvproject.group3.jjc.ast.AstVisitor;
import fr.amvproject.group3.jjc.utils.NodeOperations.MockToken;

import java.util.List;

import org.antlr.v4.runtime.Token;

public class ProgramNode extends AstNode {
    private final List<ClassNode> list;
    private final Token eofToken;

    public ProgramNode(List<ClassNode> list, Token eofToken) {
        this.list = list;
        this.eofToken = eofToken;
    }

    public ProgramNode(List<ClassNode> list) {
        this.list = list;
        this.eofToken = new MockToken(null, 0, 0);
    }

    public Token getEofToken() {
      return eofToken;
    }

    public List<ClassNode> getClasses() {
        return list;
    }

    @Override
    public boolean sameAs(AstNode other) {
        if (other instanceof ProgramNode programNode && this.list.size() == programNode.list.size()) {
            for (int i = 0; i < this.list.size(); i++) {
                if (!this.list.get(i).sameAs(programNode.list.get(i))) return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitProgram(this);
    }
}
