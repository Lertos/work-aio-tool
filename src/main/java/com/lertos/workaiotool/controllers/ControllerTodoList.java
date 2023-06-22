package com.lertos.workaiotool.controllers;

import com.lertos.workaiotool.model.Data;
import com.lertos.workaiotool.model.TodoItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.FileNotFoundException;

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
    private Label lblClickHint;

    private boolean isDeleteModeEnabled;

    private static final ObservableList<TodoItem> todoItems = FXCollections.observableArrayList(
            new TodoItem(false, "First line"),
            new TodoItem(false, "Second line"),
            new TodoItem(false, "Third line"),
            new TodoItem(false, "Fourth line")
    );
    private final DataFormat TODO_ITEM_DATA_FORMAT = new DataFormat("format-todo-item");
    private final double SPACING_BUFFER = 20.0;

    @FXML
    public void initialize() {
        //Make sure buttons are in the correct start states
        enableDeletionMode(false);

        //Setup and create the list of TodoItems
        ListView<TodoItem> itemsListView = new ListView<>(todoItems);

        itemsListView.setCellFactory(param -> {
            try {
                return new TodoItemCell() {
                    @Override
                    protected void updateItem(TodoItem item, boolean empty) {
                        super.updateItem(item, empty);

                        setText(null);

                        if (empty) {
                            setGraphic(null);
                        } else {
                            //Make sure the HBox never gets squishes off the screen
                            hbox.setPrefWidth(param.getWidth() - SPACING_BUFFER);
                            hbox.setMaxWidth(param.getWidth() - SPACING_BUFFER);

                            //Make the label wrap the text so the entire text can be seen
                            label.setWrapText(true);
                            label.setText(item != null ? item.getDescription() : "");

                            //When checked, the item is completed; otherwise it's still active
                            if (item.isDone()) {
                                checkBox.setSelected(true);
                                label.setTextFill(Data.getInstance().TEXT_INACTIVE);
                            } else {
                                checkBox.setSelected(false);
                                label.setTextFill(Data.getInstance().TEXT_ACTIVE);
                            }

                            //This will make sure the row is shown as expected, with each element in the right order horizontally
                            setGraphic(hbox);
                        }
                    }
                };
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        vboxTodoList.getChildren().add(0, itemsListView);

        //Refresh the list so the new cell dimensions are updated from the "current getWidth()" call in the updateItem method
        vboxTodoList.widthProperty().addListener((obs, oldVal, newVal) -> {
            itemsListView.refresh();
        });
    }

    //Enables deleting of rows simply by clicking the rows
    private void enableDeletionMode(boolean value) {
        isDeleteModeEnabled = value;

        //Set the hint label to the text that makes sense for the current state
        if (value) {
            lblClickHint.setText("To DELETE, click the text of a row");
            lblClickHint.setTextFill(Data.getInstance().TEXT_ERROR);
        } else {
            lblClickHint.setText("To EDIT, click the text of a row");
            lblClickHint.setTextFill(Data.getInstance().TEXT_ACTIVE);
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
        todoItems.removeIf(item -> item.isDone() == true);
    }

    @FXML
    private void onAddClicked() {
        System.out.println("Add");
    }

    @FXML
    private void onFinishDeletingClicked() {
        enableDeletionMode(false);
    }

    private class TodoItemCell extends ListCell<TodoItem> {
        HBox hbox = new HBox();
        CheckBox checkBox = new CheckBox();
        Label label = new Label();

        public TodoItemCell() throws FileNotFoundException {
            super();

            hbox.getChildren().addAll(checkBox, label);
            hbox.setSpacing(4);
            hbox.setHgrow(label, Priority.ALWAYS);

            //Add the label listener to either edit or delete a row
            label.setOnMouseClicked(event -> {
                //If delete mode is ON - a click means delete and add to history queue
                if (isDeleteModeEnabled) {
                    System.out.println("Deleted");
                }
                //If delete mode is OFF - a click means they wish to edit the text, so show a dialogue to change it
                else {
                    System.out.println("Edit");
                }
            });

            //When checked, the item is completed; otherwise it's still active
            checkBox.setOnAction(event -> {
                if (getItem().isDone())
                    label.setTextFill(Data.getInstance().TEXT_ACTIVE);
                else
                    label.setTextFill(Data.getInstance().TEXT_INACTIVE);
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

                    TodoItem temp = todoItems.get(draggedIdx);
                    todoItems.set(draggedIdx, todoItems.get(thisIdx));
                    todoItems.set(thisIdx, temp);

                    success = true;
                }
                event.setDropCompleted(success);
                event.consume();
            });
            setOnDragDone(DragEvent::consume);
        }
    }
}
