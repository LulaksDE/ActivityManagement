package com.lulakssoft.activitymanagement;

import com.lulakssoft.activitymanagement.user.User;
import com.lulakssoft.activitymanagement.user.UserManager;
import com.lulakssoft.activitymanagement.user.UserRole;
import com.lulakssoft.activitymanagement.user.role.PermissionChecker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
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
    @FXML
    private Button manageUsersButton;

    private List<Project> projectList;

    private ObservableList<Project> observableList;


    private User loggedInUser;

    public boolean loggedIn = false;

    @FXML
    public void initialize() {
        UserManager userManager = UserManager.INSTANCE;
        ProjectManager projectManager = ProjectManager.getInstance();
        this.loggedInUser = userManager.getCurrentUser();
        loggedIn = true;

        // Lade die Projekte des Benutzers
        projectList = projectManager.getProjectsForUser(loggedInUser);
        observableList = FXCollections.observableArrayList(projectList);

        projectComboBox.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Project project, boolean empty) {
                super.updateItem(project, empty);
                if (empty || project == null) {
                    setText(null);
                } else {
                    setText(project.getName());
                }
            }
        });

        projectComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Project project, boolean empty) {
                super.updateItem(project, empty);
                if (empty || project == null) {
                    setText(null);
                } else {
                    setText(project.getName());
                }
            }
        });


        // FilteredList für die Suche initialisieren
        FilteredList<Project> filteredList = new FilteredList<>(observableList, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(project -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return project.getName().toLowerCase().contains(lowerCaseFilter);
            });
        });

        // Setze die gefilterte Liste als Datenquelle für die ComboBox
        projectComboBox.setItems(filteredList);

        // Aktionen für die Buttons festlegen
        createButton.setOnAction(e -> handleCreate());
        deleteButton.setOnAction(e -> handleDelete());
        loadButton.setOnAction(e -> handleLoad());
        logoutButton.setOnAction(e -> handleLogout());

        // Manage Users Button hinzufügen und konfigurieren
        manageUsersButton.setOnAction(e -> handleManageUsers());

        // Nur für Admins sichtbar machen
        PermissionChecker.configureUIComponent(manageUsersButton, loggedInUser,
                UserRole::canManageUsers);
    }

    private void handleCreate() {
        // Hier wird ein neues Projekt erstellt (ggf. Logik hinzufügen)
        try {
          SceneManager sceneManager = SceneManager.getInstance();
          ProjectCreationController controller = sceneManager.openModalWindow(
                  createButton.getScene().getWindow(),
                  SceneManager.PROJECT_CREATION,
                  "Create Project");
            controller.initialize();
            Project newProject = controller.getCreatedProject();

            // Aktualisiere die Liste nach Erstellung eines neuen Projekts
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
        ProjectManager projectManager = ProjectManager.getInstance();
        Project selectedProject = projectComboBox.getSelectionModel().getSelectedItem();
        if (selectedProject == null) {
            return;
        } else {
            projectManager.setCurrentProject(selectedProject);
            System.out.println("Selected project: " + selectedProject);
        }

        try {
            SceneManager sceneManager = SceneManager.getInstance();
            ActivityListController controller = sceneManager.openModalWindow(loadButton.getScene().getWindow(),SceneManager.ACTIVITY_LIST, "Projekt editor");
            controller.initialize();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error when opening activity list: " + e.getMessage());
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

    private void handleManageUsers() {
        try {
            SceneManager sceneManager = SceneManager.getInstance();
            sceneManager.openModalWindow(
                    manageUsersButton.getScene().getWindow(),
                    SceneManager.USER_MANAGEMENT,
                    "Benutzerverwaltung");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
