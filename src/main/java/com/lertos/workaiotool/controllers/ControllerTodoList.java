package com.lertos.workaiotool.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.Serializable;

public class ControllerTodoList {

    @FXML
    private VBox vboxTodoList;

    private static final ObservableList<TodoItem> todoItems = FXCollections.observableArrayList(
            new TodoItem("1", "First line"),
            new TodoItem("2", "Second line"),
            new TodoItem("3", "Third line"),
            new TodoItem("4", "Fourth line")
    );
    private final DataFormat TODO_ITEM_DATA_FORMAT = new DataFormat("format-todo-item");

    @FXML
    public void initialize() {
        ListView<TodoItem> categoryListView = new ListView<>(todoItems);
        categoryListView.setCellFactory(param -> new TodoItemCell());

        vboxTodoList.getChildren().add(categoryListView);
    }

    public record TodoItem(
            String name,
            String description

    ) implements Serializable {
    }

    private final class TodoItemCell extends ListCell<TodoItem> {
        HBox hbox = new HBox();
        Label label = new Label();
        Pane pane = new Pane();
        Button button = new Button();

        public TodoItemCell() {
            super();

            hbox.getChildren().addAll(label, pane, button);
            hbox.setHgrow(pane, Priority.ALWAYS); //Puts the label to the left and grows the pane so the button(s) will be push to the far right

            button.setOnAction(event -> System.out.println(getItem().name + " : " + event));

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

            setOnDragOver(event -> {
                if (event.getGestureSource() != this && event.getDragboard().hasContent(TODO_ITEM_DATA_FORMAT)) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }
                event.consume();
            });

            setOnDragEntered(event -> {
                if (event.getGestureSource() != this && event.getDragboard().hasContent(TODO_ITEM_DATA_FORMAT)) {
                    setOpacity(0.3);
                }
            });

            setOnDragExited(event -> {
                if (event.getGestureSource() != this && event.getDragboard().hasContent(TODO_ITEM_DATA_FORMAT)) {
                    setOpacity(1);
                }
            });

            setOnDragDropped(event -> {
                if (getItem() == null)
                    return;

                Dragboard db = event.getDragboard();
                boolean success = false;

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

        @Override
        protected void updateItem(TodoItem item, boolean empty) {
            super.updateItem(item, empty);

            setText(null);

            if (empty) {
                setGraphic(null);
            } else {
                label.setText(item != null ? item.name + "\n" + item.description : "");
                //This will make sure the row is shown as expected, with each element in the right order horizontally
                setGraphic(hbox);
            }
        }
    }
}
