package fr.amvproject.group3.ast.ast;

import java.util.List;
import java.util.ArrayList;

public class Methode extends Decl {
    public final Ident name;
    public final List<Entete> params;
    public final Decls vars;
    public final Instrs instrs;

    public Methode(String returnType, Ident name, List<Entete> params, Decls vars, Instrs instrs) {
        super(returnType, name, null);
        this.name = name;
        this.params = params != null ? params : new ArrayList<>();
        this.vars = vars != null ? vars : new Decls();
        this.instrs = instrs != null ? instrs : new Instrs();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(METHODE ").append(type).append(" ").append(name).append(" ");
        sb.append("(ENTETES ");
        for (Entete e : params) sb.append(e).append(" ");
        sb.append(") ");
        sb.append(vars).append(" ").append(instrs).append(")");
        return sb.toString();
    }
}