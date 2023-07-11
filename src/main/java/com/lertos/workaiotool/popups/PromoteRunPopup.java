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

import java.util.ArrayList;

public class PromoteRunPopup {

    private static PromoteItem itemToPromote;

    public static PromoteItem display(int itemIndex, boolean isOneOff) {
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
        Text txtPathTypes = new Text("Path Type");
        Text txtPromoteType = new Text("Promote Type");
        Text txtFilesToPromote = new Text("Files To Promote (1 per line)");
        Text txtOriginPaths = new Text("Origin Paths (1 per line)");
        Text txtDestinationPaths = new Text("Destination Paths (1 per line)");

        TextField tfDisplayName = new TextField();

        //Path Types
        ToggleGroup groupPathTypes = new ToggleGroup();
        VBox vboxPathTypes = getEnumRadioButtons(PromoteItem.PathTypes.values(), groupPathTypes);

        //Promote Type
        ToggleGroup groupPromoteType = new ToggleGroup();
        VBox vboxPromoteType = getEnumRadioButtons(PromoteItem.PromoteType.values(), groupPromoteType);

        //Text Areas
        TextArea taFilesToPromote = new TextArea();
        TextArea taOriginPaths = new TextArea();
        TextArea taDestinationPaths = new TextArea();

        //Buttons
        Button btnCancel = new Button("Cancel");
        Button btnPromote = new Button("Promote");

        HBox buttonHBox = new HBox(btnCancel, btnPromote);

        //Set the values of the text fields from the item info
        if (item != null) {
            tfDisplayName.setText(item.getDescription());

            groupPathTypes.selectToggle(groupPathTypes.getToggles().get(item.getPathType().ordinal()));
            groupPromoteType.selectToggle(groupPromoteType.getToggles().get(item.getPromoteType().ordinal()));

            taFilesToPromote.setText(getLinesAsString(item.getFileNames()));
            taOriginPaths.setText(getLinesAsString(item.getOriginPaths()));
            taDestinationPaths.setText(getLinesAsString(item.getDestinationPaths()));
        } else {
            //Set defaults
            groupPathTypes.selectToggle(groupPathTypes.getToggles().get(0));
            groupPromoteType.selectToggle(groupPromoteType.getToggles().get(0));
        }

        //Add children
        layout.getChildren().addAll(gridPane, buttonHBox);

        //Arranging all the nodes in the grid
        gridPane.add(txtDisplayName, 0, 0);
        gridPane.add(tfDisplayName, 1, 0);
        gridPane.add(txtPathTypes, 0, 1);
        gridPane.add(vboxPathTypes, 1, 1);
        gridPane.add(txtPromoteType, 0, 2);
        gridPane.add(vboxPromoteType, 1, 2);
        gridPane.add(txtFilesToPromote, 0, 3);
        gridPane.add(taFilesToPromote, 1, 3);
        gridPane.add(txtOriginPaths, 0, 4);
        gridPane.add(taOriginPaths, 1, 4);
        gridPane.add(txtDestinationPaths, 0, 5);
        gridPane.add(taDestinationPaths, 1, 5);

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
        GridPane.setHalignment(txtPathTypes, HPos.RIGHT);
        GridPane.setHalignment(txtPromoteType, HPos.RIGHT);
        GridPane.setHalignment(txtFilesToPromote, HPos.RIGHT);
        GridPane.setHalignment(txtOriginPaths, HPos.RIGHT);
        GridPane.setHalignment(txtDestinationPaths, HPos.RIGHT);

        //Set other attributes
        int prefColCount = tfDisplayName.getPrefColumnCount() * 2;
        int prefRowCount = 3;

        taFilesToPromote.setWrapText(true);
        taFilesToPromote.setPrefRowCount(prefRowCount);
        taFilesToPromote.setPrefColumnCount(prefColCount);

        taOriginPaths.setWrapText(true);
        taOriginPaths.setPrefRowCount(prefRowCount);
        taOriginPaths.setPrefColumnCount(prefColCount);

        taDestinationPaths.setWrapText(true);
        taDestinationPaths.setPrefRowCount(prefRowCount);
        taDestinationPaths.setPrefColumnCount(prefColCount);

        tfDisplayName.setDisable(true);

        //Set listeners
        btnCancel.setOnAction(e -> {
            popupWindow.close();
        });

        btnPromote.setOnAction(e -> {
            PromoteItem.PathTypes pathTypes = getEnumFromText(PromoteItem.PathTypes.values(), ((RadioButton) groupPathTypes.getSelectedToggle()).getText());
            PromoteItem.PromoteType promoteType = getEnumFromText(PromoteItem.PromoteType.values(), ((RadioButton) groupPromoteType.getSelectedToggle()).getText());

            itemToPromote = new PromoteItem(tfDisplayName.getText().trim(), pathTypes, promoteType);

            addLinesToList(true, itemToPromote.getFileNames(), taFilesToPromote.getText());
            addLinesToList(true, itemToPromote.getOriginPaths(), taOriginPaths.getText());
            addLinesToList(true, itemToPromote.getDestinationPaths(), taDestinationPaths.getText());

            popupWindow.close();
        });

        //Set spacing
        buttonHBox.setSpacing(Config.getInstance().DEFAULT_BUTTON_SPACING);

        //Create the new scene and show the popup
        Scene scene = new Scene(layout);

        popupWindow.setScene(scene);
        popupWindow.showAndWait();

        return itemToPromote;
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

    private static <T extends Enum & PromoteItem.EnumWithLabel> T getEnumFromText(T[] enumValues, String text) {
        for (T e : enumValues) {
            if (e.getLabel().equalsIgnoreCase(text))
                return e;
        }
        return null;
    }

    private static String getLinesAsString(ArrayList<String> lines) {
        StringBuilder sb = new StringBuilder();

        for (String line : lines)
            sb.append(line).append('\n');
        return sb.toString();
    }

    private static void addLinesToList(boolean clearListFirst, ArrayList<String> list, String rawText) {
        String[] lines = rawText.split("\n");

        if (clearListFirst)
            list.clear();

        for (String line : lines)
            list.add(line);
    }
}
