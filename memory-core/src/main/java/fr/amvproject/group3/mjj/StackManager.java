package fr.amvproject.group3.mjj;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.TreeMap;

public class StackManager {
  private static final int ST = 5;
  private int updatingInstrCount = 0;
  private int instrCount = 0;
  private int currentInstrNum = 0;
  private TreeMap<Integer, Deque<Quad>> stackSnapshots = new TreeMap<>();
  private Deque<Quad> queue = new ArrayDeque<>();

  public Deque<Quad> getQueue() {
    return queue;
  }

  public void update(boolean isUpdated) {
    if (currentInstrNum < instrCount) return;
    if (isUpdated) updatingInstrCount++;
    instrCount++;
    if (updatingInstrCount % ST == 0) takeSnapshot();
  }
  
  public void goForward() {
    currentInstrNum++;
  }

  public void goBackward() {
    currentInstrNum--;
  }

  public Deque<Quad> getSnapshot() {
    return new ArrayDeque<>(stackSnapshots.get(stackSnapshots.floorKey(currentInstrNum)));
  }

  private void takeSnapshot() {
    stackSnapshots.put(instrCount, new ArrayDeque<>(queue));
  }
}
