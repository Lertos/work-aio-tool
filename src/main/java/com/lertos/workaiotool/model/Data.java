package com.lertos.workaiotool.model;

public class Data {

    private static Data instance;

    private Data() {
    }

    public static Data getInstance() {
        if (instance == null)
            instance = new Data();
        return instance;
    }
}
