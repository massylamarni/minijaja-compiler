package fr.amvproject.group3.memory;

import fr.amvproject.group3.ast.nodes.finals.IdentNode;
import fr.amvproject.group3.ast.nodes.other.TypeMethNode;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class StackTest {



    @Test
    public void givenStack__whenSorteOfLast__thenSameSizeQueueSameOrder() {
        // Given
        Stack stack = new Stack();
        stack.getQueue().push(new Stack.Quad(new IdentNode("Test",1), null, "var", new TypeMethNode("void")));
        stack.getQueue().push(new Stack.Quad(new IdentNode("x",2), 2, "var", new TypeMethNode("int")));
        stack.getQueue().push(new Stack.Quad(new IdentNode("y",3), 3, "var", new TypeMethNode("int")));

        // When
        TypeMethNode res = stack.sorte(new IdentNode("Test", 2), stack.pop());
        assertThat(res.getType(), is(new TypeMethNode("void").getType()));
        assertThat(stack.getQueue().size(), is(3));
        assertThat(stack.getQueue().pop().ident.getName(), is("y"));
        assertThat(stack.getQueue().pop().ident.getName(), is("x"));
        assertThat(stack.getQueue().pop().ident.getName(), is("Test"));
        // Then
    }

    @Test
    public void givenStack__whenValOfX__thenSameSizeQueueSameOrder() {
        // Given
        Stack stack = new Stack();
        stack.getQueue().push(new Stack.Quad(new IdentNode("Test",1), null, "var", new TypeMethNode("void")));
        stack.getQueue().push(new Stack.Quad(new IdentNode("x",2), 2, "var", new TypeMethNode("int")));
        stack.getQueue().push(new Stack.Quad(new IdentNode("y",3), 3, "var", new TypeMethNode("int")));

        // When
        Object res = stack.val(new IdentNode("x", 2), stack.pop());
        assertThat(res instanceof Integer, is(true));
        assertThat(stack.getQueue().size(), is(3));
        assertThat(stack.getQueue().pop().ident.getName(), is("y"));
        assertThat(stack.getQueue().pop().ident.getName(), is("x"));
        assertThat(stack.getQueue().pop().ident.getName(), is("Test"));
        // Then
    }

    @Test
    public void givenStack__whenMultipleSort__thenSameSizeQueueSameOrder() {
        // Given
        Stack stack = new Stack();
        stack.getQueue().push(new Stack.Quad(new IdentNode("Test",1), null, "var", new TypeMethNode("void")));
        stack.getQueue().push(new Stack.Quad(new IdentNode("x",2), 2, "var", new TypeMethNode("int")));
        stack.getQueue().push(new Stack.Quad(new IdentNode("y",3), 3, "var", new TypeMethNode("int")));

        // When
        TypeMethNode resX = stack.sorte(new IdentNode("x", 2), stack.pop());
        TypeMethNode resY = stack.sorte(new IdentNode("y", 3), stack.pop());
        assertThat(resX.getType(), is(new TypeMethNode("int").getType()));
        assertThat(stack.getQueue().size(), is(3));
        assertThat(stack.getQueue().pop().ident.getName(), is("y"));
        assertThat(stack.getQueue().pop().ident.getName(), is("x"));
        assertThat(stack.getQueue().pop().ident.getName(), is("Test"));
        // Then
    }



}
