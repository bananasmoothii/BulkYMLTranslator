package fr.bananasmoothii.bulkymltranslator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.RadioMenuItem;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.charset.Charset;

public class Main extends Application {

    private Stage primaryStage;
    private Window window;
    protected @Nullable Project project;
    protected StartController startController;
    protected String preferredFileEncoding;

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("start.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        startController = fxmlLoader.getController();
        startController.main = this;
        for (String encoding : Charset.availableCharsets().keySet()) {
            RadioMenuItem item = new RadioMenuItem(encoding);
            item.setToggleGroup(startController.fileEncodingToggleGroup);
            startController.fileEncoding.getItems().add(item);
            if (encoding.equals("UTF-8")) item.setSelected(true);
            item.setUserData(encoding);
            item.setOnAction(event -> {
                String newEncoding = (String) startController.fileEncodingToggleGroup.getSelectedToggle().getUserData();
                if (! Charset.isSupported(newEncoding)) throw new IllegalArgumentException("Invalid file encoding: " + newEncoding);
                preferredFileEncoding = newEncoding;
                startController.fileEncoding.setText("File Encoding (" + preferredFileEncoding + ')');
                if (project != null) {
                    project.config.fileEncoding = preferredFileEncoding;
                }
            });
        }
        window = scene.getWindow();
        primaryStage.setTitle("Bulk YML Translator 1.0");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public Window getWindow() {
        return window;
    }
}