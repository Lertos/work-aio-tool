package com.lertos.workaiotool.model;

import com.lertos.workaiotool.model.items.CopyItem;
import com.lertos.workaiotool.model.items.FolderItem;
import com.lertos.workaiotool.model.items.TodoItem;

public class Data {

    private static Data instance;

    public ItemList<TodoItem> todoItems;
    public ItemList<FolderItem> folderItems;
    public ItemList<CopyItem> copyItems;

    private Data() {
        todoItems = new ItemList<>();
        folderItems = new ItemList<>();
        copyItems = new ItemList<>();

        todoItems.getActiveItems().add(new TodoItem(false, "First line", "Additional text 1"));
        todoItems.getActiveItems().add(new TodoItem(false, "Second line", "Additional text 2"));
        todoItems.getActiveItems().add(new TodoItem(false, "Third line", "Additional text 3"));
        todoItems.getActiveItems().add(new TodoItem(false, "Fourth line", "Additional text 4"));

        folderItems.getActiveItems().add(new FolderItem("First button", "C:\\Users"));
        folderItems.getActiveItems().add(new FolderItem("Second button", "C:\\Users\\Public"));
        folderItems.getActiveItems().add(new FolderItem("Third button", "C:\\Users\\Public\\Documents"));
        folderItems.getActiveItems().add(new FolderItem("Fourth button", "C:\\Users\\Public\\Music"));

        copyItems.getActiveItems().add(new CopyItem("Copy button 1", "Text to copy 1"));
        copyItems.getActiveItems().add(new CopyItem("Copy button 2", "Text to copy 2"));
        copyItems.getActiveItems().add(new CopyItem("Copy button 3", "Text to copy 3"));
    }

    public static Data getInstance() {
        if (instance == null)
            instance = new Data();
        return instance;
    }

}
