package com.lertos.workaiotool.controllers;

import com.lertos.workaiotool.popups.PromotePopup;
import javafx.fxml.FXML;

public class ControllerPromoter {

    @FXML
    protected void onHelloButtonClick() {
        PromotePopup.display(0);
    }
}
