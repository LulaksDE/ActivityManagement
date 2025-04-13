package com.lulakssoft.activitymanagement;

import com.lulakssoft.activitymanagement.User.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class MainView extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        SceneManager sceneManager = SceneManager.getInstance();
        sceneManager.setMainScene(primaryStage, SceneManager.LOGIN_VIEW, "Activity Management System");
        LoginViewController controller = sceneManager.getLoader(SceneManager.LOGIN_VIEW).getController();
        controller.initialize();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
