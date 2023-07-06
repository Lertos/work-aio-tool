package com.lertos.workaiotool;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public final class Toast {

    private final static int TOAST_DELAY = 1250;
    private final static int FADE_IN_TIME = 250;
    private final static int FADE_OUT_TIME = 250;

    public static void makeText(Stage ownerStage, String toastMsg) {
        Stage toastStage = new Stage();

        toastStage.initOwner(ownerStage);
        toastStage.setResizable(false);
        toastStage.initStyle(StageStyle.TRANSPARENT);

        Text text = new Text(toastMsg);

        StackPane root = new StackPane(text);

        root.setStyle("-fx-background-radius: 20; -fx-background-color: rgba(0, 0, 0, 0.2); -fx-padding: 20px;");
        root.setOpacity(0);

        Scene scene = new Scene(root);

        scene.setFill(Color.TRANSPARENT);

        toastStage.setScene(scene);
        toastStage.show();

        //Position the popup in the center of the parent window
        double ownerWidth = ownerStage.getWidth();
        double toastWidth = toastStage.getWidth();
        double centerX = ownerWidth / 2.0 - toastWidth / 2.0;

        double ownerHeight = ownerStage.getHeight();
        double toastHeight = toastStage.getHeight();
        double centerY = ownerHeight / 2.0 - toastHeight / 2.0;

        toastStage.setX(ownerStage.getX() + centerX);
        toastStage.setY(ownerStage.getY() + centerY);

        //Set up the animations for fading in and out
        Timeline fadeInTimeline = new Timeline();
        KeyFrame fadeInKey = new KeyFrame(Duration.millis(FADE_IN_TIME), new KeyValue(toastStage.getScene().getRoot().opacityProperty(), 1));

        fadeInTimeline.getKeyFrames().add(fadeInKey);
        fadeInTimeline.setOnFinished((ae) -> {
            new Thread(() -> {
                try {
                    Thread.sleep(TOAST_DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Timeline fadeOutTimeline = new Timeline();
                KeyFrame fadeOutKey = new KeyFrame(Duration.millis(FADE_OUT_TIME), new KeyValue(toastStage.getScene().getRoot().opacityProperty(), 0));

                fadeOutTimeline.getKeyFrames().add(fadeOutKey);
                fadeOutTimeline.setOnFinished((aeb) -> toastStage.close());
                fadeOutTimeline.play();
            }).start();
        });
        fadeInTimeline.play();
    }
}