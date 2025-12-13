package fr.amvproject.group3.ui.terminal;

import fr.amvproject.group3.model.InteractiveWrapper;
import fr.amvproject.group3.ui.common.ActionButtonView;
import fr.amvproject.group3.ui.icons.IconType;
import fr.amvproject.group3.ui.terminal.nodes.TerminalBodyView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class TerminalView extends VBox {
  public TerminalView(InteractiveWrapper langWrapper) {
    langWrapper.getViewsWrapper().getToggleStates().terminalView = this;

    this.getStyleClass().add("terminal");

    HBox terminalTitleBar = new HBox();
    terminalTitleBar.getStyleClass().add("terminal-title-bar");

    ActionButtonView closActionButtonView = new ActionButtonView(IconType.CLOSE);
    closActionButtonView.setOnAction(e -> {
      langWrapper.getViewsWrapper().getToggleStates().closeTerminal();
    });

    HBox terminalActionBar = new HBox();
    terminalActionBar.getStyleClass().add("action-bar");
    terminalActionBar.getChildren().add(closActionButtonView);

    Region hSpacer = new Region();
    HBox.setHgrow(hSpacer, Priority.ALWAYS);
    terminalTitleBar.getChildren().addAll(hSpacer, terminalActionBar);

    ScrollPane terminalBodyScroll = new ScrollPane(new TerminalBodyView(langWrapper));

    this.getChildren().addAll(terminalTitleBar, terminalBodyScroll);
  }

  public void closeTerminal() {
    this.setMinHeight(0);
    findParentSplitPane().setDividerPositions(1.0);
  }

  public void openTerminal() {
    findParentSplitPane().setDividerPositions(0.5);
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
