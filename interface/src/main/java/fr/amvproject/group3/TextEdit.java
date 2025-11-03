package fr.amvproject.group3;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * Main text editor window for the MiniJaja project.
 *
 * Features:
 *  - Text editor area for writing MiniJaja or JajaCode code.
 *  - Save button (saves content as .mjj file using FileSaver).
 *  - Upload button (loads .mjj or .txt files using FileUpload).
 *  - Keyboard shortcuts:
 *       Ctrl + S  → Windows / Linux
 *       Command + S → macOS
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
    private FileSaverJJC fileSaverJJC;
    private JajacodeInterpreterCaller jajacodeInterpreterCaller;

    @Override
    public void start(Stage primaryStage) {
        // Initialize components
        textArea = new TextArea();
        fileSaver = new FileSaver();
        fileUpload = new FileUpload();
        fileSaverJJC = new FileSaverJJC();
        jajacodeInterpreterCaller = new JajacodeInterpreterCaller();

        // Create buttons
        Button saveButton = new Button("Save");
        Button uploadButton = new Button("Upload");
        Button jajaCodeButton = new Button("Generate JajaCode");
        Button jajaCodeInterpretButton = new Button("Execute JajaCode");

        // --- Button Actions ---
        // Save the current content using FileSaver
        saveButton.setOnAction(e -> fileSaver.saveToFile(textArea, primaryStage));
        // Load file content using FileUpload
        uploadButton.setOnAction(e -> fileUpload.loadFile(textArea, primaryStage));

        jajaCodeButton.setOnAction(e -> fileSaverJJC.saveToFile(textArea, primaryStage));

        //jajaCodeInterpretButton.setOnAction(e ->  jajacodeInterpreterCaller.execute(textArea.getText()));

        // Build layout (top bar with buttons + text area in center)
        BorderPane layout = createLayout(saveButton, uploadButton,jajaCodeButton,jajaCodeInterpretButton);

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
     * @param saveButton              Button for saving files
     * @param uploadButton            Button for uploading files
     * @param jajaCodeInterpretButton
     * @return BorderPane containing the entire interface
     */
    private BorderPane createLayout(Button saveButton, Button uploadButton, Button jajaCodeButton, Button jajaCodeInterpretButton) {
        // HBox for horizontal arrangement of buttons
        HBox topBar = new HBox(10, saveButton, uploadButton, jajaCodeButton,jajaCodeInterpretButton);
        topBar.setStyle("-fx-padding: 10; -fx-background-color: #f0f0f0;");

        // BorderPane as main layout container
        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setCenter(textArea);

        // Optional: styling for the text area
        textArea.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 14px;");

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
