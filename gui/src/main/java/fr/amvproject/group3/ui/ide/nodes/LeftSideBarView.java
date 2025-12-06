package fr.amvproject.group3.ui.ide.nodes;

import fr.amvproject.group3.model.LangWrapper;
import fr.amvproject.group3.ui.memory.StackView;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class LeftSideBarView extends VBox {
  public LeftSideBarView(LangWrapper langWrapper) {
    this.getStyleClass().add("left-sidebar");
    HBox.setHgrow(this, Priority.NEVER);

    Label leftSidebarTitle = new Label("Local variables");
    leftSidebarTitle.getStyleClass().add("left-sidebar-title");

    VBox leftSidebarBody = new VBox();
    leftSidebarBody.getStyleClass().add("left-sidebar-body");
    leftSidebarBody.getChildren().add(new StackView());

    // this.getChildren().addAll(leftSidebarTitle, leftSidebarBody);
  }
}
