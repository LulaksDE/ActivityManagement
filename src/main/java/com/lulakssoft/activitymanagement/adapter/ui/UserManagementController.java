package com.lulakssoft.activitymanagement.adapter.ui;

import com.lulakssoft.activitymanagement.application.service.UserService;
import com.lulakssoft.activitymanagement.config.ApplicationContext;
import com.lulakssoft.activitymanagement.domain.model.user.Role;
import com.lulakssoft.activitymanagement.domain.model.user.RoleFactory;
import com.lulakssoft.activitymanagement.domain.model.user.RoleType;
import com.lulakssoft.activitymanagement.domain.model.user.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserManagementController {

    @FXML
    private TableView<User> userTable;

    @FXML
    private TableColumn<User, String> usernameColumn;

    @FXML
    private TableColumn<User, String> roleColumn;

    @FXML
    private TableColumn<User, Void> actionsColumn;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<RoleType> roleComboBox;

    @FXML
    private Button createUserButton;

    @FXML
    private Button closeButton;

    private final Logger logger = LoggerFactory.getLogger(UserManagementController.class);

    private UserService userService;

    @FXML
    public void initialize() {
        ApplicationContext context = ApplicationContext.getInstance();
        this.userService = context.getUserService();

        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        roleColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getRole().getName()));

        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete"); {
                deleteButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    deleteUser(user);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

        roleComboBox.setItems(FXCollections.observableArrayList(RoleFactory.getAllRoleTypes()));

        loadUsers();

        createUserButton.setOnAction(event -> createUser());
        closeButton.setOnAction(event -> closeWindow());
    }

    private void loadUsers() {
        userTable.setItems(FXCollections.observableArrayList(userService.findAllUsers()));
    }

    private void createUser() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        Role selectedRole = RoleFactory.createFromString(roleComboBox.getValue().toString());

        if (username.isEmpty() || password.isEmpty() || selectedRole == null) {
            showAlert("Error", "Please enter all credentials.");
            return;
        }

        if (userService.findUserByUsername(username) != null) {
            showAlert("Error", "User already exists.");
            return;
        }

        User newUser = new User(username, password, selectedRole);
        logger.info("Creating new user: " + newUser);
        userService.saveUser(newUser);

        usernameField.clear();
        passwordField.clear();
        roleComboBox.getSelectionModel().clearSelection();

        loadUsers();
    }

    private void deleteUser(User user) {
        if (user.equals(userService.findUserByUsername(user.getUsername()))) {
            showAlert("Error", "You can't delete your own user.");
            return;
        }

        userService.deleteUser(user.getId());
        loadUsers();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void closeWindow() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}