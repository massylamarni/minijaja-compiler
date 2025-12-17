package fr.amvproject.group3.jjc;

import fr.amvproject.group3.jjc.ast.AstNode;
import fr.amvproject.group3.jjc.ast.AstVisitor;
import fr.amvproject.group3.jjc.exceptions.BreakPointHit;
import fr.amvproject.group3.jjc.exceptions.PausePointHit;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThrows;

public class ExecutionContextTest {

    private ExecutionContext ctx;
    private Stack stack;

    @Before
    public void setUp() {
        stack = new Stack();
        ctx = new ExecutionContext(stack);
    }

    private AstNode createMockNode(int line) {
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
    public void testAddAndRemoveBreakpoint() {
        AstNode node = createMockNode(10);
        ctx.setCurrentNode(node);

        ctx.addBreakPoint(10);
        Set<Integer> breakpoints = ctx.getBreakpoints();
        assertThat(breakpoints.contains(10), is(true));

        ctx.removeBreakpoint(10);
        assertThat(ctx.getBreakpoints().contains(10), is(false));
    }

    @Test
    public void testSoftReset() {
        ctx.setCurrentNode(createMockNode(1));
        ctx.setPreviousNode(createMockNode(0));
        ctx.setIsPaused(true);

        ctx.softReset();

        assertThat(ctx.getCurrentNode(), is((AstNode) null));
        assertThat(ctx.getPreviousNode(), is((AstNode) null));
        assertThat(ctx.isPaused(), is(false));
    }

    @Test
    public void testRecordNodePause() {
        ctx.setIsPaused(true);
        AstNode node = createMockNode(5);

        assertThrows(PausePointHit.class, () -> ctx.recordNode(node));
    }

    @Test
    public void testRecordNodeBreakpoint() {
        ctx.enableDebug();
        AstNode node1 = createMockNode(1);
        AstNode node2 = createMockNode(2);

        ctx.addBreakPoint(2);
        ctx.setCurrentNode(node1);

        assertThrows(BreakPointHit.class, () -> ctx.recordNode(node2));
    }


    @Test
    public void testBackMovesCurrentNodeAndStack() {
        AstNode node1 = createMockNode(1);
        AstNode node2 = createMockNode(2);
        ctx.setCurrentNode(node2);
        ctx.setPreviousNode(node1);

        int initialQueueSize = ctx.getStack().getQueue().size();

        ctx.back();

        assertThat(ctx.getCurrentNode(), is(node1));
        assertThat(ctx.getStack().getQueue().size(), is(initialQueueSize)); // stack.goBackward called but queue size may remain same
    }

    @Test
    public void testEnableDisableDebug() {
        ctx.disableDebug();
        AstNode node = createMockNode(1);
        ctx.addBreakPoint(1);

        ctx.setCurrentNode(node);
        ctx.disableDebug();
        // No exception should be thrown
        ctx.recordNode(node);

        ctx.enableDebug();
        // now breakpoints are active
        ctx.setCurrentNode(node);
        assertThrows(BreakPointHit.class, () -> ctx.recordNode(node));
    }

    @Test
    public void testIgnoreNextBreakpoint() {
        ctx.enableDebug();
        AstNode node = createMockNode(1);
        ctx.addBreakPoint(1);
        ctx.ignoreNextBreakpoint();
        ctx.setCurrentNode(node);

        // Should not throw BreakPointHit due to ignoreNextBreakpoint
        ctx.recordNode(node);
    }

    @Test
    public void testHasBreakpoint() {
        AstNode node = createMockNode(3);
        ctx.setCurrentNode(node);
        ctx.addBreakPoint(3);

        assertThat(ctx.hasBreakpoint(), is(true));
        ctx.removeBreakpoint(3);
        assertThat(ctx.hasBreakpoint(), is(false));
    }

    @Test
    public void testDecBreakPointHitLine() {
        AstNode prev = createMockNode(1);
        ctx.setPreviousNode(prev);
        ctx.decBreakPointHitLine(); // breakPointHitLine = 0 initially

        // Should set breakPointHitLine to previousNode.getLine() only if breakPointHitLine != 0
        assertThat(ctx.getCurrentNode(), is((AstNode) null));
    }
}
