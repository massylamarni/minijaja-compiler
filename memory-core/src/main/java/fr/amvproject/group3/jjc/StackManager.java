package fr.amvproject.group3.jjc;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.TreeMap;

public class StackManager {
  private static final int ST = 5;
  private int updatingInstrCount = 0;
  private int instrCount = 0;
  private int currentInstrNum = 0;
  private TreeMap<Integer, Deque<Quad>> stackSnapshots = new TreeMap<>();
  private TreeMap<Integer, List<Object>> heapSnapshots = new TreeMap<>();
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

  public Deque<Quad> getStackSnapshot() {
    return new ArrayDeque<>(stackSnapshots.get(stackSnapshots.floorKey(currentInstrNum)));
  }

  public List<Object> getHeapSnapshot() {
    return new ArrayList<>(heapSnapshots.get(heapSnapshots.floorKey(currentInstrNum)));
  }

  private void takeSnapshot() {
    stackSnapshots.put(instrCount, new ArrayDeque<>(queue));
    heapSnapshots.put(instrCount, new ArrayList<>(queue));
  }
}
