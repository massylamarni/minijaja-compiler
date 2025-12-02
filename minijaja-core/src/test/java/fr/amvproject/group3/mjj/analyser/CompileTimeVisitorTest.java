package fr.amvproject.group3.mjj.analyser;

import fr.amvproject.group3.exceptions.SemanticException;
import fr.amvproject.group3.mjj.analyser.nodes.TypeNode;
import fr.amvproject.group3.mjj.ast.nodes.exprs.BinaryExpNode;
import fr.amvproject.group3.mjj.ast.nodes.exprs.LengthNode;
import fr.amvproject.group3.mjj.ast.nodes.exprs.UnaryExpNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.BoolLiteralNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.IntLiteralNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.StringLiteralNode;
import fr.amvproject.group3.mjj.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.mjj.ast.nodes.other.BinaryOpNode;
import fr.amvproject.group3.mjj.ast.nodes.other.UnaryOpNode;
import static fr.amvproject.group3.mjj.utils.NodeOperations.MockToken;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;

public class CompileTimeVisitorTest {

    /* -------------------------------------------------------------
       BinaryExp tests
    ------------------------------------------------------------- */

    @Test
    public void givenTwoIntegersWhenBinaryAddThenReturnsSum() {
        // Given
        SymbolTable table = new SymbolTable();
        CompileTimeVisitor visitor = new CompileTimeVisitor(new ScopeManager(table));

        BinaryOpNode op = new BinaryOpNode(BinaryOpNode.BinaryOpName.ADD.getName(), 1, 0);
        BinaryExpNode node = new BinaryExpNode(op,
                new IntLiteralNode(new MockToken("5", 1, 1)),
                new IntLiteralNode(new MockToken("3", 1, 1))
        );

        // When
        IntLiteralNode result = (IntLiteralNode) visitor.visitBinaryExp(node);

        // Then
        assertThat(result.getValue(), is(8));
    }

    @Test(expected = SemanticException.class)
    public void givenMismatchedTypesWhenBinaryExpThenThrowsException() {
        // Given
        SymbolTable table = new SymbolTable();
        CompileTimeVisitor visitor = new CompileTimeVisitor(new ScopeManager(table));

        BinaryOpNode op = new BinaryOpNode(BinaryOpNode.BinaryOpName.ADD.getName(), 1, 0);
        BinaryExpNode node = new BinaryExpNode(op,
                new BoolLiteralNode(new MockToken("true", 1, 1)),
                new IntLiteralNode(new MockToken("5", 1, 1))
        );

        // When
        visitor.visitBinaryExp(node);
    }

    @Test(expected = SemanticException.class)
    public void givenZeroDivisionWhenBinaryDivThenThrowsException() {
        // Given
        SymbolTable table = new SymbolTable();
        CompileTimeVisitor visitor = new CompileTimeVisitor(new ScopeManager(table));

        BinaryOpNode op = new BinaryOpNode(BinaryOpNode.BinaryOpName.DIV.getName(), 1, 0);
        BinaryExpNode node = new BinaryExpNode(op,
                new IntLiteralNode(new MockToken("10", 1, 1)),
                new IntLiteralNode(new MockToken("0", 1, 1))
        );

        // When
        visitor.visitBinaryExp(node);
    }

    /* -------------------------------------------------------------
       UnaryExp tests
    ------------------------------------------------------------- */

    @Test
    public void givenBooleanTrueWhenUnaryNotThenGotFalse() {
        // Given
        SymbolTable table = new SymbolTable();
        CompileTimeVisitor visitor = new CompileTimeVisitor(new ScopeManager(table));

        UnaryOpNode op = new UnaryOpNode(UnaryOpNode.UnaryOpName.NOT.getName(),1,0);
        UnaryExpNode node = new UnaryExpNode(op,
                new BoolLiteralNode(new MockToken("true", 1, 1))
        );

        // When
        BoolLiteralNode result = (BoolLiteralNode) visitor.visitUnaryExp(node);

        // Then
        assertThat(result.getValue(), is(false));

    }

    @Test
    public void givenIntWhenUnaryNegThenGotNegativeInteger() {
        // Given
        SymbolTable table = new SymbolTable();
        CompileTimeVisitor visitor = new CompileTimeVisitor(new ScopeManager(table));

        UnaryOpNode op = new UnaryOpNode(UnaryOpNode.UnaryOpName.NEG.getName(),1,0);
        UnaryExpNode node = new UnaryExpNode(op,
                new IntLiteralNode(new MockToken("10", 1, 1))
        );

        // When
        IntLiteralNode result = (IntLiteralNode) visitor.visitUnaryExp(node);

        // Then
        assertThat(result.getValue(), is(-10));

    }

    /* -------------------------------------------------------------
       Length tests
    ------------------------------------------------------------- */

    @Test
    public void givenTableSymbolWhenLengthThenReturnsSize() {
        // Given
        SymbolTable table = new SymbolTable();
        IdentNode ident = new IdentNode(new MockToken("arr", 1, 1));
        table.addSymbol(ident, new TypeNode("int"), new IntLiteralNode("10",1,1));

        CompileTimeVisitor visitor = new CompileTimeVisitor(new ScopeManager(table));

        LengthNode node = new LengthNode(new MockToken("lenght",1,0),ident);

        // When
        IntLiteralNode result = (IntLiteralNode) visitor.visitLength(node);

        // Then
        assertThat(result.getValue(), is(10));
    }

    @Test(expected = SemanticException.class)
    public void givenMethodSymbolWhenLengthThenThrows() {
        // Given
        SymbolTable table = new SymbolTable();
        IdentNode ident = new IdentNode(new MockToken("m", 1, 1));
        table.addSymbol(ident, new TypeNode("int"), List.of());

        CompileTimeVisitor visitor = new CompileTimeVisitor(new ScopeManager(table));

        LengthNode node = new LengthNode(new MockToken("lenght",1,0),ident);

        // When
        visitor.visitLength(node);
    }

    @Test(expected = SemanticException.class)
    public void givenIntVarWhenLengthThenThrows() {
        // Given
        SymbolTable table = new SymbolTable();
        IdentNode ident = new IdentNode(new MockToken("i", 1, 1));
        table.addSymbol(ident, new TypeNode("int"));

        CompileTimeVisitor visitor = new CompileTimeVisitor(new ScopeManager(table));

        LengthNode node = new LengthNode(new MockToken("lenght",1,0),ident);


        // When
        visitor.visitLength(node);
    }

    /* -------------------------------------------------------------
       Literals & Ident
    ------------------------------------------------------------- */

    @Test
    public void givenIntLiteralWhenVisitThenReturnsValue() {
        // Given
        CompileTimeVisitor visitor = new CompileTimeVisitor(new ScopeManager(new SymbolTable()));
        IntLiteralNode node = new IntLiteralNode(new MockToken("42", 1, 1));

        // When
        IntLiteralNode result = (IntLiteralNode) visitor.visitIntLiteral(node);

        // Then
        assertThat(result.getValue(), is(42));
    }

    @Test
    public void givenBoolLiteralWhenVisitThenReturnsValue() {
        // Given
        CompileTimeVisitor visitor = new CompileTimeVisitor(new ScopeManager(new SymbolTable()));
        BoolLiteralNode node = new BoolLiteralNode(new MockToken("true", 1, 1));

        // When
        BoolLiteralNode result = (BoolLiteralNode) visitor.visitBoolLiteral(node);

        // Then
        assertThat(result.getValue(), is(true));
    }

    @Test
    public void givenStringLiteralWhenVisitThenReturnsValue() {
        // Given
        CompileTimeVisitor visitor = new CompileTimeVisitor(new ScopeManager(new SymbolTable()));
        StringLiteralNode node = new StringLiteralNode(new MockToken("hello", 1, 1));

        // When
        StringLiteralNode result = (StringLiteralNode) visitor.visitStringLiteral(node);

        // Then
        assertThat(result.getValue(), is("hello"));
    }

    @Test
    public void givenIdentNodeWhenVisitThenReturnsNode() {
        // Given
        CompileTimeVisitor visitor = new CompileTimeVisitor(new ScopeManager(new SymbolTable()));
        IdentNode ident = new IdentNode(new MockToken("x", 1, 1));

        // When
        Object result = visitor.visitIdent(ident);

        // Then
        assertThat(result, is(ident));
    }
}
