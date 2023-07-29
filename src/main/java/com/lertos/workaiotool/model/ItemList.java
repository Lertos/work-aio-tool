package com.lertos.workaiotool.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.util.List;

public class ItemList<T> implements Serializable {

    private final String fileName;
    private ObservableList<T> activeItems;
    private ObservableList<T> historyItems;

    public ItemList(String fileName, List<T> activeItems, List<T> historyItems) {
        this.fileName = fileName;

        this.activeItems = FXCollections.observableArrayList(activeItems);
        this.historyItems = FXCollections.observableArrayList(historyItems);
    }

    public ObservableList<T> getActiveItems() {
        return activeItems;
    }

    public ObservableList<T> getHistoryItems() {
        return historyItems;
    }

    public void addItem(T item) {
        activeItems.add(item);

        saveFile();
    }

    public void moveItemToHistory(T item) {
        activeItems.remove(item);
        historyItems.add(item);

        saveFile();
    }

    //Returns the size of the history list after the movement
    public int restoreItemFromHistory() {
        if (historyItems.size() > 0) {
            T item = historyItems.remove(historyItems.size() - 1);
            activeItems.add(item);

            saveFile();

            return historyItems.size();
        }
        return 0;
    }

    public void saveFile() {
        Data.getInstance().getFileManager().getDataFile(fileName).saveToFile(this);
    }
}
