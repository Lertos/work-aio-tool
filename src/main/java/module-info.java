module com.lertos.workaiotool {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.lertos.workaiotool to javafx.fxml;
    exports com.lertos.workaiotool;
    exports com.lertos.workaiotool.controllers;
    opens com.lertos.workaiotool.controllers to javafx.fxml;
}