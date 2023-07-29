package com.lertos.workaiotool.model;

import java.util.HashMap;

public class FileManager {

    private HashMap<String, DataFile> dataFiles;

    public FileManager() {
        this.dataFiles = new HashMap<>();
    }

    public void addDataFile(String fileName) {
        dataFiles.put(fileName, new DataFile(fileName + ".ser"));
    }

    public DataFile getDataFile(String fileName) {
        return dataFiles.get(fileName);
    }

}