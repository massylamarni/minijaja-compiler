package fr.amvproject.group3.ast.ast;

import java.util.*;

public class Instrs implements ASTNode {
    public final java.util.List<Instr> list = new ArrayList<>();
    public void add(Instr i) { list.add(i); }

    @Override
    public String toString() {
        if (list.isEmpty()) return "(INSTRS inil)";
        StringBuilder sb = new StringBuilder("(INSTRS ");
        for (Instr i : list) sb.append(i).append(" ");
        sb.append(")");
        return sb.toString();
    }
}