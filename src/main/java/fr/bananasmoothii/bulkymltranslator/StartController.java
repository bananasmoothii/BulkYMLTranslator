package fr.bananasmoothii.bulkymltranslator;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Menu;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class StartController extends MainReference {

    public ToggleGroup fileEncodingToggleGroup = new ToggleGroup();
    public Menu fileEncoding;
    public BorderPane start;
    public BorderPane blacklists;
    public ChoiceBox<TranslationNode.Language> translateFrom;
    public ChoiceBox<TranslationNode.Language> translateTo;

    @FXML
    protected void open() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("."));
        File directory = directoryChooser.showDialog(main.getWindow());
        if (directory == null) return;

        main.project = new Project(directory, main);

        slide(start, blacklists);
    }

    protected static void slide(@NotNull Region from, @NotNull Region to) {
        double width = from.getScene().getWidth();
        to.setTranslateX(width);
        to.setVisible(true);
        Timeline timeline1 = new Timeline();
        Timeline timeline2 = new Timeline();
        KeyValue kv1 = new KeyValue(from.translateXProperty(), -width, Interpolator.EASE_BOTH);
        KeyValue kv2 = new KeyValue(to.translateXProperty(), 0, Interpolator.EASE_BOTH);
        KeyFrame kf1 = new KeyFrame(Duration.millis(500d), kv1);
        KeyFrame kf2 = new KeyFrame(Duration.millis(500d), kv2);
        timeline1.getKeyFrames().add(kf1);
        timeline2.getKeyFrames().add(kf2);
        timeline1.setOnFinished(event -> from.setVisible(false));
        timeline1.play();
        timeline2.play();
    }

    @FXML
    protected void keywords() throws IOException {
        if (main.project == null) {
            new BetterAlert(Alert.AlertType.WARNING, main.getWindow(), "Please start or open a project before using keywords").show();
            return;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("keywords.fxml"));
        Stage keywordsStage = new Stage();
        keywordsStage.setScene(new Scene(fxmlLoader.load()));
        MainReference.setFromLoader(fxmlLoader, main);
        keywordsStage.setTitle("Keywords");
        keywordsStage.show();
        ((KeywordsController) fxmlLoader.getController()).replaceField.requestFocus();
        VBox list = ((KeywordsController) fxmlLoader.getController()).list;
        for (Keyword keyword : main.project.config.keywords) {
            list.getChildren().add(keyword.getDisplay());
        }
    }

    public void keywordsHelp() {
        new BetterAlert(Alert.AlertType.INFORMATION, main.getWindow(), """
                A keyword is a regex that will be used to replace words or sentences that you see very often.
                For example, if you want to translate "XXX artifact" to "artefact de XXX" (XXX can be anything), you
                can set this keyword:
                (\\w+) artifact -> artefact de $1
                You can use regex groups for plural checking, for example:
                artifact(s?) -> artefact$1
                will replace 'artifact' by 'artefact' and 'artifacts' by 'artefacts'
                If you want to write a dollar sign in the "by" field, try with two dollar '$$' signs otherwise it will
                be analysed like a group reference, same for backslashes.
                """).show();
    }
}