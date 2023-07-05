package com.lertos.workaiotool.controllers;

import com.lertos.workaiotool.model.Config;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class ControllerMain {

    @FXML
    private TabPane tabPaneMain;
    @FXML
    private Tab tabTodo;
    @FXML
    private Tab tabFolders;
    @FXML
    private Tab tabCopy;
    @FXML
    private Tab tabPromoter;
    @FXML
    private Tab tabInfo;
    @FXML
    private Tab tabSQLCompare;

    private boolean isSaveTimerRunning = false;
    private double currentWidth = 0;
    private double currentHeight = 0;

    public void setListeners(Scene scene) {
        scene.setOnKeyPressed(event -> {
            //NOTE: It seems text fields properly grasp the focus and do not filter past it so this works as intended
            switch (event.getCode()) {
                case DIGIT1 -> switchTab(tabTodo);
                case DIGIT2 -> switchTab(tabFolders);
                case DIGIT3 -> switchTab(tabCopy);
                case DIGIT4 -> switchTab(tabPromoter);
                case DIGIT5 -> switchTab(tabInfo);
                case DIGIT6 -> switchTab(tabSQLCompare);
            }
        });

        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
            saveNewDimensions(scene);
        });

        scene.heightProperty().addListener((obs, oldVal, newVal) -> {
            saveNewDimensions(scene);
        });
    }

    private void saveNewDimensions(Scene scene) {
        currentWidth = scene.getWidth();
        currentHeight = scene.getHeight();

        if (!isSaveTimerRunning)
            startSaveTimer();
    }

    private void switchTab(Tab tab) {
        tabPaneMain.getSelectionModel().select(tab);
    }

    private void startSaveTimer() {
        Runnable task = () -> {
            try {
                isSaveTimerRunning = true;
                Thread.sleep(Config.getInstance().SAVE_TIMER_WINDOW);

                Platform.runLater(() -> {
                    isSaveTimerRunning = false;

                    //TODO: Implement the save
                    System.out.println("FINAL == " + currentWidth + " " + currentHeight);
                });
            } catch (InterruptedException event) {
            }
        };
        new Thread(task).start();
    }
}