package com.lertos.workaiotool.model;

import com.lertos.workaiotool.model.items.FolderItem;
import com.lertos.workaiotool.model.items.TodoItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

public class Data {

    private static Data instance;

    private ObservableList<TodoItem> activeTodoItems;
    private ObservableList<TodoItem> historyTodoItems;
    private ObservableList<FolderItem> activeFolderItems;
    private ObservableList<FolderItem> historyFolderItems;

    public final Color TEXT_ACTIVE = Color.BLACK;
    public final Color TEXT_INACTIVE = Color.DARKGRAY;
    public final Color TEXT_ERROR = Color.RED;
    public final String DELETE_BUTTON_PATH = "./src/main/resources/images/delete.png";
    //Giving different options for spacing throughout the system to keep the look consistent
    public final double DEFAULT_BUTTON_SPACING = 10;
    public final double DEFAULT_GRID_PANE_SPACING = 10;
    public final double DEFAULT_CONTROL_SPACING = 10;
    public final double DEFAULT_CONTAINER_SPACING = 10;
    public final double BUTTON_ICON_SIZE = 18.0;
    public final double BUTTON_PADDING_SIZE = 4.0;
    public final double SPACING_BUFFER = 20.0;

    private Data() {
    }

    public static Data getInstance() {
        if (instance == null)
            instance = new Data();
        return instance;
    }

    public ObservableList<TodoItem> getActiveTodoItems() {
        //If list is empty, load it
        if (activeTodoItems == null) {
            //TODO: Load this from a save/data file; if no file is found, load an empty list
            activeTodoItems = FXCollections.observableArrayList();

            //TODO: These are simply for testing
            activeTodoItems.add(new TodoItem(false, "First line", "Additional text 1"));
            activeTodoItems.add(new TodoItem(false, "Second line", "Additional text 2"));
            activeTodoItems.add(new TodoItem(false, "Third line", "Additional text 3"));
            activeTodoItems.add(new TodoItem(false, "Fourth line", "Additional text 4"));
        }
        return activeTodoItems;
    }

    public ObservableList<TodoItem> getHistoryTodoItems() {
        //If list is empty, load it
        if (historyTodoItems == null) {
            //TODO: Load this from a save/data file; if no file is found, load an empty list
            historyTodoItems = FXCollections.observableArrayList();
        }
        return historyTodoItems;
    }

    public ObservableList<FolderItem> getActiveFolderItems() {
        //If list is empty, load it
        if (activeFolderItems == null) {
            //TODO: Load this from a save/data file; if no file is found, load an empty list
            activeFolderItems = FXCollections.observableArrayList();

            //TODO: These are simply for testing
            activeFolderItems.add(new FolderItem("First button", "C:\\Users"));
            activeFolderItems.add(new FolderItem("Second button", "C:\\Users\\Public"));
            activeFolderItems.add(new FolderItem("Third button", "C:\\Users\\Public\\Documents"));
            activeFolderItems.add(new FolderItem("Fourth button", "C:\\Users\\Public\\Music"));
        }
        return activeFolderItems;
    }

    public ObservableList<FolderItem> getHistoryFolderItems() {
        //If list is empty, load it
        if (historyFolderItems == null) {
            //TODO: Load this from a save/data file; if no file is found, load an empty list
            historyFolderItems = FXCollections.observableArrayList();
        }
        return historyFolderItems;
    }
}
