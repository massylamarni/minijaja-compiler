package fr.amvproject.group3.ast.nodes.program;

import fr.amvproject.group3.ast.*;
import java.util.List;

public class MethmainNode extends AstNode {
    private final List<AstNode> vars;
    private final List<AstNode> instrs;

    public MethmainNode(List<AstNode> vars, List<AstNode> instrs) {
        this.vars = vars;
        this.instrs = instrs;
    }

    public List<AstNode> getVars() { return vars; }
    public List<AstNode> getInstrs() { return instrs; }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitMainMethod(this);
    }
}