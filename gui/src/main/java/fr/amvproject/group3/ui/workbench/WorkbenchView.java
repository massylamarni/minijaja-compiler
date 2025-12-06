package fr.amvproject.group3.ui.workbench;

import fr.amvproject.group3.model.LangWrapper;
import fr.amvproject.group3.ui.terminal.TerminalView;
import fr.amvproject.group3.ui.workbench.nodes.EditorView;
import fr.amvproject.group3.ui.workbench.nodes.WorkbenchTitleBar;
import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class WorkbenchView extends VBox {
  public WorkbenchView(LangWrapper langWrapper) {
    this.getStyleClass().add("workbench");
    HBox.setHgrow(this, Priority.ALWAYS);

    WorkbenchTitleBar workbenchTitleBar = new WorkbenchTitleBar(langWrapper);
    EditorView editorView = new EditorView(langWrapper);
    langWrapper.editorView = editorView;
    TerminalView terminalView = new TerminalView();

    SplitPane editorTerminalSplit = new SplitPane();
    editorTerminalSplit.setOrientation(Orientation.VERTICAL);
    editorTerminalSplit.getItems().addAll(editorView, terminalView);
    VBox.setVgrow(editorTerminalSplit, Priority.ALWAYS);

    this.getChildren().addAll(
      workbenchTitleBar,
      editorTerminalSplit
    );
  }
}
