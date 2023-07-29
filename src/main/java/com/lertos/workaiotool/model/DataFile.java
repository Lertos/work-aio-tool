package com.lertos.workaiotool.model;

import java.io.*;

public class DataFile {

    private final String fileName;

    public DataFile(String fileName) {
        this.fileName = fileName;
    }

    public void saveToFile(ItemList obj) {
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            ObjectOutput out = new ObjectOutputStream(fos);

            out.writeObject(obj);
            out.flush();
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(fileName + " === FILE SAVED");
    }

    public ItemList loadFromFile() {
        ItemList obj;

        try (FileInputStream fis = new FileInputStream(fileName)) {
            ObjectInputStream ois = new ObjectInputStream(fis);

            obj = (ItemList) ois.readObject();
            ois.close();
        } catch (ClassNotFoundException e) {
            System.out.println(fileName + " === CLASS NOT FOUND; LOADING DEFAULT");
            return new ItemList<>(fileName);
        } catch (FileNotFoundException e) {
            System.out.println(fileName + " === FILE NOT FOUND; LOADING DEFAULT");
            return new ItemList<>(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(fileName + " === IOException; LOADING DEFAULT");
            return new ItemList<>(fileName);
        }
        System.out.println(fileName + " === FILE LOADED");

        return obj;
    }
}