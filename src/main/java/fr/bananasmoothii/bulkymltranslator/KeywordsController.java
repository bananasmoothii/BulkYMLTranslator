package fr.bananasmoothii.bulkymltranslator;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import java.util.Optional;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class KeywordsController extends MainReference {

    @FXML protected TextField replaceField;
    @FXML protected TextField byField;
    @FXML protected CheckBox useRegex;
    @FXML protected VBox list;

    @FXML
    protected void addKeyword() {
        for (Keyword keyword : main.project.config.keywords) {
            if (keyword.replaceFrom.toString().equals(useRegex.isSelected() ?
                    replaceField.getText() :
                    Pattern.quote(replaceField.getText()))) {
                ButtonType showMeThat = new ButtonType("Show me that");
                Optional<ButtonType> buttonType = new BetterAlert(Alert.AlertType.ERROR, main.getWindow(), "This keyword already exists",
                        showMeThat, new ButtonType("Oh sure, I forgot")).showAndWait();
                if (buttonType.isPresent() && buttonType.get() == showMeThat) {
                    keyword.getReplaceField().requestFocus();
                }
                return;
            }
        }
        try {
            Keyword keyword = new Keyword(replaceField.getText(), byField.getText(), useRegex.isSelected());
            main.project.config.keywords.add(keyword);
            list.getChildren().add(keyword.getDisplay());
            replaceField.setText("");
            byField.setText("");
        } catch (PatternSyntaxException e) {
            new BetterAlert(Alert.AlertType.ERROR, main.getWindow(), "Invalid Regex").show();
        }
    }

    @FXML
    protected void keyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) { // Enter
            if (replaceField.isFocused()) byField.requestFocus();
            else if (byField.isFocused()) {
                addKeyword();
                replaceField.requestFocus();
            }
        }
    }
}
