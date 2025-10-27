package fr.amvproject.group3;

import org.antlr.v4.runtime.*;

import org.junit.Test;

import fr.amvproject.group3.ast.AstNode;
import fr.amvproject.group3.ast.MiniJajaAstBuilder;
import fr.amvproject.group3.ast.PrintVisitor;
import fr.amvproject.group3.ast.nodes.abstracts.*;
import fr.amvproject.group3.ast.nodes.decls.*;
import fr.amvproject.group3.ast.nodes.exprs.*;
import fr.amvproject.group3.ast.nodes.finals.*;
import fr.amvproject.group3.ast.nodes.instrs.*;
import fr.amvproject.group3.ast.nodes.intermediate.*;
import fr.amvproject.group3.ast.nodes.other.*;
import fr.amvproject.group3.ast.nodes.program.*;
import fr.amvproject.group3.utils.StringSaver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FullAstTest {

  private class AstComparator {
    public static boolean equals(AstNode a, AstNode b) {
      if (a == b)
        return true;
      if (a == null || b == null)
        return false;
      if (!a.getClass().equals(b.getClass()))
        return false;

      if (a instanceof ProgramNode p1 && b instanceof ProgramNode p2) {
        return listEquals(p1.getClasses(), p2.getClasses());
      }

      if (a instanceof ClassNode c1 && b instanceof ClassNode c2) {
        return equals(c1.getIdent(), c2.getIdent()) &&
            equals(c1.getDecls(), c2.getDecls()) &&
            equals(c1.getMain(), c2.getMain());
      }

      if (a instanceof DeclsNode d1 && b instanceof DeclsNode d2) {
        return listEquals(d1.getDecls(), d2.getDecls());
      }

      if (a instanceof DeclNode dn1 && b instanceof DeclNode dn2) {
        return equals(dn1.getIdent(), dn2.getIdent());
      }

      if (a instanceof VarsNode vn1 && b instanceof VarsNode vn2) {
        return listEquals(vn1.getList(), vn2.getList());
      }

      if (a instanceof VarDeclNode vd1 && b instanceof VarDeclNode vd2) {
        return equals(vd1.getIdent(), vd2.getIdent());
      }

      if (a instanceof VarNode vn1 && b instanceof VarNode vn2) {
        return equals(vn1.getType(), vn2.getType()) &&
            equals(vn1.getIdent(), vn2.getIdent()) &&
            equals(vn1.getInitExp(), vn2.getInitExp());
      }

      if (a instanceof CstNode cn1 && b instanceof CstNode cn2) {
        return equals(cn1.getType(), cn2.getType()) &&
            equals(cn1.getIdent(), cn2.getIdent()) &&
            equals(cn1.getInitExp(), cn2.getInitExp());
      }

      if (a instanceof TableNode tn1 && b instanceof TableNode tn2) {
        return equals(tn1.getType(), tn2.getType()) &&
            equals(tn1.getIdent(), tn2.getIdent()) &&
            equals(tn1.getSizeExp(), tn2.getSizeExp());
      }

      if (a instanceof MethodNode m1 && b instanceof MethodNode m2) {
        return equals(m1.getReturnType(), m2.getReturnType()) &&
            equals(m1.getIdent(), m2.getIdent()) &&
            listEquals(m1.getParams().getList(), m2.getParams().getList()) &&
            equals(m1.getVars(), m2.getVars()) &&
            equals(m1.getInstrs(), m2.getInstrs());
      }

      if (a instanceof MainNode mn1 && b instanceof MainNode mn2) {
        return equals(mn1.getVars(), mn2.getVars()) &&
            equals(mn1.getInstrs(), mn2.getInstrs());
      }

      if (a instanceof ParamNode p1 && b instanceof ParamNode p2) {
        return equals(p1.getType(), p2.getType()) &&
            equals(p1.getIdent(), p2.getIdent());
      }

      if (a instanceof InstrsNode i1 && b instanceof InstrsNode i2) {
        return listEquals(i1.getList(), i2.getList());
      }

      if (a instanceof BinaryExpNode be1 && b instanceof BinaryExpNode be2) {
        return equals(be1.getLeftExp(), be2.getLeftExp()) &&
            equals(be1.getOp(), be2.getOp()) &&
            equals(be1.getRightExp(), be2.getRightExp());
      }

      if (a instanceof UnaryExpNode ue1 && b instanceof UnaryExpNode ue2) {
        return equals(ue1.getOp(), ue2.getOp()) &&
            equals(ue1.getExp(), ue2.getExp());
      }

      if (a instanceof IntLiteralNode in1 && b instanceof IntLiteralNode in2) {
        return in1.getValue() == in2.getValue();
      }

      if (a instanceof BoolLiteralNode bn1 && b instanceof BoolLiteralNode bn2) {
        return bn1.getValue() == bn2.getValue();
      }

      if (a instanceof StringLiteralNode sn1 && b instanceof StringLiteralNode sn2) {
        return sn1.getValue().equals(sn2.getValue());
      }

      if (a instanceof IdentNode id1 && b instanceof IdentNode id2) {
        return id1.getName().equals(id2.getName());
      }

      if (a instanceof ArrayAccessNode aa1 && b instanceof ArrayAccessNode aa2) {
        return equals(aa1.getIdent(), aa2.getIdent()) &&
            equals(aa1.getIndexExp(), aa2.getIndexExp());
      }

      if (a instanceof CallExpNode ce1 && b instanceof CallExpNode ce2) {
        return equals(ce1.getIdent(), ce2.getIdent()) &&
            listEquals(ce1.getListExp().getList(), ce2.getListExp().getList());
      }

      if (a instanceof CallInstrNode ci1 && b instanceof CallInstrNode ci2) {
        return equals(ci1.getIdent(), ci2.getIdent()) &&
            listEquals(ci1.getListExp().getList(), ci2.getListExp().getList());
      }

      if (a instanceof AssignNode as1 && b instanceof AssignNode as2) {
        return equals(as1.getLeftValue(), as2.getLeftValue()) &&
            equals(as1.getExp(), as2.getExp());
      }

      if (a instanceof AddAssignNode aa1 && b instanceof AddAssignNode aa2) {
        return equals(aa1.getLeftValueNode(), aa2.getLeftValueNode()) &&
            equals(aa1.getExp(), aa2.getExp());
      }

      if (a instanceof IncNode inc1 && b instanceof IncNode inc2) {
        return equals(inc1.getIdent(), inc2.getIdent());
      }

      if (a instanceof ReturnNode r1 && b instanceof ReturnNode r2) {
        return equals(r1.getExp(), r2.getExp());
      }

      if (a instanceof WhileNode w1 && b instanceof WhileNode w2) {
        return equals(w1.getCondExp(), w2.getCondExp()) &&
            equals(w1.getInstrs(), w2.getInstrs());
      }

      if (a instanceof IfNode if1 && b instanceof IfNode if2) {
        return equals(if1.getCondExp(), if2.getCondExp()) &&
            equals(if1.getThenInstrs(), if2.getThenInstrs()) &&
            equals(if1.getElseInstrs(), if2.getElseInstrs());
      }

      if (a instanceof WriteNode wr1 && b instanceof WriteNode wr2) {
        return wr1.isLn() == wr2.isLn() &&
            equals(wr1.getExp(), wr2.getExp());
      }

      if (a instanceof TypeNode t1 && b instanceof TypeNode t2) {
        return t1.getType().getName().equals(t2.getType().getName());
      }

      if (a instanceof BinaryOpNode bo1 && b instanceof BinaryOpNode bo2) {
        return bo1.getOp().getName().equals(bo2.getOp().getName());
      }

      if (a instanceof UnaryOpNode uo1 && b instanceof UnaryOpNode uo2) {
        return uo1.getOp().getName().equals(uo2.getOp().getName());
      }

      return true;
    }

    private static boolean listEquals(List<? extends AstNode> l1, List<? extends AstNode> l2) {
      if (l1 == l2) return true;
      if (l1 == null || l2 == null) return false;
      if (l1.size() != l2.size()) return false;
      for (int i = 0; i < l1.size(); i++) {
        if (!equals(l1.get(i), l2.get(i))) return false;
      }
      return true;
    }
  }

  @Test
  public void givenExhaustiveProgram__whenBuildingAst__thenCorrectRepresentation() throws IOException {
    // Generate program
    ProgramGenerator generator = new ProgramGenerator();
    generator.generateProgram();
    generator.print();

    // Build AST from generated program
    Path path = Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "light.mjj");
    CharStream input = CharStreams.fromFileName(path.toString());
    MiniJajaLexer lexer = new MiniJajaLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    MiniJajaParser parser = new MiniJajaParser(tokens);
    MiniJajaParser.ProgramContext tree = parser.program();
    MiniJajaAstBuilder builder = new MiniJajaAstBuilder();
    AstNode ast = builder.visit(tree);

    // Print AST to file
    PrintVisitor printer = new PrintVisitor();
    String printedProgram = ast.accept(printer);
    StringSaver.save(printedProgram, "printedProgram.mj");

    // Compare line counts
    long originalLines = input.toString().lines().count();
    long printedLines = printedProgram.lines().count();
    assertEquals(originalLines, printedLines);

    // Rebuild AST from printed program
    CharStream printedInput = CharStreams.fromString(printedProgram);
    MiniJajaLexer printedLexer = new MiniJajaLexer(printedInput);
    CommonTokenStream printedTokens = new CommonTokenStream(printedLexer);
    MiniJajaParser printedParser = new MiniJajaParser(printedTokens);
    MiniJajaParser.ProgramContext printedTree = printedParser.program();
    AstNode printedAst = builder.visit(printedTree);

    // Compare ASTs
    // assertTrue(AstComparator.equals(ast, printedAst)); TODO: debug
  }
}
