package fr.amvproject.group3.model.wrappers;

import fr.amvproject.group3.model.ToggleStates;
import fr.amvproject.group3.ui.memory.HeapView;
import fr.amvproject.group3.ui.memory.StackView;
import fr.amvproject.group3.ui.terminal.nodes.TerminalBodyView;
import fr.amvproject.group3.ui.workbench.nodes.EditorView;
import fr.amvproject.group3.utils.EditorUtils;
import javafx.scene.control.TabPane;

public class ViewsWrapper {
  private TabPane workBenchTabPane = null;
  private StackView stackView = null;
  private HeapView heapView = null;
  private TerminalBodyView terminalBodyView = null;
  private ToggleStates toggleStates = new ToggleStates();
  private boolean isMjj = true;

  public void setIsMjj(boolean isMjj) {
    this.isMjj = isMjj;
  }

  public ToggleStates getToggleStates() {
    return toggleStates;
  }

  public EditorView getEditorView() {
    if (isMjj) {
      return (EditorView) workBenchTabPane.getTabs().get(0).getContent();
    } else {
      return (EditorView) workBenchTabPane.getTabs().get(1).getContent();
    }
  }

  public EditorView getEditorView(boolean isMjj) {
    if (isMjj) {
      return (EditorView) workBenchTabPane.getTabs().get(0).getContent();
    } else {
      return (EditorView) workBenchTabPane.getTabs().get(1).getContent();
    }
  }

  public StackView getStackView() {
    return stackView;
  }

  public HeapView getHeapView() {
    return heapView;
  }

  public TerminalBodyView getTerminalBodyView() {
    return terminalBodyView;
  }

  public void setWorkBenchTabPane(TabPane workBenchTabPane) {
    this.workBenchTabPane = workBenchTabPane;
  }

  public void setStackView(StackView stackView) {
    this.stackView = stackView;
  }

  public void setHeapView(HeapView heapView) {
    this.heapView = heapView;
  }

  public void setTerminalBodyView(TerminalBodyView terminalBodyView) {
    this.terminalBodyView = terminalBodyView;
  }

  public void setWorkBenchActiveEditor(boolean isMjj) {
    if (isMjj) {
      workBenchTabPane.getSelectionModel().select(workBenchTabPane.getTabs().get(0));
    } else {
      workBenchTabPane.getSelectionModel().select(workBenchTabPane.getTabs().get(1));
    }
  }

  public void writeInTerminal(String text, boolean isReset) {
    toggleStates.terminalView.openTerminal();
    getTerminalBodyView().addOutput(text);
    if (isReset) getTerminalBodyView().resetOutput();
  }

  public void setProgramSuccess(String text) {
    toggleStates.hideDebugToolBar();
    EditorUtils.resetLineHighlighting(getEditorView());
    writeInTerminal(text, true);
  }

  public void reset() {
    getTerminalBodyView().clearOutput();
    EditorUtils.resetLineHighlighting(getEditorView());
    stackView.reset();
    heapView.reset();
    toggleStates.reset();
    getEditorView(true).reset(true);
    getEditorView(false).reset(false);
  }
}
