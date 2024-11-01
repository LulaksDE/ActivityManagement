package com.lulakssoft.activitymanagement;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.time.LocalDate;

public class ProjectCreationController {

    @FXML
    private TextField projectTitle;

    @FXML
    private TextArea projectDescription;

    @FXML
    private DatePicker dueDatePicker;

    @FXML
    private CheckBox addPersonCheckBox;

    @FXML
    private TextField personName;

    @FXML
    private Button createButton;

    @FXML
    private Button cancelButton;

    private Project createdProject;

    // Der aktuelle Benutzer, der das Projekt erstellt
    private User creator;

    public ProjectCreationController(User creator) {
        this.creator = creator;  // Der Benutzer, der das Projekt erstellt
    }

    public void initialize() {
        createButton.setOnAction(e -> handleCreateProject());
        cancelButton.setOnAction(e -> closeWindow());

        // personName wird nur aktiv, wenn addPersonCheckBox ausgewählt ist
        addPersonCheckBox.selectedProperty().addListener((observable, oldValue, newValue) ->
                personName.setDisable(!newValue)
        );
    }

    private void handleCreateProject() {
        String title = projectTitle.getText();
        String description = projectDescription.getText();
        LocalDate dueDate = dueDatePicker.getValue();

        if (title.isEmpty() || dueDate == null) {
            showAlert("Missing Information", "Please enter the project title and due date.");
            return;
        }

        // Projekt mit dem definierten Ersteller erstellen
        createdProject = new Project(title, creator);

        // Aktivität zum Projekt hinzufügen
        createdProject.addActivity(new Activity(
                creator,
                title,
                description,
                dueDate,
                false
        ));

        // Zusätzlichen Benutzer hinzufügen, falls addPersonCheckBox aktiviert ist und ein Name angegeben wurde
        if (addPersonCheckBox.isSelected() && !personName.getText().isEmpty()) {
            User additionalUser = new StandardUser(personName.getText());
            createdProject.getActivityList().get(0).getUserList().add(additionalUser);
        }

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
