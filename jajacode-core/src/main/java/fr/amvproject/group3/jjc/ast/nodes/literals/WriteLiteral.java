package fr.amvproject.group3.jjc.ast.nodes.literals;

import org.antlr.v4.runtime.Token;

import fr.amvproject.group3.jjc.ast.AstNode;
import fr.amvproject.group3.jjc.ast.AstVisitor;
import fr.amvproject.group3.jjc.ast.nodes.intermediate.InstrNode;
import fr.amvproject.group3.jjc.utils.NodeOperations.MockToken;

public class WriteLiteral extends InstrNode {
  private final Token writeToken;
  private final boolean isLn;

  public WriteLiteral(Token writeToken, boolean isLn) {
    super(writeToken.getLine(), writeToken.getCharPositionInLine());
    this.writeToken = writeToken;
    this.isLn = isLn;
  }

  public WriteLiteral(boolean isLn) {
        super(0, 0);
        writeToken = new MockToken(isLn ? "writeln" : "write", 0, 0);
        this.isLn = isLn;
    }

  public Token getWriteToken() {
    return writeToken;
  }

  public boolean isLn() {
    return isLn;
  }

  @Override
  public boolean sameAs(AstNode other) {
    return other instanceof WriteLiteral;
  }

  @Override
  public <T> T accept(AstVisitor<T> visitor) {
    return visitor.visitWrite(this);
  }
}
