package com.lulakssoft.activitymanagement;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.CheckListView;
import org.controlsfx.control.IndexedCheckModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProjectCreationController {

    @FXML
    private TextField projectTitle;

    @FXML
    private TextArea projectDescription;

    @FXML
    private DatePicker dueDatePicker;

    @FXML
    private ListView<User> userListView;

    @FXML
    private Button createButton;

    @FXML
    private Button cancelButton;

    private Project createdProject;

    // Der aktuelle Benutzer, der das Projekt erstellt
    private User creator;


    public void initialize(List<User> userList, User creator) {

        ObservableList<User> users = FXCollections.observableArrayList(userList);

        userListView.setItems(users);

        userListView.setCellFactory(listView -> new ListCell<User>() {
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

        this.creator = creator;

        createButton.setOnAction(e -> handleCreateProject());
        cancelButton.setOnAction(e -> closeWindow());

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
