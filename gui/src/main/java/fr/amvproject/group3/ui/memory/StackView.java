package fr.amvproject.group3.ui.memory;
import fr.amvproject.group3.ui.memory.nodes.StackItemVIew;
import javafx.scene.layout.VBox;

public class StackView extends VBox {
  // private final Stack stack;

  public StackView() {
    this.getStyleClass().add("stack-view");

    this.getChildren().add(new StackItemVIew());
  }
}
