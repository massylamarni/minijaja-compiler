package fr.amvproject.group3.interpreter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MemoryManager handles heap memory allocation and management for arrays.
 * Similar to the Heap class in analyser-miniJaja but adapted for JajaCode interpreter.
 */
public class MemoryManager {
    
    /**
     * Represents an allocated array block in the heap
     */
    public static class HeapBlock {
        private final int address;
        private final int size;
        private final List<Object> data;
        private final String elementType; // "int", "boolean", "void"

        public HeapBlock(int address, int size, String elementType) {
            this.address = address;
            this.size = size;
            this.elementType = elementType;
            this.data = new ArrayList<>();
            
            // Initialize array with default values
            for (int i = 0; i < size; i++) {
                switch (elementType) {
                    case "int" -> data.add(0);
                    case "boolean" -> data.add(false);
                    default -> data.add(null);
                }
            }
        }

        public Object get(int index) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Array index out of bounds: " + index);
            }
            return data.get(index);
        }

        public void set(int index, Object value) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Array index out of bounds: " + index);
            }
            data.set(index, value);
        }

        public int getSize() {
            return size;
        }

        @Override
        public String toString() {
            return "HeapBlock{" + "address=" + address + ", size=" + size + ", type=" + elementType + '}';
        }
    }

    private final Map<Integer, HeapBlock> blocks = new HashMap<>();
    private int nextAddress = 0;

    /**
     * Allocate memory in the heap and return its address
     * The address returned is the address after the size prefix
     */
    public int allocate(int size, String elementType) {
        int currentAddress = nextAddress;
        HeapBlock block = new HeapBlock(currentAddress, size, elementType);
        blocks.put(currentAddress, block);
        
        // Return address pointing to the first element (after size)
        nextAddress = currentAddress + size + 1;
        return currentAddress + 1;
    }

    /**
     * Get a value from the heap at the given address and index
     */
    public Object get(int heapAddress, int index) {
        // heapAddress points to first element, so actual block address is heapAddress - 1
        int blockAddress = heapAddress - 1;
        HeapBlock block = blocks.get(blockAddress);
        
        if (block == null) {
            throw new RuntimeException("No allocated block at address: " + blockAddress);
        }
        
        return block.get(index);
    }

    /**
     * Set a value in the heap at the given address and index
     */
    public void set(int heapAddress, int index, Object value) {
        // heapAddress points to first element, so actual block address is heapAddress - 1
        int blockAddress = heapAddress - 1;
        HeapBlock block = blocks.get(blockAddress);
        
        if (block == null) {
            throw new RuntimeException("No allocated block at address: " + blockAddress);
        }
        
        block.set(index, value);
    }

    /**
     * Get the size of an array
     */
    public int getSize(int heapAddress) {
        int blockAddress = heapAddress - 1;
        HeapBlock block = blocks.get(blockAddress);
        
        if (block == null) {
            throw new RuntimeException("No allocated block at address: " + blockAddress);
        }
        
        return block.getSize();
    }

    /**
     * Remove an allocated block from the heap
     */
    public void remove(int heapAddress) {
        int blockAddress = heapAddress - 1;
        if (blocks.containsKey(blockAddress)) {
            blocks.remove(blockAddress);
        }
    }

    /**
     * Check if an address is valid
     */
    public boolean isValid(int heapAddress) {
        int blockAddress = heapAddress - 1;
        return blocks.containsKey(blockAddress);
    }

    /**
     * Get all allocated blocks (for debugging)
     */
    public Map<Integer, HeapBlock> getAllBlocks() {
        return new HashMap<>(blocks);
    }

    @Override
    public String toString() {
        if (blocks.isEmpty()) {
            return "[No allocations]";
        }
        StringBuilder sb = new StringBuilder();
        for (HeapBlock block : blocks.values()) {
            sb.append(block).append("\n");
        }
        return sb.toString();
    }
}
