package fr.amvproject.group3.ui.terminal.nodes;

import fr.amvproject.group3.model.LangWrapper;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class TerminalBodyView extends VBox {
  public TerminalBodyView(LangWrapper langWrapper) {
    langWrapper.setTerminalBodyView(this);

    this.getStyleClass().add("terminal-body");
    VBox.setVgrow(this, Priority.ALWAYS);

    resetOutput();
  }

  public void addOutput(String output) {
    Label terminalOutput = new Label(output);
    terminalOutput.getStyleClass().add("terminal-output");

    this.getChildren().add(terminalOutput);
  }

  public void resetOutput() {
    HBox terminalDefaultOutput = new HBox();

    Label terminalUserOutput = new Label("user@USER:");
    terminalUserOutput.getStyleClass().add("terminal-user-output");

    Label terminalPathOutput = new Label("~/fr/amvproject/groupe-3$ ");
    terminalPathOutput.getStyleClass().add("terminal-path-output");

    terminalDefaultOutput.getChildren().addAll(terminalUserOutput, terminalPathOutput);
    this.getChildren().add(terminalDefaultOutput);
  }
}
