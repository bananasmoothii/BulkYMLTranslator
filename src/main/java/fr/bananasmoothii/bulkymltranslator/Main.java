package fr.bananasmoothii.bulkymltranslator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Main extends Application {

    private Stage primaryStage;
    private Window window;
    protected Charset fileEncoding;
    protected StartController startController;
    public List<Keyword> keywords = new ArrayList<>();

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
                fileEncoding = Charset.forName(((String) startController.fileEncodingToggleGroup.getSelectedToggle().getUserData()));
                startController.fileEncoding.setText("File Encoding (" + fileEncoding.name() + ')');
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

    public static class Keyword {
        public Pattern replaceFrom;
        public String replaceTo;

        private @Nullable TextField replaceField;
        private @Nullable TextField byField;

        public Keyword(Pattern replaceFrom, String replaceTo) {
            this.replaceFrom = replaceFrom;
            this.replaceTo = replaceTo;
        }

        public Keyword(String replaceFrom, String replaceTo, boolean useRegex) throws PatternSyntaxException {
            this(useRegex ? Pattern.compile(replaceFrom) : Pattern.compile(Pattern.quote(replaceFrom)), replaceTo);
        }

        public FlowPane getDisplay() {
            FlowPane flowPane = new FlowPane();

            replaceField = new TextField();
            replaceField.setPromptText("Replace...");
            replaceField.setText(replaceFrom.toString());
            replaceField.setOnKeyTyped(event -> {
                replaceFrom = Pattern.compile(replaceField.getText());
                System.out.println("New replace: " + replaceField.getText());
            });
            flowPane.getChildren().add(replaceField);

            Label label = new Label(" ➡️ ");
            label.setFont(new Font(16d));
            flowPane.getChildren().add(label);

            byField = new TextField();
            byField.setPromptText("By...");
            byField.setText(replaceTo);
            byField.setOnKeyTyped(event -> {
                replaceTo = byField.getText();
                System.out.println("New by: " + byField.getText());
            });

            flowPane.getChildren().add(byField);
            return flowPane;
        }

        public @Nullable TextField getReplaceField() {
            return replaceField;
        }

        public @Nullable TextField getByField() {
            return byField;
        }
    }

    public Charset getFileEncoding() {
        return fileEncoding;
    }

    public void setFileEncoding(Charset fileEncoding) {
        this.fileEncoding = fileEncoding;
        if (! startController.fileEncodingToggleGroup.getSelectedToggle().getUserData().equals(fileEncoding.name())) {
            for (Toggle toggle : startController.fileEncodingToggleGroup.getToggles()) {
                if (toggle.getUserData().equals(fileEncoding.name()))
                    toggle.setSelected(true);
            }
        }
        startController.fileEncoding.setText("File Encoding (" + fileEncoding.name() + ')');
    }
}