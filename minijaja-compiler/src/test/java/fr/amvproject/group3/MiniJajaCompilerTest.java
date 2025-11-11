package fr.amvproject.group3;

import fr.amvproject.group3.mjj.ast.nodes.abstracts.*;
import fr.amvproject.group3.mjj.ast.nodes.decls.*;
import fr.amvproject.group3.mjj.ast.nodes.exprs.*;
import fr.amvproject.group3.mjj.ast.nodes.finals.*;
import fr.amvproject.group3.mjj.ast.nodes.instrs.*;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.*;
import fr.amvproject.group3.mjj.ast.nodes.other.*;
import fr.amvproject.group3.mjj.ast.nodes.program.ClassNode;
import fr.amvproject.group3.mjj.ast.nodes.program.ProgramNode;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;

public class MiniJajaCompilerTest {
/*
    private MiniJajaCompiler compiler;
    private CodeEmitter emitter;
    private LabelManager labels;

    @Before
    public void setUp() {
        emitter = new CodeEmitter();
        labels = new LabelManager();
        compiler = new MiniJajaCompiler(emitter, labels);
    }

    // ====================== visitProgram ======================
    @Test
    public void givenProgramNode__whenVisitProgram__thenCorrectCodeIsEmitted() {
        // Given
        ProgramNode programNode = new ProgramNode(Arrays.asList(
                new ClassNode(
                        new IdentNode("TestClass", 1),
                        new DeclsNode(new ArrayList<>()),
                        new MainNode(new VarsNode(new ArrayList<>()), new InstrsNode(new ArrayList<>()))
                )
        ));

        // When
        compiler.visitProgram(programNode);

        // Then
        List<String> emittedLines = emitter.getCode();
        assertThat(emittedLines, hasSize(4));
        assertThat(emittedLines.get(0), is("1 init"));
        assertThat(emittedLines.get(1), is("2 push (0)"));
        assertThat(emittedLines.get(2), is("3 pop"));
        assertThat(emittedLines.get(3), is("4 jcstop"));
    }

    // ====================== visitClass ======================
    @Test
    public void givenClassNode__whenVisitClass__thenCorrectCodeIsEmitted() {
        // Given
        DeclsNode decls = new DeclsNode(new ArrayList<>());
        MainNode main = new MainNode(new VarsNode(new ArrayList<>()), new InstrsNode(new ArrayList<>()));
        ClassNode classNode = new ClassNode(new IdentNode("TestClass", 1), decls, main);

        // When
        compiler.visitClass(classNode);

        // Then
        List<String> emittedLines = emitter.getCode();
        assertThat(emittedLines.size(), is(1));
        assertThat(emittedLines.get(0), is("1 push (0)"));
    }

    // ====================== visitVar ======================
    @Test
    public void givenVarNodeWithInit__whenVisitVar__thenCorrectCodeIsEmitted() {
        // Given
        VarNode varNode = new VarNode(new TypeMethNode("int"), new IdentNode("x", 1), new IntLiteralNode(42));

        // When
        compiler.visitVar(varNode);

        // Then
        List<String> emittedLines = emitter.getCode();
        assertThat(emittedLines, hasSize(2));
        assertThat(emittedLines.get(0), is("1 push (42)"));
        assertThat(emittedLines.get(1), is("2 new(x,int,var,0)"));
    }

    @Test
    public void givenVarNodeWithoutInit__whenVisitVar__thenCorrectCodeIsEmitted() {
        // Given
        VarNode varNode = new VarNode(new TypeMethNode("int"), new IdentNode("x", 1), null);

        // When
        compiler.visitVar(varNode);

        // Then
        List<String> emittedLines = emitter.getCode();
        assertThat(emittedLines, hasSize(2));
        assertThat(emittedLines.get(0), is("1 push (0)"));
        assertThat(emittedLines.get(1), is("2 new(x,int,var,0)"));
    }

    // ====================== visitCst ======================
    @Test
    public void givenCstNodeWithInit__whenVisitCst__thenCorrectCodeIsEmitted() {
        // Given
        CstNode cstNode = new CstNode(new TypeNode("int"), new IdentNode("T", 1), new IntLiteralNode(3));

        // When
        compiler.visitCst(cstNode);

        // Then
        List<String> emittedLines = emitter.getCode();
        assertThat(emittedLines, hasSize(2));
        assertThat(emittedLines.get(0), is("1 push (3)"));
        assertThat(emittedLines.get(1), is("2 new(T,int,cst,0)"));
    }

    @Test
    public void givenCstNodeWithoutInit__whenVisitCst__thenCorrectCodeIsEmitted() {
        // Given
        CstNode cstNode = new CstNode(new TypeNode("int"), new IdentNode("T", 1), null);

        // When
        compiler.visitCst(cstNode);

        // Then
        List<String> emittedLines = emitter.getCode();
        assertThat(emittedLines, hasSize(2));
        assertThat(emittedLines.get(0), is("1 push (0)"));
        assertThat(emittedLines.get(1), is("2 new(T,int,cst,0)"));
    }

    // ====================== visitTable ======================
    @Test
    public void givenTableNodeWithSize__whenVisitTable__thenCorrectCodeIsEmitted() {
        // Given
        TableNode tableNode = new TableNode(new TypeMethNode("int"), new IdentNode("arr", 1), new IntLiteralNode(5));

        // When
        compiler.visitTable(tableNode);

        // Then
        List<String> emittedLines = emitter.getCode();
        assertThat(emittedLines, hasSize(2));
        assertThat(emittedLines.get(0), is("1 push (5)"));
        assertThat(emittedLines.get(1), is("2 newarray(arr,int)"));
    }

    @Test
    public void givenTableNodeWithoutSize__whenVisitTable__thenCorrectCodeIsEmitted() {
        // Given
        TableNode tableNode = new TableNode(new TypeMethNode("int"), new IdentNode("arr", 1), null);

        // When
        compiler.visitTable(tableNode);

        // Then
        List<String> emittedLines = emitter.getCode();
        assertThat(emittedLines, hasSize(2));
        assertThat(emittedLines.get(0), is("1 push (0)"));
        assertThat(emittedLines.get(1), is("2 newarray(arr,int)"));
    }

    // ====================== visitMain ======================
    @Test
    public void givenMainNode__whenVisitMain__thenCorrectCodeIsEmitted() {
        // Given
        VarsNode vars = new VarsNode(new ArrayList<>());
        InstrsNode instrs = new InstrsNode(new ArrayList<>());
        MainNode mainNode = new MainNode(vars, instrs);

        // When
        compiler.visitMain(mainNode);

        // Then
        List<String> emittedLines = emitter.getCode();
        assertThat(emittedLines, hasSize(1));
        assertThat(emittedLines.get(0), is("1 push (0)"));
    }

    // ====================== visitAssign ======================
    @Test
    public void givenAssignNodeWithIdent__whenVisitAssign__thenCorrectCodeIsEmitted() {
        // Given
        AssignNode assignNode = new AssignNode(new IdentNode("x", 1), new IntLiteralNode(42));

        // When
        compiler.visitAssign(assignNode);

        // Then
        List<String> emittedLines = emitter.getCode();
        assertThat(emittedLines, hasSize(2));
        assertThat(emittedLines.get(0), is("1 push (42)"));
        assertThat(emittedLines.get(1), is("2 store (x)"));
    }

    @Test
    public void givenAssignNodeWithArrayAccess__whenVisitAssign__thenCorrectCodeIsEmitted() {
        // Given
        AssignNode assignNode = new AssignNode(new ArrayAccessNode(new IdentNode("arr", 1), new IntLiteralNode(0)), new IntLiteralNode(42));

        // When
        compiler.visitAssign(assignNode);

        // Then
        List<String> emittedLines = emitter.getCode();
        assertThat(emittedLines, hasSize(3));
        assertThat(emittedLines.get(0), is("1 push (42)"));
        assertThat(emittedLines.get(1), is("2 push (0)"));
        assertThat(emittedLines.get(2), is("3 astore (arr)"));
    }

    // ====================== visitAddAssign ======================
    @Test
    public void givenAddAssignNodeWithIdent__whenVisitAddAssign__thenCorrectCodeIsEmitted() {
        // Given
        AddAssignNode addAssignNode = new AddAssignNode(new IdentNode("x", 1), new IntLiteralNode(42));

        // When
        compiler.visitAddAssign(addAssignNode);

        // Then
        List<String> emittedLines = emitter.getCode();
        assertThat(emittedLines, hasSize(2));
        assertThat(emittedLines.get(0), is("1 push (42)"));
        assertThat(emittedLines.get(1), is("2 inc (x)"));
    }

    // ====================== visitInc ======================
    @Test
    public void givenIncNode__whenVisitInc__thenCorrectCodeIsEmitted() {
        // Given
        IncNode incNode = new IncNode(new IdentNode("x", 1));

        // When
        compiler.visitInc(incNode);

        // Then
        List<String> emittedLines = emitter.getCode();
        assertThat(emittedLines, hasSize(2));
        assertThat(emittedLines.get(0), is("1 load (x)"));
        assertThat(emittedLines.get(1), is("2 inc x"));
    }

    // ====================== visitCallInstr ======================
    @Test
    public void givenCallInstrNode__whenVisitCallInstr__thenCorrectCodeIsEmitted() {
        // Given
        CallInstrNode callInstrNode = new CallInstrNode(new IdentNode("foo", 1), new ListExpNode(Arrays.asList(new IntLiteralNode(42))));

        // When
        compiler.visitCallInstr(callInstrNode);

        // Then
        List<String> emittedLines = emitter.getCode();
        assertThat(emittedLines, hasSize(5));
        assertThat(emittedLines.get(0), is("1 push (42)"));
        assertThat(emittedLines.get(1), is("2 invoke (foo)"));
        assertThat(emittedLines.get(2), is("3 swap"));
        assertThat(emittedLines.get(3), is("4 pop"));
        assertThat(emittedLines.get(4), is("5 pop")); // ignore la valeur de retour
    }

    // ====================== visitWrite ======================
    @Test
    public void givenWriteNode__whenVisitWrite__thenCorrectCodeIsEmitted() {
        // Given
        WriteNode writeNode = new WriteNode(new IntLiteralNode(42), false);

        // When
        compiler.visitWrite(writeNode);

        // Then
        List<String> emittedLines = emitter.getCode();
        assertThat(emittedLines, hasSize(2));
        assertThat(emittedLines.get(0), is("1 push (42)"));
        assertThat(emittedLines.get(1), is("2 write"));
    }

    @Test
    public void givenWriteNodeWithLn__whenVisitWrite__thenCorrectCodeIsEmitted() {
        // Given
        WriteNode writeNode = new WriteNode(new IntLiteralNode(42), true);

        // When
        compiler.visitWrite(writeNode);

        // Then
        List<String> emittedLines = emitter.getCode();
        assertThat(emittedLines, hasSize(2));
        assertThat(emittedLines.get(0), is("1 push (42)"));
        assertThat(emittedLines.get(1), is("2 writeln"));
    }

    // ====================== visitReturn ======================
    @Test
    public void givenReturnNode__whenVisitReturn__thenCorrectCodeIsEmitted() {
        // Given
        ReturnNode returnNode = new ReturnNode(new IntLiteralNode(42));

        // When
        compiler.visitReturn(returnNode);

        // Then
        List<String> emittedLines = emitter.getCode();
        assertThat(emittedLines, hasSize(3));
        assertThat(emittedLines.get(0), is("1 push (42)"));
        assertThat(emittedLines.get(1), is("2 swap"));
        assertThat(emittedLines.get(2), is("3 return"));
    }

    // ====================== visitBinaryExp ======================
    @Test
    public void givenBinaryExpNode__whenVisitBinaryExp__thenCorrectCodeIsEmitted() {
        // Given
        BinaryExpNode binaryExpNode = new BinaryExpNode(new BinaryOpNode("+"), new IntLiteralNode(1), new IntLiteralNode(2));

        // When
        compiler.visitBinaryExp(binaryExpNode);

        // Then
        List<String> emittedLines = emitter.getCode();
        assertThat(emittedLines, hasSize(3));
        assertThat(emittedLines.get(0), is("1 push (1)"));
        assertThat(emittedLines.get(1), is("2 push (2)"));
        assertThat(emittedLines.get(2), is("3 add"));
    }

    // ====================== visitUnaryExp ======================
    @Test
    public void givenUnaryExpNode__whenVisitUnaryExp__thenCorrectCodeIsEmitted() {
        // Given
        UnaryExpNode unaryExpNode = new UnaryExpNode(new UnaryOpNode("!"), new BoolLiteralNode(true));

        // When
        compiler.visitUnaryExp(unaryExpNode);

        // Then
        List<String> emittedLines = emitter.getCode();
        assertThat(emittedLines, hasSize(2));
        assertThat(emittedLines.get(0), is("1 push (true)"));
        assertThat(emittedLines.get(1), is("2 not"));
    }

    // ====================== visitCallExp ======================
    @Test
    public void givenCallExpNode__whenVisitCallExp__thenCorrectCodeIsEmitted() {
        // Given
        CallExpNode callExpNode = new CallExpNode(new IdentNode("foo", 1), new ListExpNode(Arrays.asList(new IntLiteralNode(42))));

        // When
        compiler.visitCallExp(callExpNode);

        // Then
        List<String> emittedLines = emitter.getCode();
        assertThat(emittedLines, hasSize(4));
        assertThat(emittedLines.get(0), is("1 push (42)"));
        assertThat(emittedLines.get(1), is("2 invoke (foo)"));
        assertThat(emittedLines.get(2), is("3 swap"));
        assertThat(emittedLines.get(3), is("4 pop"));
    }

    // ====================== visitLength ======================
    @Test
    public void givenLengthNode__whenVisitLength__thenCorrectCodeIsEmitted() {
        // Given
        LengthNode lengthNode = new LengthNode(new IdentNode("arr", 1));

        // When
        compiler.visitLength(lengthNode);

        // Then
        List<String> emittedLines = emitter.getCode();
        assertThat(emittedLines, hasSize(2));
        assertThat(emittedLines.get(0), is("1 load (arr)"));
        assertThat(emittedLines.get(1), is("2 length"));
    }

    // ====================== visitArrayAccess ======================
    @Test
    public void givenArrayAccessNode__whenVisitArrayAccess__thenCorrectCodeIsEmitted() {
        // Given
        ArrayAccessNode arrayAccessNode = new ArrayAccessNode(new IdentNode("arr", 1), new IntLiteralNode(0));

        // When
        compiler.visitArrayAccess(arrayAccessNode);

        // Then
        List<String> emittedLines = emitter.getCode();
        assertThat(emittedLines, hasSize(3));
        assertThat(emittedLines.get(0), is("1 load (arr)"));
        assertThat(emittedLines.get(1), is("2 push (0)"));
        assertThat(emittedLines.get(2), is("3 aload (arr)"));
    }

    // ====================== visitIntLiteral ======================
    @Test
    public void givenIntLiteralNode__whenVisitIntLiteral__thenCorrectCodeIsEmitted() {
        // Given
        IntLiteralNode intLiteralNode = new IntLiteralNode(42);

        // When
        compiler.visitIntLiteral(intLiteralNode);

        // Then
        List<String> emittedLines = emitter.getCode();
        assertThat(emittedLines, hasSize(1));
        assertThat(emittedLines.get(0), is("1 push (42)"));
    }

    // ====================== visitBoolLiteral ======================
    @Test
    public void givenBoolLiteralNode__whenVisitBoolLiteral__thenCorrectCodeIsEmitted() {
        // Given
        BoolLiteralNode boolLiteralNode = new BoolLiteralNode(true);

        // When
        compiler.visitBoolLiteral(boolLiteralNode);

        // Then
        List<String> emittedLines = emitter.getCode();
        assertThat(emittedLines, hasSize(1));
        assertThat(emittedLines.get(0), is("1 push (true)"));
    }

    // ====================== visitStringLiteral ======================
    @Test
    public void givenStringLiteralNode__whenVisitStringLiteral__thenCorrectCodeIsEmitted() {
        // Given
        StringLiteralNode stringLiteralNode = new StringLiteralNode("test");

        // When
        compiler.visitStringLiteral(stringLiteralNode);

        // Then
        List<String> emittedLines = emitter.getCode();
        assertThat(emittedLines, hasSize(1));
        assertThat(emittedLines.get(0), is("1 push ( \"test\")"));
    }

    // ====================== visitIdent ======================
    @Test
    public void givenIdentNode__whenVisitIdent__thenCorrectCodeIsEmitted() {
        // Given
        IdentNode identNode = new IdentNode("x", 1);

        // When
        compiler.visitIdent(identNode);

        // Then
        List<String> emittedLines = emitter.getCode();
        assertThat(emittedLines, hasSize(1));
        assertThat(emittedLines.get(0), is("1 load (x)"));
    }
        */
}
