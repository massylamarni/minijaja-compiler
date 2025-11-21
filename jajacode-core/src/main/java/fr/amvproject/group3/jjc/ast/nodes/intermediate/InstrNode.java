package fr.amvproject.group3.jjc.ast.nodes.intermediate;

import fr.amvproject.group3.jjc.ast.AstNode;

public abstract class InstrNode extends AstNode {
  protected InstrNode(int line, int column) {
    super(line, column);
  }
}
