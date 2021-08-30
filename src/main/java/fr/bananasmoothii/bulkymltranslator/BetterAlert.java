package fr.bananasmoothii.bulkymltranslator;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;

public class BetterAlert extends Alert {

    public BetterAlert(AlertType alertType, Window owner) {
        super(alertType);
        initOwner(owner);
        reformat();
    }

    public BetterAlert(AlertType alertType, Window owner, String contentText, ButtonType... buttons) {
        super(alertType, contentText, buttons);
        initOwner(owner);
        reformat();
    }

    private void reformat() {
        setHeaderText(null);
        setTitle(switch (getAlertType()) {
            case INFORMATION -> "Information";
            case WARNING -> "Warning";
            case CONFIRMATION -> "Confirmation";
            case ERROR -> "Error";
            default -> "Alert";
        });
    }
}
