package fr.amvproject.group3.ast.ast;

import fr.amvproject.group3.MiniJajaParser;
import fr.amvproject.group3.MiniJajaParserBaseVisitor;
import java.util.*;

public class MiniJajaASTBuilder extends MiniJajaParserBaseVisitor<ASTNode> {

    // ========== PROGRAMME ==========
    @Override
    public ASTNode visitProgram(MiniJajaParser.ProgramContext ctx) {
        Ident className = new Ident(ctx.IDENT().getText());
        Decls decls = (Decls) visit(ctx.decls());
        MainBlock main = (MainBlock) visit(ctx.methmain());
        return new Classe(className, decls, main);
    }

    // ========== DECLARATIONS ==========
    @Override
    public ASTNode visitDecls(MiniJajaParser.DeclsContext ctx) {
        Decls decls = new Decls();

        // La grammaire est récursive : decl SEMI decls | ε
        if (ctx.decl() != null) {
            ASTNode declNode = visit(ctx.decl());
            if (declNode instanceof Decl) {
                decls.add((Decl) declNode);
            }

            // Parcourir récursivement le reste
            if (ctx.decls() != null) {
                Decls restDecls = (Decls) visit(ctx.decls());
                decls.decls.addAll(restDecls.decls);
            }
        }

        return decls;
    }

    @Override
    public ASTNode visitDecl(MiniJajaParser.DeclContext ctx) {
        if (ctx.var() != null) {
            return visit(ctx.var());
        }
        // Si c'est une méthode, on pourrait la gérer ici
        // Pour l'instant on ignore les méthodes dans les déclarations
        return null;
    }

    @Override
    public ASTNode visitVar(MiniJajaParser.VarContext ctx) {
        String type = ctx.typemeth() != null ? ctx.typemeth().getText() : ctx.type().getText();
        Ident name = new Ident(ctx.IDENT().getText());

        // Déclaration finale
        if (ctx.FINAL() != null) {
            Exp exp = ctx.vexp() != null && ctx.vexp().exp() != null
                    ? (Exp) visit(ctx.vexp().exp())
                    : null;
            // Pour final, on réutilise Decl avec le type préfixé
            return new Decl("final " + type, name, exp);
        }

        // Déclaration de tableau
        if (ctx.LBRACK() != null) {
            Exp size = (Exp) visit(ctx.exp());
            // On peut créer un Decl spécial ou utiliser Decl avec notation spéciale
            return new Decl(type + "[]", name, size);
        }

        // Déclaration simple avec initialisation optionnelle
        Exp exp = null;
        if (ctx.vexp() != null && ctx.vexp().exp() != null) {
            exp = (Exp) visit(ctx.vexp().exp());
        }

        return new Decl(type, name, exp);
    }

    // ========== MAIN ==========
    @Override
    public ASTNode visitMethmain(MiniJajaParser.MethmainContext ctx) {
        Decls vars = (Decls) visit(ctx.vars());
        Instrs instrs = (Instrs) visit(ctx.instrs());
        return new MainBlock(vars, instrs);
    }

    @Override
    public ASTNode visitVars(MiniJajaParser.VarsContext ctx) {
        Decls decls = new Decls();

        // Récursif : var SEMI vars | ε
        if (ctx.var() != null) {
            decls.add((Decl) visit(ctx.var()));

            if (ctx.vars() != null) {
                Decls restVars = (Decls) visit(ctx.vars());
                decls.decls.addAll(restVars.decls);
            }
        }

        return decls;
    }

    // ========== INSTRUCTIONS ==========
    @Override
    public ASTNode visitInstrs(MiniJajaParser.InstrsContext ctx) {
        Instrs instrs = new Instrs();

        // Récursif : instr SEMI instrs | ε
        if (ctx.instr() != null) {
            Instr instr = (Instr) visit(ctx.instr());
            if (instr != null) {
                instrs.add(instr);
            }

            if (ctx.instrs() != null) {
                Instrs restInstrs = (Instrs) visit(ctx.instrs());
                instrs.list.addAll(restInstrs.list);
            }
        }

        return instrs;
    }

    @Override
    public ASTNode visitInstr(MiniJajaParser.InstrContext ctx) {
        // Affectation : ident1 = exp
        if (ctx.ASSIGN() != null) {
            ASTNode identNode = visit(ctx.ident1());
            Exp exp = (Exp) visit(ctx.exp());

            if (identNode instanceof IdentExp) {
                return new Affectation(((IdentExp) identNode).ident, exp);
            }
            // Si c'est un accès tableau, créer une affectation spéciale
            // Pour simplifier, on utilise l'ident de base
            return new Affectation(new Ident(ctx.ident1().IDENT().getText()), exp);
        }

        // += : ident1 += exp
        if (ctx.PLUSEQ() != null) {
            Ident ident = new Ident(ctx.ident1().IDENT().getText());
            Exp exp = (Exp) visit(ctx.exp());
            // Transformer en affectation : x = x + exp
            return new Affectation(ident, new BinaryExp("+", new IdentExp(ident), exp));
        }

        // Incrémentation : ident1++
        if (ctx.INCR() != null) {
            Ident ident = new Ident(ctx.ident1().IDENT().getText());
            return new Incrementation(ident);
        }

        // Appel de méthode : IDENT(listexp)
        if (ctx.LPAREN() != null && ctx.IDENT() != null) {
            // C'est un appel de méthode (instruction)
            // Pour simplifier, on ignore les appels de méthode pour l'instant
            return null;
        }

        // Return
        if (ctx.RETURN() != null) {
            Exp exp = (Exp) visit(ctx.exp());
            // On pourrait créer une classe ReturnInstr
            // Pour l'instant, on ignore ou on crée une instruction spéciale
            return null;
        }

        // Write / Writeln
        if (ctx.WRITE() != null || ctx.WRITELN() != null) {
            // Pour simplifier, on ignore write/writeln
            return null;
        }

        // IF
        if (ctx.IF() != null) {
            Exp cond = (Exp) visit(ctx.exp());

            // Le premier bloc instrs est le THEN
            Instrs thenInstrs = (Instrs) visit(ctx.instrs(0));

            // Le deuxième bloc (si présent) est le ELSE
            Instrs elseInstrs = new Instrs();
            if (ctx.instrs().size() > 1) {
                elseInstrs = (Instrs) visit(ctx.instrs(1));
            }

            return new IfInstr(cond, thenInstrs, elseInstrs);
        }

        // WHILE
        if (ctx.WHILE() != null) {
            Exp cond = (Exp) visit(ctx.exp());
            Instrs body = (Instrs) visit(ctx.instrs(0));
            return new WhileInstr(cond, body);
        }

        return null;
    }

    // ========== EXPRESSIONS ==========
    @Override
    public ASTNode visitExp(MiniJajaParser.ExpContext ctx) {
        // exp : expAnd ( OR expAnd )*
        if (ctx.expAnd().size() == 1) {
            return visit(ctx.expAnd(0));
        }

        Exp result = (Exp) visit(ctx.expAnd(0));
        for (int i = 1; i < ctx.expAnd().size(); i++) {
            Exp right = (Exp) visit(ctx.expAnd(i));
            result = new BinaryExp("||", result, right);
        }
        return result;
    }

    @Override
    public ASTNode visitExpAnd(MiniJajaParser.ExpAndContext ctx) {
        // expAnd : expEq ( AND expEq )*
        if (ctx.expEq().size() == 1) {
            return visit(ctx.expEq(0));
        }

        Exp result = (Exp) visit(ctx.expEq(0));
        for (int i = 1; i < ctx.expEq().size(); i++) {
            Exp right = (Exp) visit(ctx.expEq(i));
            result = new BinaryExp("&&", result, right);
        }
        return result;
    }

    @Override
    public ASTNode visitExpEq(MiniJajaParser.ExpEqContext ctx) {
        // expEq : expAdd ( (EQ | GT) expAdd )*
        if (ctx.expAdd().size() == 1) {
            return visit(ctx.expAdd(0));
        }

        Exp result = (Exp) visit(ctx.expAdd(0));
        for (int i = 1; i < ctx.expAdd().size(); i++) {
            String op = ctx.EQ(i-1) != null ? "==" : ">";
            Exp right = (Exp) visit(ctx.expAdd(i));
            result = new BinaryExp(op, result, right);
        }
        return result;
    }

    @Override
    public ASTNode visitExpAdd(MiniJajaParser.ExpAddContext ctx) {
        // expAdd : MINUS expAdd | expNot ( (PLUS | MINUS) expNot )*

        // Cas unaire : -expAdd
        if (ctx.MINUS() != null && ctx.expAdd() != null) {
            Exp expr = (Exp) visit(ctx.expAdd());
            return new UnaryExp("-", expr);
        }

        // Cas binaire : expNot (+|- expNot)*
        if (ctx.expNot().size() == 1) {
            return visit(ctx.expNot(0));
        }

        Exp result = (Exp) visit(ctx.expNot(0));
        for (int i = 1; i < ctx.expNot().size(); i++) {
            String op = ctx.PLUS(i-1) != null ? "+" : "-";
            Exp right = (Exp) visit(ctx.expNot(i));
            result = new BinaryExp(op, result, right);
        }
        return result;
    }

    @Override
    public ASTNode visitExpNot(MiniJajaParser.ExpNotContext ctx) {
        // expNot : NOT expNot | terme
        if (ctx.NOT() != null) {
            Exp expr = (Exp) visit(ctx.expNot());
            return new UnaryExp("!", expr);
        }
        return visit(ctx.terme());
    }

    @Override
    public ASTNode visitTerme(MiniJajaParser.TermeContext ctx) {
        // terme : fact ( (MUL | DIV) fact )*
        if (ctx.fact().size() == 1) {
            return visit(ctx.fact(0));
        }

        Exp result = (Exp) visit(ctx.fact(0));
        for (int i = 1; i < ctx.fact().size(); i++) {
            String op = ctx.MUL(i-1) != null ? "*" : "/";
            Exp right = (Exp) visit(ctx.fact(i));
            result = new BinaryExp(op, result, right);
        }
        return result;
    }

    @Override
    public ASTNode visitFact(MiniJajaParser.FactContext ctx) {
        // Littéraux
        if (ctx.INT() != null) {
            return new IntExp(Integer.parseInt(ctx.INT().getText()));
        }
        if (ctx.TRUE() != null) {
            return new BoolExp(true);
        }
        if (ctx.FALSE() != null) {
            return new BoolExp(false);
        }

        // Chaîne de caractères
        if (ctx.STRING() != null) {
            String str = ctx.STRING().getText();
            // On retourne un IntExp pour l'instant (à adapter selon ton AST)
            // Ou créer une StringExp si nécessaire
            return new IntExp(0); // Placeholder
        }

        // Identifiant ou accès tableau
        if (ctx.ident1() != null) {
            return visit(ctx.ident1());
        }

        // Expression entre parenthèses
        if (ctx.LPAREN() != null && ctx.exp() != null) {
            return visit(ctx.exp());
        }

        // Appel de méthode : IDENT(listexp)
        if (ctx.LPAREN() != null && ctx.IDENT() != null) {
            // Appel de méthode en tant qu'expression
            // Pour simplifier, on retourne une valeur par défaut
            return new IntExp(0); // Placeholder
        }

        // LENGTH(IDENT)
        if (ctx.LENGHT() != null) {
            // Pour simplifier, on retourne une valeur par défaut
            return new IntExp(0); // Placeholder
        }

        return new IntExp(0); // Fallback
    }

    @Override
    public ASTNode visitIdent1(MiniJajaParser.Ident1Context ctx) {
        Ident ident = new Ident(ctx.IDENT().getText());

        // Accès tableau : IDENT[exp]
        if (ctx.LBRACK() != null) {
            Exp index = (Exp) visit(ctx.exp());
            // Pour simplifier, on retourne juste l'IdentExp
            // Ou créer une classe ArrayAccessExp si nécessaire
            return new IdentExp(ident);
        }

        // Identifiant simple
        return new IdentExp(ident);
    }
}