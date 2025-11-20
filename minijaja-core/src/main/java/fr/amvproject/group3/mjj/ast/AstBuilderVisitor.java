package fr.amvproject.group3.mjj.ast;

import java.util.List;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

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
    return new ProgramNode(classes, getTokenOrNull(ctx.EOF()));
  }

  @Override
  public AstNode visitClasse(MiniJajaParser.ClasseContext ctx) {
    IdentNode ident = new IdentNode(getTokenOrNull(ctx.ident().IDENTIFICATEUR()));
    DeclsNode decls = (DeclsNode) visit(ctx.decls());
    MainNode main = (MainNode) visit(ctx.methmain());
    return new ClassNode(getTokenOrNull(ctx.CLASS()), ident, decls, main);
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
      return new TableNode(new TypeMethNode(ctx.typemeth().getStart()),
          new IdentNode(getTokenOrNull(ctx.ident().IDENTIFICATEUR())),
          (ExpNode) visit(ctx.exp()));
    } else if (ctx.FINAL() != null) {
      // Const
      return new CstNode(ctx.FINAL().getSymbol(), new TypeNode(ctx.type().getStart()),
          new IdentNode(getTokenOrNull(ctx.ident().IDENTIFICATEUR())), getTokenOrNull(ctx.vexp().ASSIGN()), exp);
    }
    // Var
    return new VarNode(new TypeMethNode(ctx.typemeth().getStart()),
        new IdentNode(getTokenOrNull(ctx.ident().IDENTIFICATEUR())), getTokenOrNull(ctx.vexp().ASSIGN()), exp);

  }

  @Override
  public AstNode visitMethode(MiniJajaParser.MethodeContext ctx) {
    return new MethodNode(new TypeMethNode(ctx.typemeth().getStart()),
        new IdentNode(getTokenOrNull(ctx.ident().IDENTIFICATEUR())),
        (ParamsNode) visit(ctx.entetes()), (VarsNode) visit(ctx.vars()), (InstrsNode) visit(ctx.instrs()));
  }

  @Override
  public AstNode visitMethmain(MiniJajaParser.MethmainContext ctx) {
    return new MainNode(getTokenOrNull(ctx.MAIN()), (VarsNode) visit(ctx.vars()), (InstrsNode) visit(ctx.instrs()));
  }

  @Override
  public AstNode visitEntetes(MiniJajaParser.EntetesContext ctx) {
    List<ParamNode> params = ctx.entete().stream().map(e -> (ParamNode) visit(e)).toList();
    return new ParamsNode(params);
  }

  @Override
  public AstNode visitEntete(MiniJajaParser.EnteteContext ctx) {
    return new ParamNode(new TypeNode(ctx.type().getStart()),
        new IdentNode(getTokenOrNull(ctx.ident().IDENTIFICATEUR())));
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
      result = new BinaryExpNode(new BinaryOpNode(ctx.OR(i-1).getSymbol()), result, right);
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
      result = new BinaryExpNode(new BinaryOpNode(ctx.AND(i-1).getSymbol()), result, right);
    }
    return result;
  }

  @Override
  public AstNode visitExpEq(MiniJajaParser.ExpEqContext ctx) {
    // expEq : expRel ( (EQ | GT) expRel )*
    if (ctx.expRel().size() == 1) return visit(ctx.expRel(0));
    ExpNode result = (ExpNode) visit(ctx.expRel(0));
    int eqIndex = 0;
    int gtIndex = 0;
    for (int i = 1; i < ctx.expRel().size(); i++) {
      Token op = ctx.EQ(eqIndex) != null ? ctx.EQ(eqIndex++).getSymbol() : ctx.GT(gtIndex++).getSymbol();
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
    int addIndex = 0;
    int subIndex = 0;
    ExpNode result = (ExpNode) visit(ctx.expMul(0));
    for (int i = 1; i < ctx.expMul().size(); i++) {
      Token op = ctx.ADD(addIndex) != null ? ctx.ADD(addIndex++).getSymbol() : ctx.SUB(subIndex++).getSymbol();
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
    int mulIndex = 0;
    int divIndex = 0;
    for (int i = 1; i < ctx.expUnary().size(); i++) {
      Token op = ctx.MUL(mulIndex) != null ? ctx.MUL(mulIndex++).getSymbol() : ctx.DIV(divIndex++).getSymbol();
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
      return new UnaryExpNode(new UnaryOpNode(ctx.NOT().getSymbol()), right);
    } else if (ctx.SUB() != null) {
      ExpNode right = (ExpNode) visit(ctx.expUnary());
      return new UnaryExpNode(new UnaryOpNode(ctx.SUB().getSymbol()), right);
    } else {
      return visit(ctx.fact());
    }
  }

  @Override
  public AstNode visitFact(MiniJajaParser.FactContext ctx) {
    // Literals
    if (ctx.NOMBRE() != null) return new IntLiteralNode(ctx.NOMBRE().getSymbol());
    if (ctx.TRUE() != null) return new BoolLiteralNode(ctx.TRUE().getSymbol());
    if (ctx.FALSE() != null) return new BoolLiteralNode(ctx.FALSE().getSymbol());

    // Ident1
    if (ctx.ident1() != null) return visit(ctx.ident1());

    // Ident(listexp)
    if (ctx.ident() != null && ctx.LPAREN() != null && ctx.listexp() != null) return new CallExpNode(
          new IdentNode(getTokenOrNull(ctx.ident().IDENTIFICATEUR())),
          (ListExpNode) visit(ctx.listexp()));

    // Length(ident)
    if (ctx.LENGTH() != null && ctx.ident() != null) return new LengthNode(
          ctx.LENGTH().getSymbol(),
          new IdentNode(ctx.ident().IDENTIFICATEUR().getSymbol()));

    // (exp)
    if (ctx.exp() != null) return visit(ctx.exp());

    return null;
  }

  @Override
  public AstNode visitIdent1(MiniJajaParser.Ident1Context ctx) {
    // Array
    if (ctx.LBRACK() != null) return new ArrayAccessNode(
          new IdentNode(getTokenOrNull(ctx.ident().IDENTIFICATEUR())),
          (ExpNode) visit(ctx.exp()));

    // Var
    return new IdentNode(getTokenOrNull(ctx.ident().IDENTIFICATEUR()));
  }

  /* Helper visitors */

  public AstNode visitAssign(MiniJajaParser.InstrContext ctx) {
    LeftValueNode left = (LeftValueNode) visit(ctx.ident1());
    ExpNode exp = (ExpNode) visit(ctx.exp());
    if (ctx.ASSIGN() != null) return new AssignNode(left, ctx.ASSIGN().getSymbol(), exp);
    return new AddAssignNode(left, ctx.ADDASSIGN().getSymbol(), exp);
  }

  public AstNode visitInc(MiniJajaParser.InstrContext ctx) {
    IdentNode ident = new IdentNode(getTokenOrNull(ctx.ident1().ident().IDENTIFICATEUR()));
    return new IncNode(ident, ctx.INC().getSymbol());
  }

  public AstNode visitMethodCall(MiniJajaParser.InstrContext ctx) {
    IdentNode ident = new IdentNode(getTokenOrNull(ctx.ident().IDENTIFICATEUR()));
      ListExpNode listExp = (ListExpNode) visit(ctx.listexp());
      return new CallInstrNode(ident, listExp);
  }

  public AstNode visitReturn(MiniJajaParser.InstrContext ctx) {
    return new ReturnNode(ctx.RETURN().getSymbol(), (ExpNode) visit(ctx.exp()));
  }

  public AstNode visitWrite(MiniJajaParser.InstrContext ctx) {
    ExpNode exp;
    if (ctx.ident1() != null) {
      exp = (ExpNode) visit(ctx.ident1());
    } else if (ctx.STRING() != null) {
      exp = new StringLiteralNode(ctx.STRING().getSymbol());
    } else {
      exp = null;
    }
    if (ctx.WRITELN() != null) return new WriteNode(ctx.WRITELN().getSymbol(), exp, true);
    return new WriteNode(ctx.WRITE().getSymbol(), exp, false);
  }

  public AstNode visitIf(MiniJajaParser.InstrContext ctx) {
    ExpNode cond = (ExpNode) visit(ctx.exp());
    InstrsNode thenInstrs = (InstrsNode) visit(ctx.instrs(0));
    InstrsNode elseInstrs = (ctx.instrs().size() > 1) ? (InstrsNode) visit(ctx.instrs(1)) : null;
    return new IfNode(ctx.IF().getSymbol(), cond, thenInstrs, getTokenOrNull(ctx.ELSE()), elseInstrs);
  }

  public AstNode visitWhile(MiniJajaParser.InstrContext ctx) {
    return new WhileNode(ctx.WHILE().getSymbol(), (ExpNode) visit(ctx.exp()), (InstrsNode) visit(ctx.instrs(0)));
  }

  /* Helper functions */
  public Token getTokenOrNull(TerminalNode terminalNode) {
    return terminalNode != null ? terminalNode.getSymbol() : null;
  }
}
