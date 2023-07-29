package com.lertos.workaiotool.popups;

import com.lertos.workaiotool.Helper;
import com.lertos.workaiotool.model.Config;
import com.lertos.workaiotool.model.Data;
import com.lertos.workaiotool.model.items.CopyItem;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CopyPopup {

    private static boolean updated = false;

    public static boolean display(int itemIndex) {
        //Get the item info
        CopyItem item = null;

        if (itemIndex != -1 && itemIndex < Data.getInstance().copyItems.getActiveItems().size())
            item = Data.getInstance().copyItems.getActiveItems().get(itemIndex);

        //Create the popup window
        Stage popupWindow = new Stage();

        popupWindow.initModality(Modality.APPLICATION_MODAL);

        //Create the controls
        VBox layout = new VBox(Config.getInstance().DEFAULT_CONTROL_SPACING);

        GridPane gridPane = new GridPane();

        Text txtDisplayName = new Text("Display Text");
        Text txtTextToCopy = new Text("Text To Copy");

        TextField tfDisplayName = new TextField();
        TextArea taTextToCopy = new TextArea();

        Button btnCancel = new Button("Cancel");
        Button btnSave;

        if (item == null)
            btnSave = new Button("Add");
        else
            btnSave = new Button("Update");

        HBox buttonHBox = new HBox(btnCancel, btnSave);

        //Set the values of the text fields from the item info
        if (item != null) {
            tfDisplayName.setText(item.getDescription());
            taTextToCopy.setText(item.getTextToCopy());
        }

        //Add children
        layout.getChildren().addAll(gridPane, buttonHBox);

        //Arranging all the nodes in the grid
        gridPane.add(txtDisplayName, 0, 0);
        gridPane.add(tfDisplayName, 1, 0);
        gridPane.add(txtTextToCopy, 0, 1);
        gridPane.add(taTextToCopy, 1, 1);

        //Set the padding
        layout.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        //Set the vertical and horizontal gaps between the columns
        gridPane.setVgap(Config.getInstance().DEFAULT_GRID_PANE_SPACING);
        gridPane.setHgap(Config.getInstance().DEFAULT_GRID_PANE_SPACING);

        //Set the alignments
        layout.setAlignment(Pos.CENTER);
        gridPane.setAlignment(Pos.CENTER);
        buttonHBox.setAlignment(Pos.CENTER);

        GridPane.setHalignment(txtDisplayName, HPos.RIGHT);
        GridPane.setHalignment(txtTextToCopy, HPos.RIGHT);

        //Set other attributes
        taTextToCopy.setWrapText(true);
        taTextToCopy.setPrefRowCount(4);
        taTextToCopy.setPrefColumnCount(tfDisplayName.getPrefColumnCount() * 2);

        //Set spacing
        buttonHBox.setSpacing(Config.getInstance().DEFAULT_BUTTON_SPACING);

        //Set listeners
        btnCancel.setOnAction(e -> {
            updated = false;

            popupWindow.close();
        });

        btnSave.setOnAction(e -> {
            updated = true;

            //First check the validations
            if (tfDisplayName.getText().trim().isEmpty()) {
                Helper.showAlert("'Display Name' cannot be empty");
                return;
            }

            if (taTextToCopy.getText().trim().isEmpty()) {
                Helper.showAlert("'Text to Copy' cannot be empty");
                return;
            }

            //If adding a new item
            if (itemIndex == -1) {
                CopyItem newItem = new CopyItem(tfDisplayName.getText().trim(), taTextToCopy.getText().trim());

                Data.getInstance().copyItems.addItem(newItem);
            }
            //If updating an existing FolderItem
            else {
                Data.getInstance().copyItems.getActiveItems().get(itemIndex).setDescription(tfDisplayName.getText().trim());
                Data.getInstance().copyItems.getActiveItems().get(itemIndex).setTextToCopy(taTextToCopy.getText().trim());

                Data.getInstance().copyItems.saveFile();
            }

            popupWindow.close();
        });

        //Create the new scene and show the popup
        Scene scene = new Scene(layout);

        popupWindow.setScene(scene);
        popupWindow.showAndWait();

        return updated;
    }
}
