package fr.amvproject.group3.ui.terminal;

import fr.amvproject.group3.ui.common.ActionButtonView;
import fr.amvproject.group3.ui.icons.IconType;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class TerminalView extends VBox {
  public TerminalView() {
    this.getStyleClass().add("terminal");

    HBox terminalTitleBar = new HBox();
    terminalTitleBar.getStyleClass().add("terminal-title-bar");

    ActionButtonView closActionButtonView = new ActionButtonView(IconType.CLOSE);
    closActionButtonView.setOnAction(e -> {
      this.setMinHeight(0);
      findParentSplitPane().setDividerPositions(1.0);
    });

    HBox terminalActionBar = new HBox();
    terminalActionBar.getStyleClass().add("action-bar");
    terminalActionBar.getChildren().add(closActionButtonView);

    Region terminalSpacer = new Region();
    HBox.setHgrow(terminalSpacer, Priority.ALWAYS);
    terminalTitleBar.getChildren().addAll(terminalSpacer, terminalActionBar);

    Region terminalBody = new Region();
    terminalBody.getStyleClass().add("terminal-body");
    VBox.setVgrow(terminalBody, Priority.ALWAYS);

    this.getChildren().addAll(terminalTitleBar, terminalBody);
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
