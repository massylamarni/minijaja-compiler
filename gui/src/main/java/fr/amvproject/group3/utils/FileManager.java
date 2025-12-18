package fr.amvproject.group3.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

import org.fxmisc.richtext.CodeArea;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FileManager {
  private static boolean askOverwriteConfirmation(String fileName) {
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

  private static void showInfoDialog(String title, String message, AlertType alertType) {
    Alert alert = new Alert(alertType);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  public static void saveFile(CodeArea codeArea, Stage stage) {
    String content = codeArea.getText();

    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save MiniJaja File");

    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("MiniJaja files (*.mjj)", "*.mjj");
    fileChooser.getExtensionFilters().add(extFilter);

    File file = fileChooser.showSaveDialog(stage);

    if (file != null) {
      if (!file.getName().endsWith(".mjj")) {
        file = new File(file.getParentFile(), file.getName() + ".mjj");
      }

      if (file.exists() && !askOverwriteConfirmation(file.getName())) {
        showInfoDialog("Save Canceled", "The file was not overwritten.", AlertType.INFORMATION);
        return;
      }

      try (FileWriter writer = new FileWriter(file)) {
        writer.write(content);
        System.out.println("File saved: " + file.getAbsolutePath());
      } catch (IOException e) {
        showInfoDialog("Error", "Failed to save the file:\n" + e.getMessage(), AlertType.ERROR);
      }
    }
  }

  public static String loadFile(CodeArea codeArea, Stage stage) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open MiniJaja File");
    fileChooser.getExtensionFilters().addAll(
        new FileChooser.ExtensionFilter("MiniJaja Files", "*.mjj"),
        new FileChooser.ExtensionFilter("Text Files", "*.txt"),
        new FileChooser.ExtensionFilter("All Files", "*.*"));

    File file = fileChooser.showOpenDialog(stage);
    if (file == null) {
      return "";
    }

    if (!file.exists()) {
      showInfoDialog("File Not Found", "The selected file does not exist.", AlertType.ERROR);
      return "";
    }

    if (!file.canRead()) {
      showInfoDialog("Permission Denied", "You do not have permission to read this file.", AlertType.ERROR);
      return "";
    }

    try {
      String content = Files.readString(file.toPath());

      if (content.trim().isEmpty()) {
        System.out.println("Loaded file was empty !");
        return "";
      }

      codeArea.replaceText(content);
      showInfoDialog("File Loaded",
          "File loaded successfully:\n" + file.getAbsolutePath(), AlertType.INFORMATION);
      return content;
    } catch (IOException e) {
      showInfoDialog("Read Error",
          "An error occurred while reading the file:\n" + e.getMessage(), AlertType.ERROR);
      return "";
    }
  }

}