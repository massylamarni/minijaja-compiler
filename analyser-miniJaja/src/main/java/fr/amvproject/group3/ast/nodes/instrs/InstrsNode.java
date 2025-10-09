package fr.amvproject.group3.ast.nodes.instrs;

import java.util.*;
import fr.amvproject.group3.ast.AstNode;

public class InstrsNode implements AstNode {
    public final java.util.List<InstrNode> list = new ArrayList<>();
    public void add(InstrNode i) { list.add(i); }

    @Override
    public String toString() {
        if (list.isEmpty()) return "(INSTRS inil)";
        StringBuilder sb = new StringBuilder("(INSTRS ");
        for (InstrNode i : list) sb.append(i).append(" ");
        sb.append(")");
        return sb.toString();
    }
}