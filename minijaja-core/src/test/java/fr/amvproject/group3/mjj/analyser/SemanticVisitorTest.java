package fr.amvproject.group3.mjj.analyser;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import static fr.amvproject.group3.mjj.utils.NodeOperations.MockToken;

import fr.amvproject.group3.mjj.analyser.symbols.*;
import fr.amvproject.group3.mjj.ast.nodes.abstracts.*;
import fr.amvproject.group3.mjj.ast.nodes.decls.*;
import fr.amvproject.group3.mjj.ast.nodes.exprs.BinaryExpNode;
import fr.amvproject.group3.mjj.ast.nodes.exprs.CallExpNode;
import fr.amvproject.group3.mjj.ast.nodes.exprs.LengthNode;
import fr.amvproject.group3.mjj.ast.nodes.exprs.UnaryExpNode;
import fr.amvproject.group3.mjj.ast.nodes.instrs.*;
import fr.amvproject.group3.mjj.ast.nodes.other.BinaryOpNode;
import fr.amvproject.group3.mjj.ast.nodes.other.TypeMethNode;
import fr.amvproject.group3.mjj.ast.nodes.other.UnaryOpNode;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import fr.amvproject.group3.exceptions.SemanticException;
import fr.amvproject.group3.mjj.analyser.nodes.SymbolIdentity;
import fr.amvproject.group3.mjj.analyser.nodes.TypeNode;
import fr.amvproject.group3.mjj.analyser.nodes.ObjNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.*;
import fr.amvproject.group3.mjj.ast.nodes.intermediate.*;
import fr.amvproject.group3.mjj.ast.nodes.program.ClassNode;
import fr.amvproject.group3.mjj.ast.nodes.program.ProgramNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SemanticVisitorTest {

    private SemanticVisitor visitor;

    @Before
    public void setup() {
        visitor = new SemanticVisitor();
    }

    // ------------------------
    // Variables simples
    // ------------------------
    @Test
    public void testVarDeclaration_GivenVarDecl_WhenVisited_ThenSymbolTableUpdated() {

        // Given
        IdentNode ident = new IdentNode(new MockToken("x", 1, 1));
        IntLiteralNode init = new IntLiteralNode(new MockToken("42", 1, 0));
        VarNode varNode = new VarNode(
                new TypeMethNode(new MockToken("int", 1, 0)),
                ident,
                new MockToken("=", 1, 0),
                init
        );

        // When
        SymbolIdentity type = visitor.visitVar(varNode);

        // Then
        assertThat(type.getTypeNode().getName(), is("void"));
        assertThat(visitor.getSymbolTable().lookup(ident).getTypeNode().getName(), is("int"));
    }


    @Test(expected = SemanticException.class)
    public void testDoubleVarDeclaration_GivenSameVarTwice_WhenDeclared_ThenThrowSemanticException() {

        // Given
        IdentNode ident = new IdentNode(new MockToken("x", 1, 1));
        IntLiteralNode init1 = new IntLiteralNode(new MockToken("1",1,0));
        VarNode first = new VarNode(new TypeMethNode(new MockToken("int",1,0)), ident,
                new MockToken("=",1,0), init1);

        IntLiteralNode init2 = new IntLiteralNode(new MockToken("2",1,0));
        VarNode second = new VarNode(new TypeMethNode(new MockToken("int",1,0)), ident,
                new MockToken("=",1,0), init2);

        visitor.visitVar(first);

        // When
        visitor.visitVar(second);

        // Then → exception
    }


    @Test(expected = SemanticException.class)
    public void testUseUndeclaredVar_GivenUnknownIdent_WhenVisited_ThenThrowSemanticException() {

        // Given
        IdentNode ident = new IdentNode(new MockToken("y", 1, 1));

        // When
        visitor.visitIdent(ident);

        // Then → exception
    }

    // ------------------------
    // Assignation
    // ------------------------
    @Test
    public void testAssignCompatibleType_GivenDeclaredInt_WhenAssignInt_ThenOK() {

        // Given
        IdentNode ident = new IdentNode(new MockToken("x", 1, 1));
        VarNode varNode = new VarNode(new TypeMethNode(new MockToken("int",1,0)),
                ident, new MockToken("=",1,1), new IntLiteralNode(new MockToken("10",1,1)));
        visitor.visitVar(varNode);

        AssignNode assignNode = new AssignNode(
                ident, new MockToken("=",1,1),
                new IntLiteralNode(new MockToken("5",1,1))
        );

        // When
        SymbolIdentity type = visitor.visitAssign(assignNode);

        // Then
        assertThat(type.getTypeNode().getName(), is("void"));
    }


    @Test(expected = SemanticException.class)
    public void testAssignIncompatibleType_GivenIntVar_WhenAssignBool_ThenThrowSemanticException() {

        // Given
        IdentNode ident = new IdentNode(new MockToken("x", 1, 1));
        VarNode varNode = new VarNode(new TypeMethNode(new MockToken("int",1,0)),
                ident,new MockToken("=",1,1),new IntLiteralNode(new MockToken("10",1,1)));
        visitor.visitVar(varNode);

        AssignNode assignNode = new AssignNode(
                ident,new MockToken("=",1,1),
                new BoolLiteralNode(new MockToken("true",1,1))
        );

        // When
        visitor.visitAssign(assignNode);

        // Then → exception
    }


    // ------------------------
    // Expressions binaires
    // ------------------------
    @Test
    public void testBinaryExpCompatibleTypes_GivenTwoInts_WhenAdd_ThenReturnInt() {

        // Given
        IntLiteralNode left = new IntLiteralNode(new MockToken("2",1,0));
        IntLiteralNode right = new IntLiteralNode(new MockToken("3",1,0));
        BinaryExpNode node = new BinaryExpNode(new BinaryOpNode("+",1,0), left, right);

        // When
        SymbolIdentity type = visitor.visitBinaryExp(node);

        // Then
        assertThat(type.getTypeNode().getName(), is("int"));
    }
    @Test(expected = SemanticException.class)
    public void testBinaryExpIncompatibleTypes_GivenIntAndBool_WhenAdd_ThenThrowSemanticException() {

        // Given
        IntLiteralNode left = new IntLiteralNode(new MockToken("2",1,0));
        BoolLiteralNode right = new BoolLiteralNode(new MockToken("true",1,0));
        BinaryExpNode node = new BinaryExpNode(new BinaryOpNode("+",1,0), left, right);

        // When
        visitor.visitBinaryExp(node);

        // Then → exception
    }

    // ------------------------
    // If / While
    // ------------------------
    @Test
    public void testIfBooleanCondition_GivenBool_WhenIf_ThenOK() {

        // Given
        IfNode ifNode = new IfNode(
                new MockToken("if",1,0),
                new BoolLiteralNode(new MockToken("true",1,0)),
                new InstrsNode(new ArrayList<>()),
                null, null
        );

        // When
        SymbolIdentity type = visitor.visitIf(ifNode);

        // Then
        assertThat(type.getTypeNode().getName(), is("void"));
    }


    @Test(expected = SemanticException.class)
    public void testIfNonBoolean_GivenInt_WhenIf_ThenThrowSemanticException() {

        // Given
        IfNode ifNode = new IfNode(
                new MockToken("if",1,0),
                new IntLiteralNode(new MockToken("1",1,0)),
                new InstrsNode(new ArrayList<>()),
                null, null
        );

        // When
        visitor.visitIf(ifNode);

        // Then → exception
    }


    @Test
    public void testWhileBoolean_GivenBool_WhenWhile_ThenOK() {

        // Given
        WhileNode whileNode = new WhileNode(
                new MockToken("while",1,0),
                new BoolLiteralNode(new MockToken("true",1,0)),
                new InstrsNode(new ArrayList<>())
        );

        // When
        SymbolIdentity type = visitor.visitWhile(whileNode);

        // Then
        assertThat(type.getTypeNode().getName(), is("void"));
    }

    @Test(expected = SemanticException.class)
    public void testWhileNonBoolean_GivenInt_WhenWhile_ThenThrowSemanticException() {

        // Given
        WhileNode whileNode = new WhileNode(
                new MockToken("while",1,0),
                new IntLiteralNode(new MockToken("0",1,0)),
                new InstrsNode(new ArrayList<>())
        );

        // When
        visitor.visitWhile(whileNode);

        // Then → exception
    }

    // ------------------------
    // Program / Class
    // ------------------------
    @Test
    public void testVisitProgram_GivenEmptyClass_WhenVisited_ThenOK() {

        // Given
        ClassNode classNode = new ClassNode(
                new MockToken("class",1,0),
                new IdentNode(new MockToken("C",1,0)),
                new DeclsNode(new ArrayList<>()),
                new MainNode(new MockToken("main",1,0),
                        new VarsNode(new ArrayList<>()),
                        new InstrsNode(new ArrayList<>())
                )
        );
        ProgramNode program = new ProgramNode(List.of(classNode), new MockToken("",1,0));

        // When
        SymbolIdentity type = visitor.visitProgram(program);

        // Then
        assertThat(type.getTypeNode().getName(), is("void"));
    }

    @Test(expected = SemanticException.class)
    public void givenBooleanIndex_whenVisitTable_thenThrowSemanticException() {
        // Given
        IdentNode ident = new IdentNode(new MockToken("arr", 1, 1));
        TableNode tableNode = new TableNode(new TypeMethNode("int",1,1), ident, new BoolLiteralNode(new MockToken("true",1,1)));

        // When
        visitor.visitTable(tableNode);

        // Then → exception attendue
    }

    // ----------------------------------------------------------
    // CONSTANT DECLARATION
    // ----------------------------------------------------------
    @Test
    public void givenValidConst_whenVisitCst_thenConstStored() {
        // Given
        IdentNode id = new IdentNode(new MockToken("PI", 1, 1));
        CstNode cst = new CstNode(new MockToken("cst", 1, 1),
                new fr.amvproject.group3.mjj.ast.nodes.other.TypeNode(new MockToken("int",1,1)),
                id,new MockToken("=",1,1), new IntLiteralNode(new MockToken("3", 1, 1)));

        // When
        visitor.visitCst(cst);

        // Then
        CstSymbol sym = (CstSymbol) visitor.getSymbolTable().lookup(id);
        assertThat(sym.getTypeNode().getName(), is("int"));
        assertThat(sym.isVcst(), is(false));
    }

    @Test(expected = SemanticException.class)
    public void givenInvalidConstType_whenVisitCst_thenThrowSemanticException() {
        // Given
        IdentNode id = new IdentNode(new MockToken("PI", 1, 1));
        CstNode cst = new CstNode(new MockToken("cst", 1, 1),
                new fr.amvproject.group3.mjj.ast.nodes.other.TypeNode(new MockToken("boolean",1,1)),
                id,new MockToken("=",1,1), new IntLiteralNode(new MockToken("true", 1, 1)));

        // When
        visitor.visitCst(cst);

        // Then → exception attendue
    }

    // ----------------------------------------------------------
    // METHOD DECLARATION
    // ----------------------------------------------------------
    @Test
    public void givenValidMethod_whenVisitMethod_thenMethodRegistered() {
        // Given
        IdentNode id = new IdentNode(new MockToken("add", 1, 1));

        MethodNode method = new MethodNode(
                new TypeMethNode("int",1,1),id,
                new ParamsNode(new ArrayList<>()),
                new VarsNode(new ArrayList<>()),
                new InstrsNode(Arrays.asList(
                        new ReturnNode(new MockToken("return",1,1),new IntLiteralNode(new MockToken("5",1,1)))
                ))
        );

        // When
        visitor.visitMethod(method);

        // Then
        MethodSymbol sym = (MethodSymbol) visitor.getSymbolTable().lookup(id);
        assertThat(sym.getTypeNode().getName(), is("int"));
        assertThat(sym.getParamsType().size(), is(0));
    }

    @Test(expected = SemanticException.class)
    public void givenDuplicateSignature_whenVisitMethod_thenThrowSemanticException() {
        // Given
        IdentNode id = new IdentNode(new MockToken("add", 1, 1));
        ParamsNode params = new ParamsNode(Arrays.asList(
                new ParamNode(new fr.amvproject.group3.mjj.ast.nodes.other.TypeNode(new MockToken("int",1,1)), new IdentNode(new MockToken("x", 2, 2)))
        ));
        visitor.getSymbolTable().addSymbol(id, new TypeNode("int"), List.of(new SymbolIdentity(new TypeNode("int"), new ObjNode("var", 0, 0))));



        MethodNode method = new MethodNode(
                new TypeMethNode("int",1,1),id,
                params,
                new VarsNode(new ArrayList<>()),
                new InstrsNode(Arrays.asList(
                        new ReturnNode(new MockToken("return",1,1),new IntLiteralNode(new MockToken("1",1,1)))
                ))
        );

        // When
        visitor.visitMethod(method);

        // Then → exception attendue
    }

    @Test(expected = SemanticException.class)
    public void givenWrongReturnType_whenVisitMethod_thenThrowSemanticException() {
        // Given
        IdentNode id = new IdentNode(new MockToken("test", 1, 1));

        MethodNode method = new MethodNode(
                new TypeMethNode("int",1,1),id,
                new ParamsNode(new ArrayList<>()),
                new VarsNode(new ArrayList<>()),
                new InstrsNode(Arrays.asList(
                        new ReturnNode(new MockToken("return",1,1),new BoolLiteralNode(new MockToken("true",1,1)))
                ))
        );

        // When
        visitor.visitMethod(method);

        // Then → exception attendue
    }

    @Test
    public void givenMethodWithParameters_whenVisitMethod_thenParametersStored() {
        // Given
        IdentNode id = new IdentNode(new MockToken("sum",1,1));

        ParamsNode params = new ParamsNode(Arrays.asList(
                new ParamNode(new fr.amvproject.group3.mjj.ast.nodes.other.TypeNode("int",1,1), new IdentNode(new MockToken("a",1,1))),
                new ParamNode(new fr.amvproject.group3.mjj.ast.nodes.other.TypeNode("int",1,1), new IdentNode(new MockToken("b",1,1)))
        ));

        MethodNode method = new MethodNode(
                new TypeMethNode("int",1,1),id,
                params,
                new VarsNode(new ArrayList<>()),
                new InstrsNode(Arrays.asList(new ReturnNode(new MockToken("return",1,1),new IntLiteralNode(new MockToken("1",1,1)))))
        );

        // When
        visitor.visitMethod(method);

        // Then
        MethodSymbol sym = (MethodSymbol) visitor.getSymbolTable().lookup(id);
        assertThat(sym.getParamsType().size(), is(2));
        assertThat(sym.getParamsType().get(0).getTypeNode().getName(), is("int"));
        assertThat(sym.getParamsType().get(1).getTypeNode().getName(), is("int"));
    }

    @Test
    public void testUnaryExp_GivenIntAndNeg_WhenVisited_ThenReturnInt() {

        // Given
        UnaryExpNode node = new UnaryExpNode(
                new UnaryOpNode("-", 1, 1),
                new IntLiteralNode(new MockToken("5",1,1))
        );

        // When
        SymbolIdentity type = visitor.visitUnaryExp(node);

        // Then
        assertThat(type.getTypeNode().getName(), is("int"));
    }

    @Test
    public void testUnaryExp_GivenBoolAndNot_WhenVisited_ThenReturnBoolean() {

        // Given
        UnaryExpNode node = new UnaryExpNode(
                new UnaryOpNode("!", 1, 1),
                new BoolLiteralNode(new MockToken("true",1,1))
        );

        // When
        SymbolIdentity type = visitor.visitUnaryExp(node);

        // Then
        assertThat(type.getTypeNode().getName(), is("boolean"));
    }
    @Test(expected = SemanticException.class)
    public void testUnaryExp_GivenIntAndNot_WhenVisited_ThenThrowSemanticException() {

        // Given
        UnaryExpNode node = new UnaryExpNode(
                new UnaryOpNode("!",1,1),
                new IntLiteralNode(new MockToken("5",1,1))
        );

        // When
        visitor.visitUnaryExp(node);

        // Then → exception
    }

    @Test(expected = SemanticException.class)
    public void testUnaryExp_GivenBoolAndNeg_WhenVisited_ThenThrowSemanticException() {

        // Given
        UnaryExpNode node = new UnaryExpNode(
                new UnaryOpNode("-",1,1),
                new BoolLiteralNode(new MockToken("true",1,1))
        );

        // When
        visitor.visitUnaryExp(node);

        // Then → exception
    }


    @Test
    public void testAddAssign_GivenIntVar_WhenAddAssignInt_ThenOK() {

        // Given
        IdentNode ident = new IdentNode(new MockToken("x",1,1));
        visitor.visitVar(new VarNode(new TypeMethNode(new MockToken("int",1,1)),
                ident, new MockToken("=",1,1), new IntLiteralNode(new MockToken("1",1,1))));

        AddAssignNode node = new AddAssignNode(
                ident,
                new MockToken("+=",1,1),
                new IntLiteralNode(new MockToken("2",1,1))
        );

        // When
        SymbolIdentity type = visitor.visitAddAssign(node);

        // Then
        assertThat(type.getTypeNode().getName(), is("void"));
    }


    @Test(expected = SemanticException.class)
    public void testAddAssign_GivenIntVar_WhenAddAssignBool_ThenThrowSemanticException() {

        // Given
        IdentNode ident = new IdentNode(new MockToken("x",1,1));
        visitor.visitVar(new VarNode(new TypeMethNode(new MockToken("int",1,1)),
                ident,new MockToken("=",1,1), new IntLiteralNode(new MockToken("1",1,1))));

        AddAssignNode node = new AddAssignNode(
                ident,
                new MockToken("+=",1,1),
                new BoolLiteralNode(new MockToken("true",1,1))
        );

        // When
        visitor.visitAddAssign(node);

        // Then → exception
    }


    @Test
    public void testInc_GivenIntVar_WhenInc_ThenOK() {

        // Given
        IdentNode ident = new IdentNode(new MockToken("x",1,1));
        visitor.visitVar(new VarNode(new TypeMethNode(new MockToken("int",1,1)),
                ident,new MockToken("=",1,1), new IntLiteralNode(new MockToken("10",1,1))));

        IncNode inc = new IncNode(ident, new MockToken("++",1,1));

        // When
        SymbolIdentity type = visitor.visitInc(inc);

        // Then
        assertThat(type.getTypeNode().getName(), is("void"));
    }

    @Test(expected = SemanticException.class)
    public void testInc_GivenBoolVar_WhenInc_ThenThrowSemanticException() {

        // Given
        IdentNode ident = new IdentNode(new MockToken("b",1,1));
        visitor.visitVar(new VarNode(new TypeMethNode(new MockToken("boolean",1,1)),
                ident,new MockToken("=",1,1), new BoolLiteralNode(new MockToken("true",1,1))));

        IncNode inc = new IncNode(ident, new MockToken("++",1,1));

        // When
        visitor.visitInc(inc);

        // Then → exception
    }

    @Test(expected = SemanticException.class)
    public void testLength_GivenClassSymbol_WhenVisited_ThenThrowSemanticException() {

        // Given
        IdentNode ident = new IdentNode(new MockToken("C",1,1));
        visitor.getSymbolTable().addSymbol(ident);

        LengthNode len = new LengthNode(new MockToken("length",1,1),ident);

        // When
        visitor.visitLength(len);

        // Then → exception
    }


    @Test(expected = SemanticException.class)
    public void testLength_GivenMethodSymbol_WhenVisited_ThenThrowSemanticException() {

        // Given
        IdentNode ident = new IdentNode(new MockToken("f",1,1));
        visitor.getSymbolTable().addSymbol(ident, new TypeNode("int"), List.of());

        LengthNode len = new LengthNode(new MockToken("length",1,1),ident);

        // When
        visitor.visitLength(len);
    }

    @Test(expected = SemanticException.class)
    public void testLength_GivenConstant_WhenVisited_ThenThrowSemanticException() {

        // Given
        IdentNode ident = new IdentNode(new MockToken("X",1,1));
        visitor.getSymbolTable().addSymbol(ident, new TypeNode("int"),false);

        LengthNode len = new LengthNode(new MockToken("length",1,1),ident);

        // When
        visitor.visitLength(len);
    }

    @Test(expected = SemanticException.class)
    public void testLength_GivenVariable_WhenVisited_ThenThrowSemanticException() {

        // Given
        IdentNode ident = new IdentNode(new MockToken("x",1,1));
        visitor.getSymbolTable().addSymbol(ident,new TypeNode("int"), new ObjNode("var", 0, 0));

        LengthNode len = new LengthNode(new MockToken("length",1,1),ident);

        // When
        visitor.visitLength(len);
    }


    @Test(expected = SemanticException.class)
    public void testWrite_GivenClassSymbol_WhenVisited_ThenThrowSemanticException() {

        // Given
        IdentNode ident = new IdentNode(new MockToken("C",1,1));
        visitor.getSymbolTable().addSymbol(ident);

        WriteNode write = new WriteNode(new MockToken("write",1,1),ident,false);

        // When
        visitor.visitWrite(write);
    }

    @Test(expected = SemanticException.class)
    public void testWrite_GivenMethodSymbol_WhenVisited_ThenThrowSemanticException() {

        // Given
        IdentNode ident = new IdentNode(new MockToken("f",1,1));
        visitor.getSymbolTable().addSymbol(ident, new TypeNode("int"), List.of());

        WriteNode write = new WriteNode(new MockToken("write",1,1),ident,false);

        // When
        visitor.visitWrite(write);
    }

    @Test
    public void testWrite_GivenVarInt_WhenVisited_ThenOK() {

        // Given
        IdentNode ident = new IdentNode(new MockToken("x",1,1));
        visitor.getSymbolTable().addSymbol(ident,new TypeNode("int"), new ObjNode("var", 0, 0));

        WriteNode write = new WriteNode(new MockToken("write",1,1),ident, false);

        // When
        SymbolIdentity type = visitor.visitWrite(write);

        // Then
        assertThat(type.getTypeNode().getName(), is("void"));
    }

    @Test
    public void testStringLiteral_GivenLiteral_WhenVisited_ThenReturnString() {

        // Given
        StringLiteralNode node = new StringLiteralNode(new MockToken("\"abc\"",1,1));

        // When
        SymbolIdentity type = visitor.visitStringLiteral(node);

        // Then
        assertThat(type.getTypeNode().getName(), is("string"));
    }


    @Test
    public void testCallExp_GivenMatchingParams_WhenVisited_ThenReturnMethodType() {

        // Given
        IdentNode id = new IdentNode(new MockToken("f",1,1));

        visitor.getSymbolTable().addSymbol(id, new TypeNode("int"),
                List.of(new SymbolIdentity(new TypeNode("int"), new ObjNode("var", 0, 0)), new SymbolIdentity(new TypeNode("boolean"), new ObjNode("var", 0, 0))));

        ListExpNode args = new ListExpNode(List.of(
                new IntLiteralNode(new MockToken("5",1,1)),
                new BoolLiteralNode(new MockToken("true",1,1))
        ));

        CallExpNode node = new CallExpNode(id, args);

        // When
        SymbolIdentity type = visitor.visitCallExp(node);

        // Then
        assertThat(type.getTypeNode().getName(), is("int"));
    }

    @Test(expected = SemanticException.class)
    public void testCallExp_GivenWrongParamType_WhenVisited_ThenThrowSemanticException() {

        // Given
        IdentNode id = new IdentNode(new MockToken("f",1,1));

        visitor.getSymbolTable().addSymbol(id, new TypeNode("int"),
                List.of(new SymbolIdentity(new TypeNode("int"), new ObjNode("var", 0, 0))));

        ListExpNode args = new ListExpNode(List.of(
                new BoolLiteralNode(new MockToken("true",1,1))
        ));

        CallExpNode node = new CallExpNode(id, args);

        // When
        visitor.visitCallExp(node);
    }

    @Test
    public void testCallInstr_GivenValidCall_WhenVisited_ThenReturnVoid() {

        // Given
        IdentNode id = new IdentNode(new MockToken("f",1,1));

        visitor.getSymbolTable().addSymbol(id, new TypeNode("void"), List.of());

        CallInstrNode node = new CallInstrNode(id, new ListExpNode(List.of()));

        // When
        SymbolIdentity type = visitor.visitCallInstr(node);

        // Then
        assertThat(type.getTypeNode().getName(), is("void"));
    }



}
