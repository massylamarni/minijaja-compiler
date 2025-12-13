package fr.amvproject.group3.ui.workbench.nodes;

import fr.amvproject.group3.model.LangWrapper;
import fr.amvproject.group3.ui.common.ActionButtonView;
import fr.amvproject.group3.ui.icons.IconType;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

public class DebugToolBarView extends HBox {
  public DebugToolBarView(LangWrapper langWrapper) {
    langWrapper.toggleStates.debugToolBarView = this;

    this.getStyleClass().add("debug-toolbar");
    this.setSpacing(8);
    this.setAlignment(Pos.CENTER);

    ActionButtonView continueButton = new ActionButtonView(IconType.DEBUG_CONTINUE);
    ActionButtonView stepInButton = new ActionButtonView(IconType.DEBUG_STEP_IN);
    ActionButtonView stepOutButton = new ActionButtonView(IconType.DEBUG_STEP_OUT);
    ActionButtonView restartButton = new ActionButtonView(IconType.DEBUG_RESTART);
    ActionButtonView stopButton = new ActionButtonView(IconType.DEBUG_STOP);

    continueButton.setOnAction(e -> langWrapper.resume());
    stepInButton.setOnAction(e -> langWrapper.stepForward());
    stepOutButton.setOnAction(e -> langWrapper.stepBackward());
    restartButton.setOnAction(e -> langWrapper.restart());
    stopButton.setOnAction(e -> langWrapper.restart());

    this.getChildren().addAll(
      continueButton,
      stepInButton,
      stepOutButton,
      restartButton,
      stopButton
    );
  }

  public void hideDebugToolBar() {
    this.setVisible(false);
    this.setManaged(false);
  }

  public void showDebugToolBar() {
    this.setVisible(true);
    this.setManaged(true);
  }
}
