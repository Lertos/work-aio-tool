package com.lertos.workaiotool.model;

import javafx.scene.paint.Color;

public class Data {

    private static Data instance;

    public final Color TEXT_ACTIVE = Color.BLACK;
    public final Color TEXT_INACTIVE = Color.DARKGRAY;
    public final Color TEXT_ERROR = Color.RED;
    public final Color TEXT_WARNING = Color.YELLOW;
    public final String DELETE_BUTTON_PATH = "./src/main/resources/images/delete.png";

    private Data() {
    }

    public static Data getInstance() {
        if (instance == null)
            instance = new Data();
        return instance;
    }
}
