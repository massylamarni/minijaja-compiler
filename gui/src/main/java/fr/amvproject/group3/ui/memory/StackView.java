package fr.amvproject.group3.ui.memory;

import fr.amvproject.group3.mjj.Quad;
import fr.amvproject.group3.mjj.Stack;
import fr.amvproject.group3.model.LangWrapper;
import fr.amvproject.group3.nodes.PointerNode;
import fr.amvproject.group3.ui.memory.nodes.StackItemView;
import javafx.scene.layout.VBox;

public class StackView extends VBox {
  public StackView(LangWrapper langWrapper) {
    langWrapper.setStackView(this);

    this.getStyleClass().add("stack-view");
  }

  public void buildMjjStack(Stack stack) {
    this.getChildren().clear();
    for (Quad quad : stack.getQueue()) {
      if (quad.getObj().isTab()) this.getChildren().add(new StackItemView(quad.getIdent().getName(), '@' + String.valueOf(((PointerNode) quad.getValue()).getAddress())));
      else if (!quad.getObj().isMeth()) this.getChildren().add(new StackItemView(quad.getIdent().getName(), String.valueOf(quad.getValue())));
    }
  }

  public void buildJjcStack(fr.amvproject.group3.jjc.Stack stack) {
    this.getChildren().clear();
    for (fr.amvproject.group3.jjc.Quad quad : stack.getQueue()) {
      if (!quad.getObj().isMeth()) this.getChildren().add(new StackItemView(quad.getIdent().getName(), String.valueOf(quad.getValue())));
    }
  }
}
