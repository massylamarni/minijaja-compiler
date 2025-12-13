package fr.amvproject.group3.ui.workbench;

import fr.amvproject.group3.model.InteractiveWrapper;
import fr.amvproject.group3.ui.terminal.TerminalView;
import fr.amvproject.group3.ui.workbench.nodes.EditorView;
import fr.amvproject.group3.ui.workbench.nodes.WorkbenchTitleBar;
import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class WorkbenchView extends VBox {
  public WorkbenchView(InteractiveWrapper langWrapper) {
    this.getStyleClass().add("workbench");
    HBox.setHgrow(this, Priority.ALWAYS);

    EditorView mjjEditorView = new EditorView(langWrapper);
    langWrapper.setIsMjj(false);
    EditorView jjcEditorView = new EditorView(langWrapper);
    langWrapper.setIsMjj(true);

    TabPane tabPane = new TabPane();
    Tab tab1 = new Tab("MiniJaja", mjjEditorView);
    Tab tab2 = new Tab("JajaCode", jjcEditorView);
    tab1.setClosable(false);
    tab2.setClosable(false);
    tabPane.getTabs().addAll(tab1, tab2);
    HBox.setHgrow(tabPane, Priority.ALWAYS);

    WorkbenchTitleBar workbenchTitleBar = new WorkbenchTitleBar(langWrapper, tabPane);
    TerminalView terminalView = new TerminalView(langWrapper);

    SplitPane editorTerminalSplit = new SplitPane();
    editorTerminalSplit.setOrientation(Orientation.VERTICAL);
    editorTerminalSplit.getItems().addAll(mjjEditorView, terminalView);
    VBox.setVgrow(editorTerminalSplit, Priority.ALWAYS);

    tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
      langWrapper.setIsMjj(!langWrapper.isMjj());
      editorTerminalSplit.getItems().set(0,
        newTab == tab1 ? mjjEditorView : jjcEditorView
      );
      langWrapper.getViewsWrapper().getToggleStates().holdTerminalState();
    });

    this.getChildren().addAll(
      workbenchTitleBar,
      editorTerminalSplit
    ); 
  }
}
