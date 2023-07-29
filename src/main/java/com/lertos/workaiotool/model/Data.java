package com.lertos.workaiotool.model;

import com.lertos.workaiotool.model.items.*;

import java.util.ArrayList;

public class Data {

    private static Data instance;
    private FileManager fileManager;

    public ItemList<TodoItem> todoItems;
    public ItemList<FolderItem> folderItems;
    public ItemList<CopyItem> copyItems;
    public ItemList<PromoteItem> promoteItems;
    public ItemList<InfoItem> infoItems;
    public ItemList<SQLCompareItem> sqlCompareItems;
    public final String FILE_NAME_TODO = "todo";
    public final String FILE_NAME_FOLDER = "folder";
    public final String FILE_NAME_COPY = "copy";
    public final String FILE_NAME_PROMOTE = "promote";
    public final String FILE_NAME_INFO = "info";
    public final String FILE_NAME_SQL_COMPARE = "sqlCompare";

    private Data() {
        fileManager = new FileManager();

        fileManager.addDataFile(FILE_NAME_TODO);
        fileManager.addDataFile(FILE_NAME_FOLDER);
        fileManager.addDataFile(FILE_NAME_COPY);
        fileManager.addDataFile(FILE_NAME_PROMOTE);
        fileManager.addDataFile(FILE_NAME_INFO);
        fileManager.addDataFile(FILE_NAME_SQL_COMPARE);

        //Load the lists from the files, or provide defaults
        todoItems = fileManager.getDataFile(FILE_NAME_TODO).loadFromFile();
        folderItems = fileManager.getDataFile(FILE_NAME_FOLDER).loadFromFile();
        copyItems = fileManager.getDataFile(FILE_NAME_COPY).loadFromFile();
        promoteItems = fileManager.getDataFile(FILE_NAME_PROMOTE).loadFromFile();
        infoItems = fileManager.getDataFile(FILE_NAME_INFO).loadFromFile();
        sqlCompareItems = fileManager.getDataFile(FILE_NAME_SQL_COMPARE).loadFromFile();

        //Testing items
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

        promoteItems.getActiveItems().add(new PromoteItem("Promote Item 1", PromoteItem.PathTypes.PROVIDE_FILE_NAMES_SEPARATELY, PromoteItem.PromoteType.COPY));
        promoteItems.getActiveItems().get(0).getOriginPaths().add("C:/Users/Dylan/Downloads/one");
        promoteItems.getActiveItems().get(0).getDestinationPaths().add("C:/Users/Dylan/Downloads/two");
        promoteItems.getActiveItems().get(0).getFileNames().add("1.txt");
        promoteItems.getActiveItems().get(0).getFileNames().add("2.txt");

        promoteItems.getActiveItems().add(new PromoteItem("Promote Item 2", PromoteItem.PathTypes.PROVIDE_FILE_NAMES_IN_PATHS, PromoteItem.PromoteType.MOVE));
        promoteItems.getActiveItems().get(1).getOriginPaths().add("C:/Users/Dylan/Downloads/one/1.txt");
        promoteItems.getActiveItems().get(1).getOriginPaths().add("C:/Users/Dylan/Downloads/one/2.txt");
        promoteItems.getActiveItems().get(1).getDestinationPaths().add("C:/Users/Dylan/Downloads/two/1.txt");
        promoteItems.getActiveItems().get(1).getDestinationPaths().add("C:/Users/Dylan/Downloads/two/2.txt");

        infoItems.getActiveItems().add(new InfoItem("First Info Item", "Additional Text 1"));
        infoItems.getActiveItems().add(new InfoItem("Second Info Item", "Additional Text 2"));

        ArrayList<String> databases = new ArrayList<>();
        databases.add("testworld");
        databases.add("testworld2");
        ArrayList<ItemSQL> itemsSQL = new ArrayList<>();
        ItemSQL itemSQL = new ItemSQL("testTab", "localhost", -1, "root", "", databases);
        itemsSQL.add(itemSQL);
        SQLCompareItem sqlCompareItem = new SQLCompareItem("Test Compare", "dummy_proc", SQLCompareItem.SQLType.MYSQL, itemsSQL);
        sqlCompareItems.getActiveItems().add(sqlCompareItem);
    }

    public static Data getInstance() {
        if (instance == null)
            instance = new Data();
        return instance;
    }

    public FileManager getFileManager() {
        return fileManager;
    }

}
