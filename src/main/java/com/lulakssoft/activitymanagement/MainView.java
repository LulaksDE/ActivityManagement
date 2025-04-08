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
        List<User> userList = new ArrayList<>();

        // Create some sample users
        userList.add(new Admin("admin01", "admin"));
        userList.add(new Worker("employee01"));
        userList.add(new Worker("employee02"));
        userList.add(new Supporter("support01"));
        userList.add(new Supporter("support02"));
        userList.add(new Technician("technician01"));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginScreen.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Activity Management System");
        LoginViewController controller = loader.getController();
        controller.initialize(userList);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
