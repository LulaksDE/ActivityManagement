package com.lulakssoft.activitymanagement.adapter.ui;

import com.lulakssoft.activitymanagement.domain.entities.user.Privileges;
import com.lulakssoft.activitymanagement.domain.entities.user.User;
import com.lulakssoft.activitymanagement.domain.entities.user.UserManager;
import com.lulakssoft.activitymanagement.domain.entities.user.role.RoleFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

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
    private ComboBox<Privileges> roleComboBox;

    @FXML
    private Button createUserButton;

    @FXML
    private Button closeButton;

    private UserManager userManager;

    @FXML
    public void initialize() {
        userManager = UserManager.INSTANCE;

        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        roleColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getRole().getRoleName()));

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

        roleComboBox.setItems(FXCollections.observableArrayList(Privileges.values()));

        loadUsers();

        createUserButton.setOnAction(event -> createUser());
        closeButton.setOnAction(event -> closeWindow());
    }

    private void loadUsers() {
        userTable.setItems(FXCollections.observableArrayList(userManager.getAllUsers()));
    }

    private void createUser() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        Privileges selectedRole = roleComboBox.getValue();

        if (username.isEmpty() || password.isEmpty() || selectedRole == null) {
            showAlert("Error", "Please enter all credentials.");
            return;
        }

        if (userManager.findUserByUsername(username).isPresent()) {
            showAlert("Error", "User already exists.");
            return;
        }

        User newUser = new User(username, password, RoleFactory.getRole(selectedRole));
        userManager.addUser(newUser);

        usernameField.clear();
        passwordField.clear();
        roleComboBox.getSelectionModel().clearSelection();

        loadUsers();
    }

    private void deleteUser(User user) {
        if (user.equals(userManager.getCurrentUser())) {
            showAlert("Error", "You can't delete your own user.");
            return;
        }

        userManager.removeUser(user);
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