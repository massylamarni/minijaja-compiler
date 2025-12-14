package fr.amvproject.group3.mjj.ast;

import fr.amvproject.group3.mjj.ast.nodes.abstracts.*;
import fr.amvproject.group3.mjj.ast.nodes.decls.*;
import fr.amvproject.group3.mjj.ast.nodes.exprs.BinaryExpNode;
import fr.amvproject.group3.mjj.ast.nodes.exprs.CallExpNode;
import fr.amvproject.group3.mjj.ast.nodes.exprs.LengthNode;
import fr.amvproject.group3.mjj.ast.nodes.exprs.UnaryExpNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.*;
import fr.amvproject.group3.mjj.ast.nodes.instrs.*;
import fr.amvproject.group3.mjj.ast.nodes.other.BinaryOpNode;
import fr.amvproject.group3.mjj.ast.nodes.other.TypeMethNode;
import fr.amvproject.group3.mjj.ast.nodes.other.TypeNode;
import fr.amvproject.group3.mjj.ast.nodes.other.UnaryOpNode;
import fr.amvproject.group3.mjj.ast.nodes.program.ClassNode;
import fr.amvproject.group3.mjj.ast.nodes.program.ProgramNode;
import fr.amvproject.group3.mjj.utils.NodeOperations.MockToken;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class PrintVisitorTest {

    private PrintVisitor visitor;

    @Before
    public void setUp() {
        // Given
        visitor = new PrintVisitor();
    }

    /* ---------- Literals ---------- */

    @Test
    public void Given_IntLiteralNode_When_Visited_Then_ValueIsPrinted() {
        // Given
        IntLiteralNode node = new IntLiteralNode(new MockToken("42",1,0));

        // When
        String result = node.accept(visitor);

        // Then
        assertThat(result, is("42"));
    }

    @Test
    public void Given_BoolLiteralNode_When_Visited_Then_ValueIsPrinted() {
        // Given
        BoolLiteralNode node = new BoolLiteralNode(new MockToken("true",1,0));

        // When
        String result = node.accept(visitor);

        // Then
        assertThat(result, is("true"));
    }

    @Test
    public void Given_StringLiteralNode_When_Visited_Then_ValueIsQuoted() {
        // Given
        StringLiteralNode node = new StringLiteralNode(new MockToken("hello",1,0));

        // When
        String result = node.accept(visitor);

        // Then
        assertThat(result, is("\"hello\""));
    }

    /* ---------- Ident & Type ---------- */

    @Test
    public void Given_IdentNode_When_Visited_Then_NameIsPrinted() {
        // Given
        IdentNode node = new IdentNode(new MockToken("x",1,0));

        // When
        String result = node.accept(visitor);

        // Then
        assertThat(result, is("x"));
    }

    @Test
    public void Given_TypeNode_When_Visited_Then_TypeNameIsPrinted() {
        // Given
        TypeNode node = new TypeNode(new MockToken("int",1,0));

        // When
        String result = node.accept(visitor);

        // Then
        assertThat(result, is("int"));
    }

    /* ---------- Expressions ---------- */

    @Test
    public void Given_BinaryExpression_When_Visited_Then_ExpressionIsPrintedCorrectly() {
        // Given
        BinaryExpNode node = new BinaryExpNode(
                new BinaryOpNode("+",1,0),
                new IntLiteralNode(new MockToken("1",1,0)),
                new IntLiteralNode(new MockToken("2",1,0))
        );

        // When
        String result = node.accept(visitor);

        // Then
        assertThat(result, is("(1 + 2)"));
    }

    @Test
    public void Given_UnaryExpression_When_Visited_Then_ExpressionIsPrintedCorrectly() {
        // Given
        UnaryExpNode node = new UnaryExpNode(
                new UnaryOpNode("-",1,0),
                new IntLiteralNode(new MockToken("5",1,0))
        );

        // When
        String result = node.accept(visitor);

        // Then
        assertThat(result, is("(- 5)"));
    }

    /* ---------- Instructions ---------- */

    @Test
    public void Given_AssignInstruction_When_Visited_Then_AssignmentIsPrinted() {
        // Given
        AssignNode node = new AssignNode(
                new IdentNode(new MockToken("x",1,0)), new MockToken("=",1,0),
                new IntLiteralNode(new MockToken("10",1,0))
        );

        // When
        String result = node.accept(visitor);

        // Then
        assertThat(result, containsString("x = 10"));
    }

    /* ---------- Robustness ---------- */

    @Test
    public void Given_PrintVisitor_When_VisitingMultipleNodes_Then_NoExceptionIsThrown() {
        // Given
        BinaryExpNode node = new BinaryExpNode(
                new BinaryOpNode(new MockToken("*",1,0)),
                new IntLiteralNode(new MockToken("3",1,0)),
                new IntLiteralNode(new MockToken("4",1,0))
        );

        // When / Then
        try {
            node.accept(visitor);
        } catch (Exception e) {
            assertThat("No exception should be thrown", false);
        }
    }

    @Test
    public void Given_ProgramWithTwoClasses_When_Visited_Then_ClassesAreSeparatedBySemicolonAndNewLine() {
        // Given
        ClassNode class1 = new ClassNode(
                new MockToken("class",1,0),
                new IdentNode(new MockToken("A",1,0)),
                null,
                null
        );

        ClassNode class2 = new ClassNode(
                new MockToken("class",1,0),
                new IdentNode(new MockToken("B",1,0)),
                null,
                null
        );

        ProgramNode program = new ProgramNode(List.of(class1, class2),new MockToken("",1,0));

        // When
        String result = program.accept(visitor);

        // Then
        assertThat(result, containsString("class A"));
        assertThat(result, containsString("class B"));
    }

    @Test
    public void Given_DeclsWithTwoVars_When_Visited_Then_DeclarationsAreSeparatedCorrectly() {
        // Given
        VarNode var1 = new VarNode(
                new TypeMethNode(new MockToken("int",1,0)),
                new IdentNode(new MockToken("x",1,0))
        );

        VarNode var2 = new VarNode(
                new TypeMethNode(new MockToken("int",1,0)),
                new IdentNode(new MockToken("y",1,0))
        );

        DeclsNode decls = new DeclsNode(List.of(var1, var2));

        // When
        String result = decls.accept(visitor);

        // Then
        assertThat(result, containsString("int x"));
        assertThat(result, containsString("int y"));
    }

    @Test
    public void Given_MethodWithTwoParams_When_Visited_Then_ParamsAreCommaSeparated() {
        // Given
        ParamNode p1 = new ParamNode(
                new TypeNode(new MockToken("int",1,0)),
                new IdentNode(new MockToken("a",1,0))
        );

        ParamNode p2 = new ParamNode(
                new TypeNode(new MockToken("int",1,0)),
                new IdentNode(new MockToken("b",1,0))
        );

        ParamsNode params = new ParamsNode(List.of(p1, p2));

        MethodNode method = new MethodNode(
                new TypeMethNode(new MockToken("void",1,0)),
                new IdentNode(new MockToken("foo",1,0)),
                params,
                null,
                null
        );

        // When
        String result = method.accept(visitor);

        // Then
        assertThat(result, containsString("int a, int b"));
    }

    @Test
    public void Given_CallExpressionWithTwoArgs_When_Visited_Then_ArgsAreCommaSeparated() {
        // Given
        ListExpNode args = new ListExpNode(List.of(
                new IntLiteralNode(new MockToken("1",1,0)),
                new IntLiteralNode(new MockToken("2",1,0))
        ));

        CallExpNode call = new CallExpNode(
                new IdentNode(new MockToken("foo",1,0)),
                args
        );

        // When
        String result = call.accept(visitor);

        // Then
        assertThat(result, is("foo(1, 2)"));
    }

    @Test
    public void Given_EmptyListExp_When_Visited_Then_EmptyCommentIsPrinted() {
        // Given
        ListExpNode listExp = new ListExpNode(Collections.emptyList());

        // When
        String result = listExp.accept(visitor);

        // Then
        assertThat(result, containsString("/* LISTEXP empty */"));
    }

    @Test
    public void Given_ConstWithInitExp_When_Visited_Then_FinalDeclarationIncludesAssignment() {
        // Given
        CstNode cst = new CstNode(
                new MockToken("final",1,0),
                new TypeNode(new MockToken("int",1,0)),
                new IdentNode(new MockToken("MAX",1,0)),
                new MockToken("=",1,0),

                new IntLiteralNode(new MockToken("100",1,0))
        );

        // When
        String result = cst.accept(visitor);

        // Then
        assertThat(result, is("final int MAX = 100"));
    }

    @Test
    public void Given_TableWithSizeExp_When_Visited_Then_ArrayDeclarationIncludesSize() {
        // Given
        TableNode table = new TableNode(
                new TypeMethNode(new MockToken("int",1,0)),
                new IdentNode(new MockToken("arr",1,0)),
                new IntLiteralNode(new MockToken("5",1,0))
        );

        // When
        String result = table.accept(visitor);

        // Then
        assertThat(result, is("int arr[5]"));
    }

    @Test
    public void Given_MainWithVarsAndInstrs_When_Visited_Then_ContentIsPrintedInsideMain() {
        // Given
        VarNode var = new VarNode(
                new TypeMethNode(new MockToken("int",1,0)),
                new IdentNode(new MockToken("x",1,0))
        );

        VarsNode vars = new VarsNode(List.of(var));

        AssignNode assign = new AssignNode(
                new IdentNode(new MockToken("x",1,0)), new MockToken("=",1,0),
                new IntLiteralNode(new MockToken("1",1,0))
        );

        InstrsNode instrs = new InstrsNode(List.of(assign));

        MainNode main = new MainNode(new MockToken("main",1,0),vars, instrs);

        // When
        String result = main.accept(visitor);

        // Then
        assertThat(result, containsString("int x"));
        assertThat(result, containsString("x = 1"));
    }

    @Test
    public void Given_AddAssignInstruction_When_Visited_Then_AddAssignIsPrinted() {
        // Given
        AddAssignNode addAssign = new AddAssignNode(
                new IdentNode(new MockToken("x",1,0)),
                new MockToken("+=",1,0),
                new IntLiteralNode(new MockToken("2",1,0))
        );

        // When
        String result = addAssign.accept(visitor);

        // Then
        assertThat(result, containsString("x += 2"));
    }

    @Test
    public void Given_IncInstruction_When_Visited_Then_IncrementIsPrinted() {
        // Given
        IncNode inc = new IncNode(
                new IdentNode(new MockToken("i",1,0)),
                new MockToken("++",1,0)
        );

        // When
        String result = inc.accept(visitor);

        // Then
        assertThat(result, containsString("i++"));
    }

    @Test
    public void Given_LengthExpression_When_Visited_Then_LengthCallIsPrinted() {
        // Given
        LengthNode length = new LengthNode(
                new MockToken("lenght",1,0),
                new IdentNode(new MockToken("arr",1,0))
        );

        // When
        String result = length.accept(visitor);

        // Then
        assertThat(result, is("length(arr)"));
    }

    @Test
    public void Given_IfWithoutElse_When_Visited_Then_IfBlockIsPrinted() {
        // Given
        IfNode ifNode = new IfNode(new MockToken("if",1,0),
                new BoolLiteralNode(new MockToken("true",1,0)),
                new InstrsNode(Collections.emptyList()),
                new MockToken("else",1,0),
                null
        );

        // When
        String result = ifNode.accept(visitor);

        // Then
        assertThat(result, containsString("if (true)"));
        assertThat(result, not(containsString("else")));
    }

    @Test
    public void Given_IfWithElse_When_Visited_Then_ElseBlockIsPrinted() {
        // Given
        IfNode ifNode = new IfNode(
                new MockToken("if",1,0),
                new BoolLiteralNode(new MockToken("true",1,0)),
                new InstrsNode(Collections.emptyList()),
                new MockToken("else",1,0),
                new InstrsNode(Collections.emptyList())
        );

        // When
        String result = ifNode.accept(visitor);

        // Then
        assertThat(result, containsString("else"));
    }

    @Test
    public void Given_WhileInstruction_When_Visited_Then_WhileBlockIsPrinted() {
        // Given
        WhileNode whileNode = new WhileNode(
                new MockToken("while",1,0),
                new BoolLiteralNode(new MockToken("true",1,0)),
                new InstrsNode(Collections.emptyList())
        );

        // When
        String result = whileNode.accept(visitor);

        // Then
        assertThat(result, containsString("while (true)"));
    }

    @Test
    public void Given_WriteInstruction_When_Visited_Then_WriteIsPrinted() {
        // Given
        WriteNode write = new WriteNode(
                new MockToken("write",1,0),
                new IntLiteralNode(new MockToken("1",1,0)),
                false
        );

        // When
        String result = write.accept(visitor);

        // Then
        assertThat(result, containsString("write(1)"));
    }

    @Test
    public void Given_WriteLnInstruction_When_Visited_Then_WriteLnIsPrinted() {
        // Given
        WriteNode writeln = new WriteNode(
                new MockToken("writeln",1,0),
                new IntLiteralNode(new MockToken("1",1,0)),
                true
        );

        // When
        String result = writeln.accept(visitor);

        // Then
        assertThat(result, containsString("writeln(1)"));
    }

    @Test
    public void Given_ReturnInstruction_When_Visited_Then_ReturnIsPrinted() {
        // Given
        ReturnNode ret = new ReturnNode(
                new MockToken("return",1,0),
                new IntLiteralNode(new MockToken("0",1,0))
        );

        // When
        String result = ret.accept(visitor);

        // Then
        assertThat(result, containsString("return 0"));
    }








}
