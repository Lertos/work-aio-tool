package com.lertos.workaiotool.controllers;

import com.lertos.workaiotool.model.Data;
import com.lertos.workaiotool.model.items.FolderItem;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ControllerFolders {

    @FXML
    private VBox vboxFolderButtons;
    @FXML
    private FlowPane flowPaneButtons;
    @FXML
    private Button btnAddNew;
    @FXML
    private Button btnUndoDelete;

    private final DataFormat FOLDER_ITEM_DATA_FORMAT = new DataFormat("format-folder-item");
    private ListView<FolderItem> itemsListView;

    @FXML
    public void initialize() {
        //Setup and create the list of TodoItems
        itemsListView = new ListView<>(Data.getInstance().getActiveFolderItems());

        itemsListView.setCellFactory(param -> {
            try {
                return new FolderItemCell() {
                    @Override
                    protected void updateItem(FolderItem item, boolean empty) {
                        super.updateItem(item, empty);

                        setText(null);

                        if (empty) {
                            setGraphic(null);
                        } else {
                            //Make sure the HBox never gets squishes off the screen
                            hbox.setPrefWidth(param.getWidth() - Data.getInstance().BUTTON_ICON_SIZE);
                            hbox.setMaxWidth(param.getWidth() - Data.getInstance().BUTTON_ICON_SIZE);

                            //Make the label wrap the text so the entire text can be seen
                            label.setWrapText(true);
                            label.setText(item != null ? item.getDescription() : "");

                            //Set the button's image to the same size of the button, preserving the aspect ratio
                            double totalButtonSize = Data.getInstance().BUTTON_ICON_SIZE + Data.getInstance().BUTTON_PADDING_SIZE;

                            button.setMinSize(totalButtonSize, totalButtonSize);
                            button.setMaxSize(totalButtonSize, totalButtonSize);

                            imageView.setPreserveRatio(true);
                            imageView.setFitWidth(Data.getInstance().BUTTON_ICON_SIZE);
                            imageView.setFitHeight(Data.getInstance().BUTTON_ICON_SIZE);

                            button.setGraphic(imageView);

                            //This will make sure the row is shown as expected, with each element in the right order horizontally
                            setGraphic(hbox);
                        }
                    }
                };
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        vboxFolderButtons.getChildren().add(0, itemsListView);

        //Refresh the list so the new cell dimensions are updated from the "current getWidth()" call in the updateItem method
        vboxFolderButtons.widthProperty().addListener((obs, oldVal, newVal) -> {
            itemsListView.refresh();
        });
    }

    private class FolderItemCell extends ListCell<FolderItem> {
        HBox hbox = new HBox();
        Label label = new Label();
        Pane pane = new Pane();
        ImageView imageView = new ImageView(new Image(new FileInputStream(Data.getInstance().DELETE_BUTTON_PATH)));
        Button button = new Button();

        public FolderItemCell() throws FileNotFoundException {
            super();

            hbox.getChildren().addAll(label, pane, button);
            hbox.setSpacing(4);
            hbox.setHgrow(pane, Priority.ALWAYS); //Puts the label to the left and grows the pane so the button(s) will be push to the far right

            button.setOnAction(event -> System.out.println(getItem().getDescription() + " : button clicked: " + event));

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
                content.put(FOLDER_ITEM_DATA_FORMAT, getItem());

                dragboard.setContent(content);

                event.consume();
            });

            //Accept an event's dragOver event IFF the dragged element is of the same class type
            setOnDragOver(event -> {
                if (event.getGestureSource() != this && event.getDragboard().hasContent(FOLDER_ITEM_DATA_FORMAT))
                    event.acceptTransferModes(TransferMode.MOVE);
                event.consume();
            });

            //When the source element is hovering over another row, give the row some opacity to show where it is
            setOnDragEntered(event -> {
                if (event.getGestureSource() != this && event.getDragboard().hasContent(FOLDER_ITEM_DATA_FORMAT))
                    setOpacity(0.3);
            });

            //When the row is not being hovered on, set the opacity back to normal
            setOnDragExited(event -> {
                if (event.getGestureSource() != this && event.getDragboard().hasContent(FOLDER_ITEM_DATA_FORMAT))
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
                if (db.hasContent(FOLDER_ITEM_DATA_FORMAT)) {
                    FolderItem droppedFolderItem = (FolderItem) db.getContent(FOLDER_ITEM_DATA_FORMAT);
                    ObservableList<FolderItem> items = getListView().getItems();
                    int draggedIdx = items.indexOf(droppedFolderItem);
                    int thisIdx = items.indexOf(getItem());

                    FolderItem temp = Data.getInstance().getActiveFolderItems().get(draggedIdx);
                    Data.getInstance().getActiveFolderItems().set(draggedIdx, Data.getInstance().getActiveFolderItems().get(thisIdx));
                    Data.getInstance().getActiveFolderItems().set(thisIdx, temp);

                    success = true;
                }
                event.setDropCompleted(success);
                event.consume();
            });
            setOnDragDone(DragEvent::consume);
        }
    }

}
