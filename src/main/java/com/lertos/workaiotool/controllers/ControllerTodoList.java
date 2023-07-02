package com.lertos.workaiotool.controllers;

import com.lertos.workaiotool.model.Config;
import com.lertos.workaiotool.model.Data;
import com.lertos.workaiotool.model.items.TodoItem;
import com.lertos.workaiotool.popups.TodoPopup;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ListIterator;

public class ControllerTodoList {

    @FXML
    private VBox vboxTodoList;
    @FXML
    private Button btnDeleteMultiple;
    @FXML
    private Button btnDeleteChecked;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnFinishDeleting;
    @FXML
    private Button btnUndoDelete;
    @FXML
    private Label lblClickHint;

    private ListView<TodoItem> itemsListView;
    private boolean isDeleteModeEnabled;
    private final DataFormat TODO_ITEM_DATA_FORMAT = new DataFormat("format-todo-item");

    @FXML
    public void initialize() {
        //Make sure buttons are in the correct start states
        enableDeletionMode(false);

        //Setup and create the list of TodoItems
        itemsListView = new ListView<>(Data.getInstance().getActiveTodoItems());

        itemsListView.setCellFactory(param -> new TodoItemCell() {
            @Override
            protected void updateItem(TodoItem item, boolean empty) {
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

                    //When checked, the item is completed; otherwise it's still active
                    if (item.isDone()) {
                        checkBox.setSelected(true);
                        label.setTextFill(Config.getInstance().TEXT_INACTIVE);
                    } else {
                        checkBox.setSelected(false);
                        label.setTextFill(Config.getInstance().TEXT_ACTIVE);
                    }

                    //This will make sure the row is shown as expected, with each element in the right order horizontally
                    setGraphic(hbox);
                }
            }
        });
        vboxTodoList.getChildren().add(0, itemsListView);

        //Refresh the list so the new cell dimensions are updated from the "current getWidth()" call in the updateItem method
        vboxTodoList.widthProperty().addListener((obs, oldVal, newVal) -> {
            itemsListView.refresh();
        });

        setUndoDeleteVisibility();
    }

    //Enables deleting of rows simply by clicking the rows
    private void enableDeletionMode(boolean value) {
        isDeleteModeEnabled = value;

        //Set the hint label to the text that makes sense for the current state
        if (value) {
            lblClickHint.setText("To DELETE, click the text of a row");
            lblClickHint.setTextFill(Config.getInstance().TEXT_ERROR);
        } else {
            lblClickHint.setText("To EDIT, click the text of a row");
            lblClickHint.setTextFill(Config.getInstance().TEXT_ACTIVE);
        }

        //Setting visible so the button is hidden; setting managed to false when the button shouldn't take space when hidden
        btnDeleteMultiple.setVisible(!value);
        btnDeleteMultiple.setManaged(!value);
        btnDeleteChecked.setVisible(!value);
        btnDeleteChecked.setManaged(!value);
        btnAdd.setVisible(!value);
        btnAdd.setManaged(!value);

        btnFinishDeleting.setVisible(value);
        btnFinishDeleting.setManaged(value);
    }

    @FXML
    private void onDeleteMultipleClicked() {
        enableDeletionMode(true);
    }

    @FXML
    private void onDeleteCheckedClicked() {
        ListIterator<TodoItem> iter = Data.getInstance().getActiveTodoItems().listIterator();

        while (iter.hasNext()) {
            TodoItem item = iter.next();

            if (item.isDone()) {
                Data.getInstance().getHistoryTodoItems().add(item);
                iter.remove();
            }
        }
        setUndoDeleteVisibility();
    }

    private void setUndoDeleteVisibility() {
        if (Data.getInstance().getHistoryTodoItems().size() > 0) {
            btnUndoDelete.setVisible(true);
            btnUndoDelete.setManaged(true);
        } else {
            btnUndoDelete.setVisible(false);
            btnUndoDelete.setManaged(false);
        }
    }

    @FXML
    private void onUndoDeleteClicked() {
        int size = Data.getInstance().getHistoryTodoItems().size();

        if (size > 0) {
            TodoItem item = Data.getInstance().getHistoryTodoItems().remove(size - 1);
            Data.getInstance().getActiveTodoItems().add(item);

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
        boolean updated = TodoPopup.display(itemIndex);

        if (updated)
            itemsListView.refresh();
    }

    @FXML
    private void onFinishDeletingClicked() {
        enableDeletionMode(false);
    }

    private class TodoItemCell extends ListCell<TodoItem> {
        HBox hbox = new HBox();
        CheckBox checkBox = new CheckBox();
        Label label = new Label();

        public TodoItemCell() {
            super();

            hbox.getChildren().addAll(checkBox, label);
            hbox.setSpacing(4);
            hbox.setHgrow(label, Priority.ALWAYS);

            //Add the label listener to either edit or delete a row
            hbox.setOnMouseClicked(event -> {
                //If delete mode is ON - a click means delete the item and add it to the history queue
                if (isDeleteModeEnabled) {
                    Data.getInstance().getHistoryTodoItems().add(getItem());
                    Data.getInstance().getActiveTodoItems().remove(getItem());

                    setUndoDeleteVisibility();
                }
                //If delete mode is OFF - a (secondary) click means they wish to edit the text, so show a dialogue to change it
                else {
                    if (event.getButton().toString().equals("SECONDARY")) {
                        showPopup(Data.getInstance().getActiveTodoItems().indexOf(getItem()));
                    }
                }
            });

            //When checked, the item is completed; otherwise it's still active
            checkBox.setOnAction(event -> {
                if (getItem().isDone())
                    label.setTextFill(Config.getInstance().TEXT_ACTIVE);
                else
                    label.setTextFill(Config.getInstance().TEXT_INACTIVE);
                getItem().setDone(!getItem().isDone());
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
                content.put(TODO_ITEM_DATA_FORMAT, getItem());

                dragboard.setContent(content);

                event.consume();
            });

            //Accept an event's dragOver event IFF the dragged element is of the same class type
            setOnDragOver(event -> {
                if (event.getGestureSource() != this && event.getDragboard().hasContent(TODO_ITEM_DATA_FORMAT))
                    event.acceptTransferModes(TransferMode.MOVE);
                event.consume();
            });

            //When the source element is hovering over another row, give the row some opacity to show where it is
            setOnDragEntered(event -> {
                if (event.getGestureSource() != this && event.getDragboard().hasContent(TODO_ITEM_DATA_FORMAT))
                    setOpacity(0.3);
            });

            //When the row is not being hovered on, set the opacity back to normal
            setOnDragExited(event -> {
                if (event.getGestureSource() != this && event.getDragboard().hasContent(TODO_ITEM_DATA_FORMAT))
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
                if (db.hasContent(TODO_ITEM_DATA_FORMAT)) {
                    TodoItem droppedTodoItem = (TodoItem) db.getContent(TODO_ITEM_DATA_FORMAT);
                    ObservableList<TodoItem> items = getListView().getItems();
                    int draggedIdx = items.indexOf(droppedTodoItem);
                    int thisIdx = items.indexOf(getItem());

                    TodoItem temp = Data.getInstance().getActiveTodoItems().get(draggedIdx);
                    Data.getInstance().getActiveTodoItems().set(draggedIdx, Data.getInstance().getActiveTodoItems().get(thisIdx));
                    Data.getInstance().getActiveTodoItems().set(thisIdx, temp);

                    success = true;
                }
                event.setDropCompleted(success);
                event.consume();
            });
            setOnDragDone(DragEvent::consume);
        }
    }
}
