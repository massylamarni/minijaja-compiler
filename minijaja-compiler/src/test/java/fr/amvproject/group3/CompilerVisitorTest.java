package fr.amvproject.group3;

import fr.amvproject.group3.jjc.ast.nodes.instrs.*;
import fr.amvproject.group3.jjc.ast.nodes.instrs.IncNode;
import fr.amvproject.group3.jjc.ast.nodes.literals.*;
import fr.amvproject.group3.mjj.ast.nodes.abstracts.DeclsNode;
import fr.amvproject.group3.mjj.ast.nodes.abstracts.InstrsNode;
import fr.amvproject.group3.mjj.ast.nodes.abstracts.ListExpNode;
import fr.amvproject.group3.mjj.ast.nodes.abstracts.VarsNode;
import fr.amvproject.group3.mjj.ast.nodes.decls.CstNode;
import fr.amvproject.group3.mjj.ast.nodes.decls.MainNode;
import fr.amvproject.group3.mjj.ast.nodes.decls.TableNode;
import fr.amvproject.group3.mjj.ast.nodes.decls.VarNode;
import fr.amvproject.group3.mjj.ast.nodes.exprs.BinaryExpNode;
import fr.amvproject.group3.mjj.ast.nodes.exprs.CallExpNode;
import fr.amvproject.group3.mjj.ast.nodes.exprs.UnaryExpNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.ArrayAccessNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.BoolLiteralNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.jjc.ast.nodes.finals.IntLiteralNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.StringLiteralNode;
import fr.amvproject.group3.mjj.ast.nodes.instrs.*;
import fr.amvproject.group3.mjj.ast.nodes.other.BinaryOpNode;
import fr.amvproject.group3.mjj.ast.nodes.other.TypeMethNode;
import fr.amvproject.group3.mjj.ast.nodes.other.TypeNode;
import fr.amvproject.group3.mjj.ast.nodes.other.UnaryOpNode;
import fr.amvproject.group3.mjj.ast.nodes.program.ClassNode;
import fr.amvproject.group3.mjj.ast.nodes.program.ProgramNode;
import fr.amvproject.group3.utils.NodeOperations.MockToken;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

public class CompilerVisitorTest {

    private CompilerVisitor compiler;

    @Before
    public void setUp() {
        compiler = new CompilerVisitor();
    }

    // ====================== visitProgram ======================
    @Test
    public void givenProgramNode__whenVisitProgram__thenCorrectCodeIsEmitted() {
        // Given
        MockToken classToken = new MockToken("class", 0, 0);
        MockToken identToken = new MockToken("TestClass", 1, 0);
        MockToken mainToken = new MockToken("main", 0, 0);
        MockToken eofToken = new MockToken(null, 0, 0);
        ProgramNode programNode = new ProgramNode(Arrays.asList(
                new ClassNode(classToken,
                        new IdentNode(identToken),
                        new DeclsNode(new ArrayList<>()),
                        new MainNode(mainToken, new VarsNode(new ArrayList<>()), new InstrsNode(new ArrayList<>()))
                )
        ), eofToken);

        // When
        List<fr.amvproject.group3.jjc.ast.nodes.program.ClassNode> res = compiler.visitProgram(programNode);

        // Then
        assertThat(res, hasSize(4));
        // "1 init"
        assertThat(res.get(0).getAddress().getValue(), is(1));
        assertThat(res.get(0).getInstr() instanceof InitLiteral, is(true));
        // "2 push (0)"
        assertThat(res.get(1).getAddress().getValue(), is(2));
        assertThat(res.get(1).getInstr() instanceof PushNode, is(true));
        PushNode pushNode = ((PushNode) res.get(1).getInstr());
        assertThat(((IntLiteralNode) pushNode.getValue()).getValue(), is(0));
        // "3 pop"
        assertThat(res.get(2).getAddress().getValue(), is(3));
        assertThat(res.get(2).getInstr() instanceof PopLiteral, is(true));
        //"4 jcstop"
        assertThat(res.get(3).getAddress().getValue(), is(4));
        assertThat(res.get(3).getInstr() instanceof JcstopLiteral, is(true));
    }
    // TODO TEST

    /*

    // ====================== visitClass ======================
    @Test
    public void givenClassNode__whenVisitClass__thenCorrectCodeIsEmitted() {
        // Given
        DeclsNode decls = new DeclsNode(new ArrayList<>());
        MainNode main = new MainNode(new VarsNode(new ArrayList<>()), new InstrsNode(new ArrayList<>()));
        ClassNode classNode = new ClassNode(new IdentNode("TestClass", 1), decls, main);

        // When
        List<fr.amvproject.group3.jjc.ast.nodes.program.ClassNode> res = compiler.visitClass(classNode);

        // Then
        assertThat(res, hasSize(4));
        // "1 init"
        assertThat(res.get(0).getAddress().getValue(), is(1));
        assertThat(res.get(0).getInstr() instanceof InitLiteral, is(true));
        // "2 push (0)"
        assertThat(res.get(1).getAddress().getValue(), is(2));
        assertThat(res.get(1).getInstr() instanceof PushNode, is(true));
        PushNode pushNode = ((PushNode) res.get(1).getInstr());
        assertThat(((IntLiteralNode) pushNode.getValue()).getValue(), is(0));
        // "3 pop"
        assertThat(res.get(2).getAddress().getValue(), is(3));
        assertThat(res.get(2).getInstr() instanceof PopLiteral, is(true));
        //"4 jcstop"
        assertThat(res.get(3).getAddress().getValue(), is(4));
        assertThat(res.get(3).getInstr() instanceof JcstopLiteral, is(true));
    }

    // ====================== visitVar ======================
    @Test
    public void givenVarNodeWithInit__whenVisitVar__thenCorrectCodeIsEmitted() {
        // Given
        VarNode varNode = new VarNode(new TypeMethNode("int"), new IdentNode("x", 1), new fr.amvproject.group3.mjj.ast.nodes.finals.IntLiteralNode(42));

        // When
        List<fr.amvproject.group3.jjc.ast.nodes.program.ClassNode> res = compiler.visitVar(varNode);


        // Then
        assertThat(res, hasSize(2));
        // 1 push (42)"
        assertThat(res.get(0).getAddress().getValue(), is(1));
        assertThat(res.get(0).getInstr() instanceof PushNode, is(true));
        PushNode pushNode = ((PushNode) res.get(0).getInstr());
        assertThat(((IntLiteralNode) pushNode.getValue()).getValue(), is(42));
        //"2 new(x,int,var,0)"
        assertThat(res.get(1).getAddress().getValue(), is(2));
        assertThat(res.get(1).getInstr() instanceof NewNode, is(true));
        NewNode newNode = ((NewNode) res.get(1).getInstr());
        assertThat(newNode.getIdent().getName(),is("x"));
        assertThat(newNode.getType().getName(),is("entier"));
        assertThat(newNode.getObj().getName(),is("var"));
        assertThat(newNode.getValue().sameAs(new IntLiteralNode(0)),is(true));

    }

    @Test
    public void givenVarNodeWithoutInit__whenVisitVar__thenCorrectCodeIsEmitted() {
        // Given
        VarNode varNode = new VarNode(new TypeMethNode("int"), new IdentNode("x", 1), null);

        // When
        List<fr.amvproject.group3.jjc.ast.nodes.program.ClassNode> res = compiler.visitVar(varNode);


        // Then
        assertThat(res, hasSize(1));
        //"2 new(x,int,var,0)"
        assertThat(res.get(0).getAddress().getValue(), is(1));
        assertThat(res.get(0).getInstr() instanceof NewNode, is(true));
        NewNode newNode = ((NewNode) res.get(0).getInstr());
        assertThat(newNode.getIdent().getName(),is("x"));
        assertThat(newNode.getType().getName(),is("entier"));
        assertThat(newNode.getObj().getName(),is("var"));
        assertThat(newNode.getValue().sameAs(new IntLiteralNode(0)),is(true));
    }

    // ====================== visitCst ======================
    @Test
    public void givenCstNodeWithInit__whenVisitCst__thenCorrectCodeIsEmitted() {
        // Given
        CstNode cstNode = new CstNode(new TypeNode("int"), new IdentNode("T", 1), new fr.amvproject.group3.mjj.ast.nodes.finals.IntLiteralNode(3));

        // When
        List<fr.amvproject.group3.jjc.ast.nodes.program.ClassNode> res = compiler.visitCst(cstNode);

        // Then
        assertThat(res, hasSize(2));
        //1 push (3)
        assertThat(res.get(0).getAddress().getValue(), is(1));
        assertThat(res.get(0).getInstr() instanceof PushNode, is(true));
        PushNode pushNode = ((PushNode) res.get(0).getInstr());
        assertThat(((IntLiteralNode) pushNode.getValue()).getValue(), is(3));
        //"2 new(T,int,cst,0)"
        assertThat(res.get(1).getAddress().getValue(), is(2));
        assertThat(res.get(1).getInstr() instanceof NewNode, is(true));
        NewNode newNode = ((NewNode) res.get(1).getInstr());
        assertThat(newNode.getIdent().getName(),is("T"));
        assertThat(newNode.getType().getName(),is("entier"));
        assertThat(newNode.getObj().getName(),is("cst"));
        assertThat(newNode.getValue().sameAs(new IntLiteralNode(0)),is(true));
    }


    @Test
    public void givenCstNodeWithoutInit__whenVisitCst__thenCorrectCodeIsEmitted() {
        // Given
        CstNode cstNode = new CstNode(new TypeNode("int"), new IdentNode("T", 1), null);

        // When
        List<fr.amvproject.group3.jjc.ast.nodes.program.ClassNode> res = compiler.visitCst(cstNode);

        // Then
        assertThat(res, hasSize(1));
        //"2 new(T,int,cst,0)"
        assertThat(res.get(0).getAddress().getValue(), is(1));
        assertThat(res.get(0).getInstr() instanceof NewNode, is(true));
        NewNode newNode = ((NewNode) res.get(0).getInstr());
        assertThat(newNode.getIdent().getName(),is("T"));
        assertThat(newNode.getType().getName(),is("entier"));
        assertThat(newNode.getObj().getName(),is("cst"));
        assertThat(newNode.getValue().sameAs(new IntLiteralNode(0)),is(true));
    }
    // ====================== visitTable ======================
    @Test
    public void givenTableNodeWithSize__whenVisitTable__thenCorrectCodeIsEmitted() {
        // Given
        TableNode tableNode = new TableNode(new TypeMethNode("int"), new IdentNode("arr", 1), new fr.amvproject.group3.mjj.ast.nodes.finals.IntLiteralNode(5));

        // When
        List<fr.amvproject.group3.jjc.ast.nodes.program.ClassNode> res = compiler.visitTable(tableNode);

        // Then
        assertThat(res, hasSize(2));
        //"1 push (5)"
        assertThat(res.get(0).getAddress().getValue(), is(1));
        assertThat(res.get(0).getInstr() instanceof PushNode, is(true));
        PushNode pushNode = ((PushNode) res.get(0).getInstr());
        assertThat(((IntLiteralNode) pushNode.getValue()).getValue(), is(5));
        //"2 newarray(arr,int)"
        assertThat(res.get(1).getAddress().getValue(), is(2));
        assertThat(res.get(1).getInstr() instanceof NewArrayNode, is(true));
        NewArrayNode newNode = ((NewArrayNode) res.get(1).getInstr());
        assertThat(newNode.getIdent().getName(),is("arr"));
        assertThat(newNode.getType().getName(),is("entier"));
    }

    // ====================== visitMain ======================
    @Test
    public void givenMainNode__whenVisitMain__thenCorrectCodeIsEmitted() {
        // Given
        VarsNode vars = new VarsNode(new ArrayList<>());
        InstrsNode instrs = new InstrsNode(new ArrayList<>());
        MainNode mainNode = new MainNode(vars, instrs);

        // When
        List<fr.amvproject.group3.jjc.ast.nodes.program.ClassNode> res = compiler.visitMain(mainNode);

        // Then
        assertThat(res, hasSize(1));
        //"1 push (5)"
        assertThat(res.get(0).getAddress().getValue(), is(1));
        assertThat(res.get(0).getInstr() instanceof PushNode, is(true));
        PushNode pushNode = ((PushNode) res.get(0).getInstr());
        assertThat(((IntLiteralNode) pushNode.getValue()).getValue(), is(0));
    }

    // ====================== visitAssign ======================
    @Test
    public void givenAssignNodeWithIdent__whenVisitAssign__thenCorrectCodeIsEmitted() {
        // Given
        AssignNode assignNode = new AssignNode(new IdentNode("x", 1), new fr.amvproject.group3.mjj.ast.nodes.finals.IntLiteralNode(42));

        // When
        List<fr.amvproject.group3.jjc.ast.nodes.program.ClassNode> res =compiler.visitAssign(assignNode);

        // Then
        assertThat(res, hasSize(2));
        // 1 push (42)
        assertThat(res.get(0).getAddress().getValue(), is(1));
        assertThat(res.get(0).getInstr() instanceof PushNode, is(true));
        PushNode pushNode = ((PushNode) res.get(0).getInstr());
        assertThat(((IntLiteralNode) pushNode.getValue()).getValue(), is(42));

        //"2 store (x)"
        assertThat(res.get(1).getAddress().getValue(), is(2));
        assertThat(res.get(1).getInstr() instanceof StoreNode, is(true));
        StoreNode storeNode = ((StoreNode) res.get(1).getInstr());
        assertThat(storeNode.getIdent().getName(),is("x"));

    }

    @Test
    public void givenAssignNodeWithArrayAccess__whenVisitAssign__thenCorrectCodeIsEmitted() {
        // Given
        AssignNode assignNode = new AssignNode(new ArrayAccessNode(new IdentNode("arr", 1), new fr.amvproject.group3.mjj.ast.nodes.finals.IntLiteralNode(0)), new fr.amvproject.group3.mjj.ast.nodes.finals.IntLiteralNode(42));

        // When
        List<fr.amvproject.group3.jjc.ast.nodes.program.ClassNode> res = compiler.visitAssign(assignNode);

        // Then
        assertThat(res, hasSize(3));
        // 1 push (0)
        assertThat(res.get(0).getAddress().getValue(), is(1));
        assertThat(res.get(0).getInstr() instanceof PushNode, is(true));
        PushNode pushNode = ((PushNode) res.get(0).getInstr());
        assertThat(((IntLiteralNode) pushNode.getValue()).getValue(), is(0));
        // 2 push (42)
        assertThat(res.get(1).getAddress().getValue(), is(2));
        assertThat(res.get(1).getInstr() instanceof PushNode, is(true));
        pushNode = ((PushNode) res.get(1).getInstr());
        assertThat(((IntLiteralNode) pushNode.getValue()).getValue(), is(42));

        //"3 astore (arr)"
        assertThat(res.get(2).getAddress().getValue(), is(3));
        assertThat(res.get(2).getInstr() instanceof StoreNode, is(true));
        StoreNode storeNode = ((StoreNode) res.get(2).getInstr());
        assertThat(storeNode.getIdent().getName(),is("arr"));
    }

    // ====================== visitAddAssign ======================
    @Test
    public void givenAddAssignNodeWithIdent__whenVisitAddAssign__thenCorrectCodeIsEmitted() {
        // Given
        AddAssignNode addAssignNode = new AddAssignNode(new IdentNode("x", 1), new fr.amvproject.group3.mjj.ast.nodes.finals.IntLiteralNode(42));

        // When
        List<fr.amvproject.group3.jjc.ast.nodes.program.ClassNode> res = compiler.visitAddAssign(addAssignNode);

        // Then
        // 2 push (42)
        assertThat(res.get(0).getAddress().getValue(), is(1));
        assertThat(res.get(0).getInstr() instanceof PushNode, is(true));
        PushNode pushNode = ((PushNode) res.get(0).getInstr());
        assertThat(((IntLiteralNode) pushNode.getValue()).getValue(), is(42));
        //"2 inc (x)"
        assertThat(res.get(1).getAddress().getValue(), is(2));
        IncNode incNode = ((IncNode) res.get(1).getInstr());
        assertThat(incNode.getIdent().getName(),is("x"));


    }

    // ====================== visitInc ======================
    @Test
    public void givenIncNode__whenVisitInc__thenCorrectCodeIsEmitted() {
        // Given
        fr.amvproject.group3.mjj.ast.nodes.instrs.IncNode incNode = new fr.amvproject.group3.mjj.ast.nodes.instrs.IncNode(new IdentNode("x", 1));

        // When
        List<fr.amvproject.group3.jjc.ast.nodes.program.ClassNode> res = compiler.visitInc(incNode);

        // Then
        assertThat(res, hasSize(2));
        // push 1
        assertThat(res.get(0).getAddress().getValue(), is(1));
        assertThat(res.get(0).getInstr() instanceof PushNode, is(true));
        PushNode pushNode = ((PushNode) res.get(0).getInstr());
        assertThat(((IntLiteralNode) pushNode.getValue()).getValue(), is(1));
        //"2 inc (x)"
        assertThat(res.get(1).getAddress().getValue(), is(2));
        IncNode incNodeR = ((IncNode) res.get(1).getInstr());
        assertThat(incNodeR.getIdent().getName(),is("x"));
    }

    // ====================== visitCallInstr ======================
    @Test
    public void givenCallInstrNode__whenVisitCallInstr__thenCorrectCodeIsEmitted() {
        // Given
        CallInstrNode callInstrNode = new CallInstrNode(new IdentNode("foo", 1), new ListExpNode(Arrays.asList(new fr.amvproject.group3.mjj.ast.nodes.finals.IntLiteralNode(42))));

        // When
        List<fr.amvproject.group3.jjc.ast.nodes.program.ClassNode> res = compiler.visitCallInstr(callInstrNode);

        // Then
        assertThat(res, hasSize(5));
        // 1 push (42)
        assertThat(res.get(0).getAddress().getValue(), is(1));
        assertThat(res.get(0).getInstr() instanceof PushNode, is(true));
        PushNode pushNode = ((PushNode) res.get(0).getInstr());
        assertThat(((IntLiteralNode) pushNode.getValue()).getValue(), is(42));

        //"2 invoke (foo)"
        assertThat(res.get(1).getAddress().getValue(), is(2));
        assertThat(res.get(1).getInstr() instanceof InvokeNode, is(true));
        InvokeNode invokeNode = ((InvokeNode) res.get(1).getInstr());
        assertThat(invokeNode.getIdent().getName(),is("foo"));
        //"3 swap"
        assertThat(res.get(2).getAddress().getValue(), is(3));
        assertThat(res.get(2).getInstr() instanceof SwapLiteral, is(true));
        //"4 pop"
        assertThat(res.get(3).getAddress().getValue(), is(4));
        assertThat(res.get(3).getInstr() instanceof PopLiteral, is(true));
        //"5 pop"
        assertThat(res.get(4).getAddress().getValue(), is(5));
        assertThat(res.get(4).getInstr() instanceof PopLiteral, is(true));
    }

    // ====================== visitWrite ======================
    @Test
    public void givenWriteNode__whenVisitWrite__thenCorrectCodeIsEmitted() {
        // Given
        WriteNode writeNode = new WriteNode(new fr.amvproject.group3.mjj.ast.nodes.finals.IntLiteralNode(42), false);

        // When
        List<fr.amvproject.group3.jjc.ast.nodes.program.ClassNode> res = compiler.visitWrite(writeNode);

        // Then
        assertThat(res, hasSize(2));
        // 1 push (42)
        assertThat(res.get(0).getAddress().getValue(), is(1));
        assertThat(res.get(0).getInstr() instanceof PushNode, is(true));
        PushNode pushNode = ((PushNode) res.get(0).getInstr());
        assertThat(((IntLiteralNode) pushNode.getValue()).getValue(), is(42));
        //2 write
        assertThat(res.get(1).getAddress().getValue(), is(2));
        assertThat(res.get(1).getInstr() instanceof WriteLiteral, is(true));

        WriteLiteral writeLiteral = ((WriteLiteral) res.get(1).getInstr());
        assertThat(writeLiteral.isLn(),is(false));

    }

    @Test
    public void givenWriteNodeWithLn__whenVisitWrite__thenCorrectCodeIsEmitted() {
        // Given
        WriteNode writeNode = new WriteNode(new fr.amvproject.group3.mjj.ast.nodes.finals.IntLiteralNode(42), true);

        // When
        List<fr.amvproject.group3.jjc.ast.nodes.program.ClassNode> res = compiler.visitWrite(writeNode);

        // Then
        assertThat(res, hasSize(2));
        // 1 push (42)
        assertThat(res.get(0).getAddress().getValue(), is(1));
        assertThat(res.get(0).getInstr() instanceof PushNode, is(true));
        PushNode pushNode = ((PushNode) res.get(0).getInstr());
        assertThat(((IntLiteralNode) pushNode.getValue()).getValue(), is(42));
        //2 write
        assertThat(res.get(1).getAddress().getValue(), is(2));
        assertThat(res.get(1).getInstr() instanceof WriteLiteral, is(true));

        WriteLiteral writeLiteral = ((WriteLiteral) res.get(1).getInstr());
        assertThat(writeLiteral.isLn(),is(true));
    }

    // ====================== visitReturn ======================
    @Test
    public void givenReturnNode__whenVisitReturn__thenCorrectCodeIsEmitted() {
        // Given
        ReturnNode returnNode = new ReturnNode(new fr.amvproject.group3.mjj.ast.nodes.finals.IntLiteralNode(42));

        // When
        List<fr.amvproject.group3.jjc.ast.nodes.program.ClassNode> res = compiler.visitReturn(returnNode);

        // Then
        assertThat(res, hasSize(1));
        // 1 push (42)
        assertThat(res.get(0).getAddress().getValue(), is(1));
        assertThat(res.get(0).getInstr() instanceof PushNode, is(true));
        PushNode pushNode = ((PushNode) res.get(0).getInstr());
        assertThat(((IntLiteralNode) pushNode.getValue()).getValue(), is(42));
    }

    // ====================== visitBinaryExp ======================
    @Test
    public void givenBinaryExpNode__whenVisitBinaryExp__thenCorrectCodeIsEmitted() {
        // Given
        BinaryExpNode binaryExpNode = new BinaryExpNode(new BinaryOpNode("+"), new fr.amvproject.group3.mjj.ast.nodes.finals.IntLiteralNode(1), new fr.amvproject.group3.mjj.ast.nodes.finals.IntLiteralNode(2));

        // When
        List<fr.amvproject.group3.jjc.ast.nodes.program.ClassNode> res = compiler.visitBinaryExp(binaryExpNode);

        // Then
        assertThat(res, hasSize(3));
        // 1 push (1)
        assertThat(res.get(0).getAddress().getValue(), is(1));
        assertThat(res.get(0).getInstr() instanceof PushNode, is(true));
        PushNode pushNode = ((PushNode) res.get(0).getInstr());
        assertThat(((IntLiteralNode) pushNode.getValue()).getValue(), is(1));
        // 1 push (2)
        assertThat(res.get(1).getAddress().getValue(), is(2));
        assertThat(res.get(1).getInstr() instanceof PushNode, is(true));
        pushNode = ((PushNode) res.get(1).getInstr());
        assertThat(((IntLiteralNode) pushNode.getValue()).getValue(), is(2));
        // 3 add
        assertThat(res.get(2).getAddress().getValue(), is(3));
        assertThat(res.get(2).getInstr() instanceof fr.amvproject.group3.jjc.ast.nodes.other.BinaryOpNode, is(true));
        fr.amvproject.group3.jjc.ast.nodes.other.BinaryOpNode binaryOpNode = ((fr.amvproject.group3.jjc.ast.nodes.other.BinaryOpNode) res.get(2).getInstr());
        assertThat(binaryOpNode.isAdd(),is(true));
    }

    // ====================== visitUnaryExp ======================
    @Test
    public void givenUnaryExpNode__whenVisitUnaryExp__thenCorrectCodeIsEmitted() {
        // Given
        UnaryExpNode unaryExpNode = new UnaryExpNode(new UnaryOpNode("!"), new BoolLiteralNode(true));

        // When
        List<fr.amvproject.group3.jjc.ast.nodes.program.ClassNode> res = compiler.visitUnaryExp(unaryExpNode);

        // Then
        assertThat(res, hasSize(2));
        // 1 push (true)
        assertThat(res.get(0).getAddress().getValue(), is(1));
        assertThat(res.get(0).getInstr() instanceof PushNode, is(true));
        PushNode pushNode = ((PushNode) res.get(0).getInstr());
        assertThat(((fr.amvproject.group3.jjc.ast.nodes.finals.BoolLiteralNode) pushNode.getValue()).getValue(), is(true));
        // 2 not
        assertThat(res.get(1).getAddress().getValue(), is(2));
        assertThat(res.get(1).getInstr() instanceof fr.amvproject.group3.jjc.ast.nodes.other.UnaryOpNode, is(true));
        fr.amvproject.group3.jjc.ast.nodes.other.UnaryOpNode unaryOpNode = ((fr.amvproject.group3.jjc.ast.nodes.other.UnaryOpNode) res.get(1).getInstr());
        assertThat(unaryOpNode.isNot(),is(true));


    }

    // ====================== visitCallExp ======================
    @Test
    public void givenCallExpNode__whenVisitCallExp__thenCorrectCodeIsEmitted() {
        // Given
        CallExpNode callExpNode = new CallExpNode(new IdentNode("foo", 1), new ListExpNode(Arrays.asList(new fr.amvproject.group3.mjj.ast.nodes.finals.IntLiteralNode(42))));

        // When
        List<fr.amvproject.group3.jjc.ast.nodes.program.ClassNode> res = compiler.visitCallExp(callExpNode);

        // Then
        assertThat(res, hasSize(4));
        // 1 push (42)
        assertThat(res.get(0).getAddress().getValue(), is(1));
        assertThat(res.get(0).getInstr() instanceof PushNode, is(true));
        PushNode pushNode = ((PushNode) res.get(0).getInstr());
        assertThat(((IntLiteralNode) pushNode.getValue()).getValue(), is(42));
        // 2 invoke (foo)
        assertThat(res.get(1).getAddress().getValue(), is(2));
        assertThat(res.get(1).getInstr() instanceof InvokeNode,is(true));
        InvokeNode invokeNode = ((InvokeNode) res.get(1).getInstr());
        assertThat(invokeNode.getIdent().getName(), is("foo"));

        //"3 swap"
        assertThat(res.get(2).getAddress().getValue(), is(3));
        assertThat(res.get(2).getInstr() instanceof SwapLiteral, is(true));
        //"4 pop"
        assertThat(res.get(3).getAddress().getValue(), is(4));
        assertThat(res.get(3).getInstr() instanceof PopLiteral, is(true));
    }
    // ====================== visitLength ======================
    @Test
    public void givenLengthNode__whenVisitLength__thenCorrectCodeIsEmitted() {
        // Given
        fr.amvproject.group3.mjj.ast.nodes.exprs.LengthNode lengthNode = new fr.amvproject.group3.mjj.ast.nodes.exprs.LengthNode(new IdentNode("arr", 1));

        // When
        List<fr.amvproject.group3.jjc.ast.nodes.program.ClassNode> res = compiler.visitLength(lengthNode);

        // Then
        assertThat(res, hasSize(1));
        // 2 lenght arr
        assertThat(res.get(0).getAddress().getValue(), is(1));
        assertThat(res.get(0).getInstr() instanceof fr.amvproject.group3.jjc.ast.nodes.instrs.LengthNode, is(true));
        fr.amvproject.group3.jjc.ast.nodes.instrs.LengthNode lengthNode1 = ((fr.amvproject.group3.jjc.ast.nodes.instrs.LengthNode) res.get(0).getInstr());
        assertThat(lengthNode1.getIdent().getName(), is("arr"));
    }

    // ====================== visitArrayAccess ======================
    @Test
    public void givenArrayAccessNode__whenVisitArrayAccess__thenCorrectCodeIsEmitted() {
        // Given
        ArrayAccessNode arrayAccessNode = new ArrayAccessNode(new IdentNode("arr", 1), new fr.amvproject.group3.mjj.ast.nodes.finals.IntLiteralNode(0));

        // When
        List<fr.amvproject.group3.jjc.ast.nodes.program.ClassNode> res =compiler.visitArrayAccess(arrayAccessNode);

        // Then
        assertThat(res, hasSize(2));
        // 1 push (0)
        assertThat(res.get(0).getAddress().getValue(), is(1));
        assertThat(res.get(0).getInstr() instanceof PushNode, is(true));
        PushNode pushNode = ((PushNode) res.get(0).getInstr());
        assertThat(((IntLiteralNode) pushNode.getValue()).getValue(), is(0));
        // 2 aload (arr)
        assertThat(res.get(1).getAddress().getValue(), is(2));
        assertThat(res.get(1).getInstr() instanceof LoadNode, is(true));
        LoadNode loadNode1 = ((LoadNode) res.get(1).getInstr());
        assertThat(loadNode1.getIdent().getName(), is("arr"));
        assertThat(loadNode1.isA(), is(true));
    }

    // ====================== visitIntLiteral ======================
    @Test
    public void givenIntLiteralNode__whenVisitIntLiteral__thenCorrectCodeIsEmitted() {
        // Given
        fr.amvproject.group3.mjj.ast.nodes.finals.IntLiteralNode intLiteralNode = new fr.amvproject.group3.mjj.ast.nodes.finals.IntLiteralNode(42);

        // When
        List<fr.amvproject.group3.jjc.ast.nodes.program.ClassNode> res = compiler.visitIntLiteral(intLiteralNode);

        // Then
        assertThat(res, hasSize(1));
        // 1 push (42)
        assertThat(res.get(0).getAddress().getValue(), is(1));
        assertThat(res.get(0).getInstr() instanceof PushNode, is(true));
        PushNode pushNode = ((PushNode) res.get(0).getInstr());
        assertThat(((IntLiteralNode) pushNode.getValue()).getValue(), is(42));
    }
    // ====================== visitBoolLiteral ======================
    @Test
    public void givenBoolLiteralNode__whenVisitBoolLiteral__thenCorrectCodeIsEmitted() {
        // Given
        BoolLiteralNode boolLiteralNode = new BoolLiteralNode(true);

        // When
        List<fr.amvproject.group3.jjc.ast.nodes.program.ClassNode> res = compiler.visitBoolLiteral(boolLiteralNode);

        // Then
        assertThat(res, hasSize(1));
        // 1 push (true)
        assertThat(res.get(0).getAddress().getValue(), is(1));
        assertThat(res.get(0).getInstr() instanceof PushNode, is(true));
        PushNode pushNode = ((PushNode) res.get(0).getInstr());
        assertThat(((fr.amvproject.group3.jjc.ast.nodes.finals.BoolLiteralNode) pushNode.getValue()).getValue(), is(true));
    }

    // ====================== visitStringLiteral ======================
    @Test
    public void givenStringLiteralNode__whenVisitStringLiteral__thenCorrectCodeIsEmitted() {
        // Given
        StringLiteralNode stringLiteralNode = new StringLiteralNode("test");

        // When
        List<fr.amvproject.group3.jjc.ast.nodes.program.ClassNode> res = compiler.visitStringLiteral(stringLiteralNode);

        // Then
        assertThat(res, hasSize(1));
        // 1 push ("test")
        assertThat(res.get(0).getAddress().getValue(), is(1));
        assertThat(res.get(0).getInstr() instanceof PushNode, is(true));
        PushNode pushNode = ((PushNode) res.get(0).getInstr());
        assertThat(((fr.amvproject.group3.jjc.ast.nodes.finals.StringLiteralNode) pushNode.getValue()).getValue(), is("test"));
    }

    // ====================== visitIdent ======================
    @Test
    public void givenIdentNode__whenVisitIdent__thenCorrectCodeIsEmitted() {
        // Given
        IdentNode identNode = new IdentNode("x", 1);

        // When
        List<fr.amvproject.group3.jjc.ast.nodes.program.ClassNode> res =compiler.visitIdent(identNode);

        // Then
        assertThat(res, hasSize(1));
        //1  load (x)
        assertThat(res.get(0).getAddress().getValue(), is(1));
        assertThat(res.get(0).getInstr() instanceof LoadNode, is(true));
        LoadNode loadNode = ((LoadNode) res.get(0).getInstr());
        assertThat(loadNode.getIdent().getName(), is("x"));
    }
        */
}
