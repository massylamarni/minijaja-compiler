package fr.amvproject.group3.mjj;

import fr.amvproject.group3.Heap;
import fr.amvproject.group3.exceptions.BreakPointHit;
import fr.amvproject.group3.exceptions.PausePointHit;
import fr.amvproject.group3.mjj.ast.AstNode;
import fr.amvproject.group3.mjj.ast.AstVisitor;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class ExecutionContextTest {

    private ExecutionContext ctx;
    private Heap heap;
    private Stack stack;

    @Before
    public void setUp() {
        // Given
        heap = new Heap();
        stack = new Stack(heap);
        ctx = new ExecutionContext(stack);
    }

    private AstNode nodeAtLine(int line) {
        return new AstNode() {
            @Override
            public <T> T accept(AstVisitor<T> visitor) {
                return null;
            }

            @Override
            public boolean sameAs(AstNode other) {
                return false;
            }

            @Override
            public int getLine() {
                return line;
            }
        };
    }


    @Test
    public void Given_Context_When_GetStackIsCalled_Then_StackIsNotNull() {
        // When
        Stack result = ctx.getStack();

        // Then
        assertThat(result, is(notNullValue()));
    }


    @Test
    public void Given_Context_When_AddBreakpoint_Then_BreakpointIsPresent() {
        // Given
        int line = 5;

        // When
        ctx.addBreakPoint(line);

        // Then
        assertThat(ctx.getBreakpoints().contains(line), is(true));
    }

    @Test
    public void Given_ContextWithBreakpoint_When_RemoveBreakpoint_Then_BreakpointIsRemoved() {
        // Given
        int line = 3;
        ctx.addBreakPoint(line);

        // When
        ctx.removeBreakpoint(line);

        // Then
        assertThat(ctx.getBreakpoints().contains(line), is(false));
    }


    @Test
    public void Given_DebugDisabled_When_RecordNodeOnBreakpoint_Then_NoExceptionIsThrown() {
        // Given
        ctx.disableDebug();
        ctx.addBreakPoint(10);
        AstNode node = nodeAtLine(10);

        // When / Then
        try {
            ctx.recordNode(node);
        } catch (Exception e) {
            assertThat("No exception should be thrown", false);
        }
    }

    @Test(expected = BreakPointHit.class)
    public void Given_DebugEnabledAndBreakpoint_When_RecordNode_Then_BreakPointHitIsThrown() {
        // Given
        ctx.enableDebug();
        ctx.addBreakPoint(7);
        AstNode node = nodeAtLine(7);

        // When
        ctx.recordNode(node);

        // Then -> exception
    }


    @Test(expected = PausePointHit.class)
    public void Given_ContextPaused_When_RecordNode_Then_PausePointHitIsThrown() {
        // Given
        ctx.enableDebug();
        ctx.setIsPaused(true);
        AstNode node = nodeAtLine(4);

        // When
        ctx.recordNode(node);

        // Then -> exception
    }


    @Test
    public void Given_IgnoreNextBreakpoint_When_RecordNodeOnBreakpoint_Then_NoExceptionIsThrown() {
        // Given
        ctx.enableDebug();
        ctx.addBreakPoint(6);
        ctx.ignoreNextBreakpoint();
        AstNode node = nodeAtLine(6);

        // When / Then
        try {
            ctx.recordNode(node);
        } catch (Exception e) {
            assertThat("No exception should be thrown", false);
        }
    }


    @Test
    public void Given_ContextWithState_When_SoftReset_Then_ContextIsCleared() {
        // Given
        ctx.setCurrentNode(nodeAtLine(1));
        ctx.setPreviousNode(nodeAtLine(0));
        ctx.setIsPaused(true);
        ctx.addBreakPoint(2);

        // When
        ctx.softReset();

        // Then
        assertThat(ctx.getCurrentNode(), is(nullValue()));
        assertThat(ctx.getPreviousNode(), is(nullValue()));
        assertThat(ctx.isPaused(), is(false));
    }


    @Test
    public void Given_ContextWithPreviousNode_When_BackIsCalled_Then_CurrentNodeIsPreviousNode() {
        // Given
        AstNode first = nodeAtLine(1);
        AstNode second = nodeAtLine(2);
        ctx.setCurrentNode(second);
        ctx.setPreviousNode(first);

        // When
        ctx.back();

        // Then
        assertThat(ctx.getCurrentNode(), is(first));
    }
}
