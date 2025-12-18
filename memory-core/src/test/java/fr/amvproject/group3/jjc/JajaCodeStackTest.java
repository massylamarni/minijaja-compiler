package fr.amvproject.group3.jjc;

import fr.amvproject.group3.jjc.Quad;
import fr.amvproject.group3.jjc.Stack;
import fr.amvproject.group3.jjc.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.jjc.ast.nodes.other.TypeNode;
import fr.amvproject.group3.nodes.ObjNode;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static fr.amvproject.group3.jjc.utils.NodeOperations.MockToken;

public class JajaCodeStackTest {

    @Test
    public void givenStack__whenSorteOfLast__thenSameSizeQueueSameOrder() {
        // Given
        MockToken identToken1 = new MockToken("Test", 1, 0);
        MockToken identToken2 = new MockToken("x", 2, 0);
        MockToken identToken3 = new MockToken("y", 3, 0);
        MockToken identToken4 = new MockToken("Test", 2, 0);

        MockToken typeToken1 = new MockToken("vide", 0, 0);
        MockToken typeToken2 = new MockToken("entier", 0, 0);

        Stack stack = new Stack();
        stack.push(new Quad(new IdentNode(identToken1), null, new ObjNode("var"), new TypeNode(typeToken1)));
        stack.push(new Quad(new IdentNode(identToken2), 2, new ObjNode("var"), new TypeNode(typeToken2)));
        stack.push(new Quad(new IdentNode(identToken3), 3, new ObjNode("var"), new TypeNode(typeToken2)));

        // When
        TypeNode res = (TypeNode) stack.sorte(new IdentNode(identToken4));

        // Then
        assertTrue(res.isVoid());
        assertThat(stack.getQueue().size(), is(3));
        assertThat(stack.pop().getIdent().getName(), is("y"));
        assertThat(stack.pop().getIdent().getName(), is("x"));
        assertThat(stack.pop().getIdent().getName(), is("Test"));
    }

    @Test
    public void givenStack__whenValOfX__thenSameSizeQueueSameOrder() {
        // Given
        Stack stack = new Stack();
        stack.push(new Quad(new IdentNode(new MockToken("Test",1,0)), null, new ObjNode("var"), new TypeNode(new MockToken("vide",1,0))));
        stack.push(new Quad(new IdentNode(new MockToken("x",2,0)), 2, new ObjNode("var"), new TypeNode(new MockToken("entier",1,0))));
        stack.push(new Quad(new IdentNode(new MockToken("y",3,0)), 3, new ObjNode("var"), new TypeNode(new MockToken("entier",1,0))));

        // When
        Object res = stack.val(new IdentNode(new MockToken("x", 2,0)));

        // Then
        assertTrue(res instanceof Integer);
        assertThat(stack.getQueue().size(), is(3));
        assertThat(stack.pop().getIdent().getName(), is("y"));
        assertThat(stack.pop().getIdent().getName(), is("x"));
        assertThat(stack.pop().getIdent().getName(), is("Test"));
    }

    @Test
    public void givenStack__whenMultipleSort__thenSameSizeQueueSameOrder() {
        // Given
        Stack stack = new Stack();
        stack.push(new Quad(new IdentNode(new MockToken("Test",1,0)), null, new ObjNode("var"), new TypeNode(new MockToken("vide",1,0))));
        stack.push(new Quad(new IdentNode(new MockToken("x",2,0)), 2, new ObjNode("var"), new TypeNode(new MockToken("entier",1,0))));
        stack.push(new Quad(new IdentNode(new MockToken("y",3,0)), 3, new ObjNode("var"), new TypeNode(new MockToken("entier",1,0))));

        // When
        TypeNode resX = (TypeNode) stack.sorte(new IdentNode(new MockToken("x",2,0)));
        TypeNode resY = (TypeNode) stack.sorte(new IdentNode(new MockToken("y",3,0)));

        // Then
        assertTrue(resX.isInt());
        assertTrue(resY.isInt());
        assertThat(stack.getQueue().size(), is(3));
        assertThat(stack.pop().getIdent().getName(), is("y"));
        assertThat(stack.pop().getIdent().getName(), is("x"));
        assertThat(stack.pop().getIdent().getName(), is("Test"));
    }

    @Test
    public void givenStack__whenDeclVar__thenVarIsDeclOnTop() {
        // Given
        Stack stack = new Stack();
        stack.push(new Quad(new IdentNode(new MockToken("Test",1,0)), null, new ObjNode("var"), new TypeNode(new MockToken("vide",1,0))));
        IdentNode ident = new IdentNode(new MockToken("z",4,0));

        // When
        stack.declVar(ident, 4, new TypeNode(new MockToken("entier",1,0)));

        // Then
        assertThat(stack.getQueue().size(), is(2));
        assertThat(stack.peek().getIdent().getName(), is("z"));
        assertTrue(stack.peek().getObj().isVar());
        assertTrue(((TypeNode) stack.peek().getType()).isInt());
    }

    @Test
    public void givenStack__whenDeclCst__thenVarIsDeclOnTop() {
        // Given
        Stack stack = new Stack();
        stack.push(new Quad(new IdentNode(new MockToken("Test",1,0)), null, new ObjNode("var"), new TypeNode(new MockToken("vide",1,0))));
        IdentNode ident = new IdentNode(new MockToken("T",5,0));

        // When
        stack.declCst(ident, 4, new TypeNode(new MockToken("entier",1,0)),false);

        // Then
        assertThat(stack.getQueue().size(), is(2));
        assertThat(stack.peek().getIdent().getName(), is("T"));
        assertTrue(stack.peek().getObj().isCst());
    }

    @Test
    public void givenStack__whenDeclTab__thenTabIsDeclOnTop() {
        // Given
        Stack stack = new Stack();
        stack.push(new Quad(new IdentNode(new MockToken("Test",1,0)), null, new ObjNode("var"), new TypeNode(new MockToken("vide",1,0))));
        IdentNode ident = new IdentNode(new MockToken("arr",18,0));

        // When
        stack.declTab(ident, 5, new TypeNode(new MockToken("entier",1,0)));

        // Then
        assertThat(stack.getQueue().size(), is(2));
        assertThat(stack.peek().getIdent().getName(), is("arr"));
        assertTrue(stack.peek().getObj().isTab());
        assertTrue(stack.peek().getType().isInt());
    }

    @Test
    public void givenStack__whenAffecterVal__thenValueIsUpdated() {
        // Given
        Stack stack = new Stack();
        IdentNode ident = new IdentNode(new MockToken("x",2,0));
        stack.push(new Quad(ident, 2, new ObjNode("var"), new TypeNode(new MockToken("entier",1,0))));
        stack.push(new Quad(ident, 3, new ObjNode("var"), new TypeNode(new MockToken("entier",1,0))));

        // When
        stack.affecterVal(ident, 5);

        // Then
        assertThat(stack.pop().getValue(), is(5));
    }

    @Test
    public void givenStack__whenValT__thenArrayValueIsReturned() {
        // Given
        Stack stack = new Stack();
        IdentNode ident = new IdentNode(new MockToken("arr",6,0));
        stack.declTab(ident, 5, new TypeNode(new MockToken("entier",1,0)));
        stack.affecterValT(ident, 2, 10);

        // When
        Object val = stack.valT(ident,2);

        // Then
        assertThat(val, is(10));
    }

    @Test
    public void givenStack__whenAffecterValT__thenArrayValueIsUpdated() {
        // Given
        Stack stack = new Stack();
        IdentNode ident = new IdentNode(new MockToken("arr",6,0));
        stack.declTab(ident, 5, new TypeNode(new MockToken("entier",1,0)));

        // When
        stack.affecterValT(ident, 2, 10);

        // Then
        assertThat(stack.valT(ident,2), is(10));
    }

    @Test
    public void givenStack__whenLongT__thenArrayLengthIsReturned() {
        // Given
        Stack stack = new Stack();
        IdentNode ident = new IdentNode(new MockToken("arr",6,0));
        stack.declTab(ident, 5, new TypeNode(new MockToken("entier",1,0)));

        // When
        Integer length = stack.longT(ident);

        // Then
        assertEquals(5, length.intValue());
    }

    @Test
    public void givenStack__whenRetirerDecl__thenDeclarationIsRemoved() {
        // Given
        Stack stack = new Stack();
        IdentNode ident = new IdentNode(new MockToken("x",2,0));
        stack.declVar(ident, 2, new TypeNode(new MockToken("entier",1,0)));

        // When
        stack.retirerDecl(ident);

        // Then
        assertThat(stack.getQueue().size(), is(0));
    }

    @Test
    public void givenStack__whenSwap__thenTopTwoElementsAreSwapped() {
        // Given
        Stack stack = new Stack();
        stack.push(new Quad(new IdentNode(new MockToken("x",2,0)), 2, new ObjNode("var"), new TypeNode(new MockToken("entier",1,0))));
        stack.push(new Quad(new IdentNode(new MockToken("y",3,0)), 3, new ObjNode("var"), new TypeNode(new MockToken("entier",1,0))));

        // When
        stack.swap();

        // Then
        assertThat(stack.pop().getIdent().getName(), is("x"));
        assertThat(stack.pop().getIdent().getName(), is("y"));
    }

    @Test
    public void givenStack__whenAffecterType__thenTypeIsUpdated() {
        // Given
        Stack stack = new Stack();
        stack.declVar(new IdentNode(new MockToken("x",2,0)), 2, new TypeNode(new MockToken("entier",1,0)));

        // When
        stack.affecterType(new IdentNode(new MockToken("x",2,0)), new TypeNode(new MockToken("boolean",1,0)));

        // Then
        assertTrue(((TypeNode) stack.peek().getType()).isBoolean());
    }

    @Test
    public void givenStack__whenObjet__thenObjTypeIsReturned() {
        // Given
        Stack stack = new Stack();
        stack.declVar(new IdentNode(new MockToken("x",2,0)), 2, new TypeNode(new MockToken("entier",1,0)));

        // When
        ObjNode obj = stack.objet(new IdentNode(new MockToken("x",2,0)));

        // Then
        assertTrue(obj.isVar());
    }

    @Test
    public void givenStack__whenPushWithNameAndValue__thenQuadIsAdded() {
        Stack stack = new Stack();

        stack.push("x", 42);

        assertThat(stack.getQueue().size(), is(1));
        assertThat(stack.peek().getIdent().getName(), is("x"));
        assertThat(stack.peek().getValue(), is(42));
        assertTrue(stack.peek().getObj().isCst());
    }

    @Test(expected = IllegalStateException.class)
    public void givenEmptyStack__whenPop__thenExceptionIsThrown() {
        Stack stack = new Stack();
        stack.pop();
    }
    @Test
    public void givenStack__whenPeekAndPeekTail__thenCorrectElementsReturned() {
        Stack stack = new Stack();
        stack.push("a", 1);
        stack.push("b", 2);
        stack.push("c", 3);

        assertThat(stack.peek().getIdent().getName(), is("c"));
        assertThat(stack.getClassQuad().getIdent().getName(), is("a"));
        assertThat(stack.getQueue().size(), is(3));
    }

    @Test(expected = IllegalStateException.class)
    public void givenStackWithOneElement__whenSwap__thenExceptionIsThrown() {
        Stack stack = new Stack();
        stack.push("x", 1);

        stack.swap();
    }

    @Test
    public void givenStack__whenIdentVal__thenQuadIsReplacedAtDepth() {
        Stack stack = new Stack();
        IdentNode a = new IdentNode(new MockToken("a",1,0));
        IdentNode b = new IdentNode(new MockToken("b",2,0));

        stack.declVar(a, 1, new TypeNode(new MockToken("entier",1,0)));
        stack.declVar(b, 2, new TypeNode(new MockToken("entier",1,0)));

        stack.identVal(new IdentNode(new MockToken("x",3,0)),
                new TypeNode(new MockToken("boolean",1,0)),
                1);

        assertThat(stack.getQueue().get(0).getIdent().getName(), is("x"));
        assertTrue(((TypeNode) stack.getQueue().get(0).getType()).isBoolean());
    }

    @Test
    public void givenCst__whenAffecterVal__thenValueIsNotChanged() {
        Stack stack = new Stack();
        IdentNode ident = new IdentNode(new MockToken("c",1,0));

        stack.declCst(ident, 10, new TypeNode(new MockToken("entier",1,0)), false);
        stack.affecterVal(ident, 99);

        assertThat(stack.val(ident), is(10));
    }

    @Test
    public void givenVcst__whenAffecterVal__thenBecomesCst() {
        Stack stack = new Stack();
        IdentNode ident = new IdentNode(new MockToken("v",1,0));

        stack.declCst(ident, null, new TypeNode(new MockToken("entier",1,0)), true);
        stack.affecterVal(ident, 5);

        assertThat(stack.val(ident), is(5));
        assertTrue(stack.objet(ident).isCst());
    }

    @Test
    public void givenStack__whenDeclMeth__thenMethodIsOnTop() {
        Stack stack = new Stack();
        IdentNode ident = new IdentNode(new MockToken("m",1,0));

        stack.declMeth(ident, "body", new TypeNode(new MockToken("vide",1,0)));

        assertThat(stack.peek().getIdent().getName(), is("m"));
        assertTrue(stack.peek().getObj().isMeth());
    }

    @Test
    public void givenStack__whenValOfUnknownIdent__thenNullReturned() {
        Stack stack = new Stack();
        Object val = stack.val(new IdentNode(new MockToken("unknown",1,0)));

        assertThat(val, is(nullValue()));
    }

    @Test
    public void givenEmptyStack__whenPrint__thenEmptyStackPrinted() {
        Stack stack = new Stack();

        String output = stack.print();

        assertThat(output, containsString("[Empty Stack]"));
        assertThat(output, containsString("STACK DUMP"));
    }








}
