package com.lertos.workaiotool.controllers;

import com.lertos.workaiotool.Helper;
import com.lertos.workaiotool.model.Config;
import com.lertos.workaiotool.model.Data;
import com.lertos.workaiotool.model.items.InfoItem;
import com.lertos.workaiotool.popups.InfoPopup;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ControllerInfo {

    private final DataFormat INFO_ITEM_DATA_FORMAT = new DataFormat("format-info-item");
    @FXML
    private VBox vboxInfoList;
    @FXML
    private Button btnUndoDelete;
    private ListView<InfoItem> itemsListView;

    @FXML
    public void initialize() {
        //Setup and create the list of infoItems
        itemsListView = new ListView<>(Data.getInstance().infoItems.getActiveItems());

        itemsListView.setCellFactory(param -> {
            try {
                return new InfoItemCell() {
                    @Override
                    protected void updateItem(InfoItem item, boolean empty) {
                        super.updateItem(item, empty);

                        setText(null);

                        if (empty) {
                            setGraphic(null);
                        } else {
                            //Make sure the HBox never gets squishes off the screen
                            hbox.setPrefWidth(param.getWidth() - Config.getInstance().SPACING_BUFFER);
                            hbox.setMaxWidth(param.getWidth() - Config.getInstance().SPACING_BUFFER);

                            //Make the label wrap the text so the entire text can be seen
                            label.setWrapText(true);
                            label.setText(item != null ? item.getDescription() : "");
                            label.setTooltip(new Tooltip(item.getAdditionalText()));

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
        vboxInfoList.getChildren().add(0, itemsListView);

        //Refresh the list so the new cell dimensions are updated from the "current getWidth()" call in the updateItem method
        vboxInfoList.widthProperty().addListener((obs, oldVal, newVal) -> {
            itemsListView.refresh();
        });

        setUndoDeleteVisibility();
    }

    private void setUndoDeleteVisibility() {
        if (Data.getInstance().infoItems.getHistoryItems().size() > 0) {
            btnUndoDelete.setVisible(true);
            btnUndoDelete.setManaged(true);
        } else {
            btnUndoDelete.setVisible(false);
            btnUndoDelete.setManaged(false);
        }
    }

    @FXML
    private void onUndoDeleteClicked() {
        int size = Data.getInstance().infoItems.getHistoryItems().size();

        if (size > 0) {
            InfoItem item = Data.getInstance().infoItems.getHistoryItems().remove(size - 1);
            Data.getInstance().infoItems.getActiveItems().add(item);

            if (size == 1) {
                btnUndoDelete.setVisible(false);
                btnUndoDelete.setManaged(false);
            }
        }
    }

    @FXML
    private void onAddClicked() {
        showPopup(-1);
    }

    private void showPopup(int itemIndex) {
        boolean updated = InfoPopup.display(itemIndex);

        if (updated)
            itemsListView.refresh();
    }

    private class InfoItemCell extends ListCell<InfoItem> {
        HBox hbox = new HBox();
        Label label = new Label();
        Pane pane = new Pane();
        ImageView ivDelete = new ImageView(new Image(new FileInputStream(Config.getInstance().DELETE_BUTTON_PATH)));
        ImageView ivEdit = new ImageView(new Image(new FileInputStream(Config.getInstance().EDIT_BUTTON_PATH)));
        Button buttonDelete = new Button();
        Button buttonEdit = new Button();

        public InfoItemCell() throws FileNotFoundException {
            super();

            hbox.getChildren().addAll(label, pane, buttonEdit, buttonDelete);
            hbox.setSpacing(4);
            hbox.setHgrow(pane, Priority.ALWAYS);

            buttonDelete.setOnAction(event -> {
                if (getItem() == null)
                    return;

                InfoItem item = getItem();

                Data.getInstance().infoItems.getActiveItems().remove(item);
                Data.getInstance().infoItems.getHistoryItems().add(item);

                setUndoDeleteVisibility();
            });

            buttonEdit.setOnAction(event -> {
                if (getItem() == null)
                    return;

                InfoItem item = getItem();
                int index = Data.getInstance().infoItems.getActiveItems().indexOf(item);

                if (index != -1)
                    showPopup(index);
            });

            //Add the label listener to either edit or delete a row
            hbox.setOnMouseClicked(event -> {
                showPopup(Data.getInstance().infoItems.getActiveItems().indexOf(getItem()));
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
                content.put(INFO_ITEM_DATA_FORMAT, getItem());

                dragboard.setContent(content);

                event.consume();
            });

            //Accept an event's dragOver event IFF the dragged element is of the same class type
            setOnDragOver(event -> {
                if (event.getGestureSource() != this && event.getDragboard().hasContent(INFO_ITEM_DATA_FORMAT))
                    event.acceptTransferModes(TransferMode.MOVE);
                event.consume();
            });

            //When the source element is hovering over another row, give the row some opacity to show where it is
            setOnDragEntered(event -> {
                if (event.getGestureSource() != this && event.getDragboard().hasContent(INFO_ITEM_DATA_FORMAT))
                    setOpacity(0.3);
            });

            //When the row is not being hovered on, set the opacity back to normal
            setOnDragExited(event -> {
                if (event.getGestureSource() != this && event.getDragboard().hasContent(INFO_ITEM_DATA_FORMAT))
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
                if (db.hasContent(INFO_ITEM_DATA_FORMAT)) {
                    InfoItem droppedInfoItem = (InfoItem) db.getContent(INFO_ITEM_DATA_FORMAT);
                    ObservableList<InfoItem> items = getListView().getItems();
                    int draggedIdx = items.indexOf(droppedInfoItem);
                    int thisIdx = items.indexOf(getItem());

                    InfoItem temp = Data.getInstance().infoItems.getActiveItems().get(draggedIdx);
                    Data.getInstance().infoItems.getActiveItems().set(draggedIdx, Data.getInstance().infoItems.getActiveItems().get(thisIdx));
                    Data.getInstance().infoItems.getActiveItems().set(thisIdx, temp);

                    success = true;
                }
                event.setDropCompleted(success);
                event.consume();
            });
            setOnDragDone(DragEvent::consume);
        }
    }
}
