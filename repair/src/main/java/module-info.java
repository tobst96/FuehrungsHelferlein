module de.tobiobst.repair {
    requires javafx.controls;
    requires javafx.fxml;


    opens de.tobiobst.repair to javafx.fxml;
    exports de.tobiobst.repair;
}