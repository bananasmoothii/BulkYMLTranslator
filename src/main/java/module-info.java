module fr.bananasmoothii.bulkymltranslator {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jetbrains.annotations;
    requires org.yaml.snakeyaml;


    opens fr.bananasmoothii.bulkymltranslator to javafx.fxml;
    exports fr.bananasmoothii.bulkymltranslator;
}