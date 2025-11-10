package fr.amvproject.group3;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Handles loading a file into the text editor.
 * Includes verifications for:
 *  - file existence and readability
 *  - valid extension (.mj or .txt)
 *  - non-empty content
 *  - clear and safe error handling without crashing the app
 */
public class FileUpload {

    /**
     * Opens a file chooser dialog, verifies the selected file,
     * and loads its content into the editor if valid.
     *
     * @param textArea The TextArea where file content will be displayed
     * @param stage    The main JavaFX stage
     */
    public void loadFile(TextArea textArea, Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open MiniJaja File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MiniJaja Files", "*.mj", "*.mjj"),
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        File file = fileChooser.showOpenDialog(stage);
        if (file == null) {
            // User canceled file selection
            return;
        }

        // Step 1: Check file existence and permissions
        if (!file.exists()) {
            showErrorDialog("File Not Found", "The selected file does not exist.");
            return;
        }

        if (!file.canRead()) {
            showErrorDialog("Permission Denied", "You do not have permission to read this file.");
            return;
        }

        // Step 2: Check file extension (.mj, .mjj, .txt allowed)
        String fileName = file.getName().toLowerCase();
        if (!(fileName.endsWith(".mj") || fileName.endsWith(".mjj") || fileName.endsWith(".txt"))) {
            showErrorDialog("Invalid File Format",
                    "Unsupported file format.\nPlease select a .mj, .mjj or .txt file.");
            return;
        }

        // Step 3: Attempt to read file content safely
        try {
            String content = Files.readString(file.toPath());

            // Step 4: Check if file is empty
            if (content.trim().isEmpty()) {
                showWarningDialog("Empty File",
                        "The selected file is empty.\nNothing to load into the editor.");
                return;
            }

            // Step 5: Load content into the editor
            textArea.setText(content);
            showInfoDialog("File Loaded",
                    "File loaded successfully:\n" + file.getAbsolutePath());

        } catch (IOException e) {
            // Step 6: Handle read errors (file corrupted or unreadable)
            showErrorDialog("Read Error",
                    "An error occurred while reading the file:\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Shows an information dialog for normal feedback.
     */
    private void showInfoDialog(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Shows a warning dialog for non-critical issues (like empty files).
     */
    private void showWarningDialog(String title, String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText("Warning");
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Shows an error dialog for critical problems (invalid format, permissions, etc.).
     */
    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
