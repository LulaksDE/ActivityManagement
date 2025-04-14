package com.lulakssoft.activitymanagement;

import com.lulakssoft.activitymanagement.User.User;
import com.lulakssoft.activitymanagement.User.UserManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Border;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static javafx.stage.Modality.APPLICATION_MODAL;

// Negativ Beispiel für SRP
public class FXMLController {

    // LoginViewController
    @FXML
    private Label loginInformationLabel;
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    private Border defaultBorder;

    // ProjectViewController
    @FXML
    private Button logoutButton;
    @FXML
    private ComboBox<Project> projectComboBox;

    @FXML
    private TextField searchField;

    @FXML
    private Button createButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button loadButton;

    private List<Project> projectList;

    private ObservableList<Project> observableList;


    private User loggedInUser;

    public boolean loggedIn = false;

    @FXML
    public void initializeLogin() {
        defaultBorder = usernameField.getBorder();

        loginButton.setOnAction(event -> {
            handleLoginButton();
        });
    }

    @FXML
    public void initializeProjectView() {
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
            FXMLController controller = sceneManager.loadFXML(SceneManager.PROJECT_VIEW);
            controller.initializeProjectView();

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
}
