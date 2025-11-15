package fr.amvproject.group3;

import fr.amvproject.group3.nodes.TypeMemNode;

import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;

public class HeapTest {

    private Heap heap;

    @Before
    public void setUp() {
        heap = new Heap();
    }

    @Test
    public void givenHeap__whenAllocateIntArray__thenSizeAndValuesAreCorrect() {
        // When
        int address = heap.allocate(5, new TypeMemNode("int"));

        // Then
        assertThat(heap.getQueue().size(), is(6));
        assertThat(heap.get(address - 1), is(5));
        assertThat(heap.get(address), is(0));
        assertThat(heap.get(address + 4), is(0));
    }

    @Test
    public void givenHeap__whenAllocateBooleanArray__thenSizeAndValuesAreCorrect() {
        // When
        int address = heap.allocate(3, new TypeMemNode("boolean"));

        // Then
        assertThat(heap.getQueue().size(), is(4));
        assertThat(heap.get(address - 1), is(3));
        assertThat(heap.get(address), is(false));
        assertThat(heap.get(address + 2), is(false));
    }

    @Test
    public void givenHeap__whenAllocateVoidArray__thenSizeAndValuesAreCorrect() {
        // When
        int address = heap.allocate(2, new TypeMemNode("void"));

        // Then
        assertThat(heap.getQueue().size(), is(3));
        assertThat(heap.get(address - 1), is(2));
        assertThat(heap.get(address), is(nullValue()));
        assertThat(heap.get(address + 1), is(nullValue()));
    }

    @Test
    public void givenHeap__whenGet__thenValueIsCorrect() {
        // Given
        int address = heap.allocate(2, new TypeMemNode("int"));
        heap.set(address + 0, 42);

        // When
        Object value = heap.get(address);

        // Then
        assertThat(value, is(42));
    }

    // Test 5 : Modification d'une valeur avec `set`
    @Test
    public void givenHeap__whenSet__thenValueIsUpdated() {
        // Given
        int address = heap.allocate(2, new TypeMemNode("int"));
        // When
        heap.set(address + 0, 100);

        // Then
        assertThat(heap.get(address), is(100));
    }

    @Test
    public void givenHeap__whenRemove__thenMemoryIsFreed() {
        // Given
        int address = heap.allocate(3, new TypeMemNode("int"));

        // When
        heap.remove(address, new TypeMemNode("int"));

        // Then
        assertThat(heap.get(address - 1), is(0));
        assertThat(heap.get(address), is(nullValue()));
        assertThat(heap.get(address + 1), is(nullValue()));
        assertThat(heap.get(address + 2), is(nullValue()));
    }

    // Test 7 : Mise à jour de `nextAddress`
    @Test
    public void givenHeap__whenAllocate__thenNextAddressIsUpdated() {
        // When
        int address1 = heap.allocate(2, new TypeMemNode("int"));
        int address2 = heap.allocate(3, new TypeMemNode("boolean"));

        // Then
        assertThat(heap.getNextAddress(), is(7)); // 1 (taille) + 2 (int) + 1 (taille) + 3 (boolean)
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void givenHeap__whenGetInvalidAddress__thenThrowsException() {
        // When
        heap.get(-1);
    }

    // Test 9 : Exception pour `set` avec une adresse invalide
    @Test(expected = IndexOutOfBoundsException.class)
    public void givenHeap__whenSetInvalidAddress__thenThrowsException() {
        // When
        heap.set(-1 + 0, 42);
    }

    // Test 10 : Exception pour `remove` avec une adresse invalide
    @Test(expected = IndexOutOfBoundsException.class)
    public void givenHeap__whenRemoveInvalidAddress__thenThrowsException() {
        // When
        heap.remove(-1, new TypeMemNode("int"));
    }

    @Test
    public void givenHeap__whenAllocate__thenQueueIsNotEmpty() {
        // When
        heap.allocate(1, new TypeMemNode("int"));

        // Then
        assertThat(heap.getQueue(), is(not(empty())));
    }
}

