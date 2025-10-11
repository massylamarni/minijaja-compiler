package fr.amvproject.group3.memory;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public class Heap {
    Deque<Object> heap;
    int nextAddress;

    public Heap() {
      heap = new ArrayDeque<>();
      nextAddress = 0;
    }

    // Allocate a new object in the heap and return its address
    public int allocate(Object value) {
      heap.addLast(value);
      return nextAddress++;
    }

    // Get the object at a specific address
    public Object get(int address) {
      if (address < 0 || address >= nextAddress)
        throw new IndexOutOfBoundsException();
      Iterator<Object> it = heap.iterator();
      for (int i = 0; i < address; i++)
        it.next();
      return it.next();
    }

    // Set the object at a specific address
    public void set(int address, Object value) {
      if (address < 0 || address >= nextAddress)
        throw new IndexOutOfBoundsException();
      Iterator<Object> it = heap.iterator();
      Deque<Object> temp = new ArrayDeque<>();
      for (int i = 0; i < nextAddress; i++) {
        Object current = it.next();
        if (i == address)
          temp.addLast(value);
        else
          temp.addLast(current);
      }
      heap = temp;
    }
  }
