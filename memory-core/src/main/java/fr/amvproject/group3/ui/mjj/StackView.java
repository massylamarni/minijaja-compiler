package fr.amvproject.group3.ui.mjj;

import java.util.List;

import fr.amvproject.group3.mjj.Quad;
import fr.amvproject.group3.mjj.Stack;
import fr.amvproject.group3.mjj.ast.nodes.other.TypeMethNode;
import fr.amvproject.group3.mjj.ast.nodes.other.TypeNode;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.control.Label;

public class StackView extends VBox {
  private final Stack stack;

  public StackView(Stack stack) {
    this.stack = stack;
    setSpacing(4);
    refresh();
  }

  public void refresh() {
    getChildren().clear();

    List<Quad> items = stack.getQueue();

    for (int i = 0; i < items.size(); i++) {
      Quad quad = items.get(i);
      getChildren().add(makeQuadBox(quad, i));
    }
  }

  private Node makeQuadBox(Quad quad, int index) {
    VBox box = new VBox(2);
    box.setStyle("-fx-padding: 5; -fx-border-color: black; -fx-background-color: #ececec;");

    box.getChildren().add(new Label("-- Entry #" + index + " --"));
    box.getChildren().add(new Label("Ident : " + quad.getIdent().getName()));
    box.getChildren().add(new Label("Obj   : " + quad.getObj().getName()));

    String typeName = quad.getObj().isCst()
        ? ((TypeNode) quad.getType()).getName()
        : ((TypeMethNode) quad.getType()).getName();

    box.getChildren().add(new Label("Type  : " + typeName));
    box.getChildren().add(new Label("Value : " + quad.getValue()));

    return box;
  }
}
