package fr.amvproject.group3;

import java.util.ArrayList;
import java.util.List;

import fr.amvproject.group3.nodes.TypeMemNode;
import fr.amvproject.group3.nodes.PointerNode;

public class Heap {
  private static final int DEFAULT_INT_VALUE = 0;
  private static final boolean DEFAULT_BOOL_VALUE = false;
  private static final Void DEFAULT_VOID_VALUE = null;

  private List<Object> queue = new ArrayList<>();
  private int nextAddress = 0;

  private void checkIfInBlock(PointerNode pointerNode, int index) {
    if (pointerNode.getAddress() < 0 || pointerNode.getAddress() >= nextAddress || index < 0 || index > getSize(pointerNode) - 1)
      throw new IndexOutOfBoundsException();
  }

  // Allocate memory in the heap and return its address, aka CreerTas
  public PointerNode allocate(int size, TypeMemNode type) {
    int address = nextAddress;
    queue.add(size); // Store size at current address
    if (type.isInt()) {
      for (int i = 0; i < size; i++)
        queue.add(DEFAULT_INT_VALUE);
    } else if (type.isBoolean()) {
      for (int i = 0; i < size; i++)
        queue.add(DEFAULT_BOOL_VALUE);
    } else { // Void
      for (int i = 0; i < size; i++)
        queue.add(DEFAULT_VOID_VALUE);
    }
    nextAddress += size + 1; // Update nextAddress: size + 1 for the size header
    return new PointerNode(address + 1); // Return address of first element (after size header)
  }

  // Remove the allocated memory from specific address, aka RetirerTas
  public void remove(PointerNode pointerNode, TypeMemNode type) {
    if (type == null)
      return; // Unknown use case
    checkIfInBlock(pointerNode, 0); // Will never happen if semantic analysis is correct
    int blockSize = (int) getSize(pointerNode);
    for (int i = 0; i < blockSize; i++) {
      queue.set(pointerNode.getAddress() + i, DEFAULT_VOID_VALUE);
    }
    setSize(pointerNode, 0);
  }

  // Get the object at a specific address
  public Object get(PointerNode pointerNode, int index) {
    checkIfInBlock(pointerNode, index);
    return queue.get(pointerNode.getAddress() + index);
  }

  // Set the object at a specific index starting from the ref address
  public void set(PointerNode pointerNode, int index, Object value) {
    checkIfInBlock(pointerNode, index);
    queue.set(pointerNode.getAddress() + index, value);
  }

  public int getSize(PointerNode pointerNode) {
    if (pointerNode.getAddress() < 0 || pointerNode.getAddress() >= nextAddress);
    return (int) queue.get(pointerNode.getAddress() - 1);
  }

  public void setSize(PointerNode pointerNode, int size) {
    if (pointerNode.getAddress() < 0 || pointerNode.getAddress() >= nextAddress);
    queue.set(pointerNode.getAddress() - 1, size);
  }

  // --- Getters for inspection ---

  public List<Object> getQueue() {
    return queue;
  }

  public int getNextAddress() {
    return nextAddress;
  }

  public List<Object> getReverseQueue() {
    List<Object> reverseQueue = new ArrayList<>();
    for (int i = queue.size() - 1; i >= 0; i--) {
      reverseQueue.add(queue.get(i));
    }
    return reverseQueue;
  }

  /* Print Heap */

  public String print() {
    StringBuilder sb = new StringBuilder();
    sb.append("===== HEAP =====\n");

    if (queue.isEmpty()) {
      sb.append("[Empty Heap]\n");
    } else {
      int address = 0;
      int blockStartAddress = 1;
      int blockSize;
      for (Object value : queue) {
        if (address == blockStartAddress - 1) {
          sb.append("#");
          blockSize = (int) value;
          blockStartAddress = blockStartAddress + blockSize + 1;
        } else {
          sb.append("@");
        }
        sb.append(address).append(": ").append(value).append("\n");
        address++;
      }
    }

    sb.append("======================\n");
    return sb.toString();
  }
}
