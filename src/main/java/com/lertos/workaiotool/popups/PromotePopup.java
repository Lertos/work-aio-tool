package com.lertos.workaiotool.popups;

import com.lertos.workaiotool.model.Config;
import com.lertos.workaiotool.model.Data;
import com.lertos.workaiotool.model.items.PromoteItem;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
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
        new RadioButton(PromoteItem.TransferTypes.FILES_WITH_SAME_NAMES_DIFFERENT_PLACES.label).setToggleGroup(groupTransferType);
        new RadioButton(PromoteItem.TransferTypes.RAW_ABSOLUTE_PATHS.label).setToggleGroup(groupTransferType);

        //Create the new scene and show the popup
        Scene scene = new Scene(layout);

        popupWindow.setScene(scene);
        popupWindow.showAndWait();

        return updated;
    }
}
