package fr.bananasmoothii.bulkymltranslator;

import javafx.fxml.FXMLLoader;
import org.jetbrains.annotations.NotNull;

public class MainReference {
    protected Main main;

    public static void setFromLoader(@NotNull FXMLLoader loader, Main main) {
        ((MainReference) loader.getController()).main = main;
    }
}
