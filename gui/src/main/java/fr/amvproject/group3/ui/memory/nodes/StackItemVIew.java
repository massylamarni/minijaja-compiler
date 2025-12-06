package fr.amvproject.group3.ui.memory.nodes;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class StackItemVIew extends HBox {
  public StackItemVIew() {
    this.getStyleClass().add("stack-item");

    Label stackItemKey = new Label("line");
    stackItemKey.getStyleClass().add("stack-item-key");

    Label stackItemEq = new Label("=");
    stackItemEq.getStyleClass().add("stack-item-key");

    Label stackItemValue = new Label("320");
    stackItemValue.getStyleClass().add("stack-item-value");

    this.getChildren().addAll(stackItemKey, stackItemEq, stackItemValue);
  }
}
