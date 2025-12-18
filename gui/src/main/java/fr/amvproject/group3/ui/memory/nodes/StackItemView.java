package fr.amvproject.group3.ui.memory.nodes;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class StackItemView extends HBox {
  public StackItemView(String key, String value) {
    this.getStyleClass().add("stack-item");

    Label stackItemKey = new Label(key);
    stackItemKey.getStyleClass().add("stack-item-key");

    Label stackItemEq = new Label("=");
    stackItemEq.getStyleClass().add("stack-item-key");

    Label stackItemValue = new Label(value);
    stackItemValue.getStyleClass().add("stack-item-value");

    this.getChildren().addAll(stackItemKey, stackItemEq, stackItemValue);
  }
}
