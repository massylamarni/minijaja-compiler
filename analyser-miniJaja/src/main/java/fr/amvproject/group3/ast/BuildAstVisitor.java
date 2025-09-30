package fr.amvproject.group3.ast;

import fr.amvproject.group3.*;
import fr.amvproject.group3.ast.nodes.program.*;
import fr.amvproject.group3.ast.nodes.decls.*;
import fr.amvproject.group3.ast.nodes.instrs.*;
import fr.amvproject.group3.ast.nodes.exprs.*;

import java.util.ArrayList;
import java.util.List;

public class BuildAstVisitor extends MiniJajaParserBaseVisitor<AstNode> {

    // -------- Program --------
    @Override
    public AstNode visitProgram(MiniJajaParser.ProgramContext ctx) {
        String className = ctx.IDENT().getText();
        List<AstNode> decls = new ArrayList<>();

        if (ctx.decls() != null) {
            decls = buildDecls(ctx.decls());
        }

        MethmainNode main = (MethmainNode) visit(ctx.methmain());
        return new ProgramNode(className, decls, main);
    }

    private List<AstNode> buildDecls(MiniJajaParser.DeclsContext ctx) {
        List<AstNode> decls = new ArrayList<>();
        if (ctx.decl() != null) {
            decls.add(visit(ctx.decl()));
        }
        if (ctx.decls() != null) {
            decls.addAll(buildDecls(ctx.decls()));
        }
        return decls;
    }

    // -------- Main --------
    @Override
    public AstNode visitMethmain(MiniJajaParser.MethmainContext ctx) {
        List<AstNode> vars = new ArrayList<>();
        List<AstNode> instrs = new ArrayList<>();

        if (ctx.vars() != null) vars = buildVars(ctx.vars());
        if (ctx.instrs() != null) instrs = buildInstrs(ctx.instrs());

        return new MethmainNode(vars, instrs);
    }

    private List<AstNode> buildVars(MiniJajaParser.VarsContext ctx) {
        List<AstNode> vars = new ArrayList<>();
        if (ctx.var() != null) {
            vars.add(visit(ctx.var()));
        }
        if (ctx.vars() != null) {
            vars.addAll(buildVars(ctx.vars()));
        }
        return vars;
    }

    private List<AstNode> buildInstrs(MiniJajaParser.InstrsContext ctx) {
        List<AstNode> instrs = new ArrayList<>();
        if (ctx.instr() != null) {
            instrs.add(visit(ctx.instr()));
        }
        if (ctx.instrs() != null) {
            instrs.addAll(buildInstrs(ctx.instrs()));
        }
        return instrs;
    }

    // -------- Variables --------
    @Override
    public AstNode visitVar(MiniJajaParser.VarContext ctx) {
        String type = ctx.typemeth().getText();
        String name = ctx.IDENT().getText();

        AstNode init = null;
        if (ctx.vexp() != null && ctx.vexp().exp() != null) {
            init = visit(ctx.vexp().exp());
        }

        return new VarDeclNode(type, name, init);
    }

    // -------- Instructions --------
    @Override
    public AstNode visitInstr(MiniJajaParser.InstrContext ctx) {
        if (ctx.ASSIGN() != null) {
            String ident = ctx.ident1().getText();
            AstNode expr = visit(ctx.exp());
            return new AssignNode(ident, expr);
        }

        if (ctx.WHILE() != null) {
            AstNode cond = visit(ctx.exp());
            List<AstNode> body = buildInstrs(ctx.instrs(0));
            return new WhileNode(cond, body);
        }

        // TODO: gérer RETURN, IF, WRITE, appel de méthode, etc.
        return null;
    }

    // -------- Expressions --------
    @Override
    public AstNode visitExpAdd(MiniJajaParser.ExpAddContext ctx) {
        if (ctx.expAdd() != null) {
            // cas : -expAdd
            return new BinaryOpNode("-", new LiteralNode(0), visit(ctx.expAdd()));
        }
        if (ctx.expNot().size() == 1) {
            return visit(ctx.expNot(0));
        }

        AstNode left = visit(ctx.expNot(0));
        String op = ctx.PLUS() != null ? "+" : "-";
        AstNode right = visit(ctx.expNot(1));

        return new BinaryOpNode(op, left, right);
    }

    @Override
    public AstNode visitFact(MiniJajaParser.FactContext ctx) {
        if (ctx.INT() != null) {
            return new LiteralNode(Integer.parseInt(ctx.INT().getText()));
        }
        if (ctx.TRUE() != null) {
            return new LiteralNode(true);
        }
        if (ctx.FALSE() != null) {
            return new LiteralNode(false);
        }
        if (ctx.STRING() != null) {
            String s = ctx.STRING().getText().replace("\"", "");
            return new LiteralNode(s);
        }
        if (ctx.exp() != null) {
            return visit(ctx.exp());
        }
        return null; // TODO: gérer ident1, appels, etc.
    }
}
