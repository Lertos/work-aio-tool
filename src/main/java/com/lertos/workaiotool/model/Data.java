package com.lertos.workaiotool.model;

import com.lertos.workaiotool.model.items.*;

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
