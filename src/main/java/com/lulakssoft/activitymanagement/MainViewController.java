package com.lulakssoft.activitymanagement;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class MainViewController {

    @FXML
    private ListView<String> activityListView;

    @FXML
    private TextField titleField;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private Button addButton;

    private Project currentProject = new Project("Default Project");

    @FXML
    private void initialize() {
        addButton.setOnAction(e -> handleAddActivity());
    }

    private void handleAddActivity() {
        String title = titleField.getText();
        String description = descriptionArea.getText();
        LocalDate dueDate = LocalDate.now().plusDays(7);  // Beispiel: Fälligkeitsdatum +7 Tage

        Activity activity = new Activity(title, description, dueDate);
        currentProject.addActivity(activity);
        activityListView.getItems().add(activity.getTitle());
        titleField.setText("");
        descriptionArea.setText("");
    }
}
