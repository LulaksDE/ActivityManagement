package com.lulakssoft.activitymanagement;

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
    public void initialize(List<User> userList,User loggedInUser) {

        this.loggedInUser = loggedInUser;
        loggedIn = true;
        this.userList = userList;

        // Lade die Projekte des Benutzers
        projectList = loggedInUser.getProjectList();
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
    }

    private void handleCreate() {
        // Hier wird ein neues Projekt erstellt (ggf. Logik hinzufügen)
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ProjectCreationScreen.fxml"));
            Parent root = loader.load();

            Stage creationStage = new Stage();
            creationStage.setTitle("Projekt erstellen");
            creationStage.setScene(new Scene(root));
            creationStage.initModality(Modality.APPLICATION_MODAL);

            // Übergabe der userList an den neuen Controller
            ProjectCreationController controller = loader.getController();
            controller.initialize(userList, loggedInUser);
            creationStage.showAndWait();
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
        Project selectedProject = projectComboBox.getSelectionModel().getSelectedItem();
        if (selectedProject == null) {
            return;
        }

        // Lade die Aktivität zu dem Projekt
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ActivityList.fxml"));
            Parent root = loader.load();

            // Erstelle eine neue Stage (Fenster) für den Editor
            Stage activityListStage = new Stage();
            activityListStage.setTitle("Projekt bearbeiten");
            activityListStage.initModality(Modality.WINDOW_MODAL);
            activityListStage.initOwner(loadButton.getScene().getWindow());
            activityListStage.setScene(new Scene(root));

            // Übergabe des ausgewählten Projekts an den neuen Controller
            ActivityListController controller = loader.getController();
            controller.initialize(selectedProject);
            activityListStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
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
