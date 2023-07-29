package com.lertos.workaiotool.model;

public class FileManager {
    private DataFile todoItemsFile;
    private DataFile folderItemsFile;
    private DataFile copyItemsFile;
    private DataFile promoteItemsFile;
    private DataFile infoItemsFile;
    private DataFile sqlCompareItemsFile;

    public FileManager() {
        this.todoItemsFile = new DataFile("todo.ser");
        this.folderItemsFile = new DataFile("folder.ser");
        this.copyItemsFile = new DataFile("copy.ser");
        this.promoteItemsFile = new DataFile("promote.ser");
        this.infoItemsFile = new DataFile("info.ser");
        this.sqlCompareItemsFile = new DataFile("sqlCompare.ser");
    }

    public DataFile getTodoItemsFile() {
        return todoItemsFile;
    }

    public DataFile getFolderItemsFile() {
        return folderItemsFile;
    }

    public DataFile getCopyItemsFile() {
        return copyItemsFile;
    }

    public DataFile getPromoteItemsFile() {
        return promoteItemsFile;
    }

    public DataFile getInfoItemsFile() {
        return infoItemsFile;
    }

    public DataFile getSqlCompareItemsFile() {
        return sqlCompareItemsFile;
    }
}