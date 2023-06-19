package com.lertos.workaiotool.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;

import java.io.Serializable;

public class ControllerTodoList {

    private static final ObservableList<Category> categories = FXCollections.observableArrayList(
            new Category("1", "First line"),
            new Category("2", "Second line"),
            new Category("3", "Third line"),
            new Category("4", "Fourth line")
    );
    private final DataFormat CATEGORY_DATA_FORMAT = new DataFormat("lertos-category");
    @FXML
    private VBox vboxTodoList;

    @FXML
    public void initialize() {
        ListView<Category> categoryListView = new ListView<>(categories);
        categoryListView.setCellFactory(param -> new CategoryCell());

        vboxTodoList.getChildren().add(categoryListView);
    }

    public record Category(
            String name,
            String description

    ) implements Serializable {
    }

    private final class CategoryCell extends ListCell<Category> {
        public CategoryCell() {

            setOnDragDetected(event -> {
                if (getItem() == null)
                    return;

                Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();

                content.putString(getItem().toString());
                content.put(CATEGORY_DATA_FORMAT, getItem());

                dragboard.setContent(content);

                event.consume();
            });

            setOnDragOver(event -> {
                if (event.getGestureSource() != this && event.getDragboard().hasContent(CATEGORY_DATA_FORMAT)) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }
                event.consume();
            });

            setOnDragEntered(event -> {
                if (event.getGestureSource() != this && event.getDragboard().hasContent(CATEGORY_DATA_FORMAT)) {
                    setOpacity(0.3);
                }
            });

            setOnDragExited(event -> {
                if (event.getGestureSource() != this && event.getDragboard().hasContent(CATEGORY_DATA_FORMAT)) {
                    setOpacity(1);
                }
            });

            setOnDragDropped(event -> {
                if (getItem() == null)
                    return;

                Dragboard db = event.getDragboard();
                boolean success = false;

                if (db.hasContent(CATEGORY_DATA_FORMAT)) {
                    Category droppedCategory = (Category) db.getContent(CATEGORY_DATA_FORMAT);
                    ObservableList<Category> items = getListView().getItems();
                    int draggedIdx = items.indexOf(droppedCategory);
                    int thisIdx = items.indexOf(getItem());

                    Category temp = categories.get(draggedIdx);
                    categories.set(draggedIdx, categories.get(thisIdx));
                    categories.set(thisIdx, temp);

                    success = true;
                }
                event.setDropCompleted(success);
                event.consume();
            });

            setOnDragDone(DragEvent::consume);
        }

        @Override
        protected void updateItem(Category item, boolean empty) {
            super.updateItem(item, empty);

            setText((empty || item == null) ? null : item.name + "\n" + item.description);
        }
    }
}
