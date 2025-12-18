package fr.amvproject.group3;

import java.io.File;

import fr.amvproject.group3.settings.Settings;
import fr.amvproject.group3.ui.ide.IdeView;
import fr.amvproject.group3.utils.CssManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class AppFx extends Application {

  @Override
  public void start(Stage stage) {
    CssManager.generate("gui/src/main/resources/fr/amvproject/group3/css/style.css");
    IdeView ideView = new IdeView(stage);

    String javaVersion = System.getProperty("java.version");
    String javafxVersion = System.getProperty("javafx.version");
    Label l = new Label("App, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
    
    BorderPane root = new BorderPane();
    root.setBottom(l);
    Scene scene = new Scene(ideView, Settings.SCENE_WIDTH, Settings.SCENE_HEIGHT);
    scene.getStylesheets().add(
      getClass().getResource("/fr/amvproject/group3/css/style.css").toExternalForm()
    );

    scene.setOnKeyPressed(e -> {
        if (e.getCode().toString().equals("F5")) {
            String cssUrl = new File("gui/src/main/resources/fr/amvproject/group3/css/style.css").toURI().toString();

            System.out.println("Reloading CSS...");
            scene.getStylesheets().clear();
            scene.getStylesheets().add(cssUrl);
        }
    });

    stage.setScene(scene);
    stage.setTitle(javafxVersion);
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }

}