package fr.amvproject.group3.mjj.ast;

import fr.amvproject.group3.mjj.ast.nodes.abstracts.*;
import fr.amvproject.group3.mjj.ast.nodes.decls.*;
import fr.amvproject.group3.mjj.ast.nodes.exprs.*;
import fr.amvproject.group3.mjj.ast.nodes.finals.*;
import fr.amvproject.group3.mjj.ast.nodes.instrs.*;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.*;
import fr.amvproject.group3.mjj.ast.nodes.other.*;
import fr.amvproject.group3.mjj.ast.nodes.program.ClassNode;
import fr.amvproject.group3.mjj.ast.nodes.program.ProgramNode;
import fr.amvproject.group3.mjj.utils.NodeConsts;
import fr.amvproject.group3.mjj.utils.NodeOperations.MockToken;

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
        MockToken classToken = new MockToken("class", 0, 0);
        MockToken identToken = new MockToken("TestClass", 1, 0);
        MockToken mainToken = new MockToken("main", 0, 0);
        List<ClassNode> classes = Arrays.asList(
                new ClassNode(classToken,
                        new IdentNode(identToken),
                        new DeclsNode(new ArrayList<>()),
                        new MainNode(mainToken, new VarsNode(new ArrayList<>()), new InstrsNode(new ArrayList<>()))
                )
        );

        // When
        MockToken eofToken = new MockToken(null, 0, 0);
        ProgramNode programNode = new ProgramNode(classes, eofToken);

        // Then
        assertThat(programNode, is(notNullValue()));
        assertThat(programNode.getClasses(), hasSize(1));
        assertThat(programNode.getClasses().get(0).getIdent().getName(), is("TestClass"));
    }

    // ====================== ClassNode ======================
    @Test
    public void givenClassComponents__whenCreateClassNode__thenClassNodeIsCorrect() {
        // Given
        IdentNode ident = new IdentNode(new MockToken("TestClass", 1, 0));
        DeclsNode decls = new DeclsNode(new ArrayList<>());
        MainNode main = new MainNode(new MockToken("main",1,0),new VarsNode(new ArrayList<>()), new InstrsNode(new ArrayList<>()));

        // When
        ClassNode classNode = new ClassNode(new MockToken("class",1,0),ident, decls, main);

        // Then
        assertThat(classNode, is(notNullValue()));
        assertThat(classNode.getIdent().getName(), is("TestClass"));
        assertThat(classNode.getDecls().getList(), is(empty()));
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
        assertThat(declsNode.getList(), is(empty()));
    }

    // ====================== VarNode ======================
    @Test
    public void givenVarComponents__whenCreateVarNode__thenVarNodeIsCorrect() {
        // Given
        IdentNode ident = new IdentNode(new MockToken("x", 1,0));
        TypeMethNode type = new TypeMethNode(new MockToken("int", 1,0));

        // When
        VarNode varNode = new VarNode(type, ident);

        // Then
        assertThat(varNode, is(notNullValue()));
        assertThat(varNode.getIdent().getName(), is("x"));
        assertThat(varNode.getType().getName(), is("int"));
        assertThat(varNode.getInitExp(), is(NodeConsts.DEFAULT_INT_NODE));
    }

    // ====================== CstNode ======================
    @Test
    public void givenConstComponents__whenCreateCstNode__thenCstNodeIsCorrect() {
        // Given
        IdentNode ident =new IdentNode(new MockToken("T", 1,0));
        TypeNode type = new TypeNode(new MockToken("int", 1,0));
        ExpNode exp = new IntLiteralNode(new MockToken("3", 1,0));

        // When
        CstNode cstNode = new CstNode(new MockToken("cst",1,0),type, ident,new MockToken("",1,0), exp);

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
        IdentNode ident = new IdentNode(new MockToken("arr", 1,0));
        TypeMethNode type = new TypeMethNode(new MockToken("int", 1,0));
        ExpNode exp = new IntLiteralNode(new MockToken("5", 1,0));

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
        IdentNode ident = new IdentNode(new MockToken("foo", 1,0));
        TypeMethNode returnType = new TypeMethNode(new MockToken("int", 1,0));
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
        MainNode mainNode = new MainNode(new MockToken("main",1,0),vars, instrs);

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
        IdentNode ident = new IdentNode(new MockToken("param", 1,0));
        TypeNode type = new TypeNode(new MockToken("int", 1,0));

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
        ExpNode left = new IntLiteralNode(new MockToken("1", 1,0));
        ExpNode right = new IntLiteralNode(new MockToken("2", 1,0));
        BinaryOpNode op = new BinaryOpNode(new MockToken("+", 1,0));

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
        ExpNode exp = new IntLiteralNode(new MockToken("42", 1,0));
        UnaryOpNode op =new UnaryOpNode(new MockToken("-", 1,0));

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
        IdentNode ident = new IdentNode(new MockToken("foo", 1,0));
        ListExpNode listExp = new ListExpNode(Arrays.asList(new IntLiteralNode(new MockToken("42", 1,0))));

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
        IdentNode ident = new IdentNode(new MockToken("arr", 1,0));

        // When
        LengthNode lengthNode = new LengthNode(new MockToken("lenght",1,0),ident);

        // Then
        assertThat(lengthNode, is(notNullValue()));
        assertThat(lengthNode.getIdent().getName(), is("arr"));
    }

    // ====================== ArrayAccessNode ======================
    @Test
    public void givenArrayAccessComponents__whenCreateArrayAccessNode__thenArrayAccessNodeIsCorrect() {
        // Given
        IdentNode ident = new IdentNode(new MockToken("arr", 1,0));
        ExpNode exp = new IntLiteralNode(new MockToken("0",1,0));

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
        LeftValueNode left = new IdentNode(new MockToken("x", 1,0));
        ExpNode exp = new IntLiteralNode(new MockToken("42", 1,0));

        // When
        AssignNode assignNode = new AssignNode(left,new MockToken("",1,0), exp);

        // Then
        assertThat(assignNode, is(notNullValue()));
        assertThat(assignNode.getLeftValueNode(), is(instanceOf(IdentNode.class)));
        assertThat(assignNode.getExp(), is(instanceOf(IntLiteralNode.class)));
    }

    // ====================== IncNode ======================
    @Test
    public void givenIncComponents__whenCreateIncNode__thenIncNodeIsCorrect() {
        // Given
        IdentNode ident = new IdentNode(new MockToken("x", 1,0));

        // When
        IncNode incNode = new IncNode(ident,new MockToken("inc",1,0));

        // Then
        assertThat(incNode, is(notNullValue()));
        assertThat(((IdentNode) incNode.getLeftValueNode()).getName(), is("x")); // TODO Handle array case
    }

    // ====================== CallInstrNode ======================
    @Test
    public void givenCallInstrComponents__whenCreateCallInstrNode__thenCallInstrNodeIsCorrect() {
        // Given
        IdentNode ident = new IdentNode(new MockToken("foo", 1,0));
        ListExpNode listExp = new ListExpNode(Arrays.asList(new IntLiteralNode(new MockToken("42", 1,0))));

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
        ExpNode exp = new IntLiteralNode(new MockToken("42", 1,0));

        // When
        ReturnNode returnNode = new ReturnNode(new MockToken("return",1,0),exp);

        // Then
        assertThat(returnNode, is(notNullValue()));
        assertThat(returnNode.getExp(), is(instanceOf(IntLiteralNode.class)));
    }

    // ====================== WriteNode ======================
    @Test
    public void givenWriteComponents__whenCreateWriteNode__thenWriteNodeIsCorrect() {
        // Given
        ExpNode exp = new IdentNode(new MockToken("x", 1,0));
        boolean isLn = false;

        // When
        WriteNode writeNode = new WriteNode(new MockToken("write",1,0),exp, isLn);

        // Then
        assertThat(writeNode, is(notNullValue()));
        assertThat(writeNode.getExp(), is(instanceOf(IdentNode.class)));
        assertThat(writeNode.isLn(), is(false));
    }

    // ====================== IfNode ======================
    @Test
    public void givenIfComponents__whenCreateIfNode__thenIfNodeIsCorrect() {
        // Given
        ExpNode cond = new BoolLiteralNode(new MockToken("true",1,0));
        InstrsNode thenInstrs = new InstrsNode(new ArrayList<>());
        InstrsNode elseInstrs = new InstrsNode(new ArrayList<>());

        // When
        IfNode ifNode = new IfNode(new MockToken("if",1,0),cond, thenInstrs,new MockToken("else",1,0), elseInstrs);

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
        ExpNode cond = new BoolLiteralNode(new MockToken("true",1,0));
        InstrsNode instrs = new InstrsNode(new ArrayList<>());

        // When
        WhileNode whileNode = new WhileNode(new MockToken("while",1,0),cond, instrs);

        // Then
        assertThat(whileNode, is(notNullValue()));
        assertThat(whileNode.getCondExp(), is(instanceOf(BoolLiteralNode.class)));
        assertThat(whileNode.getInstrs().getList(), is(empty()));
    }

    // ====================== IntLiteralNode ======================
    @Test
    public void givenIntLiteralValue__whenCreateIntLiteralNode__thenIntLiteralNodeIsCorrect() {
        // Given
        String value = "42";

        // When
        IntLiteralNode intLiteralNode = new IntLiteralNode(new MockToken(value,1,0));

        // Then
        assertThat(intLiteralNode, is(notNullValue()));
        assertThat(intLiteralNode.getValue(), is(42));
    }

    // ====================== BoolLiteralNode ======================
    @Test
    public void givenBoolLiteralValue__whenCreateBoolLiteralNode__thenBoolLiteralNodeIsCorrect() {
        // Given
        String value = "true";

        // When
        BoolLiteralNode boolLiteralNode = new BoolLiteralNode(new MockToken(value,1,0));

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
        IdentNode identNode = new IdentNode(new MockToken(name,line,0));

        // Then
        assertThat(identNode, is(notNullValue()));
        assertThat(identNode.getName(), is("x"));
        assertThat(identNode.getLine(), is(1));
    }
}
