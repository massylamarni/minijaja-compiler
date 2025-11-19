package fr.amvproject.group3.ui;

import fr.amvproject.group3.Heap;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class HeapView extends VBox {
  private final Heap heap;

  public HeapView(Heap heap) {
    this.heap = heap;
    refresh();
  }

  public void refresh() {
    getChildren().clear();

    getChildren().add(new Label("===== HEAP ====="));

    if (heap.getQueue().isEmpty()) {
      getChildren().add(new Label("[Empty Heap]"));
      return;
    }

    int address = 0;
    int blockStartAddress = 1;
    int blockSize;

    for (Object value : heap.getQueue()) {
      HBox row = new HBox(6);

      Label marker;
      if (address == blockStartAddress - 1) {
        marker = new Label("#");
        blockSize = (int) value;
        blockStartAddress += blockSize + 1;
      } else {
        marker = new Label("@");
      }

      row.getChildren().add(marker);
      row.getChildren().add(new Label(address + ": " + value));

      getChildren().add(row);

      address++;
    }

    getChildren().add(new Label("======================"));
  }
}
