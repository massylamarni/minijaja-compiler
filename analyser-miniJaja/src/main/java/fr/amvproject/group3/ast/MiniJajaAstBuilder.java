package fr.amvproject.group3.ast;

import fr.amvproject.group3.MiniJajaParser;
import fr.amvproject.group3.MiniJajaParserBaseVisitor;
import fr.amvproject.group3.ast.nodes.abstracts.Ident;
import fr.amvproject.group3.ast.nodes.abstracts.IdentExp;
import fr.amvproject.group3.ast.nodes.decls.MethMainNode;
import fr.amvproject.group3.ast.nodes.decls.VarNode;
import fr.amvproject.group3.ast.nodes.exprs.BinaryExpNode;
import fr.amvproject.group3.ast.nodes.exprs.BoolLiteralNode;
import fr.amvproject.group3.ast.nodes.exprs.ExpNode;
import fr.amvproject.group3.ast.nodes.exprs.IntLiteralNode;
import fr.amvproject.group3.ast.nodes.exprs.StringLiteralNode;
import fr.amvproject.group3.ast.nodes.exprs.UnaryExpNode;
import fr.amvproject.group3.ast.nodes.exprs.VarRefNode;
import fr.amvproject.group3.ast.nodes.instrs.AssignNode;
import fr.amvproject.group3.ast.nodes.instrs.IfNode;
import fr.amvproject.group3.ast.nodes.instrs.IncNode;
import fr.amvproject.group3.ast.nodes.instrs.InstrNode;
import fr.amvproject.group3.ast.nodes.instrs.InstrsNode;
import fr.amvproject.group3.ast.nodes.instrs.WhileNode;
import fr.amvproject.group3.ast.nodes.instrs.WriteNode;
import fr.amvproject.group3.ast.nodes.other.LeftValueNode;
import fr.amvproject.group3.ast.nodes.other.TypeNode;
import fr.amvproject.group3.ast.nodes.program.ClasseNode;
import fr.amvproject.group3.ast.nodes.program.DeclNode;
import fr.amvproject.group3.ast.nodes.program.DeclsNode;

public class MiniJajaAstBuilder extends MiniJajaParserBaseVisitor<AstNode> {

    // ========== PROGRAMME ==========
    @Override
    public AstNode visitProgram(MiniJajaParser.ProgramContext ctx) {
        Ident className = new Ident(ctx.IDENT().getText());
        DeclsNode decls = (DeclsNode) visit(ctx.decls());
        MethMainNode main = (MethMainNode) visit(ctx.methmain());
        return new ClasseNode(className, decls, main);
    }

    // ========== DECLARATIONS ==========
    @Override
    public AstNode visitDecls(MiniJajaParser.DeclsContext ctx) {
        DeclsNode decls = new DeclsNode();

        // La grammaire est récursive : decl SEMI decls | ε
        if (ctx.decl() != null) {
            AstNode declNode = visit(ctx.decl());
            if (declNode instanceof DeclNode) {
                decls.add((DeclNode) declNode);
            }

            // Parcourir récursivement le reste
            if (ctx.decls() != null) {
                DeclsNode restDecls = (DeclsNode) visit(ctx.decls());
                decls.decls.addAll(restDecls.decls);
            }
        }

        return decls;
    }

    @Override
    public AstNode visitDecl(MiniJajaParser.DeclContext ctx) {
        if (ctx.var() != null) {
            return visit(ctx.var());
        }
        // Si c'est une méthode, on pourrait la gérer ici
        // Pour l'instant on ignore les méthodes dans les déclarations
        return null;
    }

    @Override
    public AstNode visitVar(MiniJajaParser.VarContext ctx) {
        String type = ctx.typemeth() != null ? ctx.typemeth().getText() : ctx.type().getText();
        Ident name = new Ident(ctx.IDENT().getText());

        // Déclaration finale
        if (ctx.FINAL() != null) {
            ExpNode exp = ctx.vexp() != null && ctx.vexp().exp() != null
                    ? (ExpNode) visit(ctx.vexp().exp())
                    : null;
            // Pour final, on réutilise Decl avec le type préfixé
            return new VarNode(new TypeNode(type), name, null, exp, true);
        }

        // Déclaration de tableau
        if (ctx.LBRACK() != null) {
            ExpNode size = (ExpNode) visit(ctx.exp());
            // On peut créer un Decl spécial ou utiliser Decl avec notation spéciale
            return new VarNode(new TypeNode(type), name, size, null, false);
        }

        // Déclaration simple avec initialisation optionnelle
        ExpNode exp = null;
        if (ctx.vexp() != null && ctx.vexp().exp() != null) {
            exp = (ExpNode) visit(ctx.vexp().exp());
        }

        return new VarNode(new TypeNode(type), name, null, exp, false);
    }

    // ========== MAIN ==========
    @Override
    public AstNode visitMethmain(MiniJajaParser.MethmainContext ctx) {
        DeclsNode vars = (DeclsNode) visit(ctx.vars());
        InstrsNode instrs = (InstrsNode) visit(ctx.instrs());
        return new MethMainNode(vars, instrs);
    }

    @Override
    public AstNode visitVars(MiniJajaParser.VarsContext ctx) {
        DeclsNode decls = new DeclsNode();

        // Récursif : var SEMI vars | ε
        if (ctx.var() != null) {
            decls.add((DeclNode) visit(ctx.var()));

            if (ctx.vars() != null) {
                DeclsNode restVars = (DeclsNode) visit(ctx.vars());
                decls.decls.addAll(restVars.decls);
            }
        }

        return decls;
    }

    // ========== INSTRUCTIONS ==========
    @Override
    public AstNode visitInstrs(MiniJajaParser.InstrsContext ctx) {
        InstrsNode instrs = new InstrsNode();

        // Récursif : instr SEMI instrs | ε
        if (ctx.instr() != null) {
            InstrNode instr = (InstrNode) visit(ctx.instr());
            if (instr != null) {
                instrs.add(instr);
            }

            if (ctx.instrs() != null) {
                InstrsNode restInstrs = (InstrsNode) visit(ctx.instrs());
                instrs.list.addAll(restInstrs.list);
            }
        }

        return instrs;
    }

    @Override
    public AstNode visitInstr(MiniJajaParser.InstrContext ctx) {
        // Affectation : ident1 = exp
        if (ctx.ASSIGN() != null) {
            AstNode identNode = visit(ctx.ident1());
            ExpNode exp = (ExpNode) visit(ctx.exp());

            if (identNode instanceof VarNode) {
                return new AssignNode(new VarRefNode(((VarNode) identNode).getIdent()), exp);
            }
            // Si c'est un accès tableau, créer une affectation spéciale
            // Pour simplifier, on utilise l'ident de base
            return new AssignNode(new VarRefNode(new Ident(ctx.ident1().IDENT().getText())), exp);
        }

        // += : ident1 += exp
        if (ctx.PLUSEQ() != null) {
            Ident ident = new Ident(ctx.ident1().IDENT().getText());
            ExpNode exp = (ExpNode) visit(ctx.exp());
            // Transformer en affectation : x = x + exp
            return new AssignNode(new VarRefNode(ident), new BinaryExpNode("+", new IdentExp(ident), exp));
        }

        // Incrémentation : ident1++
        if (ctx.INCR() != null) {
            Ident ident = new Ident(ctx.ident1().IDENT().getText());
            return new IncNode(ident);
        }

        // Appel de méthode : IDENT(listexp)
        if (ctx.LPAREN() != null && ctx.IDENT() != null) {
            // C'est un appel de méthode (instruction)
            // Pour simplifier, on ignore les appels de méthode pour l'instant
            return null;
        }

        // Return
        if (ctx.RETURN() != null) {
            ExpNode exp = (ExpNode) visit(ctx.exp());
            // On pourrait créer une classe ReturnInstr
            // Pour l'instant, on ignore ou on crée une instruction spéciale
            return null;
        }

        // Write / Writeln
        if (ctx.WRITE() != null || ctx.WRITELN() != null) {
            // Write / Writeln
            if (ctx.WRITE() != null || ctx.WRITELN() != null) {
                boolean isLn = ctx.WRITELN() != null;
                ExpNode contenu = null;

                // Expression
                if (ctx.exp() != null) {
                    contenu = (ExpNode) visit(ctx.exp());
                }
                // Identifiant
                else if (ctx.ident1() != null) {
                    contenu = (ExpNode) visit(ctx.ident1());
                }
                // Chaîne de caractères
                else if (ctx.STRING() != null) {
                    String str = ctx.STRING().getText();
                    str = str.substring(1, str.length() - 1); // Enlever les guillemets
                    contenu = new StringLiteralNode(str);  // ✅ Utilise la classe Chaine en français
                }

                return new WriteNode(contenu, isLn);  // ✅ Utilise la classe Ecrire en français
            }
        }

        // IF
        if (ctx.IF() != null) {
            ExpNode cond = (ExpNode) visit(ctx.exp());

            // Le premier bloc instrs est le THEN
            InstrsNode thenInstrs = (InstrsNode) visit(ctx.instrs(0));

            // Le deuxième bloc (si présent) est le ELSE
            InstrsNode elseInstrs = new InstrsNode();
            if (ctx.instrs().size() > 1) {
                elseInstrs = (InstrsNode) visit(ctx.instrs(1));
            }

            return new IfNode(cond, thenInstrs, elseInstrs);
        }

        // WHILE
        if (ctx.WHILE() != null) {
            ExpNode cond = (ExpNode) visit(ctx.exp());
            InstrsNode body = (InstrsNode) visit(ctx.instrs(0));
            return new WhileNode(cond, body);
        }

        return null;
    }

    // ========== EXPRESSIONS ==========
    @Override
    public AstNode visitExp(MiniJajaParser.ExpContext ctx) {
        // exp : expAnd ( OR expAnd )*
        if (ctx.expAnd().size() == 1) {
            return visit(ctx.expAnd(0));
        }

        ExpNode result = (ExpNode) visit(ctx.expAnd(0));
        for (int i = 1; i < ctx.expAnd().size(); i++) {
            ExpNode right = (ExpNode) visit(ctx.expAnd(i));
            result = new BinaryExpNode("||", result, right);
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
            result = new BinaryExpNode("&&", result, right);
        }
        return result;
    }

    @Override
    public AstNode visitExpEq(MiniJajaParser.ExpEqContext ctx) {
        // expEq : expAdd ( (EQ | GT) expAdd )*
        if (ctx.expAdd().size() == 1) {
            return visit(ctx.expAdd(0));
        }

        ExpNode result = (ExpNode) visit(ctx.expAdd(0));
        for (int i = 1; i < ctx.expAdd().size(); i++) {
            String op = ctx.EQ(i-1) != null ? "==" : ">";
            ExpNode right = (ExpNode) visit(ctx.expAdd(i));
            result = new BinaryExpNode(op, result, right);
        }
        return result;
    }

    @Override
    public AstNode visitExpAdd(MiniJajaParser.ExpAddContext ctx) {
        // expAdd : MINUS expAdd | expNot ( (PLUS | MINUS) expNot )*

        // Cas unaire : -expAdd
        if (ctx.MINUS() != null && ctx.expAdd() != null) {
            ExpNode exp = (ExpNode) visit(ctx.expAdd());
            return new UnaryExpNode("-", exp);
        }

        // Cas binaire : expNot (+|- expNot)*
        if (ctx.expNot().size() == 1) {
            return visit(ctx.expNot(0));
        }

        ExpNode result = (ExpNode) visit(ctx.expNot(0));
        for (int i = 1; i < ctx.expNot().size(); i++) {
            String op = ctx.PLUS(i-1) != null ? "+" : "-";
            ExpNode right = (ExpNode) visit(ctx.expNot(i));
            result = new BinaryExpNode(op, result, right);
        }
        return result;
    }

    @Override
    public AstNode visitExpNot(MiniJajaParser.ExpNotContext ctx) {
        // expNot : NOT expNot | terme
        if (ctx.NOT() != null) {
            ExpNode exp = (ExpNode) visit(ctx.expNot());
            return new UnaryExpNode("!", exp);
        }
        return visit(ctx.terme());
    }

    @Override
    public AstNode visitTerme(MiniJajaParser.TermeContext ctx) {
        // terme : fact ( (MUL | DIV) fact )*
        if (ctx.fact().size() == 1) {
            return visit(ctx.fact(0));
        }

        ExpNode result = (ExpNode) visit(ctx.fact(0));
        for (int i = 1; i < ctx.fact().size(); i++) {
            String op = ctx.MUL(i-1) != null ? "*" : "/";
            ExpNode right = (ExpNode) visit(ctx.fact(i));
            result = new BinaryExpNode(op, result, right);
        }
        return result;
    }

    @Override
    public AstNode visitFact(MiniJajaParser.FactContext ctx) {
        // Littéraux
        if (ctx.INT() != null) {
            return new IntLiteralNode(Integer.parseInt(ctx.INT().getText()));
        }
        if (ctx.TRUE() != null) {
            return new BoolLiteralNode(true);
        }
        if (ctx.FALSE() != null) {
            return new BoolLiteralNode(false);
        }

        // Chaîne de caractères
        if (ctx.STRING() != null) {
            String str = ctx.STRING().getText();
            // On retourne un IntLiteralNode pour l'instant (à adapter selon ton AST)
            // Ou créer une StringExp si nécessaire
            return new IntLiteralNode(0); // Placeholder
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
            return new IntLiteralNode(0); // Placeholder
        }

        // LENGTH(IDENT)
        if (ctx.LENGHT() != null) {
            // Pour simplifier, on retourne une valeur par défaut
            return new IntLiteralNode(0); // Placeholder
        }

        return new IntLiteralNode(0); // Fallback
    }

    @Override
    public AstNode visitIdent1(MiniJajaParser.Ident1Context ctx) {
        Ident ident = new Ident(ctx.IDENT().getText());

        // Accès tableau : IDENT[exp]
        if (ctx.LBRACK() != null) {
            ExpNode index = (ExpNode) visit(ctx.exp());
            // Pour simplifier, on retourne juste l'IdentExp
            // Ou créer une classe ArrayAccessExp si nécessaire
            return new IdentExp(ident);
        }

        // Identifiant simple
        return new IdentExp(ident);
    }
}