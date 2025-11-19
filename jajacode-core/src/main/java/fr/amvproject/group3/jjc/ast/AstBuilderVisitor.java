package fr.amvproject.group3.jjc.ast;

import java.util.List;

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
    return new ProgramNode(classes, ctx.EOF().getSymbol());
  }

  @Override
  public AstNode visitClasse(JajaCodeParser.ClasseContext ctx) {
    return new ClassNode((AddressNode) visit(ctx.adresse()), (InstrNode) visit(ctx.instr()));
  }

  @Override
  @SuppressWarnings("java:S3776") // Cognitive complexity warning
  public AstNode visitInstr(JajaCodeParser.InstrContext ctx) {
    if (ctx.INIT() != null) return new InitLiteral(ctx.INIT().getSymbol());
    if (ctx.SWAP() != null) return new SwapLiteral(ctx.SWAP().getSymbol());
    if (ctx.NEW() != null) return visitNew(ctx);
    if (ctx.NEWARRAY() != null) return visitNewArray(ctx);
    if (ctx.INVOKE() != null) return visitInvoke(ctx);
    if (ctx.LENGHT() != null) return visitLength(ctx);
    if (ctx.RETURN() != null) return new ReturnLiteral(ctx.RETURN().getSymbol());
    if (ctx.WRITE() != null) return new WriteLiteral(ctx.WRITE().getSymbol(), false);
    if (ctx.WRITELN() != null) return new WriteLiteral(ctx.WRITELN().getSymbol(), true);
    if (ctx.PUSH() != null) return visitPush(ctx);
    if (ctx.POP() != null) return new PopLiteral(ctx.POP().getSymbol());
    if (ctx.LOAD() != null || ctx.ALOAD() != null) return visitLoad(ctx);
    if (ctx.STORE() != null || ctx.ASTORE() != null) return visitStore(ctx);
    if (ctx.IF() != null) return visitIf(ctx);
    if (ctx.GOTO() != null) return visitGoto(ctx);
    if (ctx.INC() != null || ctx.AINC() != null) return visitInc(ctx);
    if (ctx.oper() != null) return visit(ctx.oper());
    if (ctx.NOP() != null) return new NopLiteral(ctx.NOP().getSymbol());
    if (ctx.JCSTOP() != null) return new JcstopLiteral(ctx.JCSTOP().getSymbol());

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
    return new IdentNode(ctx.IDENTIFICATEUR().getSymbol());
  }

  @Override
  public AstNode visitValeur(ValeurContext ctx) {
    if (ctx.NOMBRE() != null) return new IntLiteralNode(ctx.NOMBRE().getSymbol());
    if (ctx.TRUE() != null) return new BoolLiteralNode(ctx.TRUE().getSymbol());
    if (ctx.FALSE() != null) return new BoolLiteralNode(ctx.FALSE().getSymbol());
    if (ctx.STRING() != null) return new StringLiteralNode(ctx.STRING().getSymbol());
    return null;
  }

  @Override
  public AstNode visitAdresse(AdresseContext ctx) {
    return new AddressNode(ctx.NOMBRE().getSymbol());
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
    return new NewNode(ctx.NEW().getSymbol(), identNode, typeNode, objNode, valueNode);
  }

  public AstNode visitNewArray(JajaCodeParser.InstrContext ctx) {
    IdentNode identNode = (IdentNode) visit(ctx.ident());
    TypeNode typeNode = (TypeNode) visit(ctx.type());
    return new NewArrayNode(ctx.NEWARRAY().getSymbol(), identNode, typeNode);
  }

  public AstNode visitInvoke(JajaCodeParser.InstrContext ctx) {
    IdentNode identNode = (IdentNode) visit(ctx.ident());
    return new InvokeNode(ctx.INVOKE().getSymbol(), identNode);
  }

  public AstNode visitLength(JajaCodeParser.InstrContext ctx) {
    IdentNode identNode = (IdentNode) visit(ctx.ident());
    return new LengthNode(ctx.LENGHT().getSymbol(), identNode);
  }

  public AstNode visitPush(JajaCodeParser.InstrContext ctx) {
    ValueNode valueNode = (ValueNode) visit(ctx.valeur());
    return new PushNode(ctx.PUSH().getSymbol(), valueNode);
  }

  public AstNode visitLoad(JajaCodeParser.InstrContext ctx) {
    IdentNode identNode = (IdentNode) visit(ctx.ident());
    return new LoadNode(ctx.LOAD().getSymbol(), identNode, ctx.LOAD() == null);
  }

  public AstNode visitStore(JajaCodeParser.InstrContext ctx) {
    IdentNode identNode = (IdentNode) visit(ctx.ident());
    return new StoreNode(ctx.STORE().getSymbol(), identNode, ctx.STORE() == null);
  }

  public AstNode visitIf(JajaCodeParser.InstrContext ctx) {
    AddressNode addressNode = (AddressNode) visit(ctx.adresse());
    return new IfNode(ctx.IF().getSymbol(), addressNode);
  }

  public AstNode visitGoto(JajaCodeParser.InstrContext ctx) {
    AddressNode addressNode = (AddressNode) visit(ctx.adresse());
    return new GotoNode(ctx.GOTO().getSymbol(), addressNode);
  }

  public AstNode visitInc(JajaCodeParser.InstrContext ctx) {
    IdentNode identNode = (IdentNode) visit(ctx.ident());
    return new IncNode(ctx.INC().getSymbol(), identNode, ctx.INC() == null);
  }
}
