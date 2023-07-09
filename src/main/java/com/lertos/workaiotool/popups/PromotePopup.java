package com.lertos.workaiotool.popups;

import com.lertos.workaiotool.model.Config;
import com.lertos.workaiotool.model.Data;
import com.lertos.workaiotool.model.items.PromoteItem;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PromotePopup {

    private static boolean updated = false;

    public static boolean display(int itemIndex) {
        //Get the item info
        PromoteItem item = null;

        if (itemIndex != -1 && itemIndex < Data.getInstance().promoteItems.getActiveItems().size())
            item = Data.getInstance().promoteItems.getActiveItems().get(itemIndex);

        //Create the popup window
        Stage popupWindow = new Stage();

        popupWindow.initModality(Modality.APPLICATION_MODAL);

        //Create the controls
        VBox layout = new VBox(Config.getInstance().DEFAULT_CONTROL_SPACING);

        GridPane gridPane = new GridPane();

        Text txtDisplayName = new Text("Display Text");
        Text txtTransferType = new Text("Transfer Type");
        Text txtPathTypes = new Text("Path Type");
        Text txtPromoteType = new Text("Promote Type");
        Text txtFilesToPromote = new Text("Files To Promote");
        Text txtOriginPaths = new Text("Origin Paths");
        Text txtDestinationPaths = new Text("Destination Paths");

        TextField tfDisplayName = new TextField();

        //Transfer Type
        ToggleGroup groupTransferType = new ToggleGroup();
        VBox vboxTransferType = getEnumRadioButtons(PromoteItem.TransferTypes.values(), groupTransferType);

        //Path Types
        ToggleGroup groupPathTypes = new ToggleGroup();
        VBox vboxPathTypes = getEnumRadioButtons(PromoteItem.PathTypes.values(), groupPathTypes);

        //Promote Type
        ToggleGroup groupPromoteType = new ToggleGroup();
        VBox vboxPromoteType = getEnumRadioButtons(PromoteItem.PromoteType.values(), groupPromoteType);

        //
        TextArea taTextToCopy = new TextArea();

        //Buttons
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
            groupTransferType.selectToggle(groupTransferType.getToggles().get(item.getTransferType().ordinal()));
        }

        //Add children
        layout.getChildren().addAll(gridPane, buttonHBox);

        //Arranging all the nodes in the grid
        gridPane.add(txtDisplayName, 0, 0);
        gridPane.add(tfDisplayName, 1, 0);
        gridPane.add(txtTransferType, 0, 1);
        gridPane.add(vboxTransferType, 1, 1);
        gridPane.add(txtPathTypes, 0, 2);
        gridPane.add(vboxPathTypes, 1, 2);
        gridPane.add(txtPromoteType, 0, 3);
        gridPane.add(vboxPromoteType, 1, 3);

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
        GridPane.setHalignment(txtTransferType, HPos.RIGHT);
        GridPane.setHalignment(txtPathTypes, HPos.RIGHT);
        GridPane.setHalignment(txtPromoteType, HPos.RIGHT);
        GridPane.setHalignment(txtFilesToPromote, HPos.RIGHT);
        GridPane.setHalignment(txtOriginPaths, HPos.RIGHT);
        GridPane.setHalignment(txtDestinationPaths, HPos.RIGHT);

        //Set other attributes
        taTextToCopy.setWrapText(true);
        taTextToCopy.setPrefRowCount(4);
        taTextToCopy.setPrefColumnCount(tfDisplayName.getPrefColumnCount() * 2);

        //Set spacing
        buttonHBox.setSpacing(Config.getInstance().DEFAULT_BUTTON_SPACING);

        //Create the new scene and show the popup
        Scene scene = new Scene(layout);

        popupWindow.setScene(scene);
        popupWindow.showAndWait();

        return updated;
    }

    private static <T extends Enum & PromoteItem.EnumWithLabel> VBox getEnumRadioButtons(T[] enumValues, ToggleGroup toggleGroup) {
        VBox vbox = new VBox();

        for (T e : enumValues) {
            RadioButton rb = new RadioButton(e.getLabel());
            rb.setToggleGroup(toggleGroup);
            vbox.getChildren().add(rb);
        }
        return vbox;
    }
}
