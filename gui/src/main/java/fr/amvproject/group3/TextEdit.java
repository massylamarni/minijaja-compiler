package fr.amvproject.group3;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Main text editor window for the MiniJaja project.
 *
 * Features:
 * - Text editor area for writing MiniJaja or JajaCode code.
 * - Save button (saves content as .mjj file using FileSaver).
 * - Upload button (loads .mjj or .txt files using FileUpload).
 * - Keyboard shortcuts:
 * Ctrl + S → Windows / Linux
 * Command + S → macOS
 *
 * This class connects the UI with FileSaver and FileUpload classes.
 */
public class TextEdit extends Application {
    // The text editing area
    private TextArea textArea;
    // Handles saving logic (with validations)
    private FileSaver fileSaver;
    // Handles uploading logic (with validations)
    private FileUpload fileUpload;
    // Split text area
    private SplitPane splitPane;
    private TextArea outputArea;

    private Button closeOutputButton = new Button("Close output");

    private void openOutputPane() {
        if (splitPane.getItems().size() == 1) {
            splitPane.getItems().add(outputArea);
            splitPane.setDividerPositions(0.6);
        }
        closeOutputButton.setVisible(true);
    }

    private void closeOutputPane() {
        if (splitPane.getItems().size() == 2) {
            splitPane.getItems().remove(1);
        }
        closeOutputButton.setVisible(false);
    }

    @Override
    public void start(Stage primaryStage) {
        // Initialize components
        textArea = new TextArea();
        fileSaver = new FileSaver();
        fileUpload = new FileUpload();

        outputArea = new TextArea();
        outputArea.setEditable(false);

        splitPane = new SplitPane();
        splitPane.getItems().add(textArea);
        splitPane.setDividerPositions(0.6);

        // Create buttons
        Button saveButton = new Button("Save");
        Button uploadButton = new Button("Upload");
        Button compileMjjButton = new Button("Compile");
        Button interpretMjjButton = new Button("Interpret MiniJaja");
        Button interpretJjcButton = new Button("Interpret JajaCode");
        Button generateMjjButton = new Button("Generate MiniJaja");
        Button generateJjcButton = new Button("Generate JajaCode");
        closeOutputButton.setVisible(false);

        FileSaverJJC mjjManager = new FileSaverJJC();

        // --- Button Actions ---
        // Save the current content using FileSaver
        saveButton.setOnAction(e -> fileSaver.saveToFile(textArea, primaryStage));
        // Load file content using FileUpload
        uploadButton.setOnAction(e -> fileUpload.loadFile(textArea, primaryStage));

        compileMjjButton.setOnAction(e -> {
            outputArea.setText(mjjManager.compilation(textArea.getText()));
            openOutputPane();
        });

        interpretMjjButton.setOnAction(e -> {
            outputArea.setText(mjjManager.interpret(textArea.getText()));
            openOutputPane();
        });

        interpretJjcButton.setOnAction(e -> {
            /*TODO
            JajacodeInterpreterCaller jajacodeInterpreterCaller = new JajacodeInterpreterCaller();
            jajacodeInterpreterCaller.execute(textArea.getText());
            outputArea.setText(jajacodeInterpreterCaller.getString());
            */
            openOutputPane();
        });

        generateMjjButton.setOnAction(e -> {
            fr.amvproject.group3.mjj.ast.ProgramGenerator programGenerator = new fr.amvproject.group3.mjj.ast.ProgramGenerator();
            programGenerator.generateProgram();
            outputArea.setText(programGenerator.getString());
            openOutputPane();
        });

        generateJjcButton.setOnAction(e -> {
            fr.amvproject.group3.jjc.ast.ProgramGenerator programGenerator = new fr.amvproject.group3.jjc.ast.ProgramGenerator();
            programGenerator.generateProgram();
            outputArea.setText(programGenerator.getString());
            openOutputPane();
        });

        closeOutputButton.setOnAction(e -> closeOutputPane());

        // Build layout (top bar with buttons + text area in center)
        BorderPane layout = createLayout(saveButton, uploadButton, compileMjjButton, interpretJjcButton,
                interpretMjjButton, closeOutputButton, generateMjjButton, generateJjcButton);

        // Create the scene (window content)
        Scene scene = new Scene(layout, 800, 600);

        // --- Keyboard Shortcuts ---
        // Ctrl+S for Windows / Linux users
        KeyCombination ctrlSave = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
        // Command+S for macOS users (META key = Command)
        KeyCombination cmdSave = new KeyCodeCombination(KeyCode.S, KeyCombination.META_DOWN);

        // Assign both shortcuts to trigger save functionality
        scene.getAccelerators().put(ctrlSave, () -> fileSaver.saveToFile(textArea, primaryStage));
        scene.getAccelerators().put(cmdSave, () -> fileSaver.saveToFile(textArea, primaryStage));

        // --- Window Configuration ---
        primaryStage.setTitle("MiniJaja Code Editor");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Creates the layout of the text editor window.
     *
     * @param saveButton         Button for saving files
     * @param uploadButton       Button for uploading files
     * @param interpretJjcButton
     * @return BorderPane containing the entire interface
     */
    private BorderPane createLayout(Button saveButton, Button uploadButton, Button compileMjjButton,
            Button interpretJjcButton, Button interpretMjjButton, Button closeOutputButton, Button generateMjjButton,
            Button generateJjcButton) {
        // HBox for horizontal arrangement of buttons
        HBox topBar1 = new HBox(10, saveButton, uploadButton, compileMjjButton, interpretJjcButton, interpretMjjButton,
                closeOutputButton);
        HBox topBar2 = new HBox(10, generateMjjButton, generateJjcButton);
        VBox topBar = new VBox(topBar1, topBar2);
        topBar1.setStyle("-fx-padding: 10; -fx-background-color: #f0f0f0;");
        topBar2.setStyle(
                "-fx-padding: 10;" +
                        "-fx-background-color: #f0f0f0;" +
                        "-fx-border-color: #d0d0d0 transparent transparent transparent;" +
                        "-fx-border-width: 1 0 0 0;");

        // BorderPane as main layout container
        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setCenter(splitPane);

        // Optional: styling for the text area
        textArea.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 14px;");
        outputArea.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 14px;");

        return root;
    }

    /**
     * Entry point for standalone execution.
     * This method launches the JavaFX application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
