package fr.amvproject.group3.ast;

import java.util.ArrayList;
import java.util.List;

import fr.amvproject.group3.MiniJajaParser;
import fr.amvproject.group3.MiniJajaParserBaseVisitor;
import fr.amvproject.group3.ast.nodes.abstracts.*;
import fr.amvproject.group3.ast.nodes.decls.*;
import fr.amvproject.group3.ast.nodes.exprs.*;
import fr.amvproject.group3.ast.nodes.instrs.*;
import fr.amvproject.group3.ast.nodes.other.*;
import fr.amvproject.group3.ast.nodes.program.*;

public class MiniJajaAstBuilder extends MiniJajaParserBaseVisitor<AstNode> {

    // ========== PROGRAMME ==========
    @Override
    public AstNode visitProgram(MiniJajaParser.ProgramContext ctx) {
        List<ClasseNode> classes = new ArrayList<>();
        for (MiniJajaParser.ClasseContext classeCtx : ctx.classe()) {
            classes.add((ClasseNode) visitClasse(classeCtx));
        }
        return new ProgramNode(classes);
    }


    @Override
    public AstNode visitClasse(MiniJajaParser.ClasseContext ctx) {
        Ident ident = new Ident(ctx.ident().getText());
        DeclsNode decls = (DeclsNode) visit(ctx.decls());
        MethMainNode main = (MethMainNode) visit(ctx.methmain());
        return new ClasseNode(ident, decls, main);
    }

    // ========== DECLARATIONS ==========
    @Override
    public AstNode visitDecls(MiniJajaParser.DeclsContext ctx) {
        DeclsNode decls = new DeclsNode();

        for (MiniJajaParser.DeclContext declCtx : ctx.decl()) {
            AstNode declNode = visit(declCtx);
            if (declNode instanceof DeclNode) {
                decls.add((DeclNode) declNode);
            }
        }

        return decls;
    }


    @Override
    public AstNode visitDecl(MiniJajaParser.DeclContext ctx) {
        if (ctx.var() != null) {
            return visit(ctx.var());
        }
        
        return visit(ctx.methode());
    }

    @Override 
    public AstNode visitVar(MiniJajaParser.VarContext ctx) {
        ExpNode exp = (ctx.vexp() != null && ctx.vexp().exp() != null) ? (ExpNode) visit(ctx.vexp().exp()) : null;
        Boolean isFinal = (ctx.FINAL() != null);

        // Array, grammar does not allow to init arrays
        if (ctx.LBRACK() != null) {
            // TODO: Handle case where exp is defined
            return new VarNode(new TypeNode(ctx.typemeth().getText()), new Ident(ctx.ident().getText()), (ExpNode) visit(ctx.exp()), null, false);
        }

        // Var or Final
        return new VarNode(new TypeNode(isFinal ? ctx.type().getText() : ctx.typemeth().getText()), new Ident(ctx.ident().getText()), null, exp, isFinal);
    }

    // ========== MAIN ==========
    @Override
    public AstNode visitMethmain(MiniJajaParser.MethmainContext ctx) {
        return new MethMainNode((VarsNode) visit(ctx.vars()), (InstrsNode) visit(ctx.instrs()));
    }

    @Override
    public AstNode visitVars(MiniJajaParser.VarsContext ctx) {
        VarsNode vars = new VarsNode();

        for (MiniJajaParser.VarContext varCtx : ctx.var()) {
            AstNode varNode = visit(varCtx);
            if (varNode instanceof VarNode) {
                vars.add((VarNode) varNode);
            }
        }

        return vars;
    }

    // ========== INSTRUCTIONS ==========
    @Override
    public AstNode visitInstrs(MiniJajaParser.InstrsContext ctx) {
        InstrsNode instrs = new InstrsNode();

        for (MiniJajaParser.InstrContext instrCtx : ctx.instr()) {
            InstrNode instr = (InstrNode) visit(instrCtx);
            if (instr != null) {
                instrs.add(instr);
            }
        }

        return instrs;
    }

    @Override
    public AstNode visitInstr(MiniJajaParser.InstrContext ctx) {
        // = : ident1 = exp
        if (ctx.ASSIGN() != null) {
            AstNode leftValueNode = visit(ctx.ident1());
            ExpNode exp = (ExpNode) visit(ctx.exp());

            if (leftValueNode instanceof VarRefNode) { // Var
                return new AssignNode(new VarRefNode(((VarRefNode) leftValueNode).getIdent()), exp);
            } else if (leftValueNode instanceof ArrayAccessNode) { // Array
                return new AssignNode(new ArrayAccessNode(((ArrayAccessNode) leftValueNode).getIdent(), ((ArrayAccessNode) leftValueNode).getIndexExp()), exp);
            } else {
                throw new RuntimeException("Unexpected left-hand side in =: " + leftValueNode);
            }            
        }

        // += : ident1 += exp
        if (ctx.ADDASSIGN() != null) {
            AstNode leftValueNode = visit(ctx.ident1());
            ExpNode exp = (ExpNode) visit(ctx.exp());

            if (leftValueNode instanceof VarRefNode) {
                return new AddAssignNode(new VarRefNode(((VarRefNode) leftValueNode).getIdent()), exp);
            } else {
                return new AddAssignNode(new ArrayAccessNode(((ArrayAccessNode) leftValueNode).getIdent(), ((ArrayAccessNode) leftValueNode).getIndexExp()), exp);
            } // Other checks are done internally
        }


        // ++ : ident1++
        if (ctx.INC() != null) {
            return new IncNode(new Ident(ctx.ident1().ident().getText()));
        }

        // MethodCallExp : IDENT(listexp)
        if (ctx.LPAREN() != null && ctx.ident() != null) {
            Ident ident = new Ident(ctx.ident().getText());
            ListExpNode listExp = (ListExpNode) visit(ctx.listexp());
            return new MethodCallExpNode(ident, listExp);
        }

        // Return
        if (ctx.RETURN() != null) {
            ExpNode exp = (ExpNode) visit(ctx.exp());
            return new RetourNode(exp);
        }

        // Write / Writeln
        if (ctx.WRITE() != null || ctx.WRITELN() != null) {
            boolean isLn = ctx.WRITELN() != null;
            ExpNode exp = null;

            // Ident1
            if (ctx.ident1() != null) {
                exp = (ExpNode) visit(ctx.ident1());
            }
            // String
            else if (ctx.STRING() != null) {
                String str = ctx.STRING().getText();
                str = str.substring(1, str.length() - 1);
                exp = new StringLiteralNode(str);
            }

            return new WriteNode(exp, isLn);
        }

        // IF
        if (ctx.IF() != null) {
            ExpNode condExp = (ExpNode) visit(ctx.exp());

            // Then
            InstrsNode thenInstrs = (InstrsNode) visit(ctx.instrs(0));

            // Else
            InstrsNode elseInstrs = (ctx.instrs().size() > 1) ? (InstrsNode) visit(ctx.instrs(1)) : null;

            return new IfNode(condExp, thenInstrs, elseInstrs);
        }

        // WHILE
        if (ctx.WHILE() != null) {
            return new WhileNode((ExpNode) visit(ctx.exp()), (InstrsNode) visit(ctx.instrs(0)));
        }

        return null;
    }

    // ========== EXPRESSIONS ==========
    @Override
    public AstNode visitExp(MiniJajaParser.ExpContext ctx) {
        return visit(ctx.expOr());
    }

    public AstNode visitExpOr(MiniJajaParser.ExpOrContext ctx) {
        // expOr : expAnd ( OR expAnd )*
        if (ctx.expAnd().size() == 1) {
            return visit(ctx.expAnd(0));
        }

        ExpNode result = (ExpNode) visit(ctx.expAnd(0));
        for (int i = 1; i < ctx.expAnd().size(); i++) {
            ExpNode right = (ExpNode) visit(ctx.expAnd(i));
            result = new BinaryExpNode(new BinaryOpNode("||"), result, right);
        }
        return result;
    }

    @Override
    public AstNode visitExpAnd(MiniJajaParser.ExpAndContext ctx) {
        // expAnd : expEq ( AND expEq )*
        if (ctx.expEq().size() == 1) {
            return visit(ctx.expEq(0));
        }

        ExpNode result = (ExpNode) visit(ctx.expEq(0));
        for (int i = 1; i < ctx.expEq().size(); i++) {
            ExpNode right = (ExpNode) visit(ctx.expEq(i));
            result = new BinaryExpNode(new BinaryOpNode("&&"), result, right);
        }
        return result;
    }

    @Override
    public AstNode visitExpEq(MiniJajaParser.ExpEqContext ctx) {
        // expEq : expRel ( (EQ | GT) expRel )*
        if (ctx.expRel().size() == 1) {
            return visit(ctx.expRel(0));
        }

        ExpNode result = (ExpNode) visit(ctx.expRel(0));
        for (int i = 1; i < ctx.expRel().size(); i++) {
            String op = ctx.EQ(i-1) != null ? "==" : ">";
            ExpNode right = (ExpNode) visit(ctx.expRel(i));
            result = new BinaryExpNode(new BinaryOpNode(op), result, right);
        }
        return result;
    }

    public AstNode visitExpRel(MiniJajaParser.ExpRelContext ctx) {
        return visit(ctx.expAdd());
    }

    @Override
    public AstNode visitExpAdd(MiniJajaParser.ExpAddContext ctx) {
        // expAdd : expMul ( (ADD | SUB) expMul )*
        if (ctx.expMul().size() == 1) {
            return visit(ctx.expMul(0));
        }

        ExpNode result = (ExpNode) visit(ctx.expMul(0));
        for (int i = 1; i < ctx.expMul().size(); i++) {
            String op = ctx.ADD(i-1) != null ? "+" : "-";
            ExpNode right = (ExpNode) visit(ctx.expMul(i));
            result = new BinaryExpNode(new BinaryOpNode(op), result, right);
        }
        return result;
    }

    @Override
    public AstNode visitExpMul(MiniJajaParser.ExpMulContext ctx) {
        // expMul: expUnary ((MUL | DIV) expUnary)*;
        if (ctx.expUnary().size() == 1) {
            return visit(ctx.expUnary(0));
        }

        ExpNode result = (ExpNode) visit(ctx.expUnary(0));
        for (int i = 1; i < ctx.expUnary().size(); i++) {
            String op = ctx.MUL(i-1) != null ? "*" : "/";
            ExpNode right = (ExpNode) visit(ctx.expUnary(i));
            result = new BinaryExpNode(new BinaryOpNode(op), result, right);
        }
        return result;
    }

    @Override
    public AstNode visitExpUnary(MiniJajaParser.ExpUnaryContext ctx) {
        // expUnary: (NOT | SUB) expUnary | fact;
        if (ctx.NOT() != null) {
            ExpNode right = (ExpNode) visit(ctx.expUnary());
            return new UnaryExpNode(new UnaryOpNode("!"), right);
        } else if (ctx.SUB() != null) {
            ExpNode right = (ExpNode) visit(ctx.expUnary());
            return new UnaryExpNode(new UnaryOpNode("-"), right);
        } else {
            return visit(ctx.fact());
        }
    }

    
    @Override
    public AstNode visitFact(MiniJajaParser.FactContext ctx) {
        // Literals
        if (ctx.NOMBRE() != null) {
            return new IntLiteralNode(Integer.parseInt(ctx.NOMBRE().getText()));
        }
        if (ctx.TRUE() != null) {
            return new BoolLiteralNode(true);
        }
        if (ctx.FALSE() != null) {
            return new BoolLiteralNode(false);
        }

        // Ident1
        if (ctx.ident1() != null) {
            return visit(ctx.ident1());
        }

        // Ident(listexp)
        if (ctx.ident() != null && ctx.LPAREN() != null && ctx.exp() != null) {
            return new MethodCallExpNode(new Ident(ctx.ident().getText()), (ListExpNode) visit(ctx.listexp()));
        }

        // Length(ident)
        if (ctx.LENGTH() != null && ctx.ident() != null) {
            return new LengthNode(new Ident(ctx.ident().getText()));
        }

        // (exp)
        if (ctx.exp() != null) {
            return visit(ctx.exp());
        }

        return new IntLiteralNode(0); // Fallback TODO: explanation
    }

    @Override
    public AstNode visitIdent1(MiniJajaParser.Ident1Context ctx) {
        // Array
        if (ctx.LBRACK() != null) {
            
            return new ArrayAccessNode(new Ident(ctx.ident().getText()), (ExpNode) visit(ctx.exp()));
        }

        // Var
        return new VarRefNode(new Ident(ctx.ident().getText()));
    }
}