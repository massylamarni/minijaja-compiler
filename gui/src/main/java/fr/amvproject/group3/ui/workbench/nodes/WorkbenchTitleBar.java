package fr.amvproject.group3.ui.workbench.nodes;

import fr.amvproject.group3.model.LangWrapper;
import fr.amvproject.group3.ui.common.ActionButtonView;
import fr.amvproject.group3.ui.icons.IconType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class WorkbenchTitleBar extends HBox {
  public WorkbenchTitleBar(LangWrapper langWrapper) {
    this.getStyleClass().add("workbench-title-bar");

    HBox workbenchActionBar = new HBox();
    workbenchActionBar.getStyleClass().add("action-bar");

    ActionButtonView compileActionButtonView = new ActionButtonView(IconType.COMPILE);
    ActionButtonView runWithDebugActionButtonView = new ActionButtonView(IconType.RUN_WITH_DEBUG);
    ActionButtonView runActionButtonView = new ActionButtonView(IconType.RUN);

    compileActionButtonView.setOnAction(null); // TODO compile
    runWithDebugActionButtonView.setOnAction(e -> langWrapper.runWithDebug());
    runActionButtonView.setOnAction(e -> langWrapper.run());

    workbenchActionBar.getChildren().addAll(compileActionButtonView, runWithDebugActionButtonView, runActionButtonView);

    Region spacerLeft = new Region();
    HBox.setHgrow(spacerLeft, Priority.ALWAYS);
    Region spacerRight = new Region();
    HBox.setHgrow(spacerRight, Priority.ALWAYS);
    this.getChildren().addAll(spacerLeft, new DebugToolBar(langWrapper), spacerRight, workbenchActionBar);
  }
}
