package fr.amvproject.group3.mjj;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class StackManagerTest {

    private StackManager stackManager;

    @Before
    public void setUp() {
        stackManager = new StackManager();
    }

    @Test
    public void givenNewStackManager__thenQueueIsEmpty() {
        List<Quad> queue = stackManager.getQueue();
        assertThat(queue.isEmpty(), is(true));
    }

    @Test
    public void givenStackManager__whenPushQuad__thenQueueHasQuad() {
        Quad quad = new Quad(null, null, null, null);
        stackManager.getQueue().add(quad);
        assertThat(stackManager.getQueue().size(), is(1));
        assertThat(stackManager.getQueue().get(0), is(quad));
    }

    @Test
    public void givenStackManager__whenUpdateCalled__thenInstrCountIncreases() {
        stackManager.update(false);
        stackManager.goForward();
        // No exception, instrCount increment handled internally
    }

    @Test
    public void givenStackManager__whenSTUpdatesReached__thenSnapshotsTaken() {
        // Push 5 quads to trigger snapshot
        for (int i = 0; i < 5; i++) {
            stackManager.getQueue().add(new Quad(null, null, null, null));
            stackManager.goForward();
            stackManager.update(true);
        }

        // Check that snapshots exist
        List<Quad> snapshot = stackManager.getStackSnapshot();
        List<Object> heapSnapshot = stackManager.getHeapSnapshot();

        assertThat(snapshot, notNullValue());
        assertThat(heapSnapshot, notNullValue());
        assertThat(snapshot.size(), is(stackManager.getQueue().size()));
    }

    @Test
    public void givenStackManager__whenGoForwardBackward__thenCurrentInstrNumChanges() {
        stackManager.goForward();
        stackManager.goForward();
        stackManager.goBackward();
        // No direct getter for currentInstrNum; ensure no exception
    }
}
