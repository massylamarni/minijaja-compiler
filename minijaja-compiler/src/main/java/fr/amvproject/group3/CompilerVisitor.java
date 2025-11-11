package fr.amvproject.group3;

import java.util.ArrayList;
import java.util.List;

import fr.amvproject.group3.jjc.ast.nodes.finals.AddressNode;
import fr.amvproject.group3.jjc.ast.nodes.finals.BoolLiteralNode;
import fr.amvproject.group3.jjc.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.jjc.ast.nodes.finals.IntLiteralNode;
import fr.amvproject.group3.jjc.ast.nodes.finals.StringLiteralNode;
import fr.amvproject.group3.jjc.ast.nodes.instrs.GotoNode;
import fr.amvproject.group3.jjc.ast.nodes.instrs.NewArrayNode;
import fr.amvproject.group3.jjc.ast.nodes.instrs.NewNode;
import fr.amvproject.group3.jjc.ast.nodes.instrs.PushNode;
import fr.amvproject.group3.jjc.ast.nodes.instrs.StoreNode;
import fr.amvproject.group3.jjc.ast.nodes.intermediate.InstrNode;
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
import fr.amvproject.group3.mjj.ast.nodes.finals.ArrayAccessNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.ParamNode;
import fr.amvproject.group3.jjc.ast.nodes.instrs.IfNode;
import fr.amvproject.group3.jjc.ast.nodes.instrs.IncNode;
import fr.amvproject.group3.jjc.ast.nodes.instrs.LoadNode;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.DeclNode;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.ExpNode;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.VarDeclNode;
import fr.amvproject.group3.mjj.utils.NodeOperations;

@SuppressWarnings("java:S6539") // Visitor pattern: large by design
public class CompilerVisitor implements fr.amvproject.group3.mjj.ast.AstVisitor<List<ClassNode>> {
  private int instrCount = 0;
  private int instrBlockCount = 0;

  // A Jjc program is a list of classes
  private ClassNode newClass(InstrNode node) {
    return new ClassNode(new AddressNode(++instrCount), node);
  }

  // Mjj type to Jjc
  private TypeNode typeMapper(fr.amvproject.group3.mjj.ast.nodes.other.TypeNode node) {
    if (node.isInt()) return new TypeNode("entier");
    return new TypeNode("boolean");
  }

  private TypeNode typeMapper(fr.amvproject.group3.mjj.ast.nodes.other.TypeMethNode node) {
    if (node.isInt()) return new TypeNode("entier");
    if (node.isBoolean()) return new TypeNode("boolean");
    return new TypeNode("vide");
  }

  // Mjj op to Jjc
  private BinaryOpNode opMapper(fr.amvproject.group3.mjj.ast.nodes.other.BinaryOpNode node) {
    if (node.isAdd()) return new BinaryOpNode("add");
    if (node.isSub()) return new BinaryOpNode("sub");
    if (node.isMul()) return new BinaryOpNode("mul");
    if (node.isDiv()) return new BinaryOpNode("div");
    if (node.isEq()) return new BinaryOpNode("cmp");
    if (node.isGt()) return new BinaryOpNode("sup");
    if (node.isAnd()) return new BinaryOpNode("and");
    return new BinaryOpNode("or");
  }

  private UnaryOpNode opMapper(fr.amvproject.group3.mjj.ast.nodes.other.UnaryOpNode node) {
    if (node.isNeg()) return new UnaryOpNode("neg");
    return new UnaryOpNode("not");
  }

  // --------- Nodes ---------
  @Override
  public List<ClassNode> visitProgram(fr.amvproject.group3.mjj.ast.nodes.program.ProgramNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    for (fr.amvproject.group3.mjj.ast.nodes.program.ClassNode classNode : node.getClasses()) {
      classNodes.addAll(visitClass(classNode));
    }

    instrBlockCount = instrCount;
    return classNodes;
  }

  @Override
  public List<ClassNode> visitClass(fr.amvproject.group3.mjj.ast.nodes.program.ClassNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    List<ClassNode> pdss = node.getDecls().accept(this);
    List<ClassNode> pmma = node.getMain().accept(this);
    List<ClassNode> prdss = visitRDecl(pdss.size());

    classNodes.add(newClass(new InitLiteral()));
    classNodes.addAll(pdss);
    classNodes.addAll(pmma);
    classNodes.addAll(prdss);
    classNodes.add(newClass(new PopLiteral()));
    classNodes.add(newClass(new JcstopLiteral()));
    instrBlockCount = instrCount;
    return classNodes;
  }

  @Override
  public List<ClassNode> visitDecls(fr.amvproject.group3.mjj.ast.nodes.abstracts.DeclsNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    for (DeclNode declNode : node.getDecls()) {
      classNodes.addAll(declNode.accept(this));
    }
    instrBlockCount = instrCount;
    return classNodes;
  }

  @Override
  public List<ClassNode> visitDecl(fr.amvproject.group3.mjj.ast.nodes.intermediate.DeclNode node) {
    return null;
  }

  @Override
  public List<ClassNode> visitVars(fr.amvproject.group3.mjj.ast.nodes.abstracts.VarsNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    for (VarDeclNode varDeclNode : node.getList()) {
      classNodes.addAll(varDeclNode.accept(this));
    }
    instrBlockCount = instrCount;
    return classNodes;
  }

  @Override
  public List<ClassNode> visitVarDecl(fr.amvproject.group3.mjj.ast.nodes.intermediate.VarDeclNode node) {
    return null;
  }

  @Override
  public List<ClassNode> visitVar(fr.amvproject.group3.mjj.ast.nodes.decls.VarNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    List<ClassNode> pe = node.getInitExp() != null ? node.getInitExp().accept(this) : new ArrayList<>();

    classNodes.addAll(pe);
    classNodes.add(newClass(new NewNode(new IdentNode(node.getIdent().getName(), node.getIdent().getLine()),
        typeMapper(node.getType()), new ObjNode("var"), new IntLiteralNode(0))));

    instrBlockCount = instrCount;
        return classNodes;
  }

  @Override
  public List<ClassNode> visitCst(fr.amvproject.group3.mjj.ast.nodes.decls.CstNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    List<ClassNode> pe = node.getInitExp() != null ? node.getInitExp().accept(this) : new ArrayList<>();

    classNodes.addAll(pe);
    classNodes.add(newClass(new NewNode(new IdentNode(node.getIdent().getName(), node.getIdent().getLine()),
        typeMapper(node.getType()), new ObjNode("cst"), new IntLiteralNode(0))));

    instrBlockCount = instrCount;
        return classNodes;
  }

  @Override
  public List<ClassNode> visitTable(fr.amvproject.group3.mjj.ast.nodes.decls.TableNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    List<ClassNode> pe = node.getSizeExp().accept(this);

    classNodes.addAll(pe);
    classNodes.add(newClass(new NewArrayNode(new IdentNode(node.getIdent().getName(), node.getIdent().getLine()),
        typeMapper(node.getType()))));

    instrBlockCount = instrCount;
        return classNodes;
  }

  @Override
  public List<ClassNode> visitMethod(fr.amvproject.group3.mjj.ast.nodes.decls.MethodNode node) { // TODO What is methodeR ?
    List<ClassNode> classNodes = new ArrayList<>();
    List<ClassNode> pens = node.getParams().accept(this);
    List<ClassNode> pdvs = node.getVars().accept(this);
    List<ClassNode> piss = node.getInstrs().accept(this);
    List<ClassNode> prdvs = visitRDecl(pdvs.size());

    classNodes.add(newClass(new PushNode(new IntLiteralNode(instrBlockCount + 3))));
    classNodes.add(newClass(new NewNode(new IdentNode(node.getIdent().getName(), node.getIdent().getLine()),
        typeMapper(node.getReturnType()), new ObjNode("meth"), new IntLiteralNode(0))));
    classNodes.add(newClass(new GotoNode(new AddressNode(instrBlockCount + pens.size() + pdvs.size() + piss.size() + prdvs.size() + 5))));
    classNodes.addAll(pens);
    classNodes.addAll(pdvs);
    classNodes.addAll(piss);
    classNodes.addAll(prdvs);
    newClass(new SwapLiteral());
    newClass(new ReturnLiteral());
    instrBlockCount = instrCount;
    return classNodes;
  }

  @Override
  public List<ClassNode> visitMain(fr.amvproject.group3.mjj.ast.nodes.decls.MainNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    List<ClassNode> pdvs = node.getVars().accept(this);
    List<ClassNode> piss = node.getInstrs().accept(this);
    List<ClassNode> prdvs = visitRDecl(pdvs.size());

    classNodes.addAll(pdvs);
    classNodes.addAll(piss);
    newClass(new PushNode(new IntLiteralNode(0)));
    classNodes.addAll(prdvs);
    instrBlockCount = instrCount;
    return classNodes;
  }

  @Override
  public List<ClassNode> visitParams(fr.amvproject.group3.mjj.ast.nodes.abstracts.ParamsNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    for (ParamNode paramNode : node.getList()) {
      classNodes.addAll(paramNode.accept(this));
    }
    instrBlockCount = instrCount;
    return classNodes;
  }

  @Override
  public List<ClassNode> visitParam(fr.amvproject.group3.mjj.ast.nodes.finals.ParamNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    classNodes.add(newClass(new NewNode(new IdentNode(node.getIdent().getName(), node.getIdent().getLine()),
        typeMapper(node.getType()), new ObjNode("var"), new IntLiteralNode(0)))); // TODO What is k ?
    instrBlockCount = instrCount;
        return classNodes;
  }

  @Override
  public List<ClassNode> visitInstrs(fr.amvproject.group3.mjj.ast.nodes.abstracts.InstrsNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    for (fr.amvproject.group3.mjj.ast.nodes.intermediate.InstrNode instrNode : node.getList()) {
      classNodes.addAll(instrNode.accept(this));
    }
    instrBlockCount = instrCount;
    return classNodes;
  }

  @Override
  public List<ClassNode> visitListExp(fr.amvproject.group3.mjj.ast.nodes.abstracts.ListExpNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    for (ExpNode expNode : node.getList()) {
      classNodes.addAll(expNode.accept(this));
    }
    instrBlockCount = instrCount;
    return classNodes;
  }

  @Override
  public List<ClassNode> visitBinaryExp(fr.amvproject.group3.mjj.ast.nodes.exprs.BinaryExpNode node) {
    List<ClassNode> classNodes = new ArrayList<>();

    classNodes.addAll(node.getLeftExp().accept(this));
    classNodes.addAll(node.getRightExp().accept(this));
    classNodes.addAll(node.getOp().accept(this));

    instrBlockCount = instrCount;
    return classNodes;
  }

  @Override
  public List<ClassNode> visitUnaryExp(fr.amvproject.group3.mjj.ast.nodes.exprs.UnaryExpNode node) {
    List<ClassNode> classNodes = new ArrayList<>();

    classNodes.addAll(node.getExp().accept(this));
    classNodes.addAll(node.getOp().accept(this));

    instrBlockCount = instrCount;
    return classNodes;
  }

  @Override
  public List<ClassNode> visitAssign(fr.amvproject.group3.mjj.ast.nodes.instrs.AssignNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    if (node.getLeftValue() instanceof ArrayAccessNode arrayAccessNode) {
      List<ClassNode> pe = arrayAccessNode.getIndexExp().accept(this);
      List<ClassNode> pe1 = node.getExp().accept(this);

      classNodes.addAll(pe);
      classNodes.addAll(pe1);
      classNodes.add(newClass(new StoreNode(new IdentNode(arrayAccessNode.getIdent().getName(),
          arrayAccessNode.getIdent().getLine()), true)));
    } else {
      List<ClassNode> pe = node.getExp().accept(this);

      classNodes.addAll(pe);
      classNodes.add(newClass(new StoreNode(new IdentNode(((fr.amvproject.group3.mjj.ast.nodes.finals.IdentNode) node.getLeftValue()).getName(), ((fr.amvproject.group3.mjj.ast.nodes.finals.IdentNode) node.getLeftValue()).getLine()), false)));
    }
    instrBlockCount = instrCount;
    return classNodes;
  }

  @Override
  public List<ClassNode> visitAddAssign(fr.amvproject.group3.mjj.ast.nodes.instrs.AddAssignNode node) {
    NodeOperations no = new NodeOperations();
    return visitAssign(no.addAssignToAssign(node));
  }

  public List<ClassNode> visitInc(fr.amvproject.group3.mjj.ast.nodes.instrs.IncNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    if (node.getLeftValueNode() instanceof ArrayAccessNode arrayAccessNode) {
      List<ClassNode> pe = arrayAccessNode.getIndexExp().accept(this);

      classNodes.addAll(pe);
      classNodes.add(newClass(new PushNode(new IntLiteralNode(1))));
      classNodes.add(newClass(new IncNode(new IdentNode(arrayAccessNode.getIdent().getName(), arrayAccessNode.getIdent().getLine()), true)));
    } else {
      classNodes.add(newClass(new PushNode(new IntLiteralNode(1))));
      classNodes.add(newClass(new IncNode(new IdentNode(((fr.amvproject.group3.mjj.ast.nodes.finals.IdentNode) node.getLeftValueNode()).getName(), ((fr.amvproject.group3.mjj.ast.nodes.finals.IdentNode) node.getLeftValueNode()).getLine()), true)));
    }
    instrBlockCount = instrCount;
    return classNodes;
  }

  @Override
  public List<ClassNode> visitLength(fr.amvproject.group3.mjj.ast.nodes.exprs.LengthNode node) {
    List<ClassNode> classNodes = new ArrayList<>(); // TODO Unknown rule
    instrBlockCount = instrCount;
    return classNodes;
  }

  @Override
  public List<ClassNode> visitIf(fr.amvproject.group3.mjj.ast.nodes.instrs.IfNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    List<ClassNode> pe = node.getCondExp().accept(this);
    List<ClassNode> ps1 = node.getElseInstrs() != null ? node.getElseInstrs().accept(this) : new ArrayList<>();
    List<ClassNode> ps = node.getThenInstrs() != null ? node.getThenInstrs().accept(this) : new ArrayList<>();

    classNodes.addAll(pe);
    classNodes.add(newClass(new IfNode(new AddressNode(instrBlockCount + pe.size() + ps1.size() + 2))));
    classNodes.addAll(ps1);
    classNodes.add(newClass(new GotoNode(new AddressNode(instrBlockCount + pe.size() + ps1.size() + ps.size() + 2))));
    classNodes.addAll(ps);
    instrBlockCount = instrCount;
    return classNodes;
  }

  @Override
  public List<ClassNode> visitWhile(fr.amvproject.group3.mjj.ast.nodes.instrs.WhileNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    List<ClassNode> pe = node.getCondExp().accept(this);
    List<ClassNode> piss = node.getInstrs() != null ? node.getInstrs().accept(this) : new ArrayList<>();

    classNodes.addAll(pe);
    classNodes.add(newClass(new NopLiteral()));
    classNodes.add(newClass(new IfNode(new AddressNode(instrBlockCount + pe.size() + piss.size()))));
    classNodes.addAll(piss);
    newClass(new GotoNode(new AddressNode(instrBlockCount)));
    instrBlockCount = instrCount;
    return classNodes;
  }

  @Override
  public List<ClassNode> visitWrite(fr.amvproject.group3.mjj.ast.nodes.instrs.WriteNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    List<ClassNode> pe = node.getExp().accept(this);

    classNodes.addAll(pe);
    classNodes.add(newClass(new WriteLiteral(node.isLn())));
    instrBlockCount = instrCount;
    return classNodes;
  }

  @Override
  public List<ClassNode> visitReturn(fr.amvproject.group3.mjj.ast.nodes.instrs.ReturnNode node) {
    return node.getExp().accept(this);
  }

  @Override
  public List<ClassNode> visitIntLiteral(fr.amvproject.group3.mjj.ast.nodes.finals.IntLiteralNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    classNodes.add(newClass(new PushNode(new IntLiteralNode(node.getValue()))));
    instrBlockCount = instrCount;
    return classNodes;
  }

  @Override
  public List<ClassNode> visitBoolLiteral(fr.amvproject.group3.mjj.ast.nodes.finals.BoolLiteralNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    classNodes.add(newClass(new PushNode(new BoolLiteralNode(node.getValue()))));
    instrBlockCount = instrCount;
    return classNodes;
  }

  @Override
  public List<ClassNode> visitStringLiteral(fr.amvproject.group3.mjj.ast.nodes.finals.StringLiteralNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    classNodes.add(newClass(new PushNode(new StringLiteralNode(node.getValue()))));
    instrBlockCount = instrCount;
    return classNodes;
  }

  @Override
  public List<ClassNode> visitArrayAccess(fr.amvproject.group3.mjj.ast.nodes.finals.ArrayAccessNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    List<ClassNode> pe = node.getIndexExp().accept(this);

    classNodes.addAll(pe);
    newClass(new LoadNode(new IdentNode(node.getIdent().getName(), node.getIdent().getLine()), true));
    instrBlockCount = instrCount;
    return classNodes;
  }

  @Override
  public List<ClassNode> visitCallExp(fr.amvproject.group3.mjj.ast.nodes.exprs.CallExpNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    instrBlockCount = instrCount;
    return classNodes;
  }

  @Override
  public List<ClassNode> visitCallInstr(fr.amvproject.group3.mjj.ast.nodes.instrs.CallInstrNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    instrBlockCount = instrCount;
    return classNodes;
  }

  @Override
  public List<ClassNode> visitType(fr.amvproject.group3.mjj.ast.nodes.other.TypeNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    instrBlockCount = instrCount;
    return classNodes;
  }

  @Override
  public List<ClassNode> visitTypeMeth(fr.amvproject.group3.mjj.ast.nodes.other.TypeMethNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    instrBlockCount = instrCount;
    return classNodes;
  }

  @Override
  public List<ClassNode> visitBinaryOp(fr.amvproject.group3.mjj.ast.nodes.other.BinaryOpNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    classNodes.add(newClass(opMapper(node)));
    instrBlockCount = instrCount;
    return classNodes;
  }

  @Override
  public List<ClassNode> visitUnaryOp(fr.amvproject.group3.mjj.ast.nodes.other.UnaryOpNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    classNodes.add(newClass(opMapper(node)));
    instrBlockCount = instrCount;
    return classNodes;
  }

  @Override
  public List<ClassNode> visitIdent(fr.amvproject.group3.mjj.ast.nodes.finals.IdentNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    classNodes.add(newClass(new LoadNode(new IdentNode(node.getName(), node.getLine()), false)));
    instrBlockCount = instrCount;
    return classNodes;
  }

  public List<ClassNode> visitRDecl(int size) { // rvar, rcst, rmethode, rdecls, rvars, rlexp
    List<ClassNode> classNodes = new ArrayList<>();
    for (int i = 0; i < size; i++) {
      classNodes.add(newClass(new SwapLiteral()));
      classNodes.add(newClass(new PopLiteral()));
    }
    instrBlockCount = instrCount;
    return classNodes;
  }
}
