package fr.amvproject.group3.nodes;

public class ArrayInfoNode {
  private int size;
  private int refCount;

  public ArrayInfoNode(int size, int refCount) {
    this.size = size;
    this.refCount = refCount;
  }

  public int getSize() {
    return size;
  }

  public int getRefCount() {
    return refCount;
  }

  public void incRefCount() {
    refCount++;
  }

  public void decRefCount() {
    refCount--;
  }

  public void destroy() {
    refCount = 0;
    size = 0;
  }
}
