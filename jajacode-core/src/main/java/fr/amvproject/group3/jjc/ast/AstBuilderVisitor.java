package fr.amvproject.group3.jjc.ast;

import java.util.List;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import fr.amvproject.group3.JajaCodeParser;
import fr.amvproject.group3.JajaCodeParserBaseVisitor;
import fr.amvproject.group3.JajaCodeParser.AdresseContext;
import fr.amvproject.group3.JajaCodeParser.IdentContext;
import fr.amvproject.group3.JajaCodeParser.Oper1Context;
import fr.amvproject.group3.JajaCodeParser.Oper2Context;
import fr.amvproject.group3.JajaCodeParser.SorteContext;
import fr.amvproject.group3.JajaCodeParser.TypeContext;
import fr.amvproject.group3.JajaCodeParser.ValeurContext;
import fr.amvproject.group3.jjc.ast.nodes.finals.*;
import fr.amvproject.group3.jjc.ast.nodes.instrs.*;
import fr.amvproject.group3.jjc.ast.nodes.intermediate.InstrNode;
import fr.amvproject.group3.jjc.ast.nodes.intermediate.ValueNode;
import fr.amvproject.group3.jjc.ast.nodes.literals.InitLiteral;
import fr.amvproject.group3.jjc.ast.nodes.literals.JcstopLiteral;
import fr.amvproject.group3.jjc.ast.nodes.literals.NopLiteral;
import fr.amvproject.group3.jjc.ast.nodes.literals.PopLiteral;
import fr.amvproject.group3.jjc.ast.nodes.literals.ReturnLiteral;
import fr.amvproject.group3.jjc.ast.nodes.literals.SwapLiteral;
import fr.amvproject.group3.jjc.ast.nodes.literals.WriteLiteral;
import fr.amvproject.group3.jjc.ast.nodes.other.BinaryOpNode;
import fr.amvproject.group3.jjc.ast.nodes.other.ObjNode;
import fr.amvproject.group3.jjc.ast.nodes.other.TypeNode;
import fr.amvproject.group3.jjc.ast.nodes.other.UnaryOpNode;
import fr.amvproject.group3.jjc.ast.nodes.program.ClassNode;
import fr.amvproject.group3.jjc.ast.nodes.program.ProgramNode;

public class AstBuilderVisitor extends JajaCodeParserBaseVisitor<AstNode> {
  @Override
  public AstNode visitProgram(JajaCodeParser.ProgramContext ctx) {
    List<ClassNode> classes = ctx.classe().stream().map(c -> (ClassNode) visit(c)).toList();
    return new ProgramNode(classes, getTokenOrNull(ctx.EOF()));
  }

  @Override
  public AstNode visitClasse(JajaCodeParser.ClasseContext ctx) {
    return new ClassNode((AddressNode) visit(ctx.adresse()), (InstrNode) visit(ctx.instr()));
  }

  @Override
  @SuppressWarnings("java:S3776") // Cognitive complexity warning
  public AstNode visitInstr(JajaCodeParser.InstrContext ctx) {
    if (ctx.INIT() != null) return new InitLiteral(getTokenOrNull(ctx.INIT()));
    if (ctx.SWAP() != null) return new SwapLiteral(getTokenOrNull(ctx.SWAP()));
    if (ctx.NEW() != null) return visitNew(ctx);
    if (ctx.NEWARRAY() != null) return visitNewArray(ctx);
    if (ctx.INVOKE() != null) return visitInvoke(ctx);
    if (ctx.LENGHT() != null) return visitLength(ctx);
    if (ctx.RETURN() != null) return new ReturnLiteral(getTokenOrNull(ctx.RETURN()));
    if (ctx.WRITE() != null) return new WriteLiteral(getTokenOrNull(ctx.WRITE()), false);
    if (ctx.WRITELN() != null) return new WriteLiteral(getTokenOrNull(ctx.WRITELN()), true);
    if (ctx.PUSH() != null) return visitPush(ctx);
    if (ctx.POP() != null) return new PopLiteral(getTokenOrNull(ctx.POP()));
    if (ctx.LOAD() != null || ctx.ALOAD() != null) return visitLoad(ctx);
    if (ctx.STORE() != null || ctx.ASTORE() != null) return visitStore(ctx);
    if (ctx.IF() != null) return visitIf(ctx);
    if (ctx.GOTO() != null) return visitGoto(ctx);
    if (ctx.INC() != null || ctx.AINC() != null) return visitInc(ctx);
    if (ctx.oper() != null) return visit(ctx.oper());
    if (ctx.NOP() != null) return new NopLiteral(getTokenOrNull(ctx.NOP()));
    if (ctx.JCSTOP() != null) return new JcstopLiteral(getTokenOrNull(ctx.JCSTOP()));

    return null;
  }

  @Override
  public AstNode visitOper1(Oper1Context ctx) {
    return new UnaryOpNode(ctx.getStart());
  }

  @Override
  public AstNode visitOper2(Oper2Context ctx) {
    return new BinaryOpNode(ctx.getStart());
  }

  @Override
  public AstNode visitIdent(IdentContext ctx) {
    return new IdentNode(getTokenOrNull(ctx.IDENTIFICATEUR()));
  }

  @Override
  public AstNode visitValeur(ValeurContext ctx) {
    if (ctx.NOMBRE() != null) return new IntLiteralNode(getTokenOrNull(ctx.NOMBRE()));
    if (ctx.TRUE() != null) return new BoolLiteralNode(getTokenOrNull(ctx.TRUE()));
    if (ctx.FALSE() != null) return new BoolLiteralNode(getTokenOrNull(ctx.FALSE()));
    if (ctx.STRING() != null) return new StringLiteralNode(getTokenOrNull(ctx.STRING()));
    return null;
  }

  @Override
  public AstNode visitAdresse(AdresseContext ctx) {
    return new AddressNode(getTokenOrNull(ctx.NOMBRE()));
  }

  @Override
  public AstNode visitType(TypeContext ctx) {
    return new TypeNode(ctx.getStart());
  }

  @Override
  public AstNode visitSorte(SorteContext ctx) {
    return new ObjNode(ctx.getStart());
  }

  /* Helper visitors */

  public AstNode visitNew(JajaCodeParser.InstrContext ctx) {
    IdentNode identNode = (IdentNode) visit(ctx.ident());
    TypeNode typeNode = (TypeNode) visit(ctx.type());
    ObjNode objNode = (ObjNode) visit(ctx.sorte());
    ValueNode valueNode = (ValueNode) visit(ctx.valeur());
    return new NewNode(getTokenOrNull(ctx.NEW()), identNode, typeNode, objNode, valueNode);
  }

  public AstNode visitNewArray(JajaCodeParser.InstrContext ctx) {
    IdentNode identNode = (IdentNode) visit(ctx.ident());
    TypeNode typeNode = (TypeNode) visit(ctx.type());
    return new NewArrayNode(getTokenOrNull(ctx.NEWARRAY()), identNode, typeNode);
  }

  public AstNode visitInvoke(JajaCodeParser.InstrContext ctx) {
    IdentNode identNode = (IdentNode) visit(ctx.ident());
    return new InvokeNode(getTokenOrNull(ctx.INVOKE()), identNode);
  }

  public AstNode visitLength(JajaCodeParser.InstrContext ctx) {
    IdentNode identNode = (IdentNode) visit(ctx.ident());
    return new LengthNode(getTokenOrNull(ctx.LENGHT()), identNode);
  }

  public AstNode visitPush(JajaCodeParser.InstrContext ctx) {
    ValueNode valueNode = (ValueNode) visit(ctx.valeur());
    return new PushNode(getTokenOrNull(ctx.PUSH()), valueNode);
  }

  public AstNode visitLoad(JajaCodeParser.InstrContext ctx) {
    IdentNode identNode = (IdentNode) visit(ctx.ident());
    return new LoadNode(getTokenOrNull(ctx.LOAD()), identNode, ctx.LOAD() == null);
  }

  public AstNode visitStore(JajaCodeParser.InstrContext ctx) {
    IdentNode identNode = (IdentNode) visit(ctx.ident());
    return new StoreNode(getTokenOrNull(ctx.STORE()), identNode, ctx.STORE() == null);
  }

  public AstNode visitIf(JajaCodeParser.InstrContext ctx) {
    AddressNode addressNode = (AddressNode) visit(ctx.adresse());
    return new IfNode(getTokenOrNull(ctx.IF()), addressNode);
  }

  public AstNode visitGoto(JajaCodeParser.InstrContext ctx) {
    AddressNode addressNode = (AddressNode) visit(ctx.adresse());
    return new GotoNode(getTokenOrNull(ctx.GOTO()), addressNode);
  }

  public AstNode visitInc(JajaCodeParser.InstrContext ctx) {
    IdentNode identNode = (IdentNode) visit(ctx.ident());
    return new IncNode(getTokenOrNull(ctx.INC()), identNode, ctx.INC() == null);
  }

  /* Helper functions */
  public Token getTokenOrNull(TerminalNode terminalNode) {
    return terminalNode != null ? terminalNode.getSymbol() : null;
  }
}
