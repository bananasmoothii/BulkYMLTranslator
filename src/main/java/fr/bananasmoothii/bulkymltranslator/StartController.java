package fr.bananasmoothii.bulkymltranslator;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.control.Menu;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StartController extends MainReference {

    public ToggleGroup fileEncodingToggleGroup = new ToggleGroup();
    public Menu fileEncoding;

    @FXML
    protected void open() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("."));
        directoryChooser.showDialog(main.getWindow());
    }

    @FXML
    protected void keywords() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("keywords.fxml"));
        Stage keywordsStage = new Stage();
        keywordsStage.setScene(new Scene(fxmlLoader.load()));
        MainReference.setFromLoader(fxmlLoader, main);
        keywordsStage.setTitle("Keywords");
        keywordsStage.show();
        ((KeywordsController) fxmlLoader.getController()).replaceField.requestFocus();
        VBox list = ((KeywordsController) fxmlLoader.getController()).list;
        for (Main.Keyword keyword : main.keywords) {
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