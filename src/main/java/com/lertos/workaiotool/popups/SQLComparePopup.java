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
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SQLComparePopup {

    private static final int INDEX_HOST_FIELD = 0;
    private static final int INDEX_PORT_FIELD = 1;
    private static final int INDEX_USERNAME_FIELD = 2;
    private static final int INDEX_PASSWORD_FIELD = 3;
    private static final int INDEX_DATABASES_FIELD = 4;

    private static boolean updated = false;
    private static TabPane tabPane;
    private static int prefColCount;

    public static boolean display(int itemIndex) {
        //Get the item info
        SQLCompareItem item = null;

        if (itemIndex != -1 && itemIndex < Data.getInstance().sqlCompareItems.getActiveItems().size())
            item = Data.getInstance().sqlCompareItems.getActiveItems().get(itemIndex);

        //Create the popup window
        Stage popupWindow = new Stage();

        popupWindow.initModality(Modality.APPLICATION_MODAL);

        //Create the parent to hold all controls
        VBox layout = new VBox(Config.getInstance().DEFAULT_CONTROL_SPACING);

        //Create the tab pane to hold all the server configurations
        tabPane = new TabPane();
        tabPane.setPrefSize(350, 300);

        //Top section controls
        GridPane gridPane = new GridPane();

        Text txtDisplayName = new Text("Display Text");
        Text txtSQLType = new Text("SQL Type");
        Text txtProcedureName = new Text("Procedure Name");

        TextField tfDisplayName = new TextField();
        TextField tfProcedureName = new TextField();

        //Set up SQL types
        ToggleGroup groupSQLTypes = new ToggleGroup();
        VBox vboxSQLTypes = Helper.getEnumRadioButtons(SQLCompareItem.SQLType.values(), groupSQLTypes);

        //Set up buttons
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
            tfProcedureName.setText(item.getProcedureName());

            groupSQLTypes.selectToggle(groupSQLTypes.getToggles().get(item.getSqlType().ordinal()));
        }

        //Arranging all the nodes in the grid
        gridPane.add(txtDisplayName, 0, 0);
        gridPane.add(tfDisplayName, 1, 0);
        gridPane.add(txtSQLType, 0, 1);
        gridPane.add(vboxSQLTypes, 1, 1);
        gridPane.add(txtProcedureName, 0, 2);
        gridPane.add(tfProcedureName, 1, 2);

        //Separator to split the top section from the tab pane with all the server set up
        Separator separator = new Separator(Orientation.HORIZONTAL);

        //Add children
        layout.getChildren().addAll(gridPane, separator, createAddNewServerHBox(), tabPane, buttonHBox);

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
        GridPane.setHalignment(txtSQLType, HPos.RIGHT);
        GridPane.setHalignment(txtProcedureName, HPos.RIGHT);

        //Set spacing
        buttonHBox.setSpacing(Config.getInstance().DEFAULT_BUTTON_SPACING);

        //Set other attributes
        prefColCount = tfDisplayName.getPrefColumnCount() * 2;

        //Create each tab for each SQLItem
        if (itemIndex != -1) {
            for (ItemSQL itemSQL : item.getItemsSQL()) {
                GridPane newTabPane = createServerTab(itemSQL.getTabName());

                try {
                    //The math of the get() is because the grid pane is always Text label then TextField field; so we need to double each index and subtract 1 for zero-based
                    ((TextField) newTabPane.getChildren().get((INDEX_HOST_FIELD + 1) * 2 - 1)).setText(itemSQL.getHost());
                    ((TextField) newTabPane.getChildren().get((INDEX_PORT_FIELD + 1) * 2 - 1)).setText(String.valueOf(itemSQL.getPort()));
                    ((TextField) newTabPane.getChildren().get((INDEX_USERNAME_FIELD + 1) * 2 - 1)).setText(itemSQL.getUsername());
                    ((TextField) newTabPane.getChildren().get((INDEX_PASSWORD_FIELD + 1) * 2 - 1)).setText(itemSQL.getPassword());
                    ((TextArea) newTabPane.getChildren().get((INDEX_DATABASES_FIELD + 1) * 2 - 1)).setText(Helper.getLinesAsString(itemSQL.getDatabaseNames()));
                } catch (ClassCastException e) {
                    Helper.showAlert("The creation of the tab is incorrect. Please consult the developer.");
                }
            }
        }

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
            } else if (groupSQLTypes.getSelectedToggle() == null) {
                Helper.showAlert("'SQL Type' must be selected");
                return;
            } else if (tfProcedureName.getText().trim().isEmpty()) {
                Helper.showAlert("'Procedure Name' cannot be empty");
                return;
            }

            popupWindow.close();
        });

        //Create the new scene and show the popup
        Scene scene = new Scene(layout);

        popupWindow.setScene(scene);
        popupWindow.showAndWait();

        return updated;
    }

    private static HBox createAddNewServerHBox() {
        TextField tfTabName = new TextField();
        Button btnAddServer = new Button("Add Server Tab");

        tfTabName.setPromptText("Tab Name");

        btnAddServer.setOnMouseClicked(mouseEvent -> {
            if (tfTabName.getText().isEmpty()) {
                Helper.showAlert("'Tab Name' must not be empty");
                return;
            }

            createServerTab(tfTabName.getText());

            tfTabName.setText("");
        });

        return new HBox(tfTabName, btnAddServer);
    }

    private static GridPane createServerTab(String tabName) {
        GridPane serverGridPane = new GridPane();

        Text txtHost = new Text("Host");
        Text txtPort = new Text("Port");
        Text txtUsername = new Text("Username");
        Text txtPassword = new Text("Password");
        Text txtDatabases = new Text("Databases");

        TextField tfHost = new TextField();
        TextField tfPort = new TextField();
        TextField tfUsername = new TextField();
        TextField tfPassword = new TextField();

        TextArea taDatabases = new TextArea();

        taDatabases.setPromptText("Each line is a new database");

        //Arranging all the nodes in the grid
        serverGridPane.add(txtHost, 0, INDEX_HOST_FIELD);
        serverGridPane.add(tfHost, 1, INDEX_HOST_FIELD);
        serverGridPane.add(txtPort, 0, INDEX_PORT_FIELD);
        serverGridPane.add(tfPort, 1, INDEX_PORT_FIELD);
        serverGridPane.add(txtUsername, 0, INDEX_USERNAME_FIELD);
        serverGridPane.add(tfUsername, 1, INDEX_USERNAME_FIELD);
        serverGridPane.add(txtPassword, 0, INDEX_PASSWORD_FIELD);
        serverGridPane.add(tfPassword, 1, INDEX_PASSWORD_FIELD);
        serverGridPane.add(txtDatabases, 0, INDEX_DATABASES_FIELD);
        serverGridPane.add(taDatabases, 1, INDEX_DATABASES_FIELD);

        //Set the alignments
        serverGridPane.setAlignment(Pos.CENTER);

        GridPane.setHalignment(txtHost, HPos.RIGHT);
        GridPane.setHalignment(txtPort, HPos.RIGHT);
        GridPane.setHalignment(txtUsername, HPos.RIGHT);
        GridPane.setHalignment(txtPassword, HPos.RIGHT);
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
