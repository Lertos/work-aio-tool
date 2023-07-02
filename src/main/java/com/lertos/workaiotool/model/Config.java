package com.lertos.workaiotool.model;

import javafx.scene.paint.Color;

public class Config {

    private static Config instance;

    //Colors
    public final Color TEXT_ACTIVE = Color.BLACK;
    public final Color TEXT_INACTIVE = Color.DARKGRAY;
    public final Color TEXT_ERROR = Color.RED;

    //Image paths
    public final String DELETE_BUTTON_PATH = "./src/main/resources/images/delete.png";
    public final String EDIT_BUTTON_PATH = "./src/main/resources/images/edit.png";

    //Giving different options for spacing throughout the system to keep the look consistent
    public final double DEFAULT_BUTTON_SPACING = 10;
    public final double DEFAULT_GRID_PANE_SPACING = 10;
    public final double DEFAULT_CONTROL_SPACING = 10;
    public final double DEFAULT_CONTAINER_SPACING = 10;
    public final double BUTTON_ICON_SIZE = 18.0;
    public final double BUTTON_PADDING_SIZE = 4.0;
    public final double SPACING_BUFFER = 20.0;

    private Config() {
    }

    public static Config getInstance() {
        if (instance == null)
            instance = new Config();
        return instance;
    }

}
