package com.lulakssoft.activitymanagement.adapter.notification;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Popup;
import javafx.stage.Window;
import javafx.util.Duration;

public final class Toast {
    private static Popup popup;

    public static Toast makeText(Window displayWindow, String message, int duration) {
        return new Toast(displayWindow, message, duration);
    }

    private final Window displayWindow;
    private final String message;
    private final int duration;

    private Toast(Window displayWindow, String message, int duration) {
        this.displayWindow = displayWindow;
        this.message = message;
        this.duration = duration;
    }

    public void show() {
        if (popup != null) {
            popup.hide();
        }

        popup = new Popup();
        popup.setAutoFix(true);
        popup.setAutoHide(true);

        Label label = new Label(message);
        label.getStyleClass().add("toast");
        label.setStyle("-fx-background-color: rgba(50, 50, 50, 0.8); " +
                "-fx-text-fill: white; " +
                "-fx-padding: 10px; " +
                "-fx-background-radius: 5px; " +
                "-fx-max-width: 400px; " +
                "-fx-min-height: 40px;");

        StackPane root = new StackPane(label);
        root.setAlignment(Pos.CENTER);
        popup.getContent().add(root);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(duration), ae -> popup.hide())
        );

        if (displayWindow != null) {
            popup.show(displayWindow,
                    (displayWindow.getX() + displayWindow.getWidth()/2) - 50,
                    displayWindow.getY() + displayWindow.getHeight() - 100);
            timeline.play();
        }
    }
}
