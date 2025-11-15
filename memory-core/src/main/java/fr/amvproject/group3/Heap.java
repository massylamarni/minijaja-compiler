package fr.amvproject.group3;

import java.util.ArrayList;
import java.util.List;

import fr.amvproject.group3.mjj.ast.nodes.other.TypeMethNode;
import fr.amvproject.group3.mjj.ast.nodes.other.TypeNode;
import fr.amvproject.group3.nodes.TypeMemNode;
import fr.amvproject.group3.utils.StringSaver;

public class Heap {
    private List<Object> queue = new ArrayList<>();
    private int nextAddress = 0;

    // Allocate memory in the heap and return its address, aka CreerTas
    public int allocate(int size, TypeMemNode type) {
      int address = nextAddress;
      queue.add(size); // Store size at current address
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
      nextAddress += size + 1; // Update nextAddress: size + 1 for the size header
      return address + 1; // Return address of first element (after size header)
    }

    // Remove the allocated memory from specific address, aka RetirerTas
    public void remove(int address, TypeMemNode type) {
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
    public void set(int address, Object value) {
      if (address < 0 || address >= nextAddress) throw new IndexOutOfBoundsException();
      queue.set(address, value);
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
          if (address == blockStartAddress-1) {
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
