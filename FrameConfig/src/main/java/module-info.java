module de.tobiobst.frameconfig {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires json.simple;


    opens de.tobiobst.frameconfig to javafx.fxml;
    exports de.tobiobst.frameconfig;
}