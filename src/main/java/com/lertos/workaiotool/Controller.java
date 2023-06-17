package com.lertos.workaiotool;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class Controller {

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
    @FXML
    private Label welcomeText;

    public void setListeners(Scene scene) {
        scene.setOnKeyPressed(event -> {
            //TODO: Will most likely need to check if the focused item is a text field and return if so
            switch (event.getCode()) {
                case DIGIT1 -> switchTab(tabTodo);
                case DIGIT2 -> switchTab(tabFolders);
                case DIGIT3 -> switchTab(tabCopy);
                case DIGIT4 -> switchTab(tabPromoter);
                case DIGIT5 -> switchTab(tabInfo);
                case DIGIT6 -> switchTab(tabSQLCompare);
            }
        });
    }

    private void switchTab(Tab tab) {
        tabPaneMain.getSelectionModel().select(tab);
    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}