package fr.amvproject.group3.ast;

import fr.amvproject.group3.mjj.ast.nodes.abstracts.*;
import fr.amvproject.group3.mjj.ast.nodes.decls.*;
import fr.amvproject.group3.mjj.ast.nodes.exprs.*;
import fr.amvproject.group3.mjj.ast.nodes.finals.*;
import fr.amvproject.group3.mjj.ast.nodes.instrs.*;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.*;
import fr.amvproject.group3.mjj.ast.nodes.other.*;
import fr.amvproject.group3.mjj.ast.nodes.program.ClassNode;
import fr.amvproject.group3.mjj.ast.nodes.program.ProgramNode;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;

public class AstNodeTest {

    // ====================== ProgramNode ======================
    @Test
    public void givenClassList__whenCreateProgramNode__thenProgramNodeIsCorrect() {
        // Given
        List<ClassNode> classes = Arrays.asList(
                new ClassNode(
                        new IdentNode("TestClass", 1),
                        new DeclsNode(new ArrayList<>()),
                        new MainNode(new VarsNode(new ArrayList<>()), new InstrsNode(new ArrayList<>()))
                )
        );

        // When
        ProgramNode programNode = new ProgramNode(classes);

        // Then
        assertThat(programNode, is(notNullValue()));
        assertThat(programNode.getClasses(), hasSize(1));
        assertThat(programNode.getClasses().get(0).getIdent().getName(), is("TestClass"));
    }

    // ====================== ClassNode ======================
    @Test
    public void givenClassComponents__whenCreateClassNode__thenClassNodeIsCorrect() {
        // Given
        IdentNode ident = new IdentNode("TestClass", 1);
        DeclsNode decls = new DeclsNode(new ArrayList<>());
        MainNode main = new MainNode(new VarsNode(new ArrayList<>()), new InstrsNode(new ArrayList<>()));

        // When
        ClassNode classNode = new ClassNode(ident, decls, main);

        // Then
        assertThat(classNode, is(notNullValue()));
        assertThat(classNode.getIdent().getName(), is("TestClass"));
        assertThat(classNode.getDecls().getDecls(), is(empty()));
        assertThat(classNode.getMain(), is(notNullValue()));
    }

    // ====================== DeclsNode ======================
    @Test
    public void givenEmptyDeclList__whenCreateDeclsNode__thenDeclsNodeIsCorrect() {
        // Given
        List<DeclNode> decls = new ArrayList<>();

        // When
        DeclsNode declsNode = new DeclsNode(decls);

        // Then
        assertThat(declsNode, is(notNullValue()));
        assertThat(declsNode.getDecls(), is(empty()));
    }

    // ====================== VarNode ======================
    @Test
    public void givenVarComponents__whenCreateVarNode__thenVarNodeIsCorrect() {
        // Given
        IdentNode ident = new IdentNode("x", 1);
        TypeMethNode type = new TypeMethNode("int");

        // When
        VarNode varNode = new VarNode(type, ident, null);

        // Then
        assertThat(varNode, is(notNullValue()));
        assertThat(varNode.getIdent().getName(), is("x"));
        assertThat(varNode.getType().getName(), is("int"));
        assertThat(varNode.getInitExp(), is(nullValue()));
    }

    // ====================== CstNode ======================
    @Test
    public void givenConstComponents__whenCreateCstNode__thenCstNodeIsCorrect() {
        // Given
        IdentNode ident = new IdentNode("T", 1);
        TypeNode type = new TypeNode("int");
        ExpNode exp = new IntLiteralNode(3);

        // When
        CstNode cstNode = new CstNode(type, ident, exp);

        // Then
        assertThat(cstNode, is(notNullValue()));
        assertThat(cstNode.getIdent().getName(), is("T"));
        assertThat(cstNode.getType().getName(), is("int"));
        assertThat(cstNode.getInitExp(), is(instanceOf(IntLiteralNode.class)));
    }

    // ====================== TableNode ======================
    @Test
    public void givenTableComponents__whenCreateTableNode__thenTableNodeIsCorrect() {
        // Given
        IdentNode ident = new IdentNode("arr", 1);
        TypeMethNode type = new TypeMethNode("int");
        ExpNode exp = new IntLiteralNode(5);

        // When
        TableNode tableNode = new TableNode(type, ident, exp);

        // Then
        assertThat(tableNode, is(notNullValue()));
        assertThat(tableNode.getIdent().getName(), is("arr"));
        assertThat(tableNode.getType().getName(), is("int"));
        assertThat(tableNode.getSizeExp(), is(instanceOf(IntLiteralNode.class)));
    }

    // ====================== MethodNode ======================
    @Test
    public void givenMethodComponents__whenCreateMethodNode__thenMethodNodeIsCorrect() {
        // Given
        IdentNode ident = new IdentNode("foo", 1);
        TypeMethNode returnType = new TypeMethNode("int");
        ParamsNode params = new ParamsNode(new ArrayList<>());
        VarsNode vars = new VarsNode(new ArrayList<>());
        InstrsNode instrs = new InstrsNode(new ArrayList<>());

        // When
        MethodNode methodNode = new MethodNode(returnType, ident, params, vars, instrs);

        // Then
        assertThat(methodNode, is(notNullValue()));
        assertThat(methodNode.getIdent().getName(), is("foo"));
        assertThat(methodNode.getReturnType().getName(), is("int"));
        assertThat(methodNode.getParams().getList(), is(empty()));
        assertThat(methodNode.getVars().getList(), is(empty()));
        assertThat(methodNode.getInstrs().getList(), is(empty()));
    }

    // ====================== MainNode ======================
    @Test
    public void givenMainComponents__whenCreateMainNode__thenMainNodeIsCorrect() {
        // Given
        VarsNode vars = new VarsNode(new ArrayList<>());
        InstrsNode instrs = new InstrsNode(new ArrayList<>());

        // When
        MainNode mainNode = new MainNode(vars, instrs);

        // Then
        assertThat(mainNode, is(notNullValue()));
        assertThat(mainNode.getVars().getList(), is(empty()));
        assertThat(mainNode.getInstrs().getList(), is(empty()));
    }

    // ====================== ParamsNode ======================
    @Test
    public void givenEmptyParamList__whenCreateParamsNode__thenParamsNodeIsCorrect() {
        // Given
        List<ParamNode> params = new ArrayList<>();

        // When
        ParamsNode paramsNode = new ParamsNode(params);

        // Then
        assertThat(paramsNode, is(notNullValue()));
        assertThat(paramsNode.getList(), is(empty()));
    }

    // ====================== ParamNode ======================
    @Test
    public void givenParamComponents__whenCreateParamNode__thenParamNodeIsCorrect() {
        // Given
        IdentNode ident = new IdentNode("param", 1);
        TypeNode type = new TypeNode("int");

        // When
        ParamNode paramNode = new ParamNode(type, ident);

        // Then
        assertThat(paramNode, is(notNullValue()));
        assertThat(paramNode.getIdent().getName(), is("param"));
        assertThat(paramNode.getType().getName(), is("int"));
    }

    // ====================== VarsNode ======================
    @Test
    public void givenEmptyVarList__whenCreateVarsNode__thenVarsNodeIsCorrect() {
        // Given
        List<VarDeclNode> vars = new ArrayList<>();

        // When
        VarsNode varsNode = new VarsNode(vars);

        // Then
        assertThat(varsNode, is(notNullValue()));
        assertThat(varsNode.getList(), is(empty()));
    }

    // ====================== InstrsNode ======================
    @Test
    public void givenEmptyInstrList__whenCreateInstrsNode__thenInstrsNodeIsCorrect() {
        // Given
        List<InstrNode> instrs = new ArrayList<>();

        // When
        InstrsNode instrsNode = new InstrsNode(instrs);

        // Then
        assertThat(instrsNode, is(notNullValue()));
        assertThat(instrsNode.getList(), is(empty()));
    }

    // ====================== ListExpNode ======================
    @Test
    public void givenEmptyExpList__whenCreateListExpNode__thenListExpNodeIsCorrect() {
        // Given
        List<ExpNode> listexp = new ArrayList<>();

        // When
        ListExpNode listExpNode = new ListExpNode(listexp);

        // Then
        assertThat(listExpNode, is(notNullValue()));
        assertThat(listExpNode.getList(), is(empty()));
    }

    // ====================== BinaryExpNode ======================
    @Test
    public void givenBinaryExpComponents__whenCreateBinaryExpNode__thenBinaryExpNodeIsCorrect() {
        // Given
        ExpNode left = new IntLiteralNode(1);
        ExpNode right = new IntLiteralNode(2);
        BinaryOpNode op = new BinaryOpNode("+");

        // When
        BinaryExpNode binaryExpNode = new BinaryExpNode(op, left, right);

        // Then
        assertThat(binaryExpNode, is(notNullValue()));
        assertThat(binaryExpNode.getOp().getOp().getName(), is("+"));
        assertThat(binaryExpNode.getLeftExp(), is(instanceOf(IntLiteralNode.class)));
        assertThat(binaryExpNode.getRightExp(), is(instanceOf(IntLiteralNode.class)));
    }

    // ====================== UnaryExpNode ======================
    @Test
    public void givenUnaryExpComponents__whenCreateUnaryExpNode__thenUnaryExpNodeIsCorrect() {
        // Given
        ExpNode exp = new IntLiteralNode(42);
        UnaryOpNode op = new UnaryOpNode("-");

        // When
        UnaryExpNode unaryExpNode = new UnaryExpNode(op, exp);

        // Then
        assertThat(unaryExpNode, is(notNullValue()));
        assertThat(unaryExpNode.getOp().getOp().getName(), is("-"));
        assertThat(unaryExpNode.getExp(), is(instanceOf(IntLiteralNode.class)));
    }

    // ====================== CallExpNode ======================
    @Test
    public void givenCallExpComponents__whenCreateCallExpNode__thenCallExpNodeIsCorrect() {
        // Given
        IdentNode ident = new IdentNode("foo", 1);
        ListExpNode listExp = new ListExpNode(Arrays.asList(new IntLiteralNode(42)));

        // When
        CallExpNode callExpNode = new CallExpNode(ident, listExp);

        // Then
        assertThat(callExpNode, is(notNullValue()));
        assertThat(callExpNode.getIdent().getName(), is("foo"));
        assertThat(callExpNode.getListExp().getList(), hasSize(1));
    }

    // ====================== LengthNode ======================
    @Test
    public void givenLengthComponents__whenCreateLengthNode__thenLengthNodeIsCorrect() {
        // Given
        IdentNode ident = new IdentNode("arr", 1);

        // When
        LengthNode lengthNode = new LengthNode(ident);

        // Then
        assertThat(lengthNode, is(notNullValue()));
        assertThat(lengthNode.getIdent().getName(), is("arr"));
    }

    // ====================== ArrayAccessNode ======================
    @Test
    public void givenArrayAccessComponents__whenCreateArrayAccessNode__thenArrayAccessNodeIsCorrect() {
        // Given
        IdentNode ident = new IdentNode("arr", 1);
        ExpNode exp = new IntLiteralNode(0);

        // When
        ArrayAccessNode arrayAccessNode = new ArrayAccessNode(ident, exp);

        // Then
        assertThat(arrayAccessNode, is(notNullValue()));
        assertThat(arrayAccessNode.getIdent().getName(), is("arr"));
        assertThat(arrayAccessNode.getIndexExp(), is(instanceOf(IntLiteralNode.class)));
    }

    // ====================== AssignNode ======================
    @Test
    public void givenAssignComponents__whenCreateAssignNode__thenAssignNodeIsCorrect() {
        // Given
        LeftValueNode left = new IdentNode("x", 1);
        ExpNode exp = new IntLiteralNode(42);

        // When
        AssignNode assignNode = new AssignNode(left, exp);

        // Then
        assertThat(assignNode, is(notNullValue()));
        assertThat(assignNode.getLeftValue(), is(instanceOf(IdentNode.class)));
        assertThat(assignNode.getExp(), is(instanceOf(IntLiteralNode.class)));
    }

    // ====================== IncNode ======================
    @Test
    public void givenIncComponents__whenCreateIncNode__thenIncNodeIsCorrect() {
        // Given
        IdentNode ident = new IdentNode("x", 1);

        // When
        IncNode incNode = new IncNode(ident);

        // Then
        assertThat(incNode, is(notNullValue()));
        assertThat(((IdentNode) incNode.getLeftValueNode()).getName(), is("x")); // TODO Handle array case
    }

    // ====================== CallInstrNode ======================
    @Test
    public void givenCallInstrComponents__whenCreateCallInstrNode__thenCallInstrNodeIsCorrect() {
        // Given
        IdentNode ident = new IdentNode("foo", 1);
        ListExpNode listExp = new ListExpNode(Arrays.asList(new IntLiteralNode(42)));

        // When
        CallInstrNode callInstrNode = new CallInstrNode(ident, listExp);

        // Then
        assertThat(callInstrNode, is(notNullValue()));
        assertThat(callInstrNode.getIdent().getName(), is("foo"));
        assertThat(callInstrNode.getListExp().getList(), hasSize(1));
    }

    // ====================== ReturnNode ======================
    @Test
    public void givenReturnComponents__whenCreateReturnNode__thenReturnNodeIsCorrect() {
        // Given
        ExpNode exp = new IntLiteralNode(42);

        // When
        ReturnNode returnNode = new ReturnNode(exp);

        // Then
        assertThat(returnNode, is(notNullValue()));
        assertThat(returnNode.getExp(), is(instanceOf(IntLiteralNode.class)));
    }

    // ====================== WriteNode ======================
    @Test
    public void givenWriteComponents__whenCreateWriteNode__thenWriteNodeIsCorrect() {
        // Given
        ExpNode exp = new IdentNode("x", 1);
        boolean isLn = false;

        // When
        WriteNode writeNode = new WriteNode(exp, isLn);

        // Then
        assertThat(writeNode, is(notNullValue()));
        assertThat(writeNode.getExp(), is(instanceOf(IdentNode.class)));
        assertThat(writeNode.isLn(), is(false));
    }

    // ====================== IfNode ======================
    @Test
    public void givenIfComponents__whenCreateIfNode__thenIfNodeIsCorrect() {
        // Given
        ExpNode cond = new BoolLiteralNode(true);
        InstrsNode thenInstrs = new InstrsNode(new ArrayList<>());
        InstrsNode elseInstrs = new InstrsNode(new ArrayList<>());

        // When
        IfNode ifNode = new IfNode(cond, thenInstrs, elseInstrs);

        // Then
        assertThat(ifNode, is(notNullValue()));
        assertThat(ifNode.getCondExp(), is(instanceOf(BoolLiteralNode.class)));
        assertThat(ifNode.getThenInstrs().getList(), is(empty()));
        assertThat(ifNode.getElseInstrs().getList(), is(empty()));
    }

    // ====================== WhileNode ======================
    @Test
    public void givenWhileComponents__whenCreateWhileNode__thenWhileNodeIsCorrect() {
        // Given
        ExpNode cond = new BoolLiteralNode(true);
        InstrsNode instrs = new InstrsNode(new ArrayList<>());

        // When
        WhileNode whileNode = new WhileNode(cond, instrs);

        // Then
        assertThat(whileNode, is(notNullValue()));
        assertThat(whileNode.getCondExp(), is(instanceOf(BoolLiteralNode.class)));
        assertThat(whileNode.getInstrs().getList(), is(empty()));
    }

    // ====================== IntLiteralNode ======================
    @Test
    public void givenIntLiteralValue__whenCreateIntLiteralNode__thenIntLiteralNodeIsCorrect() {
        // Given
        int value = 42;

        // When
        IntLiteralNode intLiteralNode = new IntLiteralNode(value);

        // Then
        assertThat(intLiteralNode, is(notNullValue()));
        assertThat(intLiteralNode.getValue(), is(42));
    }

    // ====================== BoolLiteralNode ======================
    @Test
    public void givenBoolLiteralValue__whenCreateBoolLiteralNode__thenBoolLiteralNodeIsCorrect() {
        // Given
        boolean value = true;

        // When
        BoolLiteralNode boolLiteralNode = new BoolLiteralNode(value);

        // Then
        assertThat(boolLiteralNode, is(notNullValue()));
        assertThat(boolLiteralNode.getValue(), is(true));
    }

    // ====================== IdentNode ======================
    @Test
    public void givenIdentComponents__whenCreateIdentNode__thenIdentNodeIsCorrect() {
        // Given
        String name = "x";
        int line = 1;

        // When
        IdentNode identNode = new IdentNode(name, line);

        // Then
        assertThat(identNode, is(notNullValue()));
        assertThat(identNode.getName(), is("x"));
        assertThat(identNode.getLine(), is(1));
    }
}
