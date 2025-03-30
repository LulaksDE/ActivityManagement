package com.lulakssoft.activitymanagement.controller;

import com.lulakssoft.activitymanagement.Admin;
import com.lulakssoft.activitymanagement.AppContext;
import com.lulakssoft.activitymanagement.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Base64;
import java.util.List;

import static javafx.stage.Modality.APPLICATION_MODAL;

public class LoginViewController {

    @FXML
    private Label loginInformationLabel;
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    private List<User> userList;

    private Border defaultBorder;
    private final AppContext appContext = AppContext.getInstance();


    @FXML
    public void initialize() {
        defaultBorder = usernameField.getBorder();

        loginButton.setOnAction(event -> handleLoginButton());
        registerButton.setOnAction(event -> handleRegisterButton());
    }

    public void setUserList(List<User> userList) {
        appContext.setUserList(userList);
    }

    private void handleLoginButton() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        for (User user : appContext.getUserList()) {
            if (user.getUsername().equals(username) && decodePassword(user.getPassword()).equals(password)) {
                appContext.setCurrentUser(user);
                createProjectView(user);
                return;
            }
        }
        changeLabelInformation("Login failed", Color.RED);
    }

    private void handleRegisterButton() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isBlank() || password.isBlank()) {
            changeLabelInformation("Please enter username and password", Color.RED);
            if (username.isBlank()) {
                usernameField.requestFocus();
                usernameField.selectAll();
                usernameField.borderProperty().set(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            } else {
                passwordField.requestFocus();
                passwordField.borderProperty().set(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.DOTTED, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            }
            return;
        } else {
            usernameField.borderProperty().set(defaultBorder);
            passwordField.borderProperty().set(defaultBorder);
        }

        List<User> userList = appContext.getUserList();
        for (User user : userList) {
            if (user.getUsername().equals(username)) {
                changeLabelInformation("User with that name already exists", Color.RED);
                usernameField.requestFocus();
                usernameField.borderProperty().set(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                return;
            }
        }

        try {
            User newUser = new Admin(username, password);
            userList.add(newUser);
            appContext.setUserList(userList);
            changeLabelInformation("User " + username + " created", Color.GREEN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createProjectView(User user) {
        try {
            // Create a new window for the project view
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ProjectView.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle("Project View");
            stage.initModality(APPLICATION_MODAL);

            ProjectViewController controller = loader.getController();
            controller.initData(appContext.getUserList(), user);

            stage.showAndWait();

            if (!controller.loggedIn) {
                usernameField.clear();
                passwordField.clear();
                return;
            }

            // Close the application
            Stage loginStage = (Stage) loginButton.getScene().getWindow();
            loginStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changeLabelInformation(String text, Color color) {
        loginInformationLabel.setText(text);
        loginInformationLabel.setTextFill(color);
    }

    private String decodePassword(String password) {
        return new String(Base64.getDecoder().decode(password));
    }}
