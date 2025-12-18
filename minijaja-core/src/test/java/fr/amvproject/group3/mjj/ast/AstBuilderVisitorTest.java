package fr.amvproject.group3.mjj.ast;

import fr.amvproject.group3.MiniJajaLexer;
import fr.amvproject.group3.MiniJajaParser;
import fr.amvproject.group3.mjj.ast.nodes.abstracts.ListExpNode;
import fr.amvproject.group3.mjj.ast.nodes.decls.*;
import fr.amvproject.group3.mjj.ast.nodes.exprs.BinaryExpNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.IntLiteralNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.ParamNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.StringLiteralNode;
import fr.amvproject.group3.mjj.ast.nodes.instrs.*;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.DeclNode;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.ExpNode;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.InstrNode;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.VarDeclNode;
import fr.amvproject.group3.mjj.ast.nodes.other.TypeNode;
import fr.amvproject.group3.mjj.ast.nodes.program.ClassNode;
import fr.amvproject.group3.mjj.ast.nodes.program.ProgramNode;
import fr.amvproject.group3.mjj.utils.NodeOperations.MockToken;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AstBuilderVisitorTest {

    private ProgramNode parse(String source) {
        MiniJajaLexer lexer = new MiniJajaLexer(CharStreams.fromString(source));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MiniJajaParser parser = new MiniJajaParser(tokens);

        AstBuilderVisitor visitor = new AstBuilderVisitor();
        return (ProgramNode) visitor.visit(parser.program());
    }

    @Test
    public void Given_EmptyClass_When_Parsed_Then_ProgramContainsOneClass() {
        // Given
        String code = """
        class A {
          main { }
        }
        """;

        // When
        ProgramNode program = parse(code);

        // Then
        assertThat(program.getClasses().size(), is(1));
        assertThat(program.getClasses().get(0), instanceOf(ClassNode.class));
    }

    @Test
    public void Given_VariableDeclaration_When_Parsed_Then_VarNodeIsCreated() {
        String code = """
        class A {
          main {
            int x;
          }
        }
        """;

        ProgramNode program = parse(code);
        ClassNode clazz = program.getClasses().get(0);
        MainNode main = clazz.getMain();

        VarNode var = (VarNode) main.getVars().getList().get(0);

        assertThat(var.getIdent().getName(), is("x"));
    }

    @Test
    public void Given_FinalVariable_When_Parsed_Then_CstNodeIsCreated() {
        String code = """
        class A {
          main {
            final int x = 3;
          }
        }
        """;

        ProgramNode program = parse(code);
        VarDeclNode decl = program.getClasses().get(0)
                .getMain()
                .getVars()
                .getList()
                .get(0);

        assertThat(decl, instanceOf(CstNode.class));
    }

    @Test
    public void Given_ArrayDeclaration_When_Parsed_Then_TableNodeIsCreated() {
        String code = """
        class A {
          main {
            int t[10];
          }
        }
        """;

        VarDeclNode decl = parse(code)
                .getClasses().get(0)
                .getMain()
                .getVars()
                .getList()
                .get(0);

        assertThat(decl, instanceOf(TableNode.class));
    }

    @Test
    public void Given_AssignInstruction_When_Parsed_Then_AssignNodeIsCreated() {
        String code = """
        class A {
          main {
            int x;
            x = 5;
          }
        }
        """;

        InstrNode instr = parse(code)
                .getClasses().get(0)
                .getMain()
                .getInstrs()
                .getList()
                .get(0);

        assertThat(instr, instanceOf(AssignNode.class));
    }

    @Test
    public void Given_AddAssignInstruction_When_Parsed_Then_AddAssignNodeIsCreated() {
        String code = """
        class A {
          main {
            int x;
            x += 1;
          }
        }
        """;

        InstrNode instr = parse(code)
                .getClasses().get(0)
                .getMain()
                .getInstrs()
                .getList()
                .get(0);

        assertThat(instr, instanceOf(AddAssignNode.class));
    }

    @Test
    public void Given_IfStatement_When_Parsed_Then_IfNodeIsCreated() {
        String code = """
        class A {
          main {
            if (true) {
            };
          }
        }
        """;

        InstrNode instr = parse(code)
                .getClasses().get(0)
                .getMain()
                .getInstrs()
                .getList()
                .get(0);

        assertThat(instr, instanceOf(IfNode.class));
    }

    @Test
    public void Given_WhileStatement_When_Parsed_Then_WhileNodeIsCreated() {
        String code = """
        class A {
          main {
            while (true) {
            };
          }
        }
        """;

        InstrNode instr = parse(code)
                .getClasses().get(0)
                .getMain()
                .getInstrs()
                .getList()
                .get(0);

        assertThat(instr, instanceOf(WhileNode.class));
    }

    @Test
    public void Given_ExpressionWithPrecedence_When_Parsed_Then_BinaryTreeIsCorrect() {
        String code = """
        class A {
          main {
            int x;
            x = 1 + 2 * 3;
          }
        }
        """;

        AssignNode assign = (AssignNode) parse(code)
                .getClasses().get(0)
                .getMain()
                .getInstrs()
                .getList()
                .get(0);

        ExpNode exp = assign.getExp();
        assertThat(exp, instanceOf(BinaryExpNode.class));
    }

    @Test
    public void Given_ExpOr_WithTwoOrOperands_When_Parsed_Then_BinaryExpNodeCreated() {
        String code = """
        class A {
          main {
            int x;
            x = 1 || 0;
          }
        }
        """;

        AssignNode assign = (AssignNode) parse(code)
                .getClasses().get(0)
                .getMain()
                .getInstrs()
                .getList()
                .get(0);

        ExpNode exp = assign.getExp();
        assertThat(exp, instanceOf(BinaryExpNode.class));
        BinaryExpNode bin = (BinaryExpNode) exp;
        assertThat(bin.getOp().getOp().getName(), is("||"));
        assertThat(bin.getLeftExp(), instanceOf(IntLiteralNode.class));
        assertThat(bin.getRightExp(), instanceOf(IntLiteralNode.class));
    }

    @Test
    public void Given_ExpAnd_WithTwoAndOperands_When_Parsed_Then_BinaryExpNodeCreated() {
        String code = """
        class A {
          main {
            int x;
            x = 1 && 1;
          }
        }
        """;

        AssignNode assign = (AssignNode) parse(code)
                .getClasses().get(0)
                .getMain()
                .getInstrs()
                .getList()
                .get(0);

        ExpNode exp = assign.getExp();
        assertThat(exp, instanceOf(BinaryExpNode.class));
        BinaryExpNode bin = (BinaryExpNode) exp;
        assertThat(bin.getOp().getOp().getName(), is("&&"));
        assertThat(bin.getLeftExp(), instanceOf(IntLiteralNode.class));
        assertThat(bin.getRightExp(), instanceOf(IntLiteralNode.class));
    }

    @Test
    public void Given_ExpEq_WithEqOperator_When_Parsed_Then_BinaryExpNodeCreated() {
        String code = """
        class A {
          main {
            int x;
            x = 1 == 2;
          }
        }
        """;

        AssignNode assign = (AssignNode) parse(code)
                .getClasses().get(0)
                .getMain()
                .getInstrs()
                .getList()
                .get(0);

        ExpNode exp = assign.getExp();
        assertThat(exp, instanceOf(BinaryExpNode.class));
        BinaryExpNode bin = (BinaryExpNode) exp;
        assertThat(bin.getOp().getOp().getName(), is("=="));
        assertThat(bin.getLeftExp(), instanceOf(IntLiteralNode.class));
        assertThat(bin.getRightExp(), instanceOf(IntLiteralNode.class));
    }

    @Test
    public void Given_ExpEq_WithGtOperator_When_Parsed_Then_BinaryExpNodeCreated() {
        String code = """
        class A {
          main {
            int x;
            x = 2 > 1;
          }
        }
        """;

        AssignNode assign = (AssignNode) parse(code)
                .getClasses().get(0)
                .getMain()
                .getInstrs()
                .getList()
                .get(0);

        ExpNode exp = assign.getExp();
        assertThat(exp, instanceOf(BinaryExpNode.class));
        BinaryExpNode bin = (BinaryExpNode) exp;
        assertThat(bin.getOp().getOp().getName(), is(">"));
        assertThat(bin.getLeftExp(), instanceOf(IntLiteralNode.class));
        assertThat(bin.getRightExp(), instanceOf(IntLiteralNode.class));
    }

    @Test
    public void Given_SingleMethod_When_Visited_Then_MethodNodeCreated() {
        String code = """
        class A {
          int foo(int x, int y) {
            return x;
          };
          main {}
        }
        """;

        ClassNode classNode = (ClassNode) parse(code).getClasses().get(0);
        MethodNode method = classNode.getDecls().getList().stream()
                .filter(n -> n instanceof MethodNode)
                .map(n -> (MethodNode) n)
                .findFirst().get();

        assertThat(method, instanceOf(MethodNode.class));
        assertThat(method.getIdent().getName(), is("foo"));
        assertThat(method.getParams().getList(), hasSize(2));
        assertThat(method.getParams().getList().get(0).getIdent().getName(), is("x"));
        assertThat(method.getParams().getList().get(1).getIdent().getName(), is("y"));
    }

    @Test
    public void Given_SingleParam_When_Visited_Then_ParamNodeCreated() {
        String code = """
        class A {
          int foo(int x) { return x; };
          main {}
        }
        """;

        MethodNode method = (MethodNode) parse(code).getClasses().get(0)
                .getDecls().getList().get(0);

        ParamNode param = method.getParams().getList().get(0);
        assertThat(param, instanceOf(ParamNode.class));
        assertThat(param.getIdent().getName(), is("x"));
        assertThat(param.getType(), instanceOf(TypeNode.class));
    }

    @Test
    public void Given_DeclWithVar_When_Visited_Then_VarNodeReturned() {
        String code = """
        class A {
          int x;
          main {}
        }
        """;

        DeclNode decl = parse(code).getClasses().get(0)
                .getDecls().getList().get(0);

        assertThat(decl, instanceOf(VarNode.class));
        assertThat(((VarNode) decl).getIdent().getName(), is("x"));
    }

    @Test
    public void Given_DeclWithMethod_When_Visited_Then_MethodNodeReturned() {
        String code = """
        class A {
          int foo(int x) { return x; };
          main {}
        }
        """;

        DeclNode decl = parse(code).getClasses().get(0)
                .getDecls().getList().get(0);

        assertThat(decl, instanceOf(MethodNode.class));
        assertThat(((MethodNode) decl).getIdent().getName(), is("foo"));
    }

    @Test
    public void Given_WriteWithString_When_Visited_Then_WriteNodeCreated() {
        StringLiteralNode literal = new StringLiteralNode(new MockToken("hello",1,0));
        WriteNode writeNode = new WriteNode(new MockToken("write",1,0), literal, false);

        assertThat(writeNode.getExp(), instanceOf(StringLiteralNode.class));
        assertThat(((StringLiteralNode) writeNode.getExp()).getValue(), is("hello"));
        assertThat(writeNode.isLn(), is(false));
    }

    @Test
    public void Given_IncNode_When_Created_Then_ContainsLeftValue() {
        IdentNode ident = new IdentNode(new MockToken("x",1,0));
        IncNode incNode = new IncNode(ident, new MockToken("++",1,0));

        assertThat(incNode.getLeftValueNode(), instanceOf(IdentNode.class));
        assertThat(((IdentNode) incNode.getLeftValueNode()).getName(), is("x"));
    }

    @Test
    public void Given_CallInstrNode_When_Created_Then_ContainsIdentAndListExp() {
        IdentNode ident = new IdentNode(new MockToken("foo",1,0));
        ListExpNode listExp = new ListExpNode(List.of(
                new IntLiteralNode(new MockToken("1",1,0)),
                new IntLiteralNode(new MockToken("2",1,0))
        ));
        CallInstrNode call = new CallInstrNode(ident, listExp);

        assertThat(call.getIdent().getName(), is("foo"));
        assertThat(call.getListExp().getList().size(), is(2));
    }

    @Test
    public void Given_ListExpNodeWithTwoNodes_When_Created_Then_ListContainsNodes() {
        IntLiteralNode n1 = new IntLiteralNode(new MockToken("1",1,0));
        IntLiteralNode n2 = new IntLiteralNode(new MockToken("2",1,0));
        ListExpNode listExp = new ListExpNode(List.of(n1, n2));

        assertThat(listExp.getList(), contains(n1, n2));
    }





}
