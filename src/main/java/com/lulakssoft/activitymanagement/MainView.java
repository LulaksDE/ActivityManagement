package com.lulakssoft.activitymanagement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class MainView extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        AppContext appContext;
        SceneManager sceneManager;
        // Setup managers
        sceneManager = SceneManager.getInstance();
        sceneManager.setPrimaryStage(primaryStage);
        appContext = AppContext.getInstance();

        // Create users
        List<User> userList = new ArrayList<>();
        userList.add(new Admin("admin01", "admin"));
        userList.add(new Worker("employee01"));
        userList.add(new Worker("employee02"));
        userList.add(new Supporter("support01"));
        userList.add(new Supporter("support02"));
        userList.add(new Technician("technician01"));

        appContext.setUserList(userList);


        // Load initial screen
        sceneManager.loadScene("LoginScreen.fxml", "Activity Management System");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
