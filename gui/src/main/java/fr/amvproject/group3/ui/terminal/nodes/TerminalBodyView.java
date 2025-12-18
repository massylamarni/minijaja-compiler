package fr.amvproject.group3.ui.terminal.nodes;

import fr.amvproject.group3.model.wrappers.InteractiveWrapper;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class TerminalBodyView extends VBox {

    private HBox currentLine;


    public TerminalBodyView(InteractiveWrapper langWrapper) {
    langWrapper.getViewsWrapper().setTerminalBodyView(this);

    this.getStyleClass().add("terminal-body");
    VBox.setVgrow(this, Priority.ALWAYS);

    resetOutput();
  }

    public void addOutput(String output) {
        if (currentLine == null) {
            startNewLine();
        }

        String[] parts = output.split("\n", -1);

        for (int i = 0; i < parts.length; i++) {
            if (!parts[i].isEmpty()) {
                Label terminalOutput = new Label(parts[i]);
                terminalOutput.getStyleClass().add("terminal-output");
                currentLine.getChildren().add(terminalOutput);
            }
            if (i < parts.length - 1) {
                startNewLine();
            }
        }
    }


    private void startNewLine() {
        currentLine = new HBox();
        currentLine.getStyleClass().add("terminal-output");
        this.getChildren().add(currentLine);
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

  public void clearOutput() {
    HBox terminalDefaultOutput = new HBox();

    Label terminalUserOutput = new Label("user@USER:");
    terminalUserOutput.getStyleClass().add("terminal-user-output");

    Label terminalPathOutput = new Label("~/fr/amvproject/groupe-3$ ");
    terminalPathOutput.getStyleClass().add("terminal-path-output");

    terminalDefaultOutput.getChildren().addAll(terminalUserOutput, terminalPathOutput);
    this.getChildren().setAll(terminalDefaultOutput);
  }
}
