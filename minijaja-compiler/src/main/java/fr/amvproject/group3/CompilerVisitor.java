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
import fr.amvproject.group3.jjc.ast.nodes.instrs.IfNode;
import fr.amvproject.group3.jjc.ast.nodes.instrs.IncNode;
import fr.amvproject.group3.jjc.ast.nodes.instrs.InvokeNode;
import fr.amvproject.group3.jjc.ast.nodes.instrs.LengthNode;
import fr.amvproject.group3.jjc.ast.nodes.instrs.LoadNode;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.DeclNode;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.ExpNode;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.VarDeclNode;

@SuppressWarnings("java:S6539") // Visitor pattern: large by design
public class CompilerVisitor implements fr.amvproject.group3.mjj.ast.AstVisitor<List<ClassNode>> {
  private int instrCount = 0;
  private int counterSilencer = 0;  // Used to get the instruction count before adding them
  private int k = 0; // Used for parameters

  public void reset() {
    instrCount = 0;
    counterSilencer = 0;
    k = 0;
  }

  // A Jjc program is a list of classes
  private ClassNode newClass(InstrNode node) {
    return new ClassNode(new AddressNode(String.valueOf(counterSilencer == 0 ? ++instrCount : instrCount), 0, 0), node);
  }

  // Mjj type to Jjc
  private TypeNode typeMapper(fr.amvproject.group3.mjj.ast.nodes.other.TypeNode node) {
    if (node.isInt()) return new TypeNode("entier", 0, 0);
    return new TypeNode("boolean", 0, 0);
  }

  private TypeNode typeMapper(fr.amvproject.group3.mjj.ast.nodes.other.TypeMethNode node) {
    if (node.isInt()) return new TypeNode("entier", 0, 0);
    if (node.isBoolean()) return new TypeNode("boolean", 0, 0);
    return new TypeNode("vide", 0, 0);
  }

  private ObjNode objMapper(String obj) {
    if (!obj.equals("var") && !obj.equals("cst") && !obj.equals("vcst") && !obj.equals("tab") && !obj.equals("meth")) throw new IllegalArgumentException("ObjType incorrect");
    return new ObjNode(obj, 0, 0);
  }

  // Mjj op to Jjc
  private BinaryOpNode opMapper(fr.amvproject.group3.mjj.ast.nodes.other.BinaryOpNode node) {
    if (node.isAdd()) return new BinaryOpNode("add", 0, 0);
    if (node.isSub()) return new BinaryOpNode("sub", 0, 0);
    if (node.isMul()) return new BinaryOpNode("mul", 0, 0);
    if (node.isDiv()) return new BinaryOpNode("div", 0, 0);
    if (node.isEq()) return new BinaryOpNode("cmp", 0, 0);
    if (node.isGt()) return new BinaryOpNode("sup", 0, 0);
    if (node.isAnd()) return new BinaryOpNode("and", 0, 0);
    return new BinaryOpNode("or", 0, 0);
  }

  private UnaryOpNode opMapper(fr.amvproject.group3.mjj.ast.nodes.other.UnaryOpNode node) {
    if (node.isNeg()) return new UnaryOpNode("neg", 0, 0);
    return new UnaryOpNode("not", 0, 0);
  }

  // --------- Nodes ---------
  @Override
  public List<ClassNode> visitProgram(fr.amvproject.group3.mjj.ast.nodes.program.ProgramNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    for (fr.amvproject.group3.mjj.ast.nodes.program.ClassNode classNode : node.getClasses()) {
      classNodes.addAll(visitClass(classNode));
    }

    return classNodes;
  }

  @Override
  public List<ClassNode> visitClass(fr.amvproject.group3.mjj.ast.nodes.program.ClassNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    classNodes.add(newClass(new InitLiteral()));
    List<ClassNode> pdss = node.getDecls().accept(this);
    List<ClassNode> pmma = node.getMain().accept(this);
    List<ClassNode> prdss = visitRDecl(node.getDecls().getList().size());
    classNodes.addAll(pdss);
    classNodes.addAll(pmma);
    classNodes.addAll(prdss);
    classNodes.add(newClass(new PopLiteral()));
    classNodes.add(newClass(new JcstopLiteral()));
    return classNodes;
  }

  @Override
  public List<ClassNode> visitDecls(fr.amvproject.group3.mjj.ast.nodes.abstracts.DeclsNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    for (DeclNode declNode : node.getList()) {
      classNodes.addAll(declNode.accept(this));
    }
    return classNodes;
  }

  @Override
  public List<ClassNode> visitVars(fr.amvproject.group3.mjj.ast.nodes.abstracts.VarsNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    for (VarDeclNode varDeclNode : node.getList()) {
      classNodes.addAll(varDeclNode.accept(this));
    }
    return classNodes;
  }

  @Override
  public List<ClassNode> visitVar(fr.amvproject.group3.mjj.ast.nodes.decls.VarNode node) {
    List<ClassNode> classNodes = new ArrayList<>();

    List<ClassNode> pe = node.getInitExp() != null ? node.getInitExp().accept(this) : new ArrayList<>();
    classNodes.addAll(pe);
    classNodes.add(newClass(new NewNode(new IdentNode(node.getIdent().getIdentToken()),
        typeMapper(node.getType()), objMapper("var"), new IntLiteralNode("0", 0, 0))));

    return classNodes;
  }

  @Override
  public List<ClassNode> visitCst(fr.amvproject.group3.mjj.ast.nodes.decls.CstNode node) {
    List<ClassNode> classNodes = new ArrayList<>();

    List<ClassNode> pe = node.getInitExp() != null ? node.getInitExp().accept(this) : new ArrayList<>();
    classNodes.addAll(pe);
    classNodes.add(newClass(new NewNode(new IdentNode(node.getIdent().getIdentToken()),
        typeMapper(node.getType()), objMapper(node.isVcst() ? "vcst" : "cst"), new IntLiteralNode("0", 0, 0))));

    return classNodes;
  }

  @Override
  public List<ClassNode> visitTable(fr.amvproject.group3.mjj.ast.nodes.decls.TableNode node) {
    List<ClassNode> classNodes = new ArrayList<>();

    List<ClassNode> pe = node.getSizeExp().accept(this);
    classNodes.addAll(pe);
    classNodes.add(newClass(new NewArrayNode(new IdentNode(node.getIdent().getIdentToken()),
        typeMapper(node.getType()))));

    return classNodes;
  }

  @Override
  public List<ClassNode> visitMethod(fr.amvproject.group3.mjj.ast.nodes.decls.MethodNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    int instrBlockCount = instrCount+1;
    counterSilencer++;
    int npens = node.getParams().accept(this).size();
    int npdvs = node.getVars().accept(this).size();
    int npiss = node.getInstrs().accept(this).size();
    int nprdvs = visitRDecl(node.getVars().getList().size()).size();
    counterSilencer--;

    boolean isVoid = node.getReturnType().isVoid();
    classNodes.add(newClass(new PushNode(new IntLiteralNode(String.valueOf(instrBlockCount + 3), 0, 0))));
    classNodes.add(newClass(new NewNode(new IdentNode(node.getIdent().getIdentToken()),
        typeMapper(node.getReturnType()), objMapper("meth"), new IntLiteralNode("0", 0, 0))));
    classNodes.add(newClass(new GotoNode(new AddressNode(String.valueOf(instrBlockCount + npens + npdvs + npiss + nprdvs + (isVoid ? 6 : 5)), 0, 0))));
    List<ClassNode> pens = node.getParams().accept(this);
    List<ClassNode> pdvs = node.getVars().accept(this);
    List<ClassNode> piss = node.getInstrs().accept(this);
    ClassNode pv = isVoid ? newClass(new PushNode(new IntLiteralNode("0", 0, 0))) : null;
    List<ClassNode> prdvs = visitRDecl(node.getVars().getList().size());
    classNodes.addAll(pens);
    classNodes.addAll(pdvs);
    classNodes.addAll(piss);
    if (isVoid) classNodes.add(pv);
    classNodes.addAll(prdvs);
    classNodes.add(newClass(new SwapLiteral()));
    classNodes.add(newClass(new ReturnLiteral()));
    return classNodes;
  }

  @Override
  public List<ClassNode> visitMain(fr.amvproject.group3.mjj.ast.nodes.decls.MainNode node) {
    List<ClassNode> classNodes = new ArrayList<>();

    List<ClassNode> pdvs = node.getVars().accept(this);
    List<ClassNode> piss = node.getInstrs().accept(this);
    classNodes.addAll(pdvs);
    classNodes.addAll(piss);
    classNodes.add(newClass(new PushNode(new IntLiteralNode("0", 0, 0))));
    List<ClassNode> prdvs = visitRDecl(node.getVars().getList().size());
    classNodes.addAll(prdvs);
    return classNodes;
  }

  @Override
  public List<ClassNode> visitParams(fr.amvproject.group3.mjj.ast.nodes.abstracts.ParamsNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    for (int i = node.getList().size() - 1; i >= 0; i--) {
      classNodes.addAll(node.getList().get(i).accept(this));
    }
    k = 0;
    return classNodes;
  }

  @Override
  public List<ClassNode> visitParam(fr.amvproject.group3.mjj.ast.nodes.finals.ParamNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    classNodes.add(newClass(new NewNode(new IdentNode(node.getIdent().getIdentToken()),
        typeMapper(node.getType()), objMapper("var"), new IntLiteralNode(String.valueOf(++k), 0, 0))));
    return classNodes;
  }

  @Override
  public List<ClassNode> visitInstrs(fr.amvproject.group3.mjj.ast.nodes.abstracts.InstrsNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    for (fr.amvproject.group3.mjj.ast.nodes.intermediate.InstrNode instrNode : node.getList()) {
      classNodes.addAll(instrNode.accept(this));
    }
    return classNodes;
  }

  @Override
  public List<ClassNode> visitListExp(fr.amvproject.group3.mjj.ast.nodes.abstracts.ListExpNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    for (ExpNode expNode : node.getList()) {
      classNodes.addAll(expNode.accept(this));
    }
    return classNodes;
  }

  @Override
  public List<ClassNode> visitBinaryExp(fr.amvproject.group3.mjj.ast.nodes.exprs.BinaryExpNode node) {
    List<ClassNode> classNodes = new ArrayList<>();

    classNodes.addAll(node.getLeftExp().accept(this));
    classNodes.addAll(node.getRightExp().accept(this));
    classNodes.addAll(node.getOp().accept(this));

    return classNodes;
  }

  @Override
  public List<ClassNode> visitUnaryExp(fr.amvproject.group3.mjj.ast.nodes.exprs.UnaryExpNode node) {
    List<ClassNode> classNodes = new ArrayList<>();

    classNodes.addAll(node.getExp().accept(this));
    classNodes.addAll(node.getOp().accept(this));

    return classNodes;
  }

  @Override
  public List<ClassNode> visitAssign(fr.amvproject.group3.mjj.ast.nodes.instrs.AssignNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    if (node.getLeftValueNode() instanceof ArrayAccessNode arrayAccessNode) {
      List<ClassNode> pe = arrayAccessNode.getIndexExp().accept(this);
      List<ClassNode> pe1 = node.getExp().accept(this);
      classNodes.addAll(pe);
      classNodes.addAll(pe1);
      classNodes.add(newClass(new StoreNode(new IdentNode(arrayAccessNode.getIdent().getIdentToken()), true)));
    } else {
      List<ClassNode> pe = node.getExp().accept(this);
      classNodes.addAll(pe);
      classNodes.add(newClass(new StoreNode(new IdentNode(((fr.amvproject.group3.mjj.ast.nodes.finals.IdentNode) node.getLeftValueNode()).getIdentToken()), false)));
    }
    return classNodes;
  }

  @Override
  public List<ClassNode> visitAddAssign(fr.amvproject.group3.mjj.ast.nodes.instrs.AddAssignNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    if (node.getLeftValueNode() instanceof ArrayAccessNode arrayAccessNode) {
      List<ClassNode> pe = arrayAccessNode.getIndexExp().accept(this);
      List<ClassNode> pe1 = node.getExp().accept(this);
      classNodes.addAll(pe);
      classNodes.addAll(pe1);
      classNodes.add(newClass(new IncNode(new IdentNode(arrayAccessNode.getIdent().getIdentToken()), true)));
    } else {
      List<ClassNode> pe = node.getExp().accept(this);
      classNodes.addAll(pe);
      classNodes.add(newClass(new IncNode(new IdentNode(((fr.amvproject.group3.mjj.ast.nodes.finals.IdentNode) node.getLeftValueNode()).getIdentToken()), false)));
    }
    return classNodes;
  }

  public List<ClassNode> visitInc(fr.amvproject.group3.mjj.ast.nodes.instrs.IncNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    if (node.getLeftValueNode() instanceof ArrayAccessNode arrayAccessNode) {
      List<ClassNode> pe = arrayAccessNode.getIndexExp().accept(this);

      classNodes.addAll(pe);
      classNodes.add(newClass(new PushNode(new IntLiteralNode("1", 0, 0))));
      classNodes.add(newClass(new IncNode(new IdentNode(arrayAccessNode.getIdent().getIdentToken()), true)));
    } else {
      classNodes.add(newClass(new PushNode(new IntLiteralNode("1", 0, 0))));
      classNodes.add(newClass(new IncNode(new IdentNode(((fr.amvproject.group3.mjj.ast.nodes.finals.IdentNode) node.getLeftValueNode()).getIdentToken()), false)));
    }
    return classNodes;
  }

  @Override
  public List<ClassNode> visitLength(fr.amvproject.group3.mjj.ast.nodes.exprs.LengthNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    classNodes.add(newClass(new LengthNode(new IdentNode(node.getIdent().getIdentToken()))));
    return classNodes;
  }

  @Override
  public List<ClassNode> visitIf(fr.amvproject.group3.mjj.ast.nodes.instrs.IfNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    int instrBlockCount = instrCount+1;
    counterSilencer++;
    int nps1 = node.getElseInstrs() != null ? node.getElseInstrs().accept(this).size() : 0;
    int nps = node.getThenInstrs() != null ? node.getThenInstrs().accept(this).size() : 0;
    counterSilencer--;

    List<ClassNode> pe = node.getCondExp().accept(this);
    classNodes.addAll(pe);
    classNodes.add(newClass(new IfNode(new AddressNode(String.valueOf(instrBlockCount + pe.size() + nps1 + 2), 0, 0))));
    List<ClassNode> ps1 = node.getElseInstrs() != null ? node.getElseInstrs().accept(this) : new ArrayList<>();
    classNodes.addAll(ps1);
    classNodes.add(newClass(new GotoNode(new AddressNode(String.valueOf(instrBlockCount + pe.size() + ps1.size() + nps + 2), 0, 0))));
    List<ClassNode> ps = node.getThenInstrs() != null ? node.getThenInstrs().accept(this) : new ArrayList<>();
    classNodes.addAll(ps);
    return classNodes;
  }

  @Override
  public List<ClassNode> visitWhile(fr.amvproject.group3.mjj.ast.nodes.instrs.WhileNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    int instrBlockCount = instrCount+1;
    counterSilencer++;
    int npiss = node.getInstrs() != null ? node.getInstrs().accept(this).size() : 0;
    counterSilencer--;

    List<ClassNode> pe = node.getCondExp().accept(this);
    classNodes.addAll(pe);
    classNodes.add(newClass(new UnaryOpNode("not", 0, 0)));
    classNodes.add(newClass(new IfNode(new AddressNode(String.valueOf(instrBlockCount + pe.size() + npiss + 3), 0, 0))));
    List<ClassNode> piss = node.getInstrs() != null ? node.getInstrs().accept(this) : new ArrayList<>();
    classNodes.addAll(piss);
    classNodes.add(newClass(new GotoNode(new AddressNode(String.valueOf(instrBlockCount), 0, 0))));
    return classNodes;
  }

  @Override
  public List<ClassNode> visitWrite(fr.amvproject.group3.mjj.ast.nodes.instrs.WriteNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    List<ClassNode> pe = node.getExp().accept(this);
    classNodes.addAll(pe);
    classNodes.add(newClass(new WriteLiteral(node.isLn())));
    return classNodes;
  }

  @Override
  public List<ClassNode> visitReturn(fr.amvproject.group3.mjj.ast.nodes.instrs.ReturnNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    classNodes.addAll(node.getExp().accept(this));
    return classNodes;
  }

  @Override
  public List<ClassNode> visitIntLiteral(fr.amvproject.group3.mjj.ast.nodes.finals.IntLiteralNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    classNodes.add(newClass(new PushNode(new IntLiteralNode(String.valueOf(node.getValue()), 0, 0))));
    return classNodes;
  }

  @Override
  public List<ClassNode> visitBoolLiteral(fr.amvproject.group3.mjj.ast.nodes.finals.BoolLiteralNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    classNodes.add(newClass(new PushNode(new BoolLiteralNode(String.valueOf(node.getValue()), 0, 0))));
    return classNodes;
  }

  @Override
  public List<ClassNode> visitStringLiteral(fr.amvproject.group3.mjj.ast.nodes.finals.StringLiteralNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    classNodes.add(newClass(new PushNode(new StringLiteralNode(node.getValue(), 0, 0))));
    return classNodes;
  }

  @Override
  public List<ClassNode> visitArrayAccess(fr.amvproject.group3.mjj.ast.nodes.finals.ArrayAccessNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    List<ClassNode> pe = node.getIndexExp().accept(this);
    classNodes.addAll(pe);
    classNodes.add(newClass(new LoadNode(new IdentNode(node.getIdent().getIdentToken()), true)));
    return classNodes;
  }

  @Override
  public List<ClassNode> visitCallExp(fr.amvproject.group3.mjj.ast.nodes.exprs.CallExpNode node) {
    List<ClassNode> classNodes = new ArrayList<>();

    List<ClassNode> plexp = node.getListExp().accept(this);
    classNodes.addAll(plexp);
    classNodes.add(newClass(new InvokeNode(new IdentNode(node.getIdent().getIdentToken()))));
    List<ClassNode> prlexp = visitRDecl(node.getListExp().getList().size());
    classNodes.addAll(prlexp);

    return classNodes;
  }

  @Override
  public List<ClassNode> visitCallInstr(fr.amvproject.group3.mjj.ast.nodes.instrs.CallInstrNode node) {
    List<ClassNode> classNodes = new ArrayList<>();

    List<ClassNode> plexp = node.getListExp().accept(this);
    classNodes.addAll(plexp);
    classNodes.add(newClass(new InvokeNode(new IdentNode(node.getIdent().getIdentToken()))));
    List<ClassNode> prlexp = visitRDecl(node.getListExp().getList().size());
    classNodes.addAll(prlexp);
    classNodes.add(newClass(new PopLiteral()));

    return classNodes;
  }

  @Override
  public List<ClassNode> visitType(fr.amvproject.group3.mjj.ast.nodes.other.TypeNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    return classNodes;
  }

  @Override
  public List<ClassNode> visitTypeMeth(fr.amvproject.group3.mjj.ast.nodes.other.TypeMethNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    return classNodes;
  }

  @Override
  public List<ClassNode> visitBinaryOp(fr.amvproject.group3.mjj.ast.nodes.other.BinaryOpNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    classNodes.add(newClass(opMapper(node)));
    return classNodes;
  }

  @Override
  public List<ClassNode> visitUnaryOp(fr.amvproject.group3.mjj.ast.nodes.other.UnaryOpNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    classNodes.add(newClass(opMapper(node)));
    return classNodes;
  }

  @Override
  public List<ClassNode> visitIdent(fr.amvproject.group3.mjj.ast.nodes.finals.IdentNode node) {
    List<ClassNode> classNodes = new ArrayList<>();
    classNodes.add(newClass(new LoadNode(new IdentNode(node.getIdentToken()), false)));
    return classNodes;
  }

  public List<ClassNode> visitRDecl(int size) { // rvar, rcst, rmethode, rdecls, rvars, rlexp
    List<ClassNode> classNodes = new ArrayList<>();
    for (int i = 0; i < size; i++) {
      classNodes.add(newClass(new SwapLiteral()));
      classNodes.add(newClass(new PopLiteral()));
    }
    return classNodes;
  }
}
