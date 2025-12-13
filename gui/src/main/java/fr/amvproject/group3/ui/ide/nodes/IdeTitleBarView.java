package fr.amvproject.group3.ui.ide.nodes;

import fr.amvproject.group3.model.wrappers.InteractiveWrapper;
import fr.amvproject.group3.utils.FileManager;
import javafx.geometry.Pos;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class IdeTitleBarView extends HBox {
  public IdeTitleBarView(Stage stage, InteractiveWrapper langWrapper) {
    this.getStyleClass().add("ide-title-bar");
    this.setAlignment(Pos.CENTER_LEFT);

    Menu menuFile = new Menu("File");
    MenuItem newItem = new MenuItem("New");
    MenuItem openItem = new MenuItem("Open...");
    MenuItem saveItem = new MenuItem("Save...");
    MenuItem exitItem = new MenuItem("Exit");
    menuFile.getItems().addAll(newItem, openItem, saveItem, new SeparatorMenuItem(), exitItem);

    Menu menuRun = new Menu("Run");
    MenuItem runNormallyItem = new MenuItem("Run normally");
    MenuItem runWithDebugItem = new MenuItem("Run with debug");
    MenuItem compileItem = new MenuItem("Compile");
    MenuItem removeAllBreakpointsItem = new MenuItem("Remove all breakpoints");
    menuRun.getItems().addAll(runNormallyItem, runWithDebugItem, compileItem, removeAllBreakpointsItem);

    MenuBar menuBar = new MenuBar();
    menuBar.getMenus().addAll(menuFile, menuRun);

    /* Handlers menuFile */

    newItem.setOnAction(e -> {
      langWrapper.reset();
    });

    openItem.setOnAction(e -> {
      FileManager.loadFile(langWrapper.getViewsWrapper().getEditorView(), stage);
      langWrapper.getViewsWrapper().getEditorView().setSyntaxHighlighting(getAccessibleHelp());
    });

    saveItem.setOnAction(e -> {
      FileManager.saveFile(langWrapper.getViewsWrapper().getEditorView(), stage);
    });

    exitItem.setOnAction(e -> stage.close());

    /* Handlers menuRun */

    this.getChildren().add(menuBar);
  }
}
