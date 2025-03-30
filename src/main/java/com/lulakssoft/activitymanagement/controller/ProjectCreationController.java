package com.lulakssoft.activitymanagement.controller;

import com.lulakssoft.activitymanagement.Activity;
import com.lulakssoft.activitymanagement.AppContext;
import com.lulakssoft.activitymanagement.Project;
import com.lulakssoft.activitymanagement.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class ProjectCreationController extends BaseController {
    @FXML private TextField projectTitle;
    @FXML private TextArea projectDescription;
    @FXML private DatePicker dueDatePicker;
    @FXML private ListView<User> userListView;
    @FXML private TableView<User> personTableView;
    @FXML private TableColumn<User, String> colPersonName;
    @FXML private TableColumn<User, String> colPersonPerms;
    @FXML private TableColumn<User, Void> colPersonAdded;
    @FXML private Button createButton;
    @FXML private Button cancelButton;

    private Project createdProject;
    private final AppContext appContext = AppContext.getInstance();
    private ObservableList<User> availableUsers;
    private ObservableList<User> projectUsers;

    @FXML
    public void initialize() {
        availableUsers = FXCollections.observableArrayList(appContext.getUserList());
        projectUsers = FXCollections.observableArrayList();

        setupUI();

        createButton.setOnAction(e -> handleCreateProject());
        cancelButton.setOnAction(e -> handleCancelation());
    }

    private void setupUI() {
        userListView.setItems(projectUsers);
        setupListView(userListView, User::getUsername);

        colPersonName.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getUsername()));
        colPersonPerms.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getClass().getSimpleName()));
        setupActionColumn();

        personTableView.setItems(availableUsers);
    }

    private void setupActionColumn() {
        colPersonAdded.setCellFactory(param -> new TableCell<>() {
            private final Button addButton = new Button("Add");
            {
                addButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    availableUsers.remove(user);
                    projectUsers.add(user);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : addButton);
            }
        });
    }

    private void handleCreateProject() {
        String title = projectTitle.getText();
        String description = projectDescription.getText();
        LocalDate dueDate = dueDatePicker.getValue();

        if (title.isEmpty() || dueDate == null) {
            showAlert("Missing Information", "Please enter the project title and due date.", Alert.AlertType.WARNING);
            return;
        }

        // Create project
        createdProject = new Project(title, appContext.getCurrentUser());
        createdProject.addActivity(new Activity(
                appContext.getCurrentUser(),
                title + " - Kickoff Meeting",
                description + "\n Project Description",
                dueDate,
                false
        ));

        closeWindow(createButton);
    }

    private void handleCancelation() {
        createdProject = null;
        closeWindow(cancelButton);
    }

    public Project getCreatedProject() {
        return createdProject;
    }
}
