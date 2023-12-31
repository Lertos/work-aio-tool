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

public class SQLComparePopup {

    private static final int INDEX_HOST_FIELD = 0;
    private static final int INDEX_PORT_FIELD = 1;
    private static final int INDEX_USERNAME_FIELD = 2;
    private static final int INDEX_PASSWORD_FIELD = 3;
    private static final int INDEX_USES_INTEGRATED_SECURITY_FIELD = 4;
    private static final int INDEX_DATABASES_FIELD = 5;

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
                    TextField tfHost = (TextField) getGridInputField(newTabPane, INDEX_HOST_FIELD);
                    TextField tfPort = (TextField) getGridInputField(newTabPane, INDEX_PORT_FIELD);
                    TextField tfUsername = (TextField) getGridInputField(newTabPane, INDEX_USERNAME_FIELD);
                    PasswordField tfPassword = (PasswordField) getGridInputField(newTabPane, INDEX_PASSWORD_FIELD);
                    CheckBox cbUsesIntegratedSecurity = (CheckBox) getGridInputField(newTabPane, INDEX_USES_INTEGRATED_SECURITY_FIELD);
                    TextArea taDatabases = (TextArea) getGridInputField(newTabPane, INDEX_DATABASES_FIELD);

                    tfHost.setText(itemSQL.getHost());
                    tfPort.setText(String.valueOf(itemSQL.getPort()));
                    tfUsername.setText(itemSQL.getUsername());
                    tfPassword.setText(itemSQL.getPassword());
                    cbUsesIntegratedSecurity.setSelected(itemSQL.usesIntegratedSecurity());
                    taDatabases.setText(Helper.getLinesAsString(itemSQL.getDatabaseNames()));
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

            String displayName = tfDisplayName.getText().trim();
            String procedureName = tfProcedureName.getText().trim();

            //First check the validations
            if (displayName.isEmpty()) {
                Helper.showAlert("'Display Name' cannot be empty");
                return;
            } else if (groupSQLTypes.getSelectedToggle() == null) {
                Helper.showAlert("'SQL Type' must be selected");
                return;
            } else if (procedureName.isEmpty()) {
                Helper.showAlert("'Procedure Name' cannot be empty");
                return;
            }

            ArrayList<ItemSQL> newListItemSQL = new ArrayList<>();

            //Validate each tab to make sure they pass all validations
            for (Tab tab : tabPane.getTabs()) {
                GridPane tabGridPane = (GridPane) tab.getContent();

                TextField tfHost = (TextField) getGridInputField(tabGridPane, INDEX_HOST_FIELD);
                TextField tfPort = (TextField) getGridInputField(tabGridPane, INDEX_PORT_FIELD);
                TextField tfUsername = (TextField) getGridInputField(tabGridPane, INDEX_USERNAME_FIELD);
                PasswordField tfPassword = (PasswordField) getGridInputField(tabGridPane, INDEX_PASSWORD_FIELD);
                CheckBox cbUsesIntegratedSecurity = (CheckBox) getGridInputField(tabGridPane, INDEX_USES_INTEGRATED_SECURITY_FIELD);
                TextArea taDatabases = (TextArea) getGridInputField(tabGridPane, INDEX_DATABASES_FIELD);

                String host = tfHost.getText().trim();
                String port = tfPort.getText().trim();
                String username = tfUsername.getText().trim();
                String password = tfPassword.getText().trim();
                boolean usesIntegratedSecurity = cbUsesIntegratedSecurity.isSelected();
                ArrayList<String> databases = Helper.getLinesToList(taDatabases.getText().trim());

                String tabName = tab.getText().trim();

                if (host.isEmpty()) {
                    Helper.showAlert("Tab: [" + tabName + "] - the 'host' field is empty");
                    return;
                } else if (port.isEmpty()) {
                    Helper.showAlert("Tab: [" + tabName + "] - the 'port' field is empty");
                    return;
                } else if (!usesIntegratedSecurity) {
                    //Only if not using integrated security do we need to check the username and password
                    if (username.isEmpty()) {
                        Helper.showAlert("Tab: [" + tabName + "] - the 'username' field is empty");
                        return;
                    } else if (password.isEmpty()) {
                        Helper.showAlert("Tab: [" + tabName + "] - the 'password' field is empty");
                        return;
                    }
                } else if (taDatabases.getText().trim().isEmpty()) {
                    Helper.showAlert("Tab: [" + tabName + "] - the 'databases' field is empty");
                    return;
                }

                //Check to see if the port is a correct number
                int portAsInt;

                try {
                    portAsInt = Integer.parseInt(port);
                } catch (NumberFormatException nfe) {
                    Helper.showAlert("Tab: [" + tabName + "] - the 'port' value is not a valid integer");
                    return;
                }

                newListItemSQL.add(new ItemSQL(tab.getText(), host, portAsInt, username, password, usesIntegratedSecurity, databases));
            }

            //If this is a new item, create a new item
            if (itemIndex == -1) {
                SQLCompareItem.SQLType sqlType = Helper.getEnumFromText(SQLCompareItem.SQLType.values(), ((RadioButton) groupSQLTypes.getSelectedToggle()).getText());
                SQLCompareItem newItem = new SQLCompareItem(displayName, procedureName, sqlType, newListItemSQL);

                Data.getInstance().sqlCompareItems.addItem(newItem);
            }
            //If it is an existing item, update the fields
            else {
                SQLCompareItem existingItem = Data.getInstance().sqlCompareItems.getActiveItems().get(itemIndex);

                existingItem.setDescription(displayName);
                existingItem.setSqlType(Helper.getEnumFromText(SQLCompareItem.SQLType.values(), ((RadioButton) groupSQLTypes.getSelectedToggle()).getText()));
                existingItem.setProcedureName(procedureName);
                existingItem.setItemsSQL(newListItemSQL);

                Data.getInstance().sqlCompareItems.saveFile();
            }

            popupWindow.close();
        });

        //Create the new scene and show the popup
        Scene scene = new Scene(layout);

        popupWindow.setScene(scene);
        popupWindow.showAndWait();

        return updated;
    }

    //The math of the get() is because the grid pane has the Text node in the first column, then the Input node in the second,
    //so we need to double each index and subtract 1 for zero-based
    private static Node getGridInputField(GridPane gridPane, int fieldIndex) {
        return gridPane.getChildren().get((fieldIndex + 1) * 2 - 1);
    }

    private static HBox createAddNewServerHBox() {
        TextField tfTabName = new TextField();
        Button btnAddServer = new Button("Add Server Tab");

        tfTabName.setPromptText("Tab Name");

        btnAddServer.setOnMouseClicked(mouseEvent -> {
            if (tfTabName.getText().isEmpty()) {
                Helper.showAlert("'Tab Name' must not be empty");
                return;
            } else {
                String newTabName = tfTabName.getText();

                //Check if there is an existing tab with the same name - if so, don't allow it
                for (Tab tab : tabPane.getTabs()) {
                    if (newTabName.equalsIgnoreCase(tab.getText())) {
                        Helper.showAlert("'Tab Name' must be unique");
                        return;
                    }
                }
            }
            //If all validations pass, create the new server tab and then clear the tab name text field
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
        Text txtUsesIntegratedSecurity = new Text("Integrated Security");
        Text txtDatabases = new Text("Databases");

        TextField tfHost = new TextField();
        TextField tfPort = new TextField();
        TextField tfUsername = new TextField();
        PasswordField tfPassword = new PasswordField();
        CheckBox cbUsesIntegratedSecurity = new CheckBox();

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
        serverGridPane.add(txtUsesIntegratedSecurity, 0, INDEX_USES_INTEGRATED_SECURITY_FIELD);
        serverGridPane.add(cbUsesIntegratedSecurity, 1, INDEX_USES_INTEGRATED_SECURITY_FIELD);
        serverGridPane.add(txtDatabases, 0, INDEX_DATABASES_FIELD);
        serverGridPane.add(taDatabases, 1, INDEX_DATABASES_FIELD);

        //Set listeners
        cbUsesIntegratedSecurity.selectedProperty().addListener((obs, wasChecked, isNowChecked) -> {
            boolean usesIntegratedSecurity = cbUsesIntegratedSecurity.isSelected();

            toggleFieldVisibility(txtUsername, !usesIntegratedSecurity);
            toggleFieldVisibility(tfUsername, !usesIntegratedSecurity);
            toggleFieldVisibility(txtPassword, !usesIntegratedSecurity);
            toggleFieldVisibility(tfPassword, !usesIntegratedSecurity);
        });

        //Set the alignments
        serverGridPane.setAlignment(Pos.CENTER);

        GridPane.setHalignment(txtHost, HPos.RIGHT);
        GridPane.setHalignment(txtPort, HPos.RIGHT);
        GridPane.setHalignment(txtUsername, HPos.RIGHT);
        GridPane.setHalignment(txtPassword, HPos.RIGHT);
        GridPane.setHalignment(txtDatabases, HPos.RIGHT);

        //Set other attributes
        tfPort.setPromptText("Use -1 to assign no port");

        serverGridPane.setPadding(new Insets(Config.getInstance().DEFAULT_CONTROL_SPACING, Config.getInstance().DEFAULT_CONTROL_SPACING, Config.getInstance().DEFAULT_CONTROL_SPACING, Config.getInstance().DEFAULT_CONTROL_SPACING));

        serverGridPane.setVgap(Config.getInstance().DEFAULT_GRID_PANE_SPACING);
        serverGridPane.setHgap(Config.getInstance().DEFAULT_GRID_PANE_SPACING);

        taDatabases.setWrapText(true);
        taDatabases.setPrefRowCount(3);
        taDatabases.setPrefColumnCount(prefColCount);

        //Add the newly created tab and open it
        tabPane.getTabs().add(new Tab(tabName, serverGridPane));
        tabPane.getSelectionModel().select(tabPane.getTabs().size() - 1);

        return serverGridPane;
    }

    private static void toggleFieldVisibility(Node node, boolean isVisible) {
        node.setVisible(isVisible);
        node.setManaged(isVisible);
    }

}
