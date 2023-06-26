package com.lertos.workaiotool;

import com.lertos.workaiotool.model.Data;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class Helper {

    //Uses the default spacing
    public static HBox createTextFieldWithLabel(String labelText) {
        HBox hBox = new HBox();
        Label label = new Label(labelText);
        TextField textField = new TextField();

        hBox.getChildren().addAll(label, textField);
        hBox.setSpacing(Data.getInstance().DEFAULT_CONTROL_SPACING);

        return hBox;
    }

    //Uses a spacing desired by the user
    public static HBox createTextFieldWithLabel(String labelText, double spacing) {
        HBox hBox = new HBox();
        Label label = new Label(labelText);
        TextField textField = new TextField();

        hBox.getChildren().addAll(label, textField);
        hBox.setSpacing(spacing);

        return hBox;
    }

    public static void showAlert(String text) {
        new Alert(Alert.AlertType.NONE, text, ButtonType.OK).showAndWait();
    }

}
