package fr.amvproject.group3.mjj.analyser;

import fr.amvproject.group3.mjj.analyser.SymbolTable;
import fr.amvproject.group3.mjj.analyser.nodes.TypeNode;
import fr.amvproject.group3.mjj.analyser.symbols.*;
import fr.amvproject.group3.mjj.ast.nodes.finals.IdentNode;
import static fr.amvproject.group3.mjj.utils.NodeOperations.MockToken;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class SymbolTableTest {

    @Test
    public void givenClassIdentWhenAddClassSymbolThenLookupReturnsClassSymbol() {
        // Given
        SymbolTable table = new SymbolTable();
        IdentNode ident = new IdentNode(new MockToken("MyClass",1,0));

        // When
        table.addSymbol(ident);
        Symbols result = table.lookup(ident);

        // Then
        assertThat(result, is(notNullValue()));
        assertThat(result, instanceOf(ClassSymbol.class));
        assertThat(result.getIdentNode().getName(), is("MyClass"));
    }

    @Test
    public void givenVarIdentAndTypeWhenAddVarSymbolThenLookupReturnsVarSymbol() {
        // Given
        SymbolTable table = new SymbolTable();
        IdentNode ident = new IdentNode(new MockToken("x", 5,0));
        TypeNode type = new TypeNode("int");

        // When
        table.addSymbol(ident, type);
        Symbols result = table.lookup(ident);

        // Then
        assertThat(result, is(notNullValue()));
        assertThat(result, instanceOf(VarSymbol.class));
        assertThat(result.getIdentNode().getName(), is("x"));
    }

    @Test
    public void givenConstIdentAndTypeWhenAddConstSymbolThenLookupReturnsCstSymbol() {
        // Given
        SymbolTable table = new SymbolTable();
        IdentNode ident = new IdentNode(new MockToken("MAX", 3,0));
        TypeNode type = new TypeNode("int");

        // When
        table.addSymbol(ident, type, true);
        Symbols result = table.lookup(ident);

        // Then
        assertThat(result, is(notNullValue()));
        assertThat(result, instanceOf(CstSymbol.class));
        CstSymbol cst = (CstSymbol) result;
        assertThat(cst.isVcst(), is(true));
    }

    @Test
    public void givenTableIdentTypeAndSizeWhenAddTableSymbolThenLookupReturnsTableSymbol() {
        // Given
        SymbolTable table = new SymbolTable();
        IdentNode ident = new IdentNode(new MockToken("arr", 7,0));
        TypeNode type = new TypeNode("int");

        // When
        table.addSymbol(ident, type, 10);
        Symbols result = table.lookup(ident);

        // Then
        assertThat(result, is(notNullValue()));
        assertThat(result, instanceOf(TableSymbol.class));
        assertThat(((TableSymbol) result).getSize(), is(10));
    }

    @Test
    public void givenMethodIdentTypeAndParamsWhenAddMethodSymbolThenLookupReturnsMethodSymbol() {
        // Given
        SymbolTable table = new SymbolTable();
        IdentNode ident = new IdentNode(new MockToken("compute", 12,0));
        TypeNode returnType = new TypeNode("int");
        List<TypeNode> params = List.of(new TypeNode("int"), new TypeNode("int"));

        // When
        table.addSymbol(ident, returnType, params);
        Symbols result = table.lookup(ident);

        // Then
        assertThat(result, is(notNullValue()));
        assertThat(result, instanceOf(MethodSymbol.class));
        MethodSymbol method = (MethodSymbol) result;
        assertThat(method.getParamsType().size(), is(2));
    }

    @Test
    public void givenExistingSymbolWhenRecordUsageThenUsageIsRecorded() {
        // Given
        SymbolTable table = new SymbolTable();
        IdentNode ident = new IdentNode(new MockToken("x", 20,0));
        TypeNode type = new TypeNode("int");
        table.addSymbol(ident, type);

        // When
        Symbols sym = table.recordUsage(ident, 2);

        // Then
        assertThat(sym, is(notNullValue()));
        assertThat(sym.getLinesOfUsage().get(0), is(20));
        assertThat(sym.getScopesOfUsage(), contains(2));
    }
}
