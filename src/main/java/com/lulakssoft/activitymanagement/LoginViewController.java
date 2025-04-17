package com.lulakssoft.activitymanagement;

import com.lulakssoft.activitymanagement.notification.LoggerFactory;
import com.lulakssoft.activitymanagement.notification.LoggerNotifier;
import com.lulakssoft.activitymanagement.user.User;
import com.lulakssoft.activitymanagement.user.UserManager;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Base64;
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

    private final LoggerNotifier logger = LoggerFactory.getLogger();


    @FXML
    public void initialize() {
        loginButton.setOnAction(event -> handleLoginButton());
    }

    private void handleLoginButton() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        UserManager userManager = UserManager.INSTANCE;
        LoggerNotifier logger = LoggerFactory.getLogger();

        logger.logInfo("Attempting to login user " + username + " with password " + password);

        Optional<User> userOptional = userManager.findUserByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Password is plain text for simplicity, in real applications use hashed passwords
            if (password.equals(user.getPassword())) {
                logger.logInfo("Login successful for user: " + username);
                userManager.setCurrentUser(user);
                createProjectView();
                return;
            } else {
                logger.logWarning("Login failed for user: " + username);
                changeLabelInformation("Login failed", Color.RED);
                usernameField.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
                passwordField.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
            }
        } else {
            logger.logWarning("User not found: " + username);
            usernameField.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
            passwordField.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
        }
        changeLabelInformation("Login failed", Color.RED);
    }

    private void createProjectView() {

        try {
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
            logger.logError("Error loading ProjectView", e);
            changeLabelInformation("Error loading ProjectView", Color.RED);
        }
    }

    private void changeLabelInformation(String text, Color color) {
        loginInformationLabel.setText(text);
        loginInformationLabel.setTextFill(color);
    }
}
