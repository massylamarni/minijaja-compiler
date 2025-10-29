package fr.amvproject.group3.ast;

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
import fr.amvproject.group3.ast.nodes.finals.*;
import fr.amvproject.group3.ast.nodes.instrs.*;
import fr.amvproject.group3.ast.nodes.intermediate.*;
import fr.amvproject.group3.ast.nodes.literals.InitLiteral;
import fr.amvproject.group3.ast.nodes.literals.JcstopLiteral;
import fr.amvproject.group3.ast.nodes.literals.NopLiteral;
import fr.amvproject.group3.ast.nodes.literals.PopLiteral;
import fr.amvproject.group3.ast.nodes.literals.ReturnLiteral;
import fr.amvproject.group3.ast.nodes.literals.SwapLiteral;
import fr.amvproject.group3.ast.nodes.literals.WriteLiteral;
import fr.amvproject.group3.ast.nodes.other.*;
import fr.amvproject.group3.ast.nodes.program.*;

public class JajaCodeAstBuilder extends JajaCodeParserBaseVisitor<AstNode> {
  @Override
  public AstNode visitProgram(JajaCodeParser.ProgramContext ctx) {
    List<ClassNode> classes = ctx.classe().stream().map(c -> (ClassNode) visit(c)).toList();
    return new ProgramNode(classes);
  }

  @Override
  public AstNode visitClasse(JajaCodeParser.ClasseContext ctx) {
    return new ClassNode((AddressNode) visit(ctx.adresse()), (InstrNode) visit(ctx.instr()));
  }

  @Override
  @SuppressWarnings("java:S3776") // Cognitive complexity warning
  public AstNode visitInstr(JajaCodeParser.InstrContext ctx) {
    if (ctx.INIT() != null) return new InitLiteral();
    if (ctx.SWAP() != null) return new SwapLiteral();
    if (ctx.NEW() != null) return visitNew(ctx);
    if (ctx.NEWARRAY() != null) return visitNewArray(ctx);
    if (ctx.INVOKE() != null) return visitInvoke(ctx);
    if (ctx.LENGHT() != null) return visitLength(ctx);
    if (ctx.RETURN() != null) return new ReturnLiteral();
    if (ctx.WRITE() != null || ctx.WRITELN() != null) return new WriteLiteral(ctx.WRITE() == null);
    if (ctx.PUSH() != null) return visitPush(ctx);
    if (ctx.POP() != null) return new PopLiteral();
    if (ctx.LOAD() != null || ctx.ALOAD() != null) return visitLoad(ctx);
    if (ctx.STORE() != null || ctx.ASTORE() != null) return visitStore(ctx);
    if (ctx.IF() != null) return visitIf(ctx);
    if (ctx.GOTO() != null) return visitGoto(ctx);
    if (ctx.INC() != null || ctx.AINC() != null) return visitInc(ctx);
    if (ctx.oper() != null) return visit(ctx.oper());
    if (ctx.NOP() != null) return new NopLiteral();
    if (ctx.JCSTOP() != null) return new JcstopLiteral();

    return null;
  }

  @Override
  public AstNode visitOper1(Oper1Context ctx) {
    return new UnaryOpNode(ctx.getText());
  }

  @Override
  public AstNode visitOper2(Oper2Context ctx) {
    return new BinaryOpNode(ctx.getText());
  }

  @Override
  public AstNode visitIdent(IdentContext ctx) {
    return new IdentNode(ctx.IDENTIFICATEUR().getText(), ctx.IDENTIFICATEUR().getSymbol().getLine());
  }

  @Override
  public AstNode visitValeur(ValeurContext ctx) {
    if (ctx.NOMBRE() != null) return new IntLiteralNode(Integer.parseInt(ctx.NOMBRE().getText()));
    if (ctx.TRUE() != null || ctx.FALSE() != null) return new BoolLiteralNode(ctx.TRUE() != null);
    if (ctx.STRING() != null) return new StringLiteralNode(ctx.STRING().getText());
    return null;
  }

  @Override
  public AstNode visitAdresse(AdresseContext ctx) {
    return new AddressNode(Integer.parseInt(ctx.NOMBRE().getText()));
  }

  @Override
  public AstNode visitType(TypeContext ctx) {
    return new TypeNode(ctx.getText());
  }

  @Override
  public AstNode visitSorte(SorteContext ctx) {
    return new ObjNode(ctx.getText());
  }

  /* Helper visitors */

  public AstNode visitNew(JajaCodeParser.InstrContext ctx) {
    IdentNode identNode = (IdentNode) visit(ctx.ident());
    TypeNode typeNode = (TypeNode) visit(ctx.type());
    ObjNode objNode = (ObjNode) visit(ctx.sorte());
    ValueNode valueNode = (ValueNode) visit(ctx.valeur());
    return new NewNode(identNode, typeNode, objNode, valueNode);
  }

  public AstNode visitNewArray(JajaCodeParser.InstrContext ctx) {
    IdentNode identNode = (IdentNode) visit(ctx.ident());
    TypeNode typeNode = (TypeNode) visit(ctx.type());
    return new NewArrayNode(identNode, typeNode);
  }

  public AstNode visitInvoke(JajaCodeParser.InstrContext ctx) {
    IdentNode identNode = (IdentNode) visit(ctx.ident());
    return new InvokeNode(identNode);
  }

  public AstNode visitLength(JajaCodeParser.InstrContext ctx) {
    IdentNode identNode = (IdentNode) visit(ctx.ident());
    return new LengthNode(identNode);
  }

  public AstNode visitPush(JajaCodeParser.InstrContext ctx) {
    ValueNode valueNode = (ValueNode) visit(ctx.valeur());
    return new PushNode(valueNode);
  }

  public AstNode visitLoad(JajaCodeParser.InstrContext ctx) {
    IdentNode identNode = (IdentNode) visit(ctx.ident());
    return new LoadNode(identNode, ctx.LOAD() == null);
  }

  public AstNode visitStore(JajaCodeParser.InstrContext ctx) {
    IdentNode identNode = (IdentNode) visit(ctx.ident());
    return new StoreNode(identNode, ctx.STORE() == null);
  }

  public AstNode visitIf(JajaCodeParser.InstrContext ctx) {
    AddressNode addressNode = (AddressNode) visit(ctx.adresse());
    return new IfNode(addressNode);
  }

  public AstNode visitGoto(JajaCodeParser.InstrContext ctx) {
    AddressNode addressNode = (AddressNode) visit(ctx.adresse());
    return new GotoNode(addressNode);
  }

  public AstNode visitInc(JajaCodeParser.InstrContext ctx) {
    IdentNode identNode = (IdentNode) visit(ctx.ident());
    return new IncNode(identNode, ctx.INC() == null);
  }
}