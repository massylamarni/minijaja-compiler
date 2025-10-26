package fr.amvproject.group3.memory;

import java.util.ArrayList;
import java.util.List;

import fr.amvproject.group3.ast.nodes.other.TypeNode;

public class Heap {
    private List<Object> queue;
    private int nextAddress;

    public Heap() {
      queue = new ArrayList<>();
      nextAddress = 0;
    }

    // Allocate memory in the heap and return its address, aka CreerTas
    public int allocate(int size, TypeNode type) {
      queue.add(size);
      if (type.isInt()) {
        for (int i = 0; i < size; i++)
          queue.add(0);
      } else if (type.isBoolean()) {
        for (int i = 0; i < size; i++)
          queue.add(false);
      } else { // Void
        for (int i = 0; i < size; i++)
          queue.add(null);
      }
      return nextAddress+size+1;
    }

    // Remove the allocated memory from specific address, aka RetirerTas
    public void remove(int address, TypeNode type) {
      if (type == null) return; // Unknown use case
      if (address < 0 || address >= nextAddress) throw new IndexOutOfBoundsException();
      int blockSize = (int) get(address - 1);
      for (int i = 0; i < blockSize; i++) {
        queue.set(address+i, null);
      }
      queue.set(address-1, 0);
    }

    // Get the object at a specific address
    public Object get(int address) {
      if (address < 0 || address >= nextAddress) throw new IndexOutOfBoundsException();
      return queue.get(address);
    }

    // Set the object at a specific index starting from the ref address
    public void set(int address, int index, Object value) {
      if (address < 0 || address >= nextAddress) throw new IndexOutOfBoundsException();
      queue.set(address+index, value);
    }
  }
