package fr.amvproject.group3;

import java.util.ArrayList;
import java.util.List;

import fr.amvproject.group3.nodes.TypeMemNode;
import fr.amvproject.group3.nodes.PointerNode;
import fr.amvproject.group3.nodes.ArrayInfoNode;

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
    queue.add(new ArrayInfoNode(size, 1));
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
  private void remove(PointerNode pointerNode, TypeMemNode type) {
    if (type == null)
      return; // Unknown use case
    checkIfInBlock(pointerNode, 0); // Will never happen if semantic analysis is correct
    int blockSize = (int) getSize(pointerNode);
    for (int i = 0; i < blockSize; i++) {
      queue.set(pointerNode.getAddress() + i, DEFAULT_VOID_VALUE);
    }
    getArrayInfoNode(pointerNode).destroy();
  }

  public void addRef(PointerNode pointerNode) {
    getArrayInfoNode(pointerNode).incRefCount();
  }

  public void removeRef(PointerNode pointerNode, TypeMemNode type) {
    ArrayInfoNode arrayInfoNode = getArrayInfoNode(pointerNode);
    arrayInfoNode.decRefCount();
    if (arrayInfoNode.getRefCount() <= 0) {
      remove(pointerNode, type);
    }
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

  public ArrayInfoNode getArrayInfoNode(PointerNode pointerNode) {
    if (pointerNode.getAddress() < 0 || pointerNode.getAddress() >= nextAddress);
    return ((ArrayInfoNode) queue.get(pointerNode.getAddress() - 1));
  }

  public int getSize(PointerNode pointerNode) {
    return getArrayInfoNode(pointerNode).getSize();
  }

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

  public void reset() {
    queue.clear();
    nextAddress = 0;
  }

  /* Print Heap */

  public String print() {
    StringBuilder sb = new StringBuilder();

    if (queue.isEmpty()) {
      sb.append("[Empty Heap]\n");
    } else {
      for (Object value : queue) {
        sb.append(value).append("\n");
      }
    }

    return sb.toString();
  }
}
