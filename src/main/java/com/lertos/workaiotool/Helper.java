package com.lertos.workaiotool;

import com.lertos.workaiotool.model.Data;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.robot.Robot;

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

    //Displays a simple alert with the given text parameter as a string
    public static void showAlert(String text) {
        new Alert(Alert.AlertType.NONE, text, ButtonType.OK).showAndWait();
    }

    //To allow a control to consume the tab event and instead use a Robot to navigate to the next control
    public static EventHandler<KeyEvent> consumeTabEventForControl() {
        return new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode code = event.getCode();

                if (code == KeyCode.TAB && !event.isShiftDown() && !event.isControlDown()) {
                    event.consume();
                    try {
                        Robot robot = new Robot();
                        robot.keyPress(KeyCode.CONTROL);
                        robot.keyPress(KeyCode.TAB);
                        robot.keyRelease(KeyCode.TAB);
                        robot.keyRelease(KeyCode.CONTROL);
                    } catch (Exception e) {
                    }
                }
            }
        };
    }

}
