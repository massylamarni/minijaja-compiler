package fr.amvproject.group3.ui.workbench.nodes;

import fr.amvproject.group3.model.wrappers.InteractiveWrapper;
import fr.amvproject.group3.ui.common.ActionButtonView;
import fr.amvproject.group3.ui.icons.IconType;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class WorkbenchTitleBar extends HBox {
  public WorkbenchTitleBar(InteractiveWrapper langWrapper, TabPane tabPane) {
    langWrapper.getViewsWrapper().setWorkBenchTabPane(tabPane);

    this.getStyleClass().add("workbench-title-bar");

    HBox workbenchActionBar = new HBox();
    workbenchActionBar.getStyleClass().add("action-bar");

    ActionButtonView compileActionButtonView = new ActionButtonView(IconType.COMPILE);
    ActionButtonView runWithDebugActionButtonView = new ActionButtonView(IconType.RUN_WITH_DEBUG);
    ActionButtonView runActionButtonView = new ActionButtonView(IconType.RUN);

    compileActionButtonView.setOnAction(e -> {
      langWrapper.getCompilerWrapper().compile();
    });
    runWithDebugActionButtonView.setOnAction(e -> langWrapper.getInterpreterWrapper().runWithDebug());
    runActionButtonView.setOnAction(e -> langWrapper.getInterpreterWrapper().run());

    workbenchActionBar.getChildren().addAll(compileActionButtonView, runWithDebugActionButtonView, runActionButtonView);

    Region hSpacer = new Region();
    HBox.setHgrow(hSpacer, Priority.ALWAYS);
    this.getChildren().addAll(tabPane, new DebugToolBarView(langWrapper), hSpacer, workbenchActionBar);
  }
}
