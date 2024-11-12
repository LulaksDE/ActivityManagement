package com.lulakssoft.activitymanagement;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Base64;
import java.util.List;

public class LoginViewController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    private List<User> userList;

    @FXML
    public void initialize(List<User> userList) {
        this.userList = userList;

        loginButton.setOnAction(event -> {
            handleLoginButton();
        });

        registerButton.setOnAction(event -> {
            handleRegisterButton();
        });
    }

    private void handleLoginButton() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        for (User user : userList) {
            if (user.getUsername().equals(username) && decodePassword(user.getPassword()).equals(password)) {
                System.out.println("Login successful");
                createProjectView(user);
                return;
            }
        }

        System.out.println("Login failed");
    }

    private void handleRegisterButton() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        for (User user : userList) {
            if (user.getUsername().equals(username)) {
                System.out.println("Username already exists");
                return;
            }
        }

        try {
            userList.add(new Admin(username, password));
            System.out.println("User registered");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createProjectView(User user) {

        try {
            // Erstelle ein neues Fenster für die Projektansicht
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ProjectView.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle("Project View");
            ProjectViewController controller = loader.getController();
            controller.initialize(userList, user);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String decodePassword(String password) {
        return new String(Base64.getDecoder().decode(password));
    }
}
