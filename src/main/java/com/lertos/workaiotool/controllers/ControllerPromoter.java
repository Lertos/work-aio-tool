package com.lertos.workaiotool.controllers;

import com.lertos.workaiotool.Helper;
import com.lertos.workaiotool.model.Config;
import com.lertos.workaiotool.model.Data;
import com.lertos.workaiotool.model.items.PromoteItem;
import com.lertos.workaiotool.popups.PromotePopup;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class ControllerPromoter {

    @FXML
    private VBox vboxPromoterButtons;
    @FXML
    private Button btnUndoDelete;

    private final DataFormat PROMOTER_ITEM_DATA_FORMAT = new DataFormat("promoter-folder-item");
    private ListView<PromoteItem> itemsListView;

    @FXML
    public void initialize() {
        //Setup and create the list of PromoterItems
        itemsListView = new ListView<>(Data.getInstance().promoteItems.getActiveItems());

        itemsListView.setCellFactory(param -> {
            try {
                return new PromoteItemCell() {
                    @Override
                    protected void updateItem(PromoteItem item, boolean empty) {
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
        vboxPromoterButtons.getChildren().add(0, itemsListView);

        //Refresh the list so the new cell dimensions are updated from the "current getWidth()" call in the updateItem method
        vboxPromoterButtons.widthProperty().addListener((obs, oldVal, newVal) -> {
            itemsListView.refresh();
        });

        setUndoDeleteVisibility();
    }

    @FXML
    private void onAddClicked() {
        showPopup(-1);
    }

    private void showPopup(int itemIndex) {
        boolean updated = PromotePopup.display(itemIndex);

        if (updated)
            itemsListView.refresh();
    }

    @FXML
    private void onUndoDeleteClicked() {
        int size = Data.getInstance().promoteItems.getHistoryItems().size();

        if (size > 0) {
            PromoteItem item = Data.getInstance().promoteItems.getHistoryItems().remove(size - 1);
            Data.getInstance().promoteItems.getActiveItems().add(item);

            if (size == 1)
                setUndoDeleteVisibility();
        }
    }

    private void setUndoDeleteVisibility() {
        if (Data.getInstance().promoteItems.getHistoryItems().size() > 0) {
            btnUndoDelete.setVisible(true);
            btnUndoDelete.setManaged(true);
        } else {
            btnUndoDelete.setVisible(false);
            btnUndoDelete.setManaged(false);
        }
    }

    private boolean doPathsExist(ArrayList<String> list) {
        for (String path : list) {
            File f = new File(path);

            if (!f.isDirectory()) {
                Helper.showAlert("The folder '" + path + "' does not exist");
                return false;
            }
        }
        return true;
    }

    private class PromoteItemCell extends ListCell<PromoteItem> {
        HBox hbox = new HBox();
        Label label = new Label();
        Pane pane = new Pane();
        ImageView ivDelete = new ImageView(new Image(new FileInputStream(Config.getInstance().DELETE_BUTTON_PATH)));
        ImageView ivEdit = new ImageView(new Image(new FileInputStream(Config.getInstance().EDIT_BUTTON_PATH)));
        Button buttonDelete = new Button();
        Button buttonEdit = new Button();

        public PromoteItemCell() throws FileNotFoundException {
            super();

            hbox.getChildren().addAll(label, pane, buttonEdit, buttonDelete);
            hbox.setSpacing(4);
            hbox.setHgrow(pane, Priority.ALWAYS); //Puts the label to the left and grows the pane so the button(s) will be push to the far right

            buttonDelete.setOnAction(event -> {
                if (getItem() == null)
                    return;

                PromoteItem item = getItem();

                Data.getInstance().promoteItems.getActiveItems().remove(item);
                Data.getInstance().promoteItems.getHistoryItems().add(item);

                setUndoDeleteVisibility();
            });

            buttonEdit.setOnAction(event -> {
                if (getItem() == null)
                    return;

                PromoteItem item = getItem();
                int index = Data.getInstance().promoteItems.getActiveItems().indexOf(item);

                if (index != -1)
                    showPopup(index);
            });

            //Add the label listener to open the folder
            hbox.setOnMouseClicked(event -> {
                if (getItem() == null)
                    return;

                //Check that every path exists before promoting anything
                if (!doPathsExist(getItem().getOriginPaths()) || !doPathsExist(getItem().getDestinationPaths()))
                    return;

                //Get the command to use
                String command = switch (getItem().getPromoteType()) {
                    case COPY -> "copy";
                    case MOVE -> "move";
                };

                try {
                    //TODO: Do the windows copy/move command base on type
                    Runtime.getRuntime().exec("cmd.exe /c copy TODO");
                } catch (IOException e) {
                    Helper.showAlert("The folder could not be opened");
                    e.printStackTrace();
                }
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
                content.put(PROMOTER_ITEM_DATA_FORMAT, getItem());

                dragboard.setContent(content);

                event.consume();
            });

            //Accept an event's dragOver event IFF the dragged element is of the same class type
            setOnDragOver(event -> {
                if (event.getGestureSource() != this && event.getDragboard().hasContent(PROMOTER_ITEM_DATA_FORMAT))
                    event.acceptTransferModes(TransferMode.MOVE);
                event.consume();
            });

            //When the source element is hovering over another row, give the row some opacity to show where it is
            setOnDragEntered(event -> {
                if (event.getGestureSource() != this && event.getDragboard().hasContent(PROMOTER_ITEM_DATA_FORMAT))
                    setOpacity(0.3);
            });

            //When the row is not being hovered on, set the opacity back to normal
            setOnDragExited(event -> {
                if (event.getGestureSource() != this && event.getDragboard().hasContent(PROMOTER_ITEM_DATA_FORMAT))
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
                if (db.hasContent(PROMOTER_ITEM_DATA_FORMAT)) {
                    PromoteItem droppedPromoteItem = (PromoteItem) db.getContent(PROMOTER_ITEM_DATA_FORMAT);
                    ObservableList<PromoteItem> items = getListView().getItems();
                    int draggedIdx = items.indexOf(droppedPromoteItem);
                    int thisIdx = items.indexOf(getItem());

                    PromoteItem temp = Data.getInstance().promoteItems.getActiveItems().get(draggedIdx);
                    Data.getInstance().promoteItems.getActiveItems().set(draggedIdx, Data.getInstance().promoteItems.getActiveItems().get(thisIdx));
                    Data.getInstance().promoteItems.getActiveItems().set(thisIdx, temp);

                    success = true;
                }
                event.setDropCompleted(success);
                event.consume();
            });
            setOnDragDone(DragEvent::consume);
        }
    }
}