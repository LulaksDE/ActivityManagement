package com.lulakssoft.activitymanagement;

import com.lulakssoft.activitymanagement.user.User;
import com.lulakssoft.activitymanagement.user.UserManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class ProjectCreationController {

    @FXML
    private TextField projectTitle;

    @FXML
    private TextArea projectDescription;

    @FXML
    private DatePicker dueDatePicker;

    @FXML
    private ListView<User> projectMemberListView;

    @FXML
    private TableView<User> personTableView;

    @FXML
    private TableColumn<User, String> colPersonName;

    @FXML
    private TableColumn<User, String> colPersonPerms;

    @FXML
    private TableColumn<User, Void> colPersonAdded;

    @FXML
    private Button createButton;

    @FXML
    private Button cancelButton;

    private Project createdProject;

    // Der aktuelle Benutzer, der das Projekt erstellt
    private User creator;


    public void initialize() {
        UserManager userManager = UserManager.INSTANCE;
        List<User> userList = userManager.getAllUsers();
        User creator = userManager.getCurrentUser();

        ObservableList<User> availableUsers = FXCollections.observableArrayList(userList);
        ObservableList<User> projectUsers = FXCollections.observableArrayList();

        projectMemberListView.setItems(projectUsers);

        projectMemberListView.setCellFactory(listView -> new ListCell<User>() {
            @Override
            protected void updateItem(User nutzer, boolean empty) {
                super.updateItem(nutzer, empty);
                if (empty || nutzer == null) {
                    setText(null);
                } else {
                    setText(nutzer.getUsername());  // Zeige nur den Titel der Aktivität an
                }
            }
        });

        colPersonName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
        colPersonPerms.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPrivilage().toString()));
        // Actions column
        colPersonAdded.setCellFactory(param -> new TableCell<>() {
            private final Button addButton = new Button("Add");

            {
                addButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    availableUsers.remove(user); // Remove user from available
                    projectUsers.add(user); // Add the user to the list
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(addButton);
                }
            }
        });

        personTableView.setItems(availableUsers);

        this.creator = creator;

        createButton.setOnAction(e -> handleCreateProject());
        cancelButton.setOnAction(e -> handleCancelation());

    }

    private void handleCreateProject() {
        ProjectManager projectManager = ProjectManager.getInstance();
        String title = projectTitle.getText();
        String description = projectDescription.getText();
        LocalDate dueDate = dueDatePicker.getValue();

        if (title.isEmpty() || dueDate == null) {
            showAlert("Missing Information", "Please enter the project title and due date.");
            return;
        }

        // Projekt mit dem definierten Ersteller erstellen
        createdProject = new Project(title, creator, projectMemberListView.getItems());
        projectManager.addProject(createdProject);

        // Aktivität zum Projekt hinzufügen
        createdProject.addActivity(new Activity(
                creator,
                createdProject.getMembers(),
                title + " - Kickoff Meeting",
                description + "\nProject Description",
                dueDate,
                false
        ));

        closeWindow();
    }

    private void handleCancelation() {
        createdProject = null;
        closeWindow();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void closeWindow() {
        Stage stage = (Stage) createButton.getScene().getWindow();
        stage.close();
    }

    public Project getCreatedProject() {
        return createdProject;
    }
}
