package fr.amvproject.group3.mjj.utils;

import java.util.List;

import fr.amvproject.group3.mjj.analyser.nodes.SymbolIdentity;

public class SemanticOperations {
  public static int getHashFromSymIdList(List<SymbolIdentity> listSymS) {
    int hashSum = 0;
    for (SymbolIdentity symId : listSymS) {
      hashSum += symId.getTypeNode().hashCode();
    }
    return hashSum;
  }
}
