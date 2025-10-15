package fr.amvproject.group3;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

public class FileSaver extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Save File Example");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save jaja File");

        // extention filter
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("Text files (*.mjj)", "*.mjj");
        fileChooser.getExtensionFilters().add(extFilter);

        // Open "Save As" dialog
        File file = fileChooser.showSaveDialog(primaryStage);
        if (file != null) {
            if (file.exists()) {
                // Ask the user whether to overwrite
                boolean overwrite = askOverwriteConfirmation(file.getName());
                if (!overwrite) {
                    showInfoDialog("Save Canceled", "The file was not overwritten.");
                    return;
                }
            }

            boolean success = saveTextToFile("Hello from MiniJaja!", file);
            if (success) {
                // Confirmation already shown in saveTextToFile
            }
        }
    }

    private boolean saveTextToFile(String content, File file) {
        try {
            // Check if parent directory exists and is writable
            File parentDir = file.getParentFile();
            if (parentDir == null || (!parentDir.exists() && !parentDir.mkdirs())) {
                showInfoDialog("Error", "The path is invalid or cannot create directories.");
                return false;
            }

            if (file.exists() && !file.canWrite()) {
                showInfoDialog("Error", "You don't have permission to write to this file.");
                return false;
            }

            // Optional: check disk space (approximation)
            long freeSpace = parentDir.getUsableSpace();
            if (freeSpace < content.length()) { // simple check in bytes
                showInfoDialog("Error", "Not enough disk space to save the file.");
                return false;
            }

            // Try writing to the file
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(content);
                System.out.println("File saved: " + file.getAbsolutePath());
            }

            showConfirmationDialog(file.getAbsolutePath());
            return true;

        } catch (IOException e) {
            showInfoDialog("Error", "Failed to save the file:\n" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private boolean askOverwriteConfirmation(String fileName) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("File Already Exists");
        alert.setHeaderText("The file \"" + fileName + "\" already exists.");
        alert.setContentText("Do you want to overwrite it?");

        ButtonType yes = new ButtonType("Yes");
        ButtonType no = new ButtonType("No");
        alert.getButtonTypes().setAll(yes, no);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == yes;
    }

    private void showConfirmationDialog(String filePath) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("File Saved");
        alert.setHeaderText("Success!");
        alert.setContentText("Your file has been saved to:\n" + filePath);
        alert.showAndWait();
    }

    private void showInfoDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
