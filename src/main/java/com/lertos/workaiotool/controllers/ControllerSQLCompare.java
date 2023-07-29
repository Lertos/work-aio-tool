package com.lertos.workaiotool.controllers;

import com.lertos.workaiotool.Helper;
import com.lertos.workaiotool.model.*;
import com.lertos.workaiotool.model.items.FolderItem;
import com.lertos.workaiotool.model.items.SQLCompareItem;
import com.lertos.workaiotool.popups.SQLComparePopup;
import com.lertos.workaiotool.popups.SQLCompareRunPopup;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ControllerSQLCompare {

    private final DataFormat SQL_ITEM_DATA_FORMAT = new DataFormat("format-sql-item");
    @FXML
    private VBox vboxSQLButtons;
    @FXML
    private Button btnUndoDelete;
    private ListView<SQLCompareItem> itemsListView;

    @FXML
    public void initialize() {
        //Setup and create the list of SQLCompareItems
        itemsListView = new ListView<>(Data.getInstance().sqlCompareItems.getActiveItems());

        itemsListView.setCellFactory(param -> {
            try {
                return new SQLCompareItemCell() {
                    @Override
                    protected void updateItem(SQLCompareItem item, boolean empty) {
                        super.updateItem(item, empty);

                        setText(null);

                        if (empty) {
                            setGraphic(null);
                        } else {
                            //Make sure the HBox never gets squishes off the screen
                            hbox.setPrefWidth(param.getWidth() - Config.getInstance().BUTTON_ICON_SIZE);
                            hbox.setMaxWidth(param.getWidth() - Config.getInstance().BUTTON_ICON_SIZE);

                            //Make the label wrap the text so the entire text can be seen
                            label.setWrapText(true);
                            label.setText(item != null ? item.getDescription() : "");

                            //Set the button's image to the same size of the button, preserving the aspect ratio
                            Helper.addImageToButton(buttonEdit, ivEdit);
                            Helper.addImageToButton(buttonDelete, ivDelete);

                            //This will make sure the row is shown as expected, with each element in the right order horizontally
                            setGraphic(hbox);
                        }
                    }
                };
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        vboxSQLButtons.getChildren().add(0, itemsListView);

        //Refresh the list so the new cell dimensions are updated from the "current getWidth()" call in the updateItem method
        vboxSQLButtons.widthProperty().addListener((obs, oldVal, newVal) -> {
            itemsListView.refresh();
        });

        setUndoDeleteVisibility();
    }

    @FXML
    private void onAddClicked() {
        showPopup(-1);
    }

    private void showPopup(int itemIndex) {
        boolean updated = SQLComparePopup.display(itemIndex);

        if (updated)
            itemsListView.refresh();
    }

    @FXML
    private void onUndoDeleteClicked() {
        if (Data.getInstance().sqlCompareItems.restoreItemFromHistory() == 0)
            setUndoDeleteVisibility();
    }

    private void setUndoDeleteVisibility() {
        if (Data.getInstance().sqlCompareItems.getHistoryItems().size() > 0) {
            btnUndoDelete.setVisible(true);
            btnUndoDelete.setManaged(true);
        } else {
            btnUndoDelete.setVisible(false);
            btnUndoDelete.setManaged(false);
        }
    }

    private void startComparison(SQLCompareItem sqlCompareItem) {
        String procedureName = sqlCompareItem.getProcedureName();
        DatabaseAccess dbo = null;
        Pair<ReturnType, String> returnedValue;
        String definitionToCompare = null;
        String newDefinitionToCompare;

        for (ItemSQL itemSQL : sqlCompareItem.getItemsSQL()) {
            switch (sqlCompareItem.getSqlType()) {
                case MYSQL -> dbo = new DatabaseAccessMySQL(itemSQL, procedureName);
                case TRANSACT_SQL -> dbo = new DatabaseAccessTSQL(itemSQL, procedureName);
            }

            //If the dbo object is still null, something fishy is happening - return!
            if (dbo == null) {
                Helper.showAlert("Something is wrong with the setup. The SQL type is incorrect.");
                return;
            }

            returnedValue = dbo.getProcedureDefinition();
            newDefinitionToCompare = returnedValue.getValue();

            //If there were any SQL errors, show the error and then halt this method
            if (returnedValue.getKey() == ReturnType.ERROR) {
                Helper.showAlert(returnedValue.getValue());
                return;
            }

            //If null is returned, it means the definitions inside the single server failed
            if (newDefinitionToCompare == null) {
                Helper.showAlert("The definitions don't match inside the server: [" + itemSQL.getHost() + "]");
                return;
            }

            //If definitions between servers do not match
            if (definitionToCompare != null && !newDefinitionToCompare.equalsIgnoreCase(definitionToCompare)) {
                Helper.showAlert("Definitions do not match across servers");
                return;
            }
        }
        Helper.showAlert("All definitions match");
    }

    private class SQLCompareItemCell extends ListCell<SQLCompareItem> {
        HBox hbox = new HBox();
        Label label = new Label();
        Pane pane = new Pane();
        ImageView ivDelete = new ImageView(new Image(new FileInputStream(Config.getInstance().DELETE_BUTTON_PATH)));
        ImageView ivEdit = new ImageView(new Image(new FileInputStream(Config.getInstance().EDIT_BUTTON_PATH)));
        Button buttonDelete = new Button();
        Button buttonEdit = new Button();

        public SQLCompareItemCell() throws FileNotFoundException {
            super();

            hbox.getChildren().addAll(label, pane, buttonEdit, buttonDelete);
            hbox.setSpacing(4);
            hbox.setHgrow(pane, Priority.ALWAYS); //Puts the label to the left and grows the pane so the button(s) will be push to the far right

            buttonDelete.setOnAction(event -> {
                if (getItem() == null)
                    return;

                SQLCompareItem item = getItem();

                Data.getInstance().sqlCompareItems.moveItemToHistory(item);

                setUndoDeleteVisibility();
            });

            buttonEdit.setOnAction(event -> {
                if (getItem() == null)
                    return;

                SQLCompareItem item = getItem();
                int index = Data.getInstance().sqlCompareItems.getActiveItems().indexOf(item);

                if (index != -1)
                    showPopup(index);
            });

            //Add the label listener to open the folder
            hbox.setOnMouseClicked(event -> {
                if (getItem() == null)
                    return;

                //Get the item to promote and get any last minute changes/details
                SQLCompareItem item = SQLCompareRunPopup.display(Data.getInstance().sqlCompareItems.getActiveItems().indexOf(getItem()));

                if (item == null) {
                    Helper.showAlert("Something is wrong with your setup for this item");
                    return;
                }

                startComparison(item);
            });

            //========================
            //Set the onDrag events
            //========================

            //When the drag is started, copy the current rows' text to the clipboard
            setOnDragDetected(event -> {
                if (getItem() == null)
                    return;

                Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();

                content.putString(getItem().toString());
                content.put(SQL_ITEM_DATA_FORMAT, getItem());

                dragboard.setContent(content);

                event.consume();
            });

            //Accept an event's dragOver event IFF the dragged element is of the same class type
            setOnDragOver(event -> {
                if (event.getGestureSource() != this && event.getDragboard().hasContent(SQL_ITEM_DATA_FORMAT))
                    event.acceptTransferModes(TransferMode.MOVE);
                event.consume();
            });

            //When the source element is hovering over another row, give the row some opacity to show where it is
            setOnDragEntered(event -> {
                if (event.getGestureSource() != this && event.getDragboard().hasContent(SQL_ITEM_DATA_FORMAT))
                    setOpacity(0.3);
            });

            //When the row is not being hovered on, set the opacity back to normal
            setOnDragExited(event -> {
                if (event.getGestureSource() != this && event.getDragboard().hasContent(SQL_ITEM_DATA_FORMAT))
                    setOpacity(1);
            });

            //When the item is dropped, swap the content in the observable array list so the ListView updates automatically
            setOnDragDropped(event -> {
                //If the item was dropped somewhere other than on another item, return
                if (getItem() == null)
                    return;

                Dragboard db = event.getDragboard();
                boolean success = false;

                //Check if the item dropped on is another item
                if (db.hasContent(SQL_ITEM_DATA_FORMAT)) {
                    FolderItem droppedFolderItem = (FolderItem) db.getContent(SQL_ITEM_DATA_FORMAT);
                    ObservableList<SQLCompareItem> items = getListView().getItems();
                    int draggedIdx = items.indexOf(droppedFolderItem);
                    int thisIdx = items.indexOf(getItem());

                    SQLCompareItem temp = Data.getInstance().sqlCompareItems.getActiveItems().get(draggedIdx);
                    Data.getInstance().sqlCompareItems.getActiveItems().set(draggedIdx, Data.getInstance().sqlCompareItems.getActiveItems().get(thisIdx));
                    Data.getInstance().sqlCompareItems.getActiveItems().set(thisIdx, temp);

                    success = true;
                }
                event.setDropCompleted(success);
                event.consume();
            });
            setOnDragDone(DragEvent::consume);
        }
    }

}
