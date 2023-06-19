package com.lertos.workaiotool.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ControllerPromoter {

    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application from " + this.getClass().getSimpleName());
    }
}
