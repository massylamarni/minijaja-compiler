package fr.amvproject.group3.ast;

import fr.amvproject.group3.jjc.ast.nodes.finals.*;
import fr.amvproject.group3.jjc.ast.nodes.instrs.*;
import fr.amvproject.group3.jjc.ast.nodes.intermediate.InstrNode;
import fr.amvproject.group3.jjc.ast.nodes.intermediate.ValueNode;
import fr.amvproject.group3.jjc.ast.nodes.literals.*;
import fr.amvproject.group3.jjc.ast.nodes.other.*;
import fr.amvproject.group3.jjc.ast.nodes.program.ClassNode;
import fr.amvproject.group3.jjc.ast.nodes.program.ProgramNode;
import fr.amvproject.group3.jjc.utils.NodeOperations.MockToken;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

public class AstNodeTest {

    // ====================== ProgramNode ======================
    @Test
    public void givenClassList__whenCreateProgramNode__thenProgramNodeIsCorrect() {
        MockToken intToken = new MockToken("1", 0, 0);
        MockToken initToken = new MockToken("init", 0, 0);
        MockToken eofToken = new MockToken(null, 0, 0);

        // Given
        List<ClassNode> classes = Arrays.asList(
                new ClassNode(
                        new AddressNode(intToken),
                        new InitLiteral(initToken)
                )
        );

        // When
        ProgramNode programNode = new ProgramNode(classes, eofToken);

        // Then
        assertThat(programNode, is(notNullValue()));
        assertThat(programNode.getClasses(), hasSize(1));
        assertThat(programNode.getClasses().get(0).getAddress().getValue(), is(1));
        assertThat(programNode.getClasses().get(0).getInstr(), is(instanceOf(InitLiteral.class)));
    }

    // TODO TEST

    /*
    // ====================== ClassNode ======================
    @Test
    public void givenClassComponents__whenCreateClassNode__thenClassNodeIsCorrect() {
        // Given
        AddressNode address = new AddressNode(1);
        InstrNode instr = new InitLiteral();

        // When
        ClassNode classNode = new ClassNode(address, instr);

        // Then
        assertThat(classNode, is(notNullValue()));
        assertThat(classNode.getAddress().getValue(), is(1));
        assertThat(classNode.getInstr(), is(instanceOf(InitLiteral.class)));
    }

    // ====================== AddressNode ======================
    @Test
    public void givenAddressValue__whenCreateAddressNode__thenAddressNodeIsCorrect() {
        // Given
        int address = 1;

        // When
        AddressNode addressNode = new AddressNode(address);

        // Then
        assertThat(addressNode, is(notNullValue()));
        assertThat(addressNode.getValue(), is(1));
    }

    // ====================== InitLiteral ======================
    @Test
    public void givenNothing__whenCreateInitLiteral__thenInitLiteralIsCorrect() {
        // When
        InitLiteral initLiteral = new InitLiteral();

        // Then
        assertThat(initLiteral, is(notNullValue()));
    }

    // ====================== SwapLiteral ======================
    @Test
    public void givenNothing__whenCreateSwapLiteral__thenSwapLiteralIsCorrect() {
        // When
        SwapLiteral swapLiteral = new SwapLiteral();

        // Then
        assertThat(swapLiteral, is(notNullValue()));
    }

    // ====================== NewNode ======================
    @Test
    public void givenNewComponents__whenCreateNewNode__thenNewNodeIsCorrect() {
        // Given
        IdentNode ident = new IdentNode("var", 1);
        TypeNode type = new TypeNode("entier");
        ObjNode obj = new ObjNode("var");
        ValueNode value = new IntLiteralNode(42);

        // When
        NewNode newNode = new NewNode(ident, type, obj, value);

        // Then
        assertThat(newNode, is(notNullValue()));
        assertThat(newNode.getIdent().getName(), is("var"));
        assertThat(newNode.getType().getName(), is("entier"));
        assertThat(newNode.getObj().getName(), is("var"));
        assertThat(newNode.getValue(), is(instanceOf(IntLiteralNode.class)));
    }

    // ====================== NewArrayNode ======================
    @Test
    public void givenNewArrayComponents__whenCreateNewArrayNode__thenNewArrayNodeIsCorrect() {
        // Given
        IdentNode ident = new IdentNode("arr", 1);
        TypeNode type = new TypeNode("entier");

        // When
        NewArrayNode newArrayNode = new NewArrayNode(ident, type);

        // Then
        assertThat(newArrayNode, is(notNullValue()));
        assertThat(newArrayNode.getIdent().getName(), is("arr"));
        assertThat(newArrayNode.getType().getName(), is("entier"));
    }

    // ====================== InvokeNode ======================
    @Test
    public void givenInvokeComponents__whenCreateInvokeNode__thenInvokeNodeIsCorrect() {
        // Given
        IdentNode ident = new IdentNode("method", 1);

        // When
        InvokeNode invokeNode = new InvokeNode(ident);

        // Then
        assertThat(invokeNode, is(notNullValue()));
        assertThat(invokeNode.getIdent().getName(), is("method"));
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

    // ====================== ReturnLiteral ======================
    @Test
    public void givenNothing__whenCreateReturnLiteral__thenReturnLiteralIsCorrect() {
        // When
        ReturnLiteral returnLiteral = new ReturnLiteral();

        // Then
        assertThat(returnLiteral, is(notNullValue()));
    }

    // ====================== WriteLiteral ======================
    @Test
    public void givenWriteComponents__whenCreateWriteLiteral__thenWriteLiteralIsCorrect() {
        // Given
        boolean isLn = true;

        // When
        WriteLiteral writeLiteral = new WriteLiteral(isLn);

        // Then
        assertThat(writeLiteral, is(notNullValue()));
        assertThat(writeLiteral.isLn(), is(true));
    }

    // ====================== PushNode ======================
    @Test
    public void givenPushComponents__whenCreatePushNode__thenPushNodeIsCorrect() {
        // Given
        ValueNode value = new IntLiteralNode(42);

        // When
        PushNode pushNode = new PushNode(value);

        // Then
        assertThat(pushNode, is(notNullValue()));
        assertThat(pushNode.getValue(), is(instanceOf(IntLiteralNode.class)));
    }

    // ====================== PopLiteral ======================
    @Test
    public void givenNothing__whenCreatePopLiteral__thenPopLiteralIsCorrect() {
        // When
        PopLiteral popLiteral = new PopLiteral();

        // Then
        assertThat(popLiteral, is(notNullValue()));
    }

    // ====================== LoadNode ======================
    @Test
    public void givenLoadComponents__whenCreateLoadNode__thenLoadNodeIsCorrect() {
        // Given
        IdentNode ident = new IdentNode("var", 1);
        boolean isArray = false;

        // When
        LoadNode loadNode = new LoadNode(ident, isArray);

        // Then
        assertThat(loadNode, is(notNullValue()));
        assertThat(loadNode.getIdent().getName(), is("var"));
        assertThat(loadNode.isA(), is(false));
    }

    // ====================== StoreNode ======================
    @Test
    public void givenStoreComponents__whenCreateStoreNode__thenStoreNodeIsCorrect() {
        // Given
        IdentNode ident = new IdentNode("var", 1);
        boolean isArray = false;

        // When
        StoreNode storeNode = new StoreNode(ident, isArray);

        // Then
        assertThat(storeNode, is(notNullValue()));
        assertThat(storeNode.getIdent().getName(), is("var"));
        assertThat(storeNode.isA(), is(false));
    }

    // ====================== IfNode ======================
    @Test
    public void givenIfComponents__whenCreateIfNode__thenIfNodeIsCorrect() {
        // Given
        AddressNode address = new AddressNode(1);

        // When
        IfNode ifNode = new IfNode(address);

        // Then
        assertThat(ifNode, is(notNullValue()));
        assertThat(ifNode.getAddress().getValue(), is(1));
    }

    // ====================== GotoNode ======================
    @Test
    public void givenGotoComponents__whenCreateGotoNode__thenGotoNodeIsCorrect() {
        // Given
        AddressNode address = new AddressNode(1);

        // When
        GotoNode gotoNode = new GotoNode(address);

        // Then
        assertThat(gotoNode, is(notNullValue()));
        assertThat(gotoNode.getAddress().getValue(), is(1));
    }

    // ====================== IncNode ======================
    @Test
    public void givenIncComponents__whenCreateIncNode__thenIncNodeIsCorrect() {
        // Given
        IdentNode ident = new IdentNode("var", 1);
        boolean isArray = false;

        // When
        IncNode incNode = new IncNode(ident, isArray);

        // Then
        assertThat(incNode, is(notNullValue()));
        assertThat(incNode.getIdent().getName(), is("var"));
        assertThat(incNode.isA(), is(false));
    }

    // ====================== NopLiteral ======================
    @Test
    public void givenNothing__whenCreateNopLiteral__thenNopLiteralIsCorrect() {
        // When
        NopLiteral nopLiteral = new NopLiteral();

        // Then
        assertThat(nopLiteral, is(notNullValue()));
    }

    // ====================== JcstopLiteral ======================
    @Test
    public void givenNothing__whenCreateJcstopLiteral__thenJcstopLiteralIsCorrect() {
        // When
        JcstopLiteral jcstopLiteral = new JcstopLiteral();

        // Then
        assertThat(jcstopLiteral, is(notNullValue()));
    }

    // ====================== UnaryOpNode ======================
    @Test
    public void givenUnaryOpValue__whenCreateUnaryOpNode__thenUnaryOpNodeIsCorrect() {
        // Given
        String op = "not";

        // When
        UnaryOpNode unaryOpNode = new UnaryOpNode(op);

        // Then
        assertThat(unaryOpNode, is(notNullValue()));
        assertThat(unaryOpNode.getOp().getName(), is("not"));
    }

    // ====================== BinaryOpNode ======================
    @Test
    public void givenBinaryOpValue__whenCreateBinaryOpNode__thenBinaryOpNodeIsCorrect() {
        // Given
        String op = "add";

        // When
        BinaryOpNode binaryOpNode = new BinaryOpNode(op);

        // Then
        assertThat(binaryOpNode, is(notNullValue()));
        assertThat(binaryOpNode.getOp().getName(), is("add"));
    }

    // ====================== IdentNode ======================
    @Test
    public void givenIdentComponents__whenCreateIdentNode__thenIdentNodeIsCorrect() {
        // Given
        String name = "var";
        int line = 1;

        // When
        IdentNode identNode = new IdentNode(name, line);

        // Then
        assertThat(identNode, is(notNullValue()));
        assertThat(identNode.getName(), is("var"));
        assertThat(identNode.getLine(), is(1));
    }

    // ====================== IntLiteralNode ======================
    @Test
    public void givenIntValue__whenCreateIntLiteralNode__thenIntLiteralNodeIsCorrect() {
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
    public void givenBoolValue__whenCreateBoolLiteralNode__thenBoolLiteralNodeIsCorrect() {
        // Given
        boolean value = true;

        // When
        BoolLiteralNode boolLiteralNode = new BoolLiteralNode(value);

        // Then
        assertThat(boolLiteralNode, is(notNullValue()));
        assertThat(boolLiteralNode.getValue(), is(true));
    }

    // ====================== StringLiteralNode ======================
    @Test
    public void givenStringValue__whenCreateStringLiteralNode__thenStringLiteralNodeIsCorrect() {
        // Given
        String value = "test";

        // When
        StringLiteralNode stringLiteralNode = new StringLiteralNode(value);

        // Then
        assertThat(stringLiteralNode, is(notNullValue()));
        assertThat(stringLiteralNode.getValue(), is("test"));
    }

    // ====================== TypeNode ======================
    @Test
    public void givenTypeValue__whenCreateTypeNode__thenTypeNodeIsCorrect() {
        // Given
        String type = "entier";

        // When
        TypeNode typeNode = new TypeNode(type);

        // Then
        assertThat(typeNode, is(notNullValue()));
        assertThat(typeNode.getName(), is("entier"));
    }

    // ====================== ObjNode ======================
    @Test
    public void givenObjValue__whenCreateObjNode__thenObjNodeIsCorrect() {
        // Given
        String obj = "var";

        // When
        ObjNode objNode = new ObjNode(obj);

        // Then
        assertThat(objNode, is(notNullValue()));
        assertThat(objNode.getName(), is("var"));
    }
    */
}
