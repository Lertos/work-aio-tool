package com.lertos.workaiotool.popups;

import com.lertos.workaiotool.Helper;
import com.lertos.workaiotool.model.Config;
import com.lertos.workaiotool.model.Data;
import com.lertos.workaiotool.model.ItemSQL;
import com.lertos.workaiotool.model.items.SQLCompareItem;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

public class SQLCompareRunPopup {

    private static final int INDEX_HOST_FIELD = 0;
    private static final int INDEX_DATABASES_FIELD = 1;

    private static SQLCompareItem sqlCompareItemToReturn;
    private static TabPane tabPane;
    private static int prefColCount;

    public static SQLCompareItem display(int itemIndex) {
        if (itemIndex == -1)
            return null;

        //Get the item info
        SQLCompareItem item = Data.getInstance().sqlCompareItems.getActiveItems().get(itemIndex);

        if (item == null)
            return null;

        //Create the popup window
        Stage popupWindow = new Stage();

        popupWindow.initModality(Modality.APPLICATION_MODAL);

        //Create the parent to hold all controls
        VBox layout = new VBox(Config.getInstance().DEFAULT_CONTROL_SPACING);

        //Create the tab pane to hold all the server configurations
        tabPane = new TabPane();
        tabPane.setPrefSize(350, 200);

        //Top section controls
        GridPane gridPane = new GridPane();

        Text txtDisplayName = new Text("Display Text");
        Text txtProcedureName = new Text("Procedure Name");

        TextField tfDisplayName = new TextField();
        TextField tfProcedureName = new TextField();

        tfDisplayName.setDisable(true);

        //Set up buttons
        Button btnCancel = new Button("Cancel");
        Button btnCompare = new Button("Compare");

        HBox buttonHBox = new HBox(btnCancel, btnCompare);

        //Set the values of the text fields from the item info
        tfDisplayName.setText(item.getDescription());
        tfProcedureName.setText(item.getProcedureName());

        //Arranging all the nodes in the grid
        gridPane.add(txtDisplayName, 0, 0);
        gridPane.add(tfDisplayName, 1, 0);
        gridPane.add(txtProcedureName, 0, 1);
        gridPane.add(tfProcedureName, 1, 1);

        //Separator to split the top section from the tab pane with all the server set up
        Separator separator = new Separator(Orientation.HORIZONTAL);

        //Add children
        layout.getChildren().addAll(gridPane, separator, tabPane, buttonHBox);

        //Set the padding
        layout.setPadding(new Insets(Config.getInstance().DEFAULT_CONTROL_SPACING, Config.getInstance().DEFAULT_CONTROL_SPACING, Config.getInstance().DEFAULT_CONTROL_SPACING, Config.getInstance().DEFAULT_CONTROL_SPACING));
        gridPane.setPadding(new Insets(Config.getInstance().DEFAULT_CONTROL_SPACING, Config.getInstance().DEFAULT_CONTROL_SPACING, Config.getInstance().DEFAULT_CONTROL_SPACING, Config.getInstance().DEFAULT_CONTROL_SPACING));

        //Set the vertical and horizontal gaps between the columns
        gridPane.setVgap(Config.getInstance().DEFAULT_GRID_PANE_SPACING);
        gridPane.setHgap(Config.getInstance().DEFAULT_GRID_PANE_SPACING);

        //Set the alignments
        layout.setAlignment(Pos.CENTER);
        gridPane.setAlignment(Pos.CENTER);
        buttonHBox.setAlignment(Pos.CENTER);

        GridPane.setHalignment(txtDisplayName, HPos.RIGHT);
        GridPane.setHalignment(txtProcedureName, HPos.RIGHT);

        //Set spacing
        buttonHBox.setSpacing(Config.getInstance().DEFAULT_BUTTON_SPACING);

        //Set other attributes
        prefColCount = tfDisplayName.getPrefColumnCount() * 2;

        //Create each tab for each SQLItem
        for (ItemSQL itemSQL : item.getItemsSQL()) {
            GridPane newTabPane = createServerTab(itemSQL.getTabName());

            try {
                TextField tfHost = (TextField) getGridInputField(newTabPane, INDEX_HOST_FIELD);
                TextArea taDatabases = (TextArea) getGridInputField(newTabPane, INDEX_DATABASES_FIELD);

                tfHost.setDisable(true);

                tfHost.setText(itemSQL.getHost());
                taDatabases.setText(Helper.getLinesAsString(itemSQL.getDatabaseNames()));
            } catch (ClassCastException e) {
                Helper.showAlert("The creation of the tab is incorrect. Please consult the developer.");
            }
        }

        //Set listeners
        btnCancel.setOnAction(e -> {
            popupWindow.close();
        });

        btnCompare.setOnAction(e -> {
            String procedureName = tfProcedureName.getText().trim();

            //First check the validations
            if (procedureName.isEmpty()) {
                Helper.showAlert("'Procedure Name' cannot be empty");
                return;
            }

            ArrayList<ItemSQL> newItemSQLList = new ArrayList<>();

            //Validate each tab to make sure they pass all validations
            for (Tab tab : tabPane.getTabs()) {
                GridPane tabGridPane = (GridPane) tab.getContent();
                String tabName = tab.getText().trim();

                TextArea taDatabases = (TextArea) getGridInputField(tabGridPane, INDEX_DATABASES_FIELD);

                if (taDatabases.getText().trim().isEmpty()) {
                    Helper.showAlert("Tab: [" + tabName + "] - the 'databases' field is empty");
                    return;
                }

                //Find the existing ItemSQL based on the tab name, and then update its values
                for (ItemSQL itemSQL : item.getItemsSQL()) {
                    if (itemSQL.getTabName().equalsIgnoreCase(tabName)) {
                        ArrayList<String> databases = Helper.getLinesToList(taDatabases.getText().trim());

                        newItemSQLList.add(new ItemSQL(tabName, itemSQL.getHost(), itemSQL.getPort(), itemSQL.getUsername(), itemSQL.getPassword(), itemSQL.usesIntegratedSecurity(), databases));
                        break;
                    }
                }
            }

            //Create a new item as a copy of the existing item and update the procedure name and databases
            sqlCompareItemToReturn = new SQLCompareItem(item.getDescription(), procedureName, item.getSqlType(), newItemSQLList);

            popupWindow.close();
        });

        //Create the new scene and show the popup
        Scene scene = new Scene(layout);

        popupWindow.setScene(scene);
        popupWindow.showAndWait();

        return sqlCompareItemToReturn;
    }

    //The math of the get() is because the grid pane has the Text node in the first column, then the Input node in the second,
    //so we need to double each index and subtract 1 for zero-based
    private static Node getGridInputField(GridPane gridPane, int fieldIndex) {
        return gridPane.getChildren().get((fieldIndex + 1) * 2 - 1);
    }

    private static GridPane createServerTab(String tabName) {
        GridPane serverGridPane = new GridPane();

        Text txtHost = new Text("Host");
        Text txtDatabases = new Text("Databases");

        TextField tfHost = new TextField();
        TextArea taDatabases = new TextArea();

        taDatabases.setPromptText("Each line is a new database");

        //Arranging all the nodes in the grid
        serverGridPane.add(txtHost, 0, INDEX_HOST_FIELD);
        serverGridPane.add(tfHost, 1, INDEX_HOST_FIELD);
        serverGridPane.add(txtDatabases, 0, INDEX_DATABASES_FIELD);
        serverGridPane.add(taDatabases, 1, INDEX_DATABASES_FIELD);

        //Set the alignments
        serverGridPane.setAlignment(Pos.CENTER);

        GridPane.setHalignment(txtHost, HPos.RIGHT);
        GridPane.setHalignment(txtDatabases, HPos.RIGHT);

        //Set other attributes
        serverGridPane.setPadding(new Insets(Config.getInstance().DEFAULT_CONTROL_SPACING, Config.getInstance().DEFAULT_CONTROL_SPACING, Config.getInstance().DEFAULT_CONTROL_SPACING, Config.getInstance().DEFAULT_CONTROL_SPACING));

        serverGridPane.setVgap(Config.getInstance().DEFAULT_GRID_PANE_SPACING);
        serverGridPane.setHgap(Config.getInstance().DEFAULT_GRID_PANE_SPACING);

        taDatabases.setWrapText(true);
        taDatabases.setPrefRowCount(3);
        taDatabases.setPrefColumnCount(prefColCount);

        tabPane.getTabs().add(new Tab(tabName, serverGridPane));

        return serverGridPane;
    }

}
