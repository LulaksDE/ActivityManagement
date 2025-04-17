package com.lulakssoft.activitymanagement;

import com.lulakssoft.activitymanagement.database.IActivityRepository;
import com.lulakssoft.activitymanagement.notification.Toast;
import com.lulakssoft.activitymanagement.notification.UINotifier;
import com.lulakssoft.activitymanagement.user.User;
import com.lulakssoft.activitymanagement.user.UserManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.time.LocalDate;
import java.util.List;

public class ProjectCreationController implements UINotifier {

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

    private User creator;

    private ProjectManager projectManager;


    public void initialize() {
        this.projectManager = ProjectManager.getInstance();
        UserManager userManager = UserManager.INSTANCE;
        List<User> userList = userManager.getAllUsers();
        creator = userManager.getCurrentUser();

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

        createButton.setOnAction(e -> handleCreateProject());
        cancelButton.setOnAction(e -> handleCancelation());

    }

    private void handleCreateProject() {
        String title = projectTitle.getText();
        String description = projectDescription.getText();
        LocalDate dueDate = dueDatePicker.getValue();

        if (title.isEmpty() || dueDate == null) {
            showPopupNotification("Please fill in all fields", "Error");
            return;
        }
        if (projectMemberListView.getItems().isEmpty()) {
            showBannerNotification("Please add at least one member to the project");
            return;
        }
        createdProject = ServiceLocator.createProject(title, creator, projectMemberListView.getItems());
        projectManager.addProject(createdProject);
        projectManager.setCurrentProject(createdProject);
        Activity kickoffActivity = new Activity(
                creator,
                title + " - Kickoff Meeting",
                description + "\nProject Description",
                "Medium",
                dueDate,
                false
        );
        createdProject.addActivity(kickoffActivity);



        ActivityManager.getInstance().saveActivity(kickoffActivity);
        createdProject.refreshActivities();

        closeWindow();
    }

    private void handleCancelation() {
        createdProject = null;
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) createButton.getScene().getWindow();
        stage.close();
    }

    public Project getCreatedProject() {
        return createdProject;
    }

    @Override
    public void showPopupNotification(String message, String title) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void showBannerNotification(String message) {
        Window currentWindow = createButton.getScene().getWindow();
        Toast toast = Toast.makeText(currentWindow, message, 3000);
        toast.show();
    }
    @Override
    public void sendNotification(String message, String receiver) {
        showBannerNotification(message);
    }
}
