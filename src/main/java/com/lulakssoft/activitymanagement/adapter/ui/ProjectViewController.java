package com.lulakssoft.activitymanagement.adapter.ui;

import com.lulakssoft.activitymanagement.application.service.ProjectService;
import com.lulakssoft.activitymanagement.application.service.UserService;
import com.lulakssoft.activitymanagement.config.ApplicationContext;
import com.lulakssoft.activitymanagement.SceneManager;
import com.lulakssoft.activitymanagement.adapter.notification.LoggerFactory;
import com.lulakssoft.activitymanagement.adapter.notification.LoggerNotifier;
import com.lulakssoft.activitymanagement.domain.model.project.Project;
import com.lulakssoft.activitymanagement.domain.model.user.Role;
import com.lulakssoft.activitymanagement.domain.model.user.User;
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

    private final LoggerNotifier logger = LoggerFactory.getLogger();

    private ProjectService projectService;
    private UserService userService;

    @FXML
    public void initialize() {
        setupManagers();

        setupProjectList();

        setupComboBoxRendering();

        FilteredList<Project> filteredList = setupSearch();

        projectComboBox.setItems(filteredList);

        createButton.setOnAction(e -> handleCreate());
        deleteButton.setOnAction(e -> handleDelete());
        loadButton.setOnAction(e -> handleLoad());
        logoutButton.setOnAction(e -> handleLogout());

        manageUsersButton.setOnAction(e -> handleManageUsers());
    }

    private void setupManagers() {
        ApplicationContext context = ApplicationContext.getInstance();
        this.projectService = context.getProjectService();
        this.userService = context.getUserService();
        this.loggedInUser = userService.getCurrentUser();
        loggedIn = true;
    }

    private void setupProjectList() {
        projectList = projectService.findProjectsByMember(loggedInUser.getId());
        observableList = FXCollections.observableArrayList(projectList);
    }

    private void setupComboBoxRendering() {
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

    private FilteredList<Project> setupSearch() {
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
        return filteredList;
    }

    private void handleCreate() {
        try {
          SceneManager sceneManager = SceneManager.getInstance();
          ProjectCreationController controller = sceneManager.openModalWindow(
                  createButton.getScene().getWindow(),
                  SceneManager.PROJECT_CREATION,
                  "Create Project");
            controller.initialize();
            Project newProject = controller.getCreatedProject();

            if (newProject != null) updateProjectList(newProject);
        } catch (Exception e) {
            logger.logError("Error creating project", e);
        }
    }

    private void handleDelete() {
        Project selectedProject = projectComboBox.getSelectionModel().getSelectedItem();
        if (selectedProject == null) {
            logger.logWarning("No project selected for deletion.");
            return;
        } else {
            logger.logInfo("Deleting project: " + selectedProject.getName());
            projectService.deleteProject(selectedProject.getId());
        }
        projectList.remove(selectedProject);
        observableList.remove(selectedProject);
        projectComboBox.getSelectionModel().clearSelection();
    }

    private void handleLoad() {
        Project selectedProject = projectComboBox.getSelectionModel().getSelectedItem();
        if (selectedProject == null) {
            return;
        } else {
            projectService.setCurrentProject(selectedProject);
            logger.logInfo("Loading project: " + selectedProject.getName());
        }

        try {
            SceneManager sceneManager = SceneManager.getInstance();
            ActivityListController controller = sceneManager.openModalWindow(loadButton.getScene().getWindow(),SceneManager.ACTIVITY_LIST, "Projekt editor");
            controller.initialize();
        } catch (Exception e) {
            logger.logError("Error loading project: " + selectedProject.getName(), e);
        }
    }

    private void updateProjectList(Project newProject) {
        projectList.add(newProject);
        observableList.add(newProject);
        logger.logInfo("Updating project: " + newProject.getName());
    }

    private void handleLogout() {
        // Hier wird der Benutzer ausgeloggt (ggf. Logik hinzufügen)
        loggedIn = false;
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.close();
    }

    private void handleManageUsers() {
        try {
            logger.logInfo("Opening user management window");
            SceneManager sceneManager = SceneManager.getInstance();
            sceneManager.openModalWindow(
                    manageUsersButton.getScene().getWindow(),
                    SceneManager.USER_MANAGEMENT,
                    "User Management");
        } catch (Exception e) {
            logger.logError("Error opening user management window", e);
        }
    }
}
