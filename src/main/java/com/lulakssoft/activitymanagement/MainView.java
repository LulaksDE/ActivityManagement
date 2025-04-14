package com.lulakssoft.activitymanagement;

import javafx.application.Application;
import javafx.stage.Stage;

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
