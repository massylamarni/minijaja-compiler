package fr.amvproject.group3.mjj.ast.nodes.intermediate;

import fr.amvproject.group3.mjj.ast.AstNode;

public abstract class InstrNode extends AstNode {
  protected InstrNode(int line, int column) {
    super(line, column);
  }
}
