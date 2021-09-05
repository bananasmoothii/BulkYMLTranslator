package fr.bananasmoothii.bulkymltranslator;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Keyword {
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
