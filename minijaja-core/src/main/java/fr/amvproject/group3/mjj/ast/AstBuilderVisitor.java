package fr.amvproject.group3.mjj.ast;

import java.util.List;

import fr.amvproject.group3.MiniJajaParser;
import fr.amvproject.group3.MiniJajaParserBaseVisitor;
import fr.amvproject.group3.mjj.ast.nodes.abstracts.*;
import fr.amvproject.group3.mjj.ast.nodes.decls.*;
import fr.amvproject.group3.mjj.ast.nodes.exprs.BinaryExpNode;
import fr.amvproject.group3.mjj.ast.nodes.exprs.CallExpNode;
import fr.amvproject.group3.mjj.ast.nodes.exprs.LengthNode;
import fr.amvproject.group3.mjj.ast.nodes.exprs.UnaryExpNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.*;
import fr.amvproject.group3.mjj.ast.nodes.instrs.*;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.*;
import fr.amvproject.group3.mjj.ast.nodes.other.BinaryOpNode;
import fr.amvproject.group3.mjj.ast.nodes.other.TypeMethNode;
import fr.amvproject.group3.mjj.ast.nodes.other.TypeNode;
import fr.amvproject.group3.mjj.ast.nodes.other.UnaryOpNode;
import fr.amvproject.group3.mjj.ast.nodes.program.ClassNode;
import fr.amvproject.group3.mjj.ast.nodes.program.ProgramNode;

public class AstBuilderVisitor extends MiniJajaParserBaseVisitor<AstNode> {
  @Override
  public AstNode visitProgram(MiniJajaParser.ProgramContext ctx) {
    List<ClassNode> classes = ctx.classe().stream().map(c -> (ClassNode) visit(c)).toList();
    return new ProgramNode(classes);
  }

  @Override
  public AstNode visitClasse(MiniJajaParser.ClasseContext ctx) {
    IdentNode ident = new IdentNode(ctx.ident().getText(), ctx.ident().IDENTIFICATEUR().getSymbol().getLine());
    DeclsNode decls = (DeclsNode) visit(ctx.decls());
    MainNode main = (MainNode) visit(ctx.methmain());
    return new ClassNode(ident, decls, main);
  }

  @Override
  public AstNode visitDecls(MiniJajaParser.DeclsContext ctx) {
    List<DeclNode> decls = ctx.decl().stream().map(d -> (DeclNode) visit(d)).toList();
    return new DeclsNode(decls);
  }

  @Override
  public AstNode visitDecl(MiniJajaParser.DeclContext ctx) {
    if (ctx.var() != null)
      return visit(ctx.var());
    return visit(ctx.methode());
  }

  @Override
  public AstNode visitVar(MiniJajaParser.VarContext ctx) {
    ExpNode exp = (ctx.vexp() != null && ctx.vexp().exp() != null) ? (ExpNode) visit(ctx.vexp().exp()) : null;

    // Array, grammar does not allow to init arrays
    if (ctx.LBRACK() != null) {
      return new TableNode(new TypeMethNode(ctx.typemeth().getText()),
          new IdentNode(ctx.ident().getText(), ctx.ident().IDENTIFICATEUR().getSymbol().getLine()),
          (ExpNode) visit(ctx.exp()));
    } else if (ctx.FINAL() != null) {
      // Const
      return new CstNode(new TypeNode(ctx.type().getText()),
          new IdentNode(ctx.ident().getText(), ctx.ident().IDENTIFICATEUR().getSymbol().getLine()), exp);
    }
    // Var
    return new VarNode(new TypeMethNode(ctx.typemeth().getText()),
        new IdentNode(ctx.ident().getText(), ctx.ident().IDENTIFICATEUR().getSymbol().getLine()), exp);

  }

  @Override
  public AstNode visitMethode(MiniJajaParser.MethodeContext ctx) {
    return new MethodNode(new TypeMethNode(ctx.typemeth().getText()),
        new IdentNode(ctx.ident().getText(), ctx.ident().IDENTIFICATEUR().getSymbol().getLine()),
        (ParamsNode) visit(ctx.entetes()), (VarsNode) visit(ctx.vars()), (InstrsNode) visit(ctx.instrs()));
  }

  @Override
  public AstNode visitMethmain(MiniJajaParser.MethmainContext ctx) {
    return new MainNode((VarsNode) visit(ctx.vars()), (InstrsNode) visit(ctx.instrs()));
  }

  @Override
  public AstNode visitEntetes(MiniJajaParser.EntetesContext ctx) {
    List<ParamNode> params = ctx.entete().stream().map(e -> (ParamNode) visit(e)).toList();
    return new ParamsNode(params);
  }

  @Override
  public AstNode visitEntete(MiniJajaParser.EnteteContext ctx) {
    return new ParamNode(new TypeNode(ctx.type().getText()),
        new IdentNode(ctx.ident().getText(), ctx.ident().IDENTIFICATEUR().getSymbol().getLine()));
  }

  @Override
  public AstNode visitVars(MiniJajaParser.VarsContext ctx) {
    List<VarDeclNode> vars = ctx.var().stream().map(v -> (VarDeclNode) visit(v)).toList();
    return new VarsNode(vars);
  }

  @Override
  public AstNode visitInstrs(MiniJajaParser.InstrsContext ctx) {
    List<InstrNode> instrs = ctx.instr().stream().map(i -> (InstrNode) visit(i)).toList();
    return new InstrsNode(instrs);
  }

  @Override
  public AstNode visitInstr(MiniJajaParser.InstrContext ctx) {
    // Assignment (= or +=)
    if (ctx.ASSIGN() != null || ctx.ADDASSIGN() != null) return visitAssign(ctx);

    // Increment (++)
    if (ctx.INC() != null) return visitInc(ctx);

    // Method call
    if (ctx.LPAREN() != null && ctx.ident() != null) return visitMethodCall(ctx);

    // Return
    if (ctx.RETURN() != null) return visitReturn(ctx);

    // Write / Writeln
    if (ctx.WRITE() != null || ctx.WRITELN() != null) return visitWrite(ctx);

    // IF statement
    if (ctx.IF() != null) return visitIf(ctx);

    // WHILE loop
    if (ctx.WHILE() != null) return visitWhile(ctx);

    return null;
  }

  @Override
  public AstNode visitListexp(MiniJajaParser.ListexpContext ctx) {
    List<ExpNode> listexp = ctx.exp().stream().map(e -> (ExpNode) visit(e)).toList();
    return new ListExpNode(listexp);
  }

  @Override
  public AstNode visitExp(MiniJajaParser.ExpContext ctx) {
    return visit(ctx.expOr());
  }

  @Override
  public AstNode visitExpOr(MiniJajaParser.ExpOrContext ctx) {
    // expOr : expAnd ( OR expAnd )*
    if (ctx.expAnd().size() == 1) return visit(ctx.expAnd(0));
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
    if (ctx.expEq().size() == 1) return visit(ctx.expEq(0));
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
    if (ctx.expRel().size() == 1) return visit(ctx.expRel(0));
    ExpNode result = (ExpNode) visit(ctx.expRel(0));
    for (int i = 1; i < ctx.expRel().size(); i++) {
      String op = ctx.EQ(i - 1) != null ? "==" : ">";
      ExpNode right = (ExpNode) visit(ctx.expRel(i));
      result = new BinaryExpNode(new BinaryOpNode(op), result, right);
    }
    return result;
  }

  @Override
  public AstNode visitExpRel(MiniJajaParser.ExpRelContext ctx) {
    return visit(ctx.expAdd());
  }

  @Override
  public AstNode visitExpAdd(MiniJajaParser.ExpAddContext ctx) {
    // expAdd : expMul ( (ADD | SUB) expMul )*
    if (ctx.expMul().size() == 1) return visit(ctx.expMul(0));
    ExpNode result = (ExpNode) visit(ctx.expMul(0));
    for (int i = 1; i < ctx.expMul().size(); i++) {
      String op = ctx.ADD(i - 1) != null ? "+" : "-";
      ExpNode right = (ExpNode) visit(ctx.expMul(i));
      result = new BinaryExpNode(new BinaryOpNode(op), result, right);
    }
    return result;
  }

  @Override
  public AstNode visitExpMul(MiniJajaParser.ExpMulContext ctx) {
    // expMul : expUnary ((MUL | DIV) expUnary)*
    if (ctx.expUnary().size() == 1) return visit(ctx.expUnary(0));
    ExpNode result = (ExpNode) visit(ctx.expUnary(0));
    for (int i = 1; i < ctx.expUnary().size(); i++) {
      String op = ctx.MUL(i - 1) != null ? "*" : "/";
      ExpNode right = (ExpNode) visit(ctx.expUnary(i));
      result = new BinaryExpNode(new BinaryOpNode(op), result, right);
    }
    return result;
  }

  @Override
  public AstNode visitExpUnary(MiniJajaParser.ExpUnaryContext ctx) {
    // expUnary : (NOT | SUB) expUnary | fact
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
    if (ctx.NOMBRE() != null) return new IntLiteralNode(Integer.parseInt(ctx.NOMBRE().getText()));
    if (ctx.TRUE() != null) return new BoolLiteralNode(true);
    if (ctx.FALSE() != null) return new BoolLiteralNode(false);

    // Ident1
    if (ctx.ident1() != null) return visit(ctx.ident1());

    // Ident(listexp)
    if (ctx.ident() != null && ctx.LPAREN() != null && ctx.listexp() != null) return new CallExpNode(
          new IdentNode(ctx.ident().getText(), ctx.ident().IDENTIFICATEUR().getSymbol().getLine()),
          (ListExpNode) visit(ctx.listexp()));

    // Length(ident)
    if (ctx.LENGTH() != null && ctx.ident() != null) return new LengthNode(
          new IdentNode(ctx.ident().getText(), ctx.ident().IDENTIFICATEUR().getSymbol().getLine()));

    // (exp)
    if (ctx.exp() != null) return visit(ctx.exp());

    return null;
  }

  @Override
  public AstNode visitIdent1(MiniJajaParser.Ident1Context ctx) {
    // Array
    if (ctx.LBRACK() != null) return new ArrayAccessNode(
          new IdentNode(ctx.ident().getText(), ctx.ident().IDENTIFICATEUR().getSymbol().getLine()),
          (ExpNode) visit(ctx.exp()));

    // Var
    return new IdentNode(ctx.ident().getText(), ctx.ident().IDENTIFICATEUR().getSymbol().getLine());
  }

  /* Helper visitors */

  public AstNode visitAssign(MiniJajaParser.InstrContext ctx) {
    LeftValueNode left = (LeftValueNode) visit(ctx.ident1());
    ExpNode exp = (ExpNode) visit(ctx.exp());
    if (ctx.ASSIGN() != null) return new AssignNode(left, exp);
    return new AddAssignNode(left, exp);
  }

  public AstNode visitInc(MiniJajaParser.InstrContext ctx) {
      LeftValueNode leftValue = (LeftValueNode) visit(ctx.ident1());
      return new IncNode(leftValue);
  }

  public AstNode visitMethodCall(MiniJajaParser.InstrContext ctx) {
    IdentNode ident = new IdentNode(ctx.ident().getText(),
          ctx.ident().IDENTIFICATEUR().getSymbol().getLine());
      ListExpNode listExp = (ListExpNode) visit(ctx.listexp());
      return new CallInstrNode(ident, listExp);
  }

  public AstNode visitReturn(MiniJajaParser.InstrContext ctx) {
    return new ReturnNode((ExpNode) visit(ctx.exp()));
  }

  public AstNode visitWrite(MiniJajaParser.InstrContext ctx) {
    boolean isLn = ctx.WRITELN() != null;
      ExpNode exp;
      if (ctx.ident1() != null) {
        exp = (ExpNode) visit(ctx.ident1());
      } else if (ctx.STRING() != null) {
        String str = ctx.STRING().getText();
        str = str.substring(1, str.length() - 1);
        exp = new StringLiteralNode(str);
      } else {
        exp = null;
      }
      return new WriteNode(exp, isLn);
  }

  public AstNode visitIf(MiniJajaParser.InstrContext ctx) {
    ExpNode cond = (ExpNode) visit(ctx.exp());
      InstrsNode thenInstrs = (InstrsNode) visit(ctx.instrs(0));
      InstrsNode elseInstrs = (ctx.instrs().size() > 1) ? (InstrsNode) visit(ctx.instrs(1)) : null;
      return new IfNode(cond, thenInstrs, elseInstrs);
  }

  public AstNode visitWhile(MiniJajaParser.InstrContext ctx) {
    return new WhileNode((ExpNode) visit(ctx.exp()), (InstrsNode) visit(ctx.instrs(0)));
  }
}
