package fr.amvproject.group3.mjj.analyser.nodes;

import java.util.Objects;

import fr.amvproject.group3.mjj.ast.nodes.finals.IdentNode;

public class SymbolSignature {
  private final IdentNode identNode;
  private final int signatue;

  public SymbolSignature(IdentNode identNode) {
    this.identNode = identNode;
    signatue = -1;
  }

  public SymbolSignature(IdentNode identNode, int signatue) {
    this.identNode = identNode;
    this.signatue = signatue;
  }

  public IdentNode getIdentNode() {
    return identNode;
  }

  public int getSignatue() {
    return signatue;
  }

  @Override
  public int hashCode() {
    return Objects.hash(identNode, signatue);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof SymbolSignature symS) {
      return identNode.sameAs(symS.getIdentNode()) && signatue == symS.getSignatue();
    }
    return false;
  }
}
