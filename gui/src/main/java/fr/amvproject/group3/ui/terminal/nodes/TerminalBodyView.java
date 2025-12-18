package fr.amvproject.group3.ui.terminal.nodes;

import fr.amvproject.group3.model.wrappers.InteractiveWrapper;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class TerminalBodyView extends VBox {
    private boolean wasLn = true;
    public TerminalBodyView(InteractiveWrapper langWrapper) {
        langWrapper.getViewsWrapper().setTerminalBodyView(this);

        this.getStyleClass().add("terminal-body");
        VBox.setVgrow(this, Priority.ALWAYS);

        resetOutput();
    }

    public void addOutput(String output, boolean isLn) {
        String newOutput = output;
        ObservableList<Node> children = this.getChildren();
        if (!wasLn && !children.isEmpty()) {
            Node last = children.remove(children.size() - 1);

            if (last instanceof Label lastLabel) {
                newOutput = lastLabel.getText() + output;
            }
        }
        Label terminalOutput = new Label(newOutput);

        terminalOutput.getStyleClass().add("terminal-output");

        this.getChildren().add(terminalOutput);
        wasLn = isLn;
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
