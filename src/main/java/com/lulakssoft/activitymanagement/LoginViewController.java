package com.lulakssoft.activitymanagement;

import com.lulakssoft.activitymanagement.User.Admin;
import com.lulakssoft.activitymanagement.User.User;
import com.lulakssoft.activitymanagement.User.UserManager;
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
import java.util.Optional;

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


    private Border defaultBorder;

    @FXML
    public void initialize() {
        defaultBorder = usernameField.getBorder();

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
        UserManager userManager = UserManager.getInstance();

        Optional<User> userOptional = userManager.findUserByUsername(username);
        if (userOptional.isPresent() && decodePassword(userOptional.get().getPassword()).equals(password)) {
            userManager.setCurrentUser(userOptional.get());
            createProjectView();
            return;
        }
        changeLabelInformation("Login failed", Color.RED);
    }

    private void handleRegisterButton() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        UserManager userManager = UserManager.getInstance();

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

        if (userManager.findUserByUsername(username).isPresent()) {
            changeLabelInformation("Username already exists", Color.RED);
            return;
        }
        if (username.length() < 3 || password.length() < 3) {
            changeLabelInformation("Username and password must be at least 3 characters long", Color.RED);
            return;
        }
        if (username.length() > 20 || password.length() > 20) {
            changeLabelInformation("Username and password must be at most 20 characters long", Color.RED);
            return;
        }

        // Erstelle einen neuen Benutzer und füge ihn zur Liste hinzu
        User newUser = new Admin(username, password);
        userManager.addUser(newUser);
        changeLabelInformation("User created", Color.GREEN);
    }

    private void createProjectView() {

        try {
            // Erstelle ein neues Fenster für die Projektansicht
            Stage stage = new Stage();
            SceneManager sceneManager = SceneManager.getInstance();
            ProjectViewController controller = sceneManager.loadFXML(SceneManager.PROJECT_VIEW);
            controller.initialize();

            stage.setScene(new Scene(sceneManager.getRoot(SceneManager.PROJECT_VIEW)));
            stage.setTitle("Project View");
            stage.initModality(APPLICATION_MODAL);
            stage.showAndWait();

            if (!controller.loggedIn) {
                usernameField.clear();
                passwordField.clear();
                return;
            }

            // Schließe die Anwendung
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
    }
}
