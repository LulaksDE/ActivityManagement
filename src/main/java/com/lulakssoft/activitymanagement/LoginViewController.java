package com.lulakssoft.activitymanagement;

import com.lulakssoft.activitymanagement.user.Privilages;
import com.lulakssoft.activitymanagement.user.role.RoleFactory;
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




    private Border defaultBorder;

    @FXML
    public void initialize() {
        defaultBorder = usernameField.getBorder();

        loginButton.setOnAction(event -> handleLoginButton());
    }

    private void handleLoginButton() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        UserManager userManager = UserManager.INSTANCE;

        Optional<User> userOptional = userManager.findUserByUsername(username);
        if (userOptional.isPresent() && decodePassword(userOptional.get().getPassword()).equals(password)) {
            userManager.setCurrentUser(userOptional.get());
            createProjectView();
            return;
        }
        changeLabelInformation("Login failed", Color.RED);
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
