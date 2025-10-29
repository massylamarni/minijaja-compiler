package fr.amvproject.group3.utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class StringSaver {
  private StringSaver() {}
  private static final Logger LOGGER = Logger.getLogger(StringSaver.class.getName());
  
  public static void save(String str, String fileName) {
    Path dir = Paths.get("analyser-miniJaja", "target", "output");
    try {
      Files.createDirectories(dir);
      Path filePath = dir.resolve(fileName);
      try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
        writer.write(str);
        LOGGER.info(() -> "File saved successfully to " + filePath.toAbsolutePath());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
