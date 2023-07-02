package com.lertos.workaiotool.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ItemList<T> {

    private ObservableList<T> activeItems;
    private ObservableList<T> historyItems;

    public ItemList() {
        activeItems = FXCollections.observableArrayList();
        historyItems = FXCollections.observableArrayList();
    }

    public ObservableList<T> getActiveItems() {
        return activeItems;
    }

    public ObservableList<T> getHistoryItems() {
        return historyItems;
    }

}
