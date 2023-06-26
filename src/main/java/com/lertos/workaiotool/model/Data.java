package com.lertos.workaiotool.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

public class Data {

    private static Data instance;

    private ObservableList<TodoItem> activeTodoItems;
    private ObservableList<TodoItem> historyTodoItems;

    public final Color TEXT_ACTIVE = Color.BLACK;
    public final Color TEXT_INACTIVE = Color.DARKGRAY;
    public final Color TEXT_ERROR = Color.RED;
    public final String DELETE_BUTTON_PATH = "./src/main/resources/images/delete.png";
    //Giving different options for spacing throughout the system to keep the look consistent
    public final double DEFAULT_BUTTON_SPACING = 10;
    public final double DEFAULT_CONTROL_SPACING = 10;
    public final double DEFAULT_CONTAINER_SPACING = 10;

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
            activeTodoItems.add(new TodoItem(false, "First line"));
            activeTodoItems.add(new TodoItem(false, "Second line"));
            activeTodoItems.add(new TodoItem(false, "Third line"));
            activeTodoItems.add(new TodoItem(false, "Fourth line"));
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
}
