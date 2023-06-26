package com.lertos.workaiotool.popups;

import com.lertos.workaiotool.model.Data;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TodoPopup {

    public static void display() {
        //Create the popup window
        Stage popupWindow = new Stage();

        popupWindow.initModality(Modality.APPLICATION_MODAL);

        //TODO: Cleanup and reorganize to have sections for each operation type on each control

        //Create the controls
        VBox layout = new VBox(10);

        GridPane gridPane = new GridPane();

        Text txtDisplayName = new Text("Display Text");
        Text txtAdditionalText = new Text("Additional Text");

        TextField tfDisplayName = new TextField();
        TextField tfAdditionalText = new TextField();

        //Setting the padding
        layout.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        //Setting the vertical and horizontal gaps between the columns
        gridPane.setVgap(5);
        gridPane.setHgap(5);

        //Setting the Grid alignment
        gridPane.setAlignment(Pos.CENTER);

        //Arranging all the nodes in the grid
        gridPane.add(txtDisplayName, 0, 0);
        gridPane.add(tfDisplayName, 1, 0);
        gridPane.add(txtAdditionalText, 0, 1);
        gridPane.add(tfAdditionalText, 1, 1);

        Button btnCancel = new Button("Cancel");
        Button btnSave = new Button("Save");

        HBox buttonHBox = new HBox(btnCancel, btnSave);

        buttonHBox.setSpacing(Data.getInstance().DEFAULT_BUTTON_SPACING);
        buttonHBox.setAlignment(Pos.CENTER);

        btnCancel.setOnAction(e -> popupWindow.close());

        layout.getChildren().addAll(gridPane, buttonHBox);

        layout.setAlignment(Pos.CENTER);

        //Create the new scene and show the popup
        Scene scene = new Scene(layout);

        popupWindow.setScene(scene);
        popupWindow.showAndWait();
    }
}
