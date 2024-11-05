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
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class MainViewController {

    @FXML
    private ComboBox<String> projectComboBox;

    @FXML
    private TextField searchField; // Textfeld für die Suchfunktion

    @FXML
    private Button createButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button loadButton;

    private List<Project> projectList;
    private ObservableList<String> projectNames;

    // userList mit Beispielwerten initialisieren
    private List<User> userList = List.of(
            new Admin("admin01"),
            new Admin("admin02"),
            new Admin("admin03"),
            new Admin("admin04"),
            new Supporter("supporter02"),
            new Technician("technician01")
    );

    @FXML
    private void initialize() {
        // Lade die Projekte des Benutzers
        projectList = new Admin("admin").getProjectList();

        // Erstelle eine ObservableList mit den Projekt-Namen
        projectNames = FXCollections.observableArrayList();
        projectList.forEach(project -> projectNames.add(project.getName()));

        // FilteredList für die Suche in der ComboBox
        FilteredList<String> filteredProjects = new FilteredList<>(projectNames, p -> true);
        projectComboBox.setItems(filteredProjects);

        // Filter-Funktion für das Suchfeld
        searchField.textProperty().addListener((obs, oldText, newText) -> {
            filteredProjects.setPredicate(project -> {
                if (newText == null || newText.isEmpty()) {
                    return true;
                }
                return project.toLowerCase().contains(newText.toLowerCase());
            });
        });

        // Aktionen für die Buttons festlegen
        createButton.setOnAction(e -> handleCreate());
        deleteButton.setOnAction(e -> handleDelete());
        loadButton.setOnAction(e -> handleLoad());
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
            controller.initialize(userList, new Worker("arbeiter01"));
            creationStage.showAndWait();
            Project newProject = controller.getCreatedProject();

            // Aktualisiere die Liste nach Erstellung eines neuen Projekts
            updateProjectList(newProject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleDelete() {
        String selectedProjectName = projectComboBox.getSelectionModel().getSelectedItem();
        if (selectedProjectName == null) {
            return;
        }

        // Entferne das Projekt aus projectList und projectNames
        projectList.removeIf(project -> project.getName().equals(selectedProjectName));
        projectNames.remove(selectedProjectName);
        projectComboBox.getSelectionModel().clearSelection();
        searchField.clear();
    }

    private void handleLoad() {
        String selectedProjectName = projectComboBox.getSelectionModel().getSelectedItem();
        if (selectedProjectName == null) {
            return;
        }

        // Hole das eigentliche Projekt-Objekt aus der projectList
        Project selectedProject = projectList.stream()
                .filter(project -> project.getName().equals(selectedProjectName))
                .findFirst()
                .orElse(null);

        if (selectedProject == null) {
            return; // Projekt nicht gefunden, Abbruch
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
        projectNames.add(newProject.getName());
    }
}
