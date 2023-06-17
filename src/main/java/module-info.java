module com.lertos.workaiotool {
    requires javafx.controls;
    requires javafx.fxml;
            
        requires org.controlsfx.controls;
                            
    opens com.lertos.workaiotool to javafx.fxml;
    exports com.lertos.workaiotool;
}