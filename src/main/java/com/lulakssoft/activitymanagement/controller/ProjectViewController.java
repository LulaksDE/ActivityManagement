package com.lulakssoft.activitymanagement.controller;

import com.lulakssoft.activitymanagement.Project;
import com.lulakssoft.activitymanagement.SceneManager;
import com.lulakssoft.activitymanagement.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class ProjectViewController {

    @FXML
    private Button logoutButton;
    @FXML
    private ComboBox<Project> projectComboBox;

    @FXML
    private TextField searchField; // Textfeld für die Suchfunktion

    @FXML
    private Button createButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button loadButton;

    private List<Project> projectList;

    private ObservableList<Project> observableList;

    private List<User> userList;

    private User loggedInUser;

    public boolean loggedIn = false;

    @FXML
    public void initialize() {
        // Initialize UI components only
        setupComboBox();

        // Setup button actions
        createButton.setOnAction(e -> handleCreate());
        deleteButton.setOnAction(e -> handleDelete());
        loadButton.setOnAction(e -> handleLoad());
        logoutButton.setOnAction(e -> handleLogout());
    }

    public void initData(List<User> userList, User loggedInUser) {
        this.loggedInUser = loggedInUser;
        loggedIn = true;
        this.userList = userList;

        // Load user projects
        projectList = loggedInUser.getProjectList();
        observableList = FXCollections.observableArrayList(projectList);

        // Setup filtered list for search
        setupFilteredList();

        // Set items to combo box
        projectComboBox.setItems(filteredList);
    }

    private void setupComboBox() {
        projectComboBox.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Project project, boolean empty) {
                super.updateItem(project, empty);
                setText(empty || project == null ? null : project.getName());
            }
        });

        projectComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Project project, boolean empty) {
                super.updateItem(project, empty);
                setText(empty || project == null ? null : project.getName());
            }
        });
    }

    private FilteredList<Project> filteredList;

    private void setupFilteredList() {
        filteredList = new FilteredList<>(observableList, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(project -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return project.getName().toLowerCase().contains(lowerCaseFilter);
            });
        });
    }

    private void handleCreate() {
        try {
            SceneManager sceneManager = SceneManager.getInstance();
            ProjectCreationController controller = sceneManager.showDialog("ProjectCreationScreen.fxml", "Projekt erstellen");

            // After dialog is closed, get the created project
            Project newProject = controller.getCreatedProject();

            // Update project list if needed
            if (newProject != null) updateProjectList(newProject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleDelete() {
        Project selectedProject = projectComboBox.getSelectionModel().getSelectedItem();
        if (selectedProject == null) {
            return;
        }

        // Hier wird das Projekt gelöscht (ggf. Logik hinzufügen)

        projectList.remove(selectedProject);
        observableList.remove(selectedProject);
        projectComboBox.getSelectionModel().clearSelection();
    }

    private void handleLoad() {
        Project selectedProject = projectComboBox.getSelectionModel().getSelectedItem();
        if (selectedProject == null) {
            return;
        }

        try {
            SceneManager sceneManager = SceneManager.getInstance();
            ActivityListController controller = sceneManager.showDialog(
                    "/com/lulakssoft/activitymanagement/ActivityList.fxml",
                    "Projekt bearbeiten"
            );

            controller.initData(selectedProject);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to load ActivityList: " + e.getMessage());
        }
    }

    private void updateProjectList(Project newProject) {
        // Füge das neue Projekt zur Liste hinzu
        projectList.add(newProject);
        observableList.add(newProject);

        System.out.println("Project created: " + newProject);
    }

    private void handleLogout() {
        // Hier wird der Benutzer ausgeloggt (ggf. Logik hinzufügen)
        loggedIn = false;
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.close();
    }
}
