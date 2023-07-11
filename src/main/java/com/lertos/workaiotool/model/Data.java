package com.lertos.workaiotool.model;

import com.lertos.workaiotool.model.items.CopyItem;
import com.lertos.workaiotool.model.items.FolderItem;
import com.lertos.workaiotool.model.items.PromoteItem;
import com.lertos.workaiotool.model.items.TodoItem;

public class Data {

    private static Data instance;

    public ItemList<TodoItem> todoItems;
    public ItemList<FolderItem> folderItems;
    public ItemList<CopyItem> copyItems;
    public ItemList<PromoteItem> promoteItems;

    private Data() {
        todoItems = new ItemList<>();
        folderItems = new ItemList<>();
        copyItems = new ItemList<>();
        promoteItems = new ItemList<>();

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

        promoteItems.getActiveItems().add(new PromoteItem("Promote item 1", PromoteItem.PathTypes.ORIGIN_KNOWN_DEST_UNKNOWN, PromoteItem.PromoteType.MOVE));
        promoteItems.getActiveItems().get(0).getOriginPaths().add("/my/path/here");
        promoteItems.getActiveItems().get(0).getOriginPaths().add("/my/second/path/here");
    }

    public static Data getInstance() {
        if (instance == null)
            instance = new Data();
        return instance;
    }

}
