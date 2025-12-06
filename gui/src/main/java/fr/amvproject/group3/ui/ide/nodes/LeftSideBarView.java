package fr.amvproject.group3.ui.ide.nodes;

import fr.amvproject.group3.model.LangWrapper;
import fr.amvproject.group3.ui.memory.HeapView;
import fr.amvproject.group3.ui.memory.StackView;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class LeftSideBarView extends VBox {
  public LeftSideBarView(LangWrapper langWrapper) {
    this.getStyleClass().add("left-sidebar");
    HBox.setHgrow(this, Priority.NEVER);

    Label leftSidebarStackTitle = new Label("Stack view");
    leftSidebarStackTitle.getStyleClass().add("left-sidebar-title");

    StackView stackView = new StackView(langWrapper);
    ScrollPane stackScroll = new ScrollPane(stackView);
    stackScroll.getStyleClass().add("left-sidebar-body");
    stackScroll.prefViewportHeightProperty().bind(this.heightProperty().multiply(0.5));

    Label leftSidebarHeapTitle = new Label("Heap view");
    leftSidebarHeapTitle.getStyleClass().add("left-sidebar-title");

    HeapView heapView = new HeapView(langWrapper);
    ScrollPane heapScroll = new ScrollPane(heapView);
    heapScroll.getStyleClass().add("left-sidebar-body");
    heapScroll.prefViewportHeightProperty().bind(this.heightProperty().multiply(0.5));

    this.getChildren().addAll(leftSidebarStackTitle, stackScroll, leftSidebarHeapTitle, heapScroll);
  }
}
