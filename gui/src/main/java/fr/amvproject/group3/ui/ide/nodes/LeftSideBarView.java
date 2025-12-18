package fr.amvproject.group3.ui.ide.nodes;

import fr.amvproject.group3.model.wrappers.InteractiveWrapper;
import fr.amvproject.group3.ui.common.ActionButtonView;
import fr.amvproject.group3.ui.icons.IconType;
import fr.amvproject.group3.ui.memory.HeapView;
import fr.amvproject.group3.ui.memory.StackView;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class LeftSideBarView extends VBox {
  public LeftSideBarView(InteractiveWrapper langWrapper) {
    langWrapper.getViewsWrapper().getToggleStates().leftSideBarView = this;

    this.getStyleClass().add("left-sidebar");
    HBox.setHgrow(this, Priority.NEVER);

    ActionButtonView closActionButtonView = new ActionButtonView(IconType.CLOSE);
    closActionButtonView.setOnAction(e -> {
      langWrapper.getViewsWrapper().getToggleStates().closeLeftSideBar();
    });

    Label leftSidebarStackTitle = new Label("Stack view");
    leftSidebarStackTitle.getStyleClass().add("left-sidebar-title");

    HBox leftSideBarStackActionBar = new HBox();
    leftSideBarStackActionBar.getStyleClass().add("action-bar");

    Region hSpacerI = new Region();
    HBox.setHgrow(hSpacerI, Priority.ALWAYS);
    leftSideBarStackActionBar.getChildren().addAll(leftSidebarStackTitle, hSpacerI, closActionButtonView);

    StackView stackView = new StackView(langWrapper);
    ScrollPane stackScroll = new ScrollPane(stackView);
    stackScroll.getStyleClass().add("left-sidebar-body");
    stackScroll.prefViewportHeightProperty().bind(this.heightProperty().multiply(0.5));

    Label leftSidebarHeapTitle = new Label("Heap view");
    leftSidebarHeapTitle.getStyleClass().add("left-sidebar-title");

    HBox leftSideBarHeapActionBar = new HBox();
    leftSideBarHeapActionBar.getStyleClass().add("action-bar");

    Region hSpacerII = new Region();
    HBox.setHgrow(hSpacerII, Priority.ALWAYS);
    leftSideBarHeapActionBar.getChildren().addAll(leftSidebarHeapTitle, hSpacerII);

    HeapView heapView = new HeapView(langWrapper);
    ScrollPane heapScroll = new ScrollPane(heapView);
    heapScroll.getStyleClass().add("left-sidebar-body");
    heapScroll.prefViewportHeightProperty().bind(this.heightProperty().multiply(0.5));

    this.getChildren().addAll(leftSideBarStackActionBar, stackScroll, leftSideBarHeapActionBar, heapScroll);
  }

  public void closeLeftSideBar() {
    this.setMinWidth(0);
    SplitPane parentSplitPane = findParentSplitPane();
    if (parentSplitPane != null) parentSplitPane.setDividerPositions(0);
  }

  public void openLeftSideBar() {
    SplitPane parentSplitPane = findParentSplitPane();
    if (parentSplitPane != null) parentSplitPane.setDividerPositions(0.3);
  }

  private SplitPane findParentSplitPane() {
    javafx.scene.Parent parent = getParent();
    while (parent != null) {
      if (parent instanceof SplitPane splitPane) {
        return splitPane;
      }
      parent = parent.getParent();
    }
    return null;
  }
}
