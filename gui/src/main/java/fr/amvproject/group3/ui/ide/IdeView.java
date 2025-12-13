package fr.amvproject.group3.ui.ide;

import fr.amvproject.group3.model.wrappers.InteractiveWrapper;
import fr.amvproject.group3.ui.ide.nodes.IdeStatusBarView;
import fr.amvproject.group3.ui.ide.nodes.IdeTitleBarView;
import fr.amvproject.group3.ui.ide.nodes.LeftSideBarView;
import fr.amvproject.group3.ui.workbench.WorkbenchView;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class IdeView extends VBox {
  public IdeView(Stage stage) {
    this.getStyleClass().add("ide");

    InteractiveWrapper langWrapper = new InteractiveWrapper();

    IdeTitleBarView ideTitleBarView = new IdeTitleBarView(stage, langWrapper);
    IdeStatusBarView ideStatusBarView = new IdeStatusBarView();

    SplitPane ideBody = new SplitPane();
    ideBody.getStyleClass().add("ide-body");
    ideBody.setOrientation(Orientation.HORIZONTAL);
    ideBody.getItems().addAll(new LeftSideBarView(langWrapper), new WorkbenchView(langWrapper));
    ideBody.setDividerPositions(0.2);
    VBox.setVgrow(ideBody, Priority.ALWAYS);

    this.getChildren().addAll(ideTitleBarView, ideBody, ideStatusBarView);

    Platform.runLater(() -> {
      langWrapper.getViewsWrapper().getToggleStates().holdLeftSideBarState();
      langWrapper.getViewsWrapper().getToggleStates().holdTerminalState();
      langWrapper.getViewsWrapper().getToggleStates().holdDebugToolBarState();
    });
  }
}
