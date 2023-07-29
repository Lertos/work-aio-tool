package com.lertos.workaiotool.model;

import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataFile {

    private final String FILE_NAME;
    private final String FILE_EXTENSION = ".ser";
    private final String FILE_NAME_ACTIVE;
    private final String FILE_NAME_HISTORY;

    public DataFile(String fileName) {
        this.FILE_NAME = fileName;

        this.FILE_NAME_ACTIVE = FILE_NAME + "_active" + FILE_EXTENSION;
        this.FILE_NAME_HISTORY = FILE_NAME + "_history" + FILE_EXTENSION;
    }

    public void saveToFile(ItemList obj) {
        saveToSpecificFile(FILE_NAME_ACTIVE, obj.getActiveItems());
        saveToSpecificFile(FILE_NAME_HISTORY, obj.getHistoryItems());
    }

    private <T> void saveToSpecificFile(String fileName, ObservableList<T> list) {
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            ObjectOutput out = new ObjectOutputStream(fos);

            out.writeObject(new ArrayList<T>(list));
            out.flush();
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(fileName + " === FILE SAVED");
    }

    public <T> ItemList loadFromFile() {
        List<T> activeItems = loadFromSpecificFile(FILE_NAME_ACTIVE);
        List<T> historyItems = loadFromSpecificFile(FILE_NAME_HISTORY);

        return new ItemList<>(FILE_NAME, activeItems, historyItems);
    }

    public <T> List<T> loadFromSpecificFile(String fileName) {
        List<T> list;

        try (FileInputStream fis = new FileInputStream(fileName)) {
            ObjectInputStream ois = new ObjectInputStream(fis);

            list = (List<T>) ois.readObject();

            ois.close();
        } catch (ClassNotFoundException e) {
            System.out.println(fileName + " === CLASS NOT FOUND; LOADING DEFAULT");
            return new ArrayList<T>();
        } catch (FileNotFoundException e) {
            System.out.println(fileName + " === FILE NOT FOUND; LOADING DEFAULT");
            return new ArrayList<T>();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(fileName + " === IOException; LOADING DEFAULT");
            return new ArrayList<T>();
        }
        System.out.println(fileName + " === FILE LOADED");

        return list;
    }
}