package com.lertos.workaiotool.popups;

import com.lertos.workaiotool.Helper;
import com.lertos.workaiotool.model.Config;
import com.lertos.workaiotool.model.Data;
import com.lertos.workaiotool.model.items.FolderItem;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;

public class FolderPopup {

    private static boolean updated = false;

    public static boolean display(int itemIndex) {
        //Get the item info
        FolderItem item = null;

        if (itemIndex != -1 && itemIndex < Data.getInstance().folderItems.getActiveItems().size())
            item = Data.getInstance().folderItems.getActiveItems().get(itemIndex);

        //Create the popup window
        Stage popupWindow = new Stage();

        popupWindow.initModality(Modality.APPLICATION_MODAL);

        //Create the controls
        VBox layout = new VBox(Config.getInstance().DEFAULT_CONTROL_SPACING);

        GridPane gridPane = new GridPane();

        Text txtDisplayName = new Text("Display Text");
        Text txtPathToOpen = new Text("Path to Open");
        Text txtCheckPathExists = new Text("Check Path Exists");

        TextField tfDisplayName = new TextField();
        TextField tfPathToOpen = new TextField();

        CheckBox cbCheckPathExists = new CheckBox();

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
            tfPathToOpen.setText(item.getPathToOpen());
        }

        //Add children
        layout.getChildren().addAll(gridPane, buttonHBox);

        //Arranging all the nodes in the grid
        gridPane.add(txtDisplayName, 0, 0);
        gridPane.add(tfDisplayName, 1, 0);
        gridPane.add(txtPathToOpen, 0, 1);
        gridPane.add(tfPathToOpen, 1, 1);
        gridPane.add(txtCheckPathExists, 0, 2);
        gridPane.add(cbCheckPathExists, 1, 2);

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
        GridPane.setHalignment(txtPathToOpen, HPos.RIGHT);

        //Set other attributes
        cbCheckPathExists.setSelected(true); //Default=ON

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

            if (tfPathToOpen.getText().trim().isEmpty()) {
                Helper.showAlert("'Path to Open' cannot be empty");
                return;
            }

            //If the checkbox is checked, check to make sure the path exists before saving
            if (cbCheckPathExists.isSelected()) {
                File f = new File(tfPathToOpen.getText().trim());

                if (!f.isDirectory()) {
                    Helper.showAlert("The given path does not exist");
                    return;
                }
            }

            //If adding a new item
            if (itemIndex == -1) {
                FolderItem newItem = new FolderItem(tfDisplayName.getText().trim(), tfPathToOpen.getText().trim());

                Data.getInstance().folderItems.getActiveItems().add(newItem);
            }
            //If updating an existing FolderItem
            else {
                Data.getInstance().folderItems.getActiveItems().get(itemIndex).setDescription(tfDisplayName.getText().trim());
                Data.getInstance().folderItems.getActiveItems().get(itemIndex).setPathToOpen(tfPathToOpen.getText().trim());
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
