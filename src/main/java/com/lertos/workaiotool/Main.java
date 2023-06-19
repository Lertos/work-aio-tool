package com.lertos.workaiotool;

import com.lertos.workaiotool.controllers.ControllerMain;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends javafx.application.Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view-tab-pane.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 500);

        ControllerMain controllerMain = fxmlLoader.getController();
        controllerMain.setListeners(scene);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}