package fr.amvproject.group3.ast.nodes.program;

import fr.amvproject.group3.ast.*;
import java.util.List;

public class ProgramNode extends AstNode {
    private final String className;
    private final List<AstNode> decls;
    private final MethmainNode main;

    public ProgramNode(String className, List<AstNode> decls, MethmainNode main) {
        this.className = className;
        this.decls = decls;
        this.main = main;
    }

    public String getClassName() { return className; }
    public List<AstNode> getDecls() { return decls; }
    public MethmainNode getMain() { return main; }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitProgram(this);
    }
}
