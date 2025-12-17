package fr.amvproject.group3.ui.memory.nodes;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class HeapItemView extends HBox {
  public HeapItemView(String key, String value) {
    this.getStyleClass().add("heap-item");

    Label heapItemKey = new Label(key);
    heapItemKey.getStyleClass().add("heap-item-key");

    Label heapItemEq = new Label(":");
    heapItemEq.getStyleClass().add("heap-item-key");

    Label heapItemValue = new Label(value);
    heapItemValue.getStyleClass().add("heap-item-value");

    this.getChildren().addAll(heapItemKey, heapItemEq, heapItemValue);
  }
}
