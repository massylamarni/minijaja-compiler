package fr.amvproject.group3.mjj.analyser;

import fr.amvproject.group3.mjj.analyser.nodes.TypeNode;
import fr.amvproject.group3.mjj.analyser.nodes.ObjNode;
import fr.amvproject.group3.mjj.analyser.nodes.SymbolSignature;
import fr.amvproject.group3.mjj.analyser.symbols.Symbols;
import fr.amvproject.group3.mjj.ast.nodes.finals.IdentNode;

import static fr.amvproject.group3.mjj.utils.NodeOperations.MockToken;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ScopeManagerTest {

    @Test
    public void givenInitialSymbolTableWhenConstructedThenScopeDepthIsZero() {
        // Given
        SymbolTable table = new SymbolTable();

        // When
        ScopeManager scopeManager = new ScopeManager(table);

        // Then
        assertThat(scopeManager.getScopeDepth(), is(0));
        assertThat(scopeManager.getSymbolTableRef(), is(table));
    }

    @Test
    public void givenScopeManagerWhenNewScopeThenDepthIncreasesAndSymbolTableChanges() {
        // Given
        SymbolTable root = new SymbolTable();
        ScopeManager scopeManager = new ScopeManager(root);

        // When
        scopeManager.newScope();
        SymbolTable newTable = scopeManager.getSymbolTableRef();

        // Then
        assertThat(scopeManager.getScopeDepth(), is(1));
        assertThat(newTable, is(not(root)));
    }

    @Test
    public void givenTwoScopesWhenExitScopeThenDepthDecreasesAndSymbolTableReturnsToParent() {
        // Given
        SymbolTable root = new SymbolTable();
        ScopeManager scopeManager = new ScopeManager(root);
        scopeManager.newScope(); // depth = 1

        SymbolTable child = scopeManager.getSymbolTableRef();

        // When
        scopeManager.exitScope();

        // Then
        assertThat(scopeManager.getScopeDepth(), is(0));
        assertThat(scopeManager.getSymbolTableRef(), is(root));
        assertThat(scopeManager.getSymbolTableRef(), is(not(child)));
    }

    @Test
    public void givenSymbolInCurrentScopeWhenLookupThenReturnsSymbol() {
        // Given
        SymbolTable root = new SymbolTable();
        ScopeManager scopeManager = new ScopeManager(root);
        IdentNode ident = new IdentNode(new MockToken("x", 1,0));
        TypeNode type = new TypeNode("int");
        root.addSymbol(ident, type, new ObjNode("var", 0, 0));

        // When
        Symbols result = scopeManager.lookup(new SymbolSignature(ident));

        // Then
        assertThat(result, is(notNullValue()));
        assertThat(result.getIdentNode().getName(), is("x"));
    }

    @Test
    public void givenSymbolInParentScopeWhenLookupFromChildScopeThenReturnsParentSymbol() {
        // Given
        SymbolTable root = new SymbolTable();
        ScopeManager scopeManager = new ScopeManager(root);

        IdentNode ident = new IdentNode(new MockToken("y", 2,0));
        root.addSymbol(ident, new TypeNode("int"), new ObjNode("var", 0, 0));

        scopeManager.newScope(); // go to child scope

        // When
        Symbols result = scopeManager.lookup(new SymbolSignature(ident));

        // Then
        assertThat(result, is(notNullValue()));
        assertThat(result.getIdentNode().getName(), is("y"));
    }

    @Test
    public void givenNoSymbolFoundWhenLookupThenReturnsNull() {
        // Given
        SymbolTable root = new SymbolTable();
        ScopeManager scopeManager = new ScopeManager(root);
        IdentNode ident = new IdentNode(new MockToken("unknown", 99,0));

        // When
        Symbols result = scopeManager.lookup(new SymbolSignature(ident));

        // Then
        assertThat(result, is(nullValue()));
    }
}
