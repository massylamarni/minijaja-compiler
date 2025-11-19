package fr.amvproject.group3;

import fr.amvproject.group3.jjc.ast.PrintVisitor;
import fr.amvproject.group3.jjc.ast.nodes.program.ProgramNode;
import fr.amvproject.group3.jjc.utils.NodeOperations;
import fr.amvproject.group3.mjj.utils.AstOperations;
import fr.amvproject.group3.mjj.ast.AstNode;
import fr.amvproject.group3.mjj.ast.AstBuilderVisitor;
import fr.amvproject.group3.utils.StringSaver;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.antlr.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

/**
 * Handles saving text content from the editor into a .mjj file.
 * Includes safety checks such as:
 *  - existing file confirmation,
 *  - disk space and permission validation,
 *  - empty editor check,
 *  - informative dialogs for user feedback.
 */
public class FileSaverJJC {

    /**
     * Opens a "Save File" dialog and writes the content of the TextArea into a .mjj file.
     * This is invoked by the Save button or the Ctrl+S shortcut in TextEdit.java.
     *
     * @param textArea The TextArea containing the text to be saved
     * @param stage    The current JavaFX window stage
     */
    public void saveToFile(TextArea textArea, Stage stage) {
        String content = textArea.getText();

        // 🛑 Step 1: Check if text editor is empty before proceeding
        if (content == null || content.trim().isEmpty()) {
            showInfoDialog("Empty File", "Cannot save an empty file.\nPlease write something first.");
            return;
        }

        // Step 2: Create a file chooser dialog
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save MiniJaja File");

        // Restrict to .mjj files
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("JajaCode files (*.jjc)", "*.jjc");
        fileChooser.getExtensionFilters().add(extFilter);

        // Step 3: Open the dialog
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            // Step 4: If file already exists → ask for confirmation
            if (file.exists()) {
                boolean overwrite = askOverwriteConfirmation(file.getName());
                if (!overwrite) {
                    showInfoDialog("Save Canceled", "The file was not overwritten.");
                    return;
                }
            }

            // Step 5: Save content into file
            content = compilation(content);
            boolean success = saveTextToFile(content, file);
            if (success) {
                showConfirmationDialog(file.getAbsolutePath());
            }
        }
    }

    public String compilation(String content) {
        AstNode ast = AstOperations.getAstFromString(content);

        CompilerVisitor compilerVisitor = new CompilerVisitor();
        ProgramNode programNode = new ProgramNode(ast.accept(compilerVisitor), new NodeOperations.MockToken(null, 0, 0));

        PrintVisitor printVisitor = new PrintVisitor();

        return programNode.accept(printVisitor);
    }

    public String interpret(String content) {
        AstNode ast = AstOperations.getAstFromString(content);

        InterpreterVisitor interpreterVisitor = new InterpreterVisitor();
        ast.accept(interpreterVisitor);

        return interpreterVisitor.getStack().print();
    }

    /**
     * Attempts to save text content into the specified file,
     * performing pre-save safety checks.
     *
     * @param content The text to write into the file
     * @param file    The destination file
     * @return true if saved successfully, false otherwise
     */
    private boolean saveTextToFile(String content, File file) {
        try {
            // Ensure the parent directory exists or can be created
            File parentDir = file.getParentFile();
            if (parentDir == null || (!parentDir.exists() && !parentDir.mkdirs())) {
                showInfoDialog("Error", "The path is invalid or cannot create directories.");
                return false;
            }

            // Check write permission
            if (file.exists() && !file.canWrite()) {
                showInfoDialog("Error", "You don't have permission to write to this file.");
                return false;
            }

            // Approximate disk space check
            long freeSpace = parentDir.getUsableSpace();
            if (freeSpace < content.length()) {
                showInfoDialog("Error", "Not enough disk space to save the file.");
                return false;
            }

            // Write text into file
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(content);
                System.out.println("File saved: " + file.getAbsolutePath());
            }

            return true;

        } catch (IOException e) {
            showInfoDialog("Error", "Failed to save the file:\n" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Displays a confirmation dialog if the file already exists.
     *
     * @param fileName The name of the file to overwrite
     * @return true if the user agrees to overwrite, false otherwise
     */
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

    /**
     * Shows a confirmation dialog when saving succeeds.
     *
     * @param filePath The path to the saved file
     */
    private void showConfirmationDialog(String filePath) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("File Saved");
        alert.setHeaderText("Success!");
        alert.setContentText("Your file has been saved to:\n" + filePath);
        alert.showAndWait();
    }

    /**
     * Shows an informational dialog for general errors or user messages.
     *
     * @param title   The title of the dialog
     * @param message The message to display
     */
    private void showInfoDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
